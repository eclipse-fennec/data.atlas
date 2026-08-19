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
import org.eclipse.fennec.data.atlas.api.EObjectSource;
import org.eclipse.fennec.data.atlas.configuration.FileDataInput;
import org.eclipse.fennec.emf.osgi.ResourceSetFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * Input configurator: for every {@code FileDataInput} configuration service it
 * registers one {@link EObjectSource}, correlated via
 * {@link DataAtlasConstants#INPUT_ID}; the source is unregistered when the
 * configuration service goes away.
 */
@Component(immediate = true)
public class FileDataInputConfigurator {

	private static final Logger LOG = System.getLogger(FileDataInputConfigurator.class.getName());

	private final BundleContext bundleContext;
	private final ResourceSetFactory resourceSetFactory;

	private final Map<FileDataInput, ServiceRegistration<EObjectSource>> registrations = new IdentityHashMap<>();

	@Activate
	public FileDataInputConfigurator(BundleContext bundleContext, @Reference ResourceSetFactory resourceSetFactory) {
		this.bundleContext = bundleContext;
		this.resourceSetFactory = resourceSetFactory;
	}

	@Deactivate
	void deactivate() {
		synchronized (registrations) {
			registrations.values().forEach(ServiceRegistration::unregister);
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
		props.put(DataAtlasConstants.INPUT_ID, input.getId());
		Object atlasName = serviceProps.get(DataAtlasConstants.ATLAS_NAME);
		if (atlasName != null) {
			props.put(DataAtlasConstants.ATLAS_NAME, atlasName);
		}
		FileEObjectSource source = new FileEObjectSource(input, resourceSetFactory);
		synchronized (registrations) {
			registrations.put(input, bundleContext.registerService(EObjectSource.class, source, props));
		}
		LOG.log(Level.INFO, () -> "Registered EObjectSource for FileDataInput '" + input.getId() + "' ("
				+ input.getUri() + ")");
	}

	void removeFileDataInput(FileDataInput input, Map<String, Object> serviceProps) {
		ServiceRegistration<EObjectSource> registration;
		synchronized (registrations) {
			registration = registrations.remove(input);
		}
		if (registration != null) {
			registration.unregister();
		}
	}
}
