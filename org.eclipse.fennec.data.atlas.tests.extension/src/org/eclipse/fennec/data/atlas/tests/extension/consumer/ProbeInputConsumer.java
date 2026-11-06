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
package org.eclipse.fennec.data.atlas.tests.extension.consumer;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.fennec.data.atlas.api.ConfigurationUris;
import org.eclipse.fennec.data.atlas.tests.extension.ProbeInput;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * The configurator of the third-party input type, binding its configuration
 * objects through DS like every Data Atlas configurator does (data.atlas#34).
 * For each bound {@link ProbeInput} it publishes a marker service carrying the
 * id and the mapping file resolved against the configuration document
 * (data.atlas#33), which the integration test looks for.
 */
@Component(service = {})
public class ProbeInputConsumer {

	/** Marker property: the id of a bound ProbeInput. */
	public static final String BOUND = "probe.bound";
	/** Marker property: the ProbeInput's mappingFile, resolved against the configuration. */
	public static final String MAPPING_FILE = "probe.mappingFile";

	private final BundleContext bundleContext;
	private final Map<String, ServiceRegistration<?>> markers = new HashMap<>();

	@Activate
	public ProbeInputConsumer(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Deactivate
	synchronized void deactivate() {
		markers.values().forEach(ServiceRegistration::unregister);
		markers.clear();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void bindProbeInput(ProbeInput input, Map<String, Object> properties) {
		Dictionary<String, Object> marker = new Hashtable<>();
		marker.put(BOUND, input.getId());
		String mappingFile = ConfigurationUris.resolve(input.getMappingFile(), properties);
		if (mappingFile != null) {
			marker.put(MAPPING_FILE, mappingFile);
		}
		markers.put(input.getId(), bundleContext.registerService(Object.class.getName(), new Object(), marker));
	}

	synchronized void unbindProbeInput(ProbeInput input, Map<String, Object> properties) {
		ServiceRegistration<?> marker = markers.remove(input.getId());
		if (marker != null) {
			marker.unregister();
		}
	}
}
