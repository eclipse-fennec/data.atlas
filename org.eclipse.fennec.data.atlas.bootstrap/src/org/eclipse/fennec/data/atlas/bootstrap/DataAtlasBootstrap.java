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
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.DataService;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.FileDataInput;
import org.eclipse.fennec.emf.osgi.configurator.EPackageConfigurator;
import org.eclipse.fennec.emf.osgi.constants.EMFNamespaces;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.PrototypeServiceFactory;
import org.osgi.framework.ServiceRegistration;
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
 * activation, registers the EPackages of the model types the configuration
 * references, and registers the contained configuration objects (the
 * {@code DataService}s and {@code DataInput}s) as OSGi services. Per-technology
 * configurator components pick those services up and create the actual runtime
 * pieces. Everything is unregistered on deactivation.
 * </p>
 *
 * <p>
 * Configuration objects are registered via a {@link PrototypeServiceFactory}
 * handing out {@link EcoreUtil#copy(EObject) copies}, so consumers can resolve
 * and adapt them without mutating the shared model. Note that non-containment
 * references of a copy (e.g. {@code configuration.dataSet}) still point into
 * the original, fully resolved configuration tree.
 * </p>
 */
@Component(name = DataAtlasBootstrap.PID, immediate = true, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = DataAtlasBootstrap.Config.class)
public class DataAtlasBootstrap {

	public static final String PID = "DataAtlasBootstrap";

	private static final Logger LOG = System.getLogger(DataAtlasBootstrap.class.getName());

	@ObjectClassDefinition(name = "Data Atlas Bootstrap Configuration")
	public @interface Config {

		@AttributeDefinition(name = "Configuration URI",
				description = "URI (or plain file path) of the DataAtlasConfiguration XMI describing this instance. "
						+ "To honour an environment variable, supply e.g. "
						+ "\"$[env:DATA_ATLAS_CONFIG_URI;default=/opt/dataatlas/data/dataatlas.xmi]\" via a "
						+ "configuration picked up by the Felix configadmin interpolation plugin.")
		String config_uri();
	}

	private final BundleContext bundleContext;
	private final ResourceSet resourceSet;

	private final List<ServiceRegistration<?>> registrations = new ArrayList<>();

	@Activate
	public DataAtlasBootstrap(BundleContext bundleContext,
			@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED) ResourceSet resourceSet, Config config) {
		this.bundleContext = bundleContext;
		this.resourceSet = resourceSet;
		load(config.config_uri());
	}

	@Deactivate
	void deactivate() {
		registrations.forEach(ServiceRegistration::unregister);
		registrations.clear();
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
		failOnUnresolvedProxies(resource);
		absolutizeFileInputUris(configuration, resource.getURI());
		registerReferencedEPackages(configuration);
		registerConfigurationObjects(configuration);
		LOG.log(Level.INFO,
				() -> "DataAtlasBootstrap: instance '" + configuration.getName() + "' bootstrapped ("
						+ configuration.getServices().size() + " service(s), "
						+ configuration.getDataInputs().size() + " input(s)).");
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

	private void failOnUnresolvedProxies(Resource resource) {
		Map<EObject, ?> unresolved = EcoreUtil.UnresolvedProxyCrossReferencer.find(resource);
		if (!unresolved.isEmpty()) {
			throw new IllegalStateException("DataAtlasBootstrap: configuration " + resource.getURI()
					+ " has unresolved references: " + unresolved.keySet());
		}
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

	/**
	 * Registers the EPackages of all EClasses the configuration references
	 * (input types, output types, supported classes), unless their nsURI is
	 * already registered. In Model Atlas mode this is where the packages would
	 * be resolved remotely instead.
	 */
	private void registerReferencedEPackages(DataAtlasConfiguration configuration) {
		Set<EPackage> ePackages = new LinkedHashSet<>();
		for (DataInput input : configuration.getDataInputs()) {
			input.getSupportedEClasses().forEach(c -> addPackage(ePackages, c));
		}
		for (DataSet dataSet : configuration.getDataSets()) {
			addPackage(ePackages, dataSet.getInputType());
			addPackage(ePackages, dataSet.getOutputType());
		}
		for (EPackage ePackage : ePackages) {
			if (EPackage.Registry.INSTANCE.containsKey(ePackage.getNsURI())) {
				continue;
			}
			Dictionary<String, Object> props = new Hashtable<>();
			props.put(EMFNamespaces.EMF_NAME, ePackage.getName());
			props.put(EMFNamespaces.EMF_MODEL_NSURI, ePackage.getNsURI());
			props.put(EMFNamespaces.EMF_MODEL_REGISTRATION, EMFNamespaces.MODEL_REGISTRATION_DYNAMIC);
			// the EPackage registry only picks up configurators with resourceset scope
			props.put(EMFNamespaces.EMF_MODEL_SCOPE, EMFNamespaces.EMF_MODEL_SCOPE_RESOURCE_SET);
			registrations.add(bundleContext.registerService(EPackageConfigurator.class,
					new DynamicEPackageConfigurator(ePackage), props));
			registrations.add(bundleContext.registerService(EPackage.class, ePackage, props));
			LOG.log(Level.INFO, () -> "DataAtlasBootstrap: registered EPackage " + ePackage.getNsURI());
		}
	}

	private void addPackage(Set<EPackage> ePackages, EClass eClass) {
		if (eClass != null && eClass.getEPackage() != null) {
			ePackages.add(eClass.getEPackage());
		}
	}

	private void registerConfigurationObjects(DataAtlasConfiguration configuration) {
		for (DataService service : configuration.getServices()) {
			registerConfigurationObject(configuration, service, service.getId());
		}
		for (DataInput input : configuration.getDataInputs()) {
			registerConfigurationObject(configuration, input, input.getId());
		}
	}

	private void registerConfigurationObject(DataAtlasConfiguration configuration, EObject object, String id) {
		if (id == null || id.isBlank()) {
			LOG.log(Level.WARNING, () -> "DataAtlasBootstrap: skipping configuration object without id: " + object);
			return;
		}
		String[] interfaces = serviceInterfaces(object);
		Dictionary<String, Object> props = new Hashtable<>();
		if (configuration.getName() != null) {
			props.put(DataAtlasConstants.ATLAS_NAME, configuration.getName());
		}
		props.put(DataAtlasConstants.CONFIG_OBJECT_ID, id);
		props.put(DataAtlasConstants.CONFIG_OBJECT_TYPE, object.eClass().getName());
		registrations.add(bundleContext.registerService(interfaces, new EObjectPrototypeFactory(object), props));
		LOG.log(Level.INFO, () -> "DataAtlasBootstrap: registered " + object.eClass().getName() + " '" + id + "'");
	}

	/**
	 * The generated model interface of the object's EClass plus all model
	 * supertypes (e.g. RestDataService, DataService, DataProvider).
	 */
	private String[] serviceInterfaces(EObject object) {
		// LinkedHashMap keyed by name to keep order and drop duplicates
		Map<String, String> names = new LinkedHashMap<>();
		addInstanceClassName(names, object.eClass());
		object.eClass().getEAllSuperTypes().forEach(s -> addInstanceClassName(names, s));
		return names.values().toArray(String[]::new);
	}

	private void addInstanceClassName(Map<String, String> names, EClass eClass) {
		String name = eClass.getInstanceClassName();
		if (name != null) {
			names.put(name, name);
		}
	}

	private static final class EObjectPrototypeFactory implements PrototypeServiceFactory<EObject> {

		private final EObject original;

		EObjectPrototypeFactory(EObject original) {
			this.original = original;
		}

		@Override
		public EObject getService(Bundle bundle, ServiceRegistration<EObject> registration) {
			return EcoreUtil.copy(original);
		}

		@Override
		public void ungetService(Bundle bundle, ServiceRegistration<EObject> registration, EObject service) {
			// nothing to release
		}
	}
}
