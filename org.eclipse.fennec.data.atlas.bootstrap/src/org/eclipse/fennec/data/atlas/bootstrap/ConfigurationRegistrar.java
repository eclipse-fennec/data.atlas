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
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.TreeIterator;
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
import org.eclipse.fennec.data.atlas.configuration.Transformation;
import org.eclipse.fennec.emf.osgi.configurator.EPackageConfigurator;
import org.eclipse.fennec.emf.osgi.constants.EMFNamespaces;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.PrototypeServiceFactory;
import org.osgi.framework.ServiceRegistration;

/**
 * The config-source-independent half of the bootstrap: publishes the EPackages
 * a resolved {@code DataAtlasConfiguration} references and the configuration
 * objects themselves as OSGi services, applies configuration <em>changes</em>
 * as a diff, and takes everything down again.
 *
 * <p>
 * {@link #apply(DataAtlasConfiguration)} diffs the new configuration against
 * the currently published state by configuration object id: unchanged objects
 * keep their service registration (their runtime pieces stay up), changed
 * objects are re-registered, removed objects are unregistered. An object only
 * counts as unchanged when it is structurally equal <em>and</em> none of the
 * EPackages it references were replaced in the same apply — otherwise its
 * EClass references would go stale against the published packages. Package
 * replacement is detected by instance identity: a config source that returns
 * the same EPackage instances across fetches (the Model Atlas client cache)
 * gets fine-grained diffs; a source that re-loads from scratch (file mode)
 * swaps consistently.
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
class ConfigurationRegistrar {

	private static final Logger LOG = System.getLogger(ConfigurationRegistrar.class.getName());

	/** Grace period before "publications declared, no handler installed" warns. */
	private static final long PUBLICATION_HANDLER_GRACE_SECONDS = 30;

	private record PackageEntry(EPackage ePackage, ServiceRegistration<?> configuratorRegistration,
			ServiceRegistration<?> packageRegistration) {
	}

	private record ObjectEntry(EObject original, ServiceRegistration<?> registration) {
	}

	private final BundleContext bundleContext;

	// both keyed state maps are guarded by this
	private final Map<String, PackageEntry> ePackages = new LinkedHashMap<>();
	private final Map<String, ObjectEntry> objects = new LinkedHashMap<>();

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
	 * Publishes the configuration, diffing against the currently published
	 * state: the referenced EPackages first, then the configuration objects.
	 */
	synchronized void apply(DataAtlasConfiguration configuration) {
		Set<String> replacedNsUris = applyEPackages(configuration);
		applyConfigurationObjects(configuration, replacedNsUris);
		diagnosePublicationDeclarations(configuration);
		LOG.log(Level.INFO,
				() -> "Data Atlas instance '" + configuration.getName() + "' published ("
						+ configuration.getServices().size() + " service(s), "
						+ configuration.getDataInputs().size() + " input(s)).");
	}

	/**
	 * A configuration that declares publications while no publication handler
	 * bundle is installed is a diagnosed configuration error, not a silently
	 * ignored declaration (data.atlas issue #4, DA-DCAT-3). The affected
	 * providers still start and serve — this only warns, keyed on the abstract
	 * {@link DataAtlasConstants#PUBLICATION_HANDLER} marker so the core stays
	 * free of any publication-kind dependency. The check is repeated once after
	 * a grace period, because the handler bundle may simply start later.
	 */
	private void diagnosePublicationDeclarations(DataAtlasConfiguration configuration) {
		long declared = Stream.concat(configuration.getServices().stream(), configuration.getDataSets().stream())
				.filter(provider -> provider.getPublication() != null).count();
		if (declared == 0 || publicationHandlerPresent()) {
			return;
		}
		String name = configuration.getName();
		CompletableFuture.delayedExecutor(PUBLICATION_HANDLER_GRACE_SECONDS, TimeUnit.SECONDS).execute(() -> {
			if (!publicationHandlerPresent()) {
				LOG.log(Level.WARNING, () -> "Data Atlas instance '" + name + "' declares " + declared
						+ " publication(s), but no publication handler is installed — the declarations are NOT "
						+ "published. Install the publication bundle (e.g. "
						+ "org.eclipse.fennec.data.atlas.publication.dcat) or remove the declarations. "
						+ "The data services serve regardless.");
			}
		});
	}

	private boolean publicationHandlerPresent() {
		try {
			var references = bundleContext.getAllServiceReferences(null,
					"(" + DataAtlasConstants.PUBLICATION_HANDLER + "=*)");
			return references != null && references.length > 0;
		} catch (InvalidSyntaxException unreachable) {
			return true;
		}
	}

	synchronized void unregisterAll() {
		objects.values().forEach(entry -> entry.registration().unregister());
		objects.clear();
		ePackages.values().forEach(ConfigurationRegistrar::unregister);
		ePackages.clear();
	}

	/**
	 * Diffs the referenced EPackages against the published ones (keyed by
	 * nsURI, compared by instance identity) and returns the nsURIs whose
	 * published package instance was replaced or removed in this apply.
	 */
	private Set<String> applyEPackages(DataAtlasConfiguration configuration) {
		Map<String, EPackage> desired = new LinkedHashMap<>();
		for (DataInput input : configuration.getDataInputs()) {
			input.getSupportedEClasses().forEach(c -> addPackage(desired, c));
		}
		for (DataSet dataSet : configuration.getDataSets()) {
			addPackage(desired, dataSet.getInputType());
			addPackage(desired, dataSet.getOutputType());
		}

		Set<String> replaced = new HashSet<>();
		// remove or replace published packages
		for (String nsUri : List.copyOf(ePackages.keySet())) {
			PackageEntry entry = ePackages.get(nsUri);
			EPackage wanted = desired.get(nsUri);
			if (wanted == entry.ePackage()) {
				continue; // identical instance: keep as-is
			}
			unregister(ePackages.remove(nsUri));
			replaced.add(nsUri);
			LOG.log(Level.INFO, () -> (wanted == null ? "Unregistered EPackage " : "Replacing EPackage ") + nsUri);
		}
		// register new (or replacing) packages
		for (EPackage ePackage : desired.values()) {
			String nsUri = ePackage.getNsURI();
			if (ePackages.containsKey(nsUri)) {
				continue;
			}
			if (!replaced.contains(nsUri) && EPackage.Registry.INSTANCE.containsKey(nsUri)) {
				// someone else (a model bundle, another instance) provides it
				continue;
			}
			registerEPackage(ePackage);
		}
		return replaced;
	}

	private void registerEPackage(EPackage ePackage) {
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
		ServiceRegistration<?> configurator = bundleContext.registerService(EPackageConfigurator.class,
				new DynamicEPackageConfigurator(ePackage), props);
		ServiceRegistration<?> packageRegistration = bundleContext.registerService(EPackage.class, ePackage, props);
		ePackages.put(ePackage.getNsURI(), new PackageEntry(ePackage, configurator, packageRegistration));
		LOG.log(Level.INFO, () -> "Registered EPackage " + ePackage.getNsURI());
	}

	private static void unregister(PackageEntry entry) {
		entry.packageRegistration().unregister();
		entry.configuratorRegistration().unregister();
	}

	private void addPackage(Map<String, EPackage> desired, EClass eClass) {
		if (eClass != null && eClass.getEPackage() != null && eClass.getEPackage().getNsURI() != null) {
			desired.putIfAbsent(eClass.getEPackage().getNsURI(), eClass.getEPackage());
		}
	}

	private void applyConfigurationObjects(DataAtlasConfiguration configuration, Set<String> replacedNsUris) {
		Map<String, EObject> desired = new LinkedHashMap<>();
		for (DataService service : configuration.getServices()) {
			addObject(desired, service, service.getId());
		}
		for (DataInput input : configuration.getDataInputs()) {
			addObject(desired, input, input.getId());
		}
		for (Transformation transformation : configuration.getTransformations()) {
			addObject(desired, transformation, transformation.getId());
		}

		// unregister removed objects
		for (String id : List.copyOf(objects.keySet())) {
			if (!desired.containsKey(id)) {
				objects.remove(id).registration().unregister();
				LOG.log(Level.INFO, () -> "Unregistered configuration object '" + id + "'");
			}
		}
		// register added and changed objects
		for (Map.Entry<String, EObject> wanted : desired.entrySet()) {
			String id = wanted.getKey();
			EObject object = wanted.getValue();
			ObjectEntry current = objects.get(id);
			if (current != null && EcoreUtil.equals(current.original(), object)
					&& !referencesReplacedPackage(object, replacedNsUris)) {
				continue; // unchanged: its runtime pieces stay up
			}
			if (current != null) {
				objects.remove(id).registration().unregister();
				LOG.log(Level.INFO, () -> "Re-registering changed configuration object '" + id + "'");
			}
			registerConfigurationObject(configuration, object, id);
		}
	}

	private void addObject(Map<String, EObject> desired, EObject object, String id) {
		if (id == null || id.isBlank()) {
			LOG.log(Level.WARNING, () -> "Skipping configuration object without id: " + object);
			return;
		}
		if (desired.putIfAbsent(id, object) != null) {
			LOG.log(Level.WARNING, () -> "Duplicate configuration object id '" + id + "', keeping the first");
		}
	}

	/**
	 * Whether the object (or its containment tree) references an EClass whose
	 * published EPackage instance was replaced in this apply — such an object
	 * must be re-registered even if structurally unchanged, or its EClass
	 * references would point at a package the runtime no longer publishes.
	 */
	private boolean referencesReplacedPackage(EObject object, Set<String> replacedNsUris) {
		if (replacedNsUris.isEmpty()) {
			return false;
		}
		for (TreeIterator<EObject> it = EcoreUtil.getAllContents(object, false); it.hasNext();) {
			for (EObject referenced : it.next().eCrossReferences()) {
				if (referenced instanceof EClass eClass && eClass.getEPackage() != null
						&& replacedNsUris.contains(eClass.getEPackage().getNsURI())) {
					return true;
				}
			}
		}
		return false;
	}

	private void registerConfigurationObject(DataAtlasConfiguration configuration, EObject object, String id) {
		String[] interfaces = serviceInterfaces(object);
		Dictionary<String, Object> props = new Hashtable<>();
		if (configuration.getName() != null) {
			props.put(DataAtlasConstants.ATLAS_NAME, configuration.getName());
		}
		props.put(DataAtlasConstants.CONFIG_OBJECT_ID, id);
		props.put(DataAtlasConstants.CONFIG_OBJECT_TYPE, object.eClass().getName());
		ServiceRegistration<?> registration = bundleContext.registerService(interfaces,
				new EObjectPrototypeFactory(object), props);
		objects.put(id, new ObjectEntry(object, registration));
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
