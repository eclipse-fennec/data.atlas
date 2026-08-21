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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration;
import org.eclipse.fennec.model.atlas.scope.api.ReadableScopeService;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
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
 * After retrieval the pipeline is identical to file mode (see
 * {@link ConfigurationRegistrar}): the resolved EPackages and the
 * configuration objects are registered as OSGi services and unregistered on
 * deactivation. Note that {@code FileDataInput} URIs are used as-is in this
 * mode — a configuration served by a Model Atlas must use absolute URIs.
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
	}

	private final ConfigurationRegistrar registrar;

	@Activate
	public ModelAtlasBootstrap(BundleContext bundleContext,
			@Reference(name = "scopeService") ReadableScopeService<EObject> scopeService, Config config) {
		this.registrar = new ConfigurationRegistrar(bundleContext);
		load(scopeService, config);
	}

	@Deactivate
	void deactivate() {
		registrar.unregisterAll();
	}

	private void load(ReadableScopeService<EObject> scopeService, Config config) {
		String source = "atlas scope '" + scopeService.getScopeName() + "', registry '" + config.atlas_registry()
				+ "', object '" + config.atlas_object_id() + "'";
		LOG.log(Level.INFO, () -> "ModelAtlasBootstrap: loading Data Atlas configuration from " + source);
		EObject object = scopeService.get(config.atlas_registry(), config.atlas_object_id())
				.orElseThrow(() -> new IllegalStateException(
						"ModelAtlasBootstrap: no object found at " + source));
		if (!(object instanceof DataAtlasConfiguration configuration)) {
			throw new IllegalStateException("ModelAtlasBootstrap: object at " + source + " is a "
					+ object.eClass().getName() + ", not a DataAtlasConfiguration");
		}
		// resolves EClass references remotely through the client's ResourceSet
		EcoreUtil.resolveAll(configuration);
		ConfigurationRegistrar.failOnUnresolvedProxies(configuration, source);
		registrar.register(configuration);
	}
}
