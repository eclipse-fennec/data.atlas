/**
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 */
package org.eclipse.fennec.data.atlas.bootstrap;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration;
import org.eclipse.fennec.model.atlas.scope.api.ReadableScopeService;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * Model-Atlas-mode configuration source of a Data Atlas instance.
 *
 * <p>
 * Retrieves the {@code DataAtlasConfiguration} from a Model Atlas through the
 * per-scope {@link ReadableScopeService} the Model Atlas client publishes
 * (factory PID {@code org.eclipse.fennec.model.atlas.rest.client}, one service
 * per scope keyed {@code atlas.scope}). The scope is selected via the
 * {@code scopeService.target} reference target in the component configuration;
 * registry and object id name the instance within that scope. EClass
 * references inside the retrieved configuration resolve remotely through the
 * client's Atlas-aware ResourceSet — the referenced EPackages come from the
 * Model Atlas schema registry, not from local files.
 * </p>
 *
 * <p>
 * <b>Lifecycle:</b> the configuration is re-fetched every
 * {@code refresh.interval.ms} (0 disables the poll). The client's cache makes
 * an unchanged check a conditional GET and returns the identical instance, so
 * short intervals are cheap; changes flow through the Model Atlas stage
 * workflow (upload to a writable stage, transition to the final one). Applied
 * changes are diffed by the {@link ConfigurationRegistrar} — unchanged
 * configuration objects keep serving. A transient fetch failure (Model Atlas
 * unreachable) keeps the current state and retries; a fetched but <em>broken</em>
 * configuration — or a removed object — fails hard: the published
 * configuration is torn down and the error logged loudly. Note that
 * {@code FileDataInput} URIs are used as-is in this mode — a configuration
 * served by a Model Atlas must use absolute URIs.
 * </p>
 */
@Component(name = ModelAtlasBootstrap.PID, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = ModelAtlasBootstrap.Config.class)
public class ModelAtlasBootstrap {

	public static final String PID = "DataAtlasModelAtlasBootstrap";

	private static final Logger LOG = System.getLogger(ModelAtlasBootstrap.class.getName());

	@ObjectClassDefinition(name = "Data Atlas Bootstrap Configuration (Model Atlas mode)")
	public @interface Config {

		@AttributeDefinition(name = "Registry",
				description = "Name of the Model Atlas object registry (registry.type OTHER) holding the "
						+ "DataAtlasConfiguration instance.")
		String atlas_registry();

		@AttributeDefinition(name = "Object id",
				description = "Object id of the DataAtlasConfiguration instance within the registry.")
		String atlas_object_id();

		@AttributeDefinition(name = "Refresh interval (ms)",
				description = "How often the configuration is re-fetched from the Model Atlas; an unchanged "
						+ "check is a conditional GET through the client cache. 0 disables the poll.")
		long refresh_interval_ms() default 300_000L;
	}

	private final ReadableScopeService<EObject> scopeService;
	private final ConfigurationRegistrar registrar;
	private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
		Thread thread = new Thread(runnable, "dataatlas-config-refresh");
		thread.setDaemon(true);
		return thread;
	});

	// guarded by this
	private String registry;
	private String objectId;
	private ScheduledFuture<?> refreshTask;
	private EObject lastApplied;

	@Activate
	public ModelAtlasBootstrap(BundleContext bundleContext,
			@Reference(name = "scopeService") ReadableScopeService<EObject> scopeService, Config config) {
		this.scopeService = scopeService;
		this.registrar = new ConfigurationRegistrar(bundleContext);
		synchronized (this) {
			configure(config);
			// initial load stays fail-fast: an invalid configuration at startup
			// fails the component activation
			apply(fetch().orElseThrow(() -> new IllegalStateException(
					"ModelAtlasBootstrap: no object found at " + source())));
			reschedule(config.refresh_interval_ms());
		}
	}

	@Modified
	synchronized void modified(Config config) {
		LOG.log(Level.INFO, () -> "ModelAtlasBootstrap: configuration changed, re-applying");
		configure(config);
		refresh();
		reschedule(config.refresh_interval_ms());
	}

	@Deactivate
	void deactivate() {
		scheduler.shutdownNow();
		registrar.unregisterAll();
	}

	private void configure(Config config) {
		this.registry = config.atlas_registry();
		this.objectId = config.atlas_object_id();
	}

	private void reschedule(long intervalMs) {
		if (refreshTask != null) {
			refreshTask.cancel(false);
			refreshTask = null;
		}
		if (intervalMs > 0) {
			refreshTask = scheduler.scheduleWithFixedDelay(this::refresh, intervalMs, intervalMs,
					TimeUnit.MILLISECONDS);
			LOG.log(Level.INFO, () -> "ModelAtlasBootstrap: refreshing " + source() + " every " + intervalMs + " ms");
		}
	}

	/**
	 * One refresh cycle. A transient fetch failure keeps the current state; a
	 * missing or broken configuration fails hard (teardown + loud log) — and
	 * the poll keeps running, so a later corrected version recovers the
	 * instance.
	 */
	private synchronized void refresh() {
		Optional<EObject> fetched;
		try {
			fetched = fetch();
		} catch (RuntimeException e) {
			LOG.log(Level.WARNING,
					() -> "ModelAtlasBootstrap: could not reach the Model Atlas (" + source()
							+ "), keeping the current configuration: " + e);
			return;
		}
		if (fetched.isEmpty()) {
			LOG.log(Level.ERROR, () -> "ModelAtlasBootstrap: configuration object disappeared at " + source()
					+ " - tearing the published configuration down");
			registrar.unregisterAll();
			lastApplied = null;
			return;
		}
		if (fetched.get() == lastApplied) {
			return; // client cache hit: unchanged
		}
		try {
			apply(fetched.get());
		} catch (RuntimeException e) {
			LOG.log(Level.ERROR, () -> "ModelAtlasBootstrap: the new configuration at " + source()
					+ " is broken - tearing the published configuration down", e);
			registrar.unregisterAll();
			lastApplied = null;
		}
	}

	private Optional<EObject> fetch() {
		return scopeService.get(registry, objectId);
	}

	private void apply(EObject object) {
		if (!(object instanceof DataAtlasConfiguration configuration)) {
			throw new IllegalStateException("ModelAtlasBootstrap: object at " + source() + " is a "
					+ object.eClass().getName() + ", not a DataAtlasConfiguration");
		}
		LOG.log(Level.INFO, () -> "ModelAtlasBootstrap: applying Data Atlas configuration from " + source());
		// resolves EClass references remotely through the client's ResourceSet
		EcoreUtil.resolveAll(configuration);
		ConfigurationRegistrar.failOnUnresolvedProxies(configuration, source());
		registrar.apply(configuration);
		lastApplied = object;
	}

	private String source() {
		return "atlas scope '" + scopeService.getScopeName() + "', registry '" + registry + "', object '" + objectId
				+ "'";
	}
}
