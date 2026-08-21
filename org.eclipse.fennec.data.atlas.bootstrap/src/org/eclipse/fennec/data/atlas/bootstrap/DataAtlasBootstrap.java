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
import java.nio.file.Paths;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.FileDataInput;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

/**
 * File-mode configuration source of a Data Atlas instance.
 *
 * <p>
 * Loads the {@code DataAtlasConfiguration} from the configured URI once on
 * activation and publishes it via the {@link ConfigurationRegistrar}: the
 * EPackages of the referenced model types and the configuration objects (the
 * {@code DataService}s and {@code DataInput}s) become OSGi services that the
 * per-technology configurator components pick up. Everything is unregistered
 * on deactivation.
 * </p>
 */
@Component(name = DataAtlasBootstrap.PID, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = DataAtlasBootstrap.Config.class)
public class DataAtlasBootstrap {

	public static final String PID = "DataAtlasBootstrap";

	private static final Logger LOG = System.getLogger(DataAtlasBootstrap.class.getName());

	@ObjectClassDefinition(name = "Data Atlas Bootstrap Configuration (file mode)")
	public @interface Config {

		@AttributeDefinition(name = "Configuration URI",
				description = "URI (or plain file path) of the DataAtlasConfiguration XMI describing this instance. "
						+ "To honour an environment variable, supply e.g. "
						+ "\"$[env:DATA_ATLAS_CONFIG_URI;default=/opt/dataatlas/data/dataatlas.xmi]\" via a "
						+ "configuration picked up by the Felix configadmin interpolation plugin.")
		String config_uri();
	}

	private final ResourceSet resourceSet;
	private final ConfigurationRegistrar registrar;

	@Activate
	public DataAtlasBootstrap(BundleContext bundleContext,
			@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED) ResourceSet resourceSet, Config config) {
		this.resourceSet = resourceSet;
		this.registrar = new ConfigurationRegistrar(bundleContext);
		load(config.config_uri());
	}

	@Deactivate
	void deactivate() {
		registrar.unregisterAll();
	}

	private void load(String configUri) {
		if (configUri == null || configUri.isBlank() || configUri.contains("$[")) {
			// Blank or an un-interpolated configadmin template -> nothing to do.
			LOG.log(Level.INFO, "DataAtlasBootstrap: no configuration URI configured, skipping.");
			return;
		}
		URI uri = toUri(configUri);
		LOG.log(Level.INFO, () -> "DataAtlasBootstrap: loading Data Atlas configuration from " + uri);
		Resource resource = resourceSet.getResource(uri, true);
		if (resource.getContents().isEmpty()
				|| !(resource.getContents().get(0) instanceof DataAtlasConfiguration configuration)) {
			throw new IllegalStateException(
					"DataAtlasBootstrap: " + uri + " does not contain a DataAtlasConfiguration root");
		}
		EcoreUtil.resolveAll(resource);
		ConfigurationRegistrar.failOnUnresolvedProxies(configuration, uri.toString());
		absolutizeFileInputUris(configuration, resource.getURI());
		registrar.register(configuration);
	}

	private URI toUri(String value) {
		if (value.startsWith("file:")) {
			// normalize to EMF's canonical file URI form; java.net-style file URIs
			// with an empty authority (file:///C:/...) break URI.toFileString()
			return URI.createFileURI(Paths.get(java.net.URI.create(value)).toString());
		}
		// a "scheme" of length 1 is a Windows drive letter, not a URI scheme
		if (value.indexOf(":/") > 1) {
			return URI.createURI(value);
		}
		return URI.createFileURI(Paths.get(value).toAbsolutePath().toString());
	}

	/**
	 * Resolves relative FileDataInput URIs against the configuration resource,
	 * so consumers of the (detached) configuration object copies do not need
	 * the resource context.
	 */
	private void absolutizeFileInputUris(DataAtlasConfiguration configuration, URI base) {
		for (DataInput input : configuration.getDataInputs()) {
			if (input instanceof FileDataInput fileInput && fileInput.getUri() != null) {
				URI fileUri = URI.createURI(fileInput.getUri());
				if (fileUri.isRelative() && base != null) {
					fileInput.setUri(fileUri.resolve(base).toString());
				}
			}
		}
	}
}
