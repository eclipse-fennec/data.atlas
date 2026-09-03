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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration;
import org.eclipse.fennec.data.atlas.configuration.DataTransformation;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistry;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryEntry;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryListener;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.model.atlas.scope.api.ReadableScopeService;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
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

	/**
	 * URI scheme of a transformation reference resolved against a local
	 * {@link EObjectRegistry} (fed from a Model Atlas registry by the
	 * model.atlas {@code AtlasEObjectProvider}):
	 * {@code eobject-registry://<registry-name>/<key>#<fragment>} — e.g.
	 * {@code eobject-registry://transformations/person-to-public#//@unit}.
	 */
	public static final String REGISTRY_URI_SCHEME = "eobject-registry";

	private final ReadableScopeService<EObject> scopeService;
	private final ConfigurationRegistrar registrar;
	// guarded by this; a registry appearing or disappearing re-applies
	private final Map<String, EObjectRegistry> eObjectRegistries = new HashMap<>();
	private boolean initialized;
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
			// initial load stays fail-fast for a BROKEN configuration: it fails
			// the component activation. A configuration that is fine but whose
			// transformation registry has not synced yet is pending, not broken
			// - the registry binding (or the poll) applies it once it is there.
			try {
				apply(fetch().orElseThrow(() -> new IllegalStateException(
						"ModelAtlasBootstrap: no object found at " + source())));
			} catch (TransientDependencyException pending) {
				LOG.log(Level.INFO, () -> "ModelAtlasBootstrap: " + pending.getMessage()
						+ " - publishing nothing until it appears");
				lastApplied = null;
			}
			reschedule(config.refresh_interval_ms());
			initialized = true;
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addEObjectRegistry(EObjectRegistry eObjectRegistry) {
		eObjectRegistries.put(eObjectRegistry.getName(), eObjectRegistry);
		// registry CONTENT changes never rebind the service, but a pending
		// configuration may be waiting for exactly one entry to be synced in -
		// so listen, and re-apply on every change (a no-change re-apply is a
		// client cache hit and costs nothing)
		eObjectRegistry.addListener(registryListener);
		if (initialized) {
			refresh();
		}
	}

	synchronized void removeEObjectRegistry(EObjectRegistry eObjectRegistry) {
		eObjectRegistry.removeListener(registryListener);
		if (eObjectRegistries.remove(eObjectRegistry.getName()) != null && initialized) {
			refresh();
		}
	}

	private final EObjectRegistryListener registryListener = new EObjectRegistryListener() {

		@Override
		public void entryAdded(EObjectRegistryEntry entry) {
			onRegistryChange();
		}

		@Override
		public void entryUpdated(EObjectRegistryEntry entry, EObjectRegistryEntry oldEntry) {
			onRegistryChange();
		}

		@Override
		public void entryRemoved(EObjectRegistryEntry entry) {
			onRegistryChange();
		}
	};

	private synchronized void onRegistryChange() {
		if (initialized) {
			refresh();
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
		} catch (TransientDependencyException pending) {
			// a dependency (a transformation registry) is not there right now:
			// like an unreachable Model Atlas this keeps the current state -
			// nothing is torn down, the poll (or the registry binding) retries
			LOG.log(lastApplied == null ? Level.INFO : Level.WARNING,
					() -> "ModelAtlasBootstrap: " + pending.getMessage()
							+ (lastApplied == null ? " - publishing nothing until it appears"
									: " - keeping the current configuration"));
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
		resolveRegistryTransformations(configuration);
		resolveTransformationDocuments(configuration);
		ConfigurationRegistrar.failOnUnresolvedProxies(configuration, source());
		registrar.apply(configuration);
		lastApplied = object;
	}

	/**
	 * Resolves {@code DataTransformation.transformation} proxies of the
	 * {@link #REGISTRY_URI_SCHEME} scheme against the named local
	 * {@link EObjectRegistry} — the WP-DA-7 picture: the CompiledUnit documents
	 * live in a Model Atlas {@code transformations} registry and reach this
	 * runtime through the model.atlas {@code AtlasEObjectProvider} feeding the
	 * local registry. The registry entry is copied (registries lend their
	 * instances), hosted in the client's ResourceSet under the reference's
	 * document URI so its remaining proxies resolve like every other document,
	 * and the reference's fragment is applied to find the AST inside it.
	 *
	 * <p>
	 * A registry that is not there (yet) is a <em>pending</em> condition — the
	 * provider syncs asynchronously, so this is the repository-waiting
	 * situation, not a broken configuration. A present registry without the
	 * named key, or a key resolving to something that is no
	 * {@code OperationalTransformation} under the fragment, IS a broken
	 * configuration and fails the apply.
	 * </p>
	 */
	private void resolveRegistryTransformations(DataAtlasConfiguration configuration) {
		for (DataTransformation transformation : configuration.getTransformations().stream()
				.filter(DataTransformation.class::isInstance).map(DataTransformation.class::cast).toList()) {
			Object raw = transformation.eGet(DAConfigPackage.eINSTANCE.getDataTransformation_Transformation(), false);
			if (!(raw instanceof InternalEObject proxy) || !proxy.eIsProxy()) {
				continue;
			}
			URI proxyUri = proxy.eProxyURI();
			if (proxyUri == null || !REGISTRY_URI_SCHEME.equals(proxyUri.scheme())) {
				continue;
			}
			String registryName = proxyUri.authority();
			String key = String.join("/", proxyUri.segmentsList());
			EObjectRegistry registry;
			synchronized (this) {
				registry = eObjectRegistries.get(registryName);
			}
			if (registry == null) {
				throw new TransientDependencyException("transformation '" + transformation.getId()
						+ "' needs the EObject registry '" + registryName + "', which is not available (yet)");
			}
			// a missing key is transient too: the registry content syncs
			// asynchronously (the atlas provider), so the entry may simply not
			// have arrived yet - the listener re-applies the moment it does. A
			// permanently wrong key therefore stays pending, WARNed on every
			// attempt, rather than tearing the instance down.
			EObject stored = registry.get(key)
					.orElseThrow(() -> new TransientDependencyException("transformation '"
							+ transformation.getId() + "' references key '" + key + "' in EObject registry '"
							+ registryName + "', which holds no such entry (yet)"));
			// copy: the registry lends its instance, and this document becomes
			// part of the applied configuration's lifecycle
			EObject copy = EcoreUtil.copy(stored);
			ResourceSet resourceSet = configuration.eResource().getResourceSet();
			URI documentUri = proxyUri.trimFragment();
			Resource previous = resourceSet.getResource(documentUri, false);
			if (previous != null) {
				previous.unload();
				resourceSet.getResources().remove(previous);
			}
			Resource holder = new XMIResourceImpl(documentUri);
			resourceSet.getResources().add(holder);
			holder.getContents().add(copy);
			String fragment = proxyUri.fragment();
			EObject target = fragment == null || fragment.isBlank() ? copy : holder.getEObject(fragment);
			if (!(target instanceof OperationalTransformation ast)) {
				throw new IllegalStateException("ModelAtlasBootstrap: transformation '" + transformation.getId()
						+ "': '" + proxyUri + "' does not address an OperationalTransformation (found "
						+ (target == null ? "nothing" : target.eClass().getName()) + ")");
			}
			transformation.setTransformation(ast);
			LOG.log(Level.INFO, () -> "ModelAtlasBootstrap: resolved transformation '" + transformation.getId()
					+ "' from EObject registry '" + registryName + "' (key '" + key + "')");
		}
	}

	/** A dependency that is expected to appear — wait, do not tear down. */
	private static final class TransientDependencyException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		TransientDependencyException(String message) {
			super(message);
		}
	}

	/**
	 * {@code EcoreUtil.resolveAll(configuration)} resolves the configuration's
	 * own cross-references, but a referenced document — a transformation's
	 * CompiledUnit XMI, loaded from the URI the configuration names (in this
	 * mode an absolute URI, like {@code FileDataInput}) — keeps its own internal
	 * proxies. They must resolve here, against the client's Atlas-aware
	 * ResourceSet, so the transformation binds the same EPackage instances the
	 * registrar publishes; a proxy left dangling in the document is a broken
	 * configuration and fails the apply (M4 semantics).
	 */
	private void resolveTransformationDocuments(DataAtlasConfiguration configuration) {
		configuration.getTransformations().stream() //
				.filter(DataTransformation.class::isInstance).map(DataTransformation.class::cast) //
				.map(DataTransformation::getTransformation) //
				.filter(ast -> ast != null && !ast.eIsProxy()) //
				.forEach(ast -> {
					EObject root = EcoreUtil.getRootContainer(ast);
					EcoreUtil.resolveAll(root);
					ConfigurationRegistrar.failOnUnresolvedProxies(root,
							source() + " (transformation document " + (root.eResource() != null
									? String.valueOf(root.eResource().getURI())
									: "<detached>") + ")");
				});
	}

	private String source() {
		return "atlas scope '" + scopeService.getScopeName() + "', registry '" + registry + "', object '" + objectId
				+ "'";
	}
}
