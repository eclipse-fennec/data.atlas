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
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.DataService;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.emf.osgi.configurator.EPackageConfigurator;
import org.eclipse.fennec.emf.osgi.constants.EMFNamespaces;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.PrototypeServiceFactory;
import org.osgi.framework.ServiceRegistration;

/**
 * The config-source-independent half of the bootstrap: registers the EPackages
 * a resolved {@code DataAtlasConfiguration} references and the configuration
 * objects themselves as OSGi services, and takes everything down again.
 *
 * <p>
 * Configuration objects are registered via a {@link PrototypeServiceFactory}
 * handing out {@link EcoreUtil#copy(EObject) copies}, so consumers can resolve
 * and adapt them without mutating the shared model. Note that non-containment
 * references of a copy (e.g. {@code configuration.dataSet}) still point into
 * the original, fully resolved configuration tree.
 * </p>
 */
class ConfigurationRegistrar {

	private static final Logger LOG = System.getLogger(ConfigurationRegistrar.class.getName());

	private final BundleContext bundleContext;
	private final List<ServiceRegistration<?>> registrations = new ArrayList<>();

	ConfigurationRegistrar(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	/**
	 * Fails fast when the (resolved) configuration still contains unresolved
	 * proxies — a bootstrap must never publish half-resolved config objects.
	 */
	static void failOnUnresolvedProxies(DataAtlasConfiguration configuration, String source) {
		Map<EObject, ?> unresolved = EcoreUtil.UnresolvedProxyCrossReferencer.find(configuration);
		if (!unresolved.isEmpty()) {
			throw new IllegalStateException(
					"Data Atlas configuration " + source + " has unresolved references: " + unresolved.keySet());
		}
	}

	/**
	 * Publishes the whole configuration: the referenced EPackages first, then
	 * the configuration objects.
	 */
	void register(DataAtlasConfiguration configuration) {
		registerReferencedEPackages(configuration);
		registerConfigurationObjects(configuration);
		LOG.log(Level.INFO,
				() -> "Data Atlas instance '" + configuration.getName() + "' bootstrapped ("
						+ configuration.getServices().size() + " service(s), "
						+ configuration.getDataInputs().size() + " input(s)).");
	}

	void unregisterAll() {
		registrations.forEach(ServiceRegistration::unregister);
		registrations.clear();
	}

	/**
	 * Registers the EPackages of all EClasses the configuration references
	 * (input types, output types, supported classes), unless their nsURI is
	 * already registered. In Model Atlas mode the packages have been resolved
	 * remotely by then; registering them here makes them available to the
	 * whole runtime (data loading, codec serialization) either way.
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
			// align the resource URI with the nsURI (InitialModelLoader precedent):
			// EClass URIs become canonical nsURI-based regardless of where the
			// package was loaded from (file path, atlas-client://...), which also
			// keeps the codec's XML schemaLocation deresolution working
			Resource resource = ePackage.eResource();
			if (resource != null && !ePackage.getNsURI().equals(String.valueOf(resource.getURI()))) {
				resource.setURI(URI.createURI(ePackage.getNsURI()));
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
			LOG.log(Level.INFO, () -> "Registered EPackage " + ePackage.getNsURI());
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
			LOG.log(Level.WARNING, () -> "Skipping configuration object without id: " + object);
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
		LOG.log(Level.INFO, () -> "Registered " + object.eClass().getName() + " '" + id + "'");
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
