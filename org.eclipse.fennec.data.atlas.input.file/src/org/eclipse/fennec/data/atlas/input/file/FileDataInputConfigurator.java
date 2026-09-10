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
package org.eclipse.fennec.data.atlas.input.file;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.Map;

import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.configuration.FileDataInput;
import org.eclipse.fennec.emf.osgi.ResourceSetFactory;
import org.eclipse.fennec.persistence.repository.RepositoryConstants;
import org.eclipse.fennec.persistence.repository.api.ReadRepository;
import org.eclipse.fennec.persistence.repository.api.RepositoryService;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.PrototypeServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Input configurator: for every {@code FileDataInput} configuration service it
 * registers one read-only, file-backed {@link ReadRepository}, correlated via
 * {@link RepositoryConstants#REPOSITORY_ID} = the input's id; the repository is
 * unregistered when the configuration service goes away.
 */
@Component(immediate = true)
public class FileDataInputConfigurator {

	private static final Logger LOG = System.getLogger(FileDataInputConfigurator.class.getName());

	private final BundleContext bundleContext;
	private final ResourceSetFactory resourceSetFactory;

	private record Registered(ServiceRegistration<?> registration) {
	}

	/** One {@link FileReadRepository} per lease; disposed when the lease ends. */
	private static final class RepositoryFactory implements PrototypeServiceFactory<FileReadRepository> {

		private final FileDataInput input;
		private final ResourceSetFactory resourceSetFactory;

		RepositoryFactory(FileDataInput input, ResourceSetFactory resourceSetFactory) {
			this.input = input;
			this.resourceSetFactory = resourceSetFactory;
		}

		@Override
		public FileReadRepository getService(Bundle bundle, ServiceRegistration<FileReadRepository> registration) {
			return new FileReadRepository(input, resourceSetFactory);
		}

		@Override
		public void ungetService(Bundle bundle, ServiceRegistration<FileReadRepository> registration,
				FileReadRepository service) {
			service.dispose();
		}
	}

	private final Map<FileDataInput, Registered> registrations = new IdentityHashMap<>();

	@Activate
	public FileDataInputConfigurator(BundleContext bundleContext, @Reference ResourceSetFactory resourceSetFactory) {
		this.bundleContext = bundleContext;
		this.resourceSetFactory = resourceSetFactory;
	}

	@Deactivate
	void deactivate() {
		synchronized (registrations) {
			registrations.values().forEach(FileDataInputConfigurator::unregister);
			registrations.clear();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	void addFileDataInput(FileDataInput input, Map<String, Object> serviceProps) {
		if (input.getId() == null || input.getUri() == null) {
			LOG.log(Level.WARNING, () -> "Ignoring FileDataInput without id or uri: " + input);
			return;
		}
		Dictionary<String, Object> props = new Hashtable<>();
		props.put(RepositoryConstants.REPOSITORY_ID, input.getId());
		props.put(RepositoryConstants.REPOSITORY_BASE_URI, input.getUri());
		props.put(RepositoryConstants.REPOSITORY_BACKEND, "file");
		props.put(RepositoryConstants.REPOSITORY_READ_ONLY, Boolean.TRUE);
		Object atlasName = serviceProps.get(DataAtlasConstants.ATLAS_NAME);
		if (atlasName != null) {
			props.put(DataAtlasConstants.ATLAS_NAME, atlasName);
		}
		// prototype scope is the repository facade's contract (a repository instance
		// owns a ResourceSet); consumers lease one instance per unit of work
		ServiceRegistration<?> registration = bundleContext.registerService(
				new String[] { RepositoryService.class.getName(), ReadRepository.class.getName() },
				new RepositoryFactory(input, resourceSetFactory), props);
		synchronized (registrations) {
			registrations.put(input, new Registered(registration));
		}
		LOG.log(Level.INFO, () -> "Registered file-backed ReadRepository for FileDataInput '" + input.getId() + "' ("
				+ input.getUri() + ")");
	}

	void removeFileDataInput(FileDataInput input, Map<String, Object> serviceProps) {
		Registered registered;
		synchronized (registrations) {
			registered = registrations.remove(input);
		}
		if (registered != null) {
			unregister(registered);
		}
	}

	private static void unregister(Registered registered) {
		registered.registration().unregister();
	}
}
