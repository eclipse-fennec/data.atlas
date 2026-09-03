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
package org.eclipse.fennec.data.atlas.input.bridge;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.api.DataTransformer;
import org.eclipse.fennec.data.atlas.configuration.BridgeRepository;
import org.eclipse.fennec.emf.osgi.ResourceSetFactory;
import org.eclipse.fennec.persistence.repository.RepositoryConstants;
import org.eclipse.fennec.persistence.repository.api.ReadRepository;
import org.eclipse.fennec.persistence.repository.api.RepositoryService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Input configurator: tracks {@code BridgeRepository} configuration services
 * together with the {@link ReadRepository} realizing their {@code source}
 * input and the {@link DataTransformer} realizing their {@code dataTrafo}. As
 * soon as both are present, one read-only bridge {@link ReadRepository} is
 * registered (correlated via {@link RepositoryConstants#REPOSITORY_ID} = the
 * bridge's id); it is unregistered when the configuration, the source
 * repository or the transformer goes away. Cascaded bridges work naturally:
 * a bridge's repository can be another bridge's source.
 */
@Component(immediate = true)
public class BridgeInputConfigurator {

	private static final Logger LOG = System.getLogger(BridgeInputConfigurator.class.getName());

	private final BundleContext bundleContext;
	private final ResourceSetFactory resourceSetFactory;

	// all access guarded by this
	private final Map<String, TrackedBridge> bridges = new HashMap<>();
	private final Map<String, ComponentServiceObjects<ReadRepository>> repositories = new HashMap<>();
	private final Map<String, DataTransformer> transformers = new HashMap<>();
	private final Map<String, Registered> registrations = new HashMap<>();
	// re-entrancy guard: registering a bridge repository fires a service event
	// SYNCHRONOUSLY back into addReadRepository on the same thread (this
	// configurator deliberately tracks the very type it registers, so bridges
	// can cascade) - without the guard that nested reconcile registers the
	// same bridge again before the outer one recorded it: infinite recursion
	private boolean reconciling;
	private boolean reconcileAgain;

	private record TrackedBridge(BridgeRepository bridge, Object atlasName) {
	}

	private record Registered(BridgeReadRepository repository, ServiceRegistration<?> registration) {
	}

	@Activate
	public BridgeInputConfigurator(BundleContext bundleContext, @Reference ResourceSetFactory resourceSetFactory) {
		this.bundleContext = bundleContext;
		this.resourceSetFactory = resourceSetFactory;
	}

	@Deactivate
	synchronized void deactivate() {
		registrations.values().forEach(BridgeInputConfigurator::unregister);
		registrations.clear();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addBridgeRepository(BridgeRepository bridge, Map<String, Object> serviceProps) {
		String id = bridge.getId();
		if (id == null || id.isBlank()) {
			LOG.log(Level.WARNING, () -> "Ignoring BridgeRepository without id: " + bridge);
			return;
		}
		bridges.put(id, new TrackedBridge(bridge, serviceProps.get(DataAtlasConstants.ATLAS_NAME)));
		reconcile();
	}

	synchronized void removeBridgeRepository(BridgeRepository bridge, Map<String, Object> serviceProps) {
		if (bridge.getId() != null && bridges.remove(bridge.getId()) != null) {
			reconcile();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addReadRepository(ComponentServiceObjects<ReadRepository> repository,
			Map<String, Object> props) {
		if (props.get(RepositoryConstants.REPOSITORY_ID) instanceof String repositoryId) {
			repositories.put(repositoryId, repository);
			reconcile();
		}
	}

	synchronized void removeReadRepository(ComponentServiceObjects<ReadRepository> repository,
			Map<String, Object> props) {
		if (props.get(RepositoryConstants.REPOSITORY_ID) instanceof String repositoryId
				&& repositories.remove(repositoryId) != null) {
			reconcile();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addDataTransformer(DataTransformer transformer, Map<String, Object> props) {
		if (props.get(DataAtlasConstants.TRANSFORMATION_ID) instanceof String transformationId) {
			transformers.put(transformationId, transformer);
			reconcile();
		}
	}

	synchronized void removeDataTransformer(DataTransformer transformer, Map<String, Object> props) {
		if (props.get(DataAtlasConstants.TRANSFORMATION_ID) instanceof String transformationId
				&& transformers.remove(transformationId) != null) {
			reconcile();
		}
	}

	private void reconcile() {
		if (reconciling) {
			reconcileAgain = true;
			return;
		}
		reconciling = true;
		try {
			do {
				reconcileAgain = false;
				doReconcile();
			} while (reconcileAgain);
		} finally {
			reconciling = false;
		}
	}

	private void doReconcile() {
		// tear down bridges whose configuration or dependencies are gone
		registrations.entrySet().removeIf(entry -> {
			TrackedBridge tracked = bridges.get(entry.getKey());
			if (tracked == null || resolve(tracked.bridge()) == null) {
				unregister(entry.getValue());
				LOG.log(Level.INFO, () -> "Unregistered bridge ReadRepository '" + entry.getKey() + "'");
				return true;
			}
			return false;
		});
		// bring up bridges whose dependencies are satisfied
		bridges.forEach((id, tracked) -> {
			if (registrations.containsKey(id)) {
				return;
			}
			Resolved resolved = resolve(tracked.bridge());
			if (resolved == null) {
				return;
			}
			registrations.put(id, register(id, tracked, resolved));
		});
	}

	private record Resolved(ComponentServiceObjects<ReadRepository> source, DataTransformer transformer) {
	}

	/**
	 * Resolves the bridge's dependencies, or {@code null} while one is
	 * missing. Configuration errors (no source/dataTrafo, an unsupported
	 * queryTrafo, a transformer whose types do not fit) are reported loudly —
	 * such a bridge never comes up.
	 */
	private Resolved resolve(BridgeRepository bridge) {
		String id = bridge.getId();
		if (bridge.getSource() == null || bridge.getSource().getId() == null) {
			LOG.log(Level.ERROR, () -> "BridgeRepository '" + id + "' has no resolvable source input");
			return null;
		}
		if (bridge.getDataTrafo() == null || bridge.getDataTrafo().getId() == null) {
			LOG.log(Level.ERROR, () -> "BridgeRepository '" + id + "' has no resolvable dataTrafo");
			return null;
		}
		if (bridge.getQueryTrafo() != null) {
			LOG.log(Level.ERROR, () -> "BridgeRepository '" + id
					+ "' configures a queryTrafo, which is not supported yet - the bridge stays down");
			return null;
		}
		ComponentServiceObjects<ReadRepository> source = repositories.get(bridge.getSource().getId());
		if (source == null) {
			return null; // source repository not (yet) available
		}
		DataTransformer transformer = transformers.get(bridge.getDataTrafo().getId());
		if (transformer == null) {
			return null; // transformer not (yet) available
		}
		if (!bridge.getSupportedEClasses().isEmpty()
				&& bridge.getSupportedEClasses().stream().noneMatch(e -> sameType(e, transformer.outputType()))) {
			LOG.log(Level.ERROR, () -> "BridgeRepository '" + id + "' declares supportedEClasses that do not "
					+ "contain the transformation's result type '" + transformer.outputType().getName()
					+ "' - the bridge stays down");
			return null;
		}
		return new Resolved(source, transformer);
	}

	private boolean sameType(EClass a, EClass b) {
		if (a == b) {
			return true;
		}
		return a != null && b != null && a.getName().equals(b.getName()) && a.getEPackage() != null
				&& b.getEPackage() != null && a.getEPackage().getNsURI() != null
				&& a.getEPackage().getNsURI().equals(b.getEPackage().getNsURI());
	}

	private Registered register(String id, TrackedBridge tracked, Resolved resolved) {
		BridgeReadRepository repository = new BridgeReadRepository(id, resolved.source(), resolved.transformer(),
				resourceSetFactory);
		Dictionary<String, Object> props = new Hashtable<>();
		props.put(RepositoryConstants.REPOSITORY_ID, id);
		props.put(RepositoryConstants.REPOSITORY_BASE_URI, repository.baseUri().toString());
		props.put(RepositoryConstants.REPOSITORY_BACKEND, "bridge");
		props.put(RepositoryConstants.REPOSITORY_READ_ONLY, Boolean.TRUE);
		if (tracked.atlasName() != null) {
			props.put(DataAtlasConstants.ATLAS_NAME, tracked.atlasName());
		}
		ServiceRegistration<?> registration = bundleContext.registerService(
				new String[] { RepositoryService.class.getName(), ReadRepository.class.getName() }, repository, props);
		LOG.log(Level.INFO, () -> "Registered bridge ReadRepository '" + id + "' (source '"
				+ tracked.bridge().getSource().getId() + "', transformation '" + tracked.bridge().getDataTrafo().getId()
				+ "')");
		return new Registered(repository, registration);
	}

	private static void unregister(Registered registered) {
		registered.registration().unregister();
		registered.repository().dispose();
	}
}
