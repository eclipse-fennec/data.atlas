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
package org.eclipse.fennec.data.atlas.rest;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.api.EObjectSource;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.RestDataService;
import org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.rest.DataServiceResource.DataSetEndpoint;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.jakartars.whiteboard.JakartarsWhiteboardConstants;
import org.osgi.service.jakartars.whiteboard.annotations.RequireJakartarsWhiteboard;
import org.osgi.service.servlet.whiteboard.annotations.RequireHttpWhiteboard;

import jakarta.ws.rs.core.Application;

/**
 * REST endpoint configurator: tracks {@code RestDataService} configuration
 * services and the {@link EObjectSource}s realizing their {@code DataInput}s.
 * As soon as every DataSet of a service is resolvable, one Jakarta-RS
 * whiteboard {@link Application} is registered under the service's
 * {@code urlContext}; it is unregistered when the configuration or a required
 * source goes away.
 *
 * <p>
 * The application carries {@code emf=true} so the fennec codec whiteboard
 * extensions (EObject/Resource message body handlers, per-request ResourceSet)
 * attach to it.
 * </p>
 */
@Component(immediate = true)
@RequireJakartarsWhiteboard
@RequireHttpWhiteboard
public class RestEndpointConfigurator {

	private static final Logger LOG = System.getLogger(RestEndpointConfigurator.class.getName());

	private final BundleContext bundleContext;

	// all access guarded by this
	private final Map<String, RestDataService> services = new HashMap<>();
	private final Map<String, EObjectSource> sources = new HashMap<>();
	private final Map<String, ServiceRegistration<Application>> applications = new HashMap<>();

	@Activate
	public RestEndpointConfigurator(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Deactivate
	synchronized void deactivate() {
		applications.values().forEach(ServiceRegistration::unregister);
		applications.clear();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addRestDataService(RestDataService service, Map<String, Object> props) {
		String id = configObjectId(props, service.getId());
		if (id == null) {
			LOG.log(Level.WARNING, () -> "Ignoring RestDataService without id: " + service);
			return;
		}
		services.put(id, service);
		reconcile();
	}

	synchronized void removeRestDataService(RestDataService service, Map<String, Object> props) {
		String id = configObjectId(props, service.getId());
		if (id != null) {
			services.remove(id);
			reconcile();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addEObjectSource(EObjectSource source, Map<String, Object> props) {
		Object inputId = props.get(DataAtlasConstants.INPUT_ID);
		if (inputId instanceof String id) {
			sources.put(id, source);
			reconcile();
		}
	}

	synchronized void removeEObjectSource(EObjectSource source, Map<String, Object> props) {
		Object inputId = props.get(DataAtlasConstants.INPUT_ID);
		if (inputId instanceof String id && sources.remove(id) != null) {
			reconcile();
		}
	}

	private String configObjectId(Map<String, Object> props, String fallback) {
		Object id = props.get(DataAtlasConstants.CONFIG_OBJECT_ID);
		return id instanceof String s ? s : fallback;
	}

	private void reconcile() {
		// tear down applications whose service is gone or no longer satisfiable
		applications.entrySet().removeIf(entry -> {
			RestDataService service = services.get(entry.getKey());
			if (service == null || resolveEndpoints(service) == null) {
				entry.getValue().unregister();
				LOG.log(Level.INFO, () -> "Unregistered REST application for service '" + entry.getKey() + "'");
				return true;
			}
			return false;
		});
		// bring up applications for satisfiable services
		services.forEach((id, service) -> {
			if (applications.containsKey(id)) {
				return;
			}
			Map<String, DataSetEndpoint> endpoints = resolveEndpoints(service);
			if (endpoints == null) {
				return;
			}
			applications.put(id, register(id, service, endpoints));
		});
	}

	/**
	 * Resolves the endpoint map (path -> DataSet/config/source) of a service,
	 * or {@code null} while a required {@link EObjectSource} is missing.
	 */
	private Map<String, DataSetEndpoint> resolveEndpoints(RestDataService service) {
		Map<String, DataSetEndpoint> endpoints = new LinkedHashMap<>();
		for (RestDataServiceConfiguration configuration : service.getConfiguration()) {
			DataSet dataSet = configuration.getDataSet();
			if (dataSet == null) {
				LOG.log(Level.WARNING, () -> "RestDataServiceConfiguration '" + configuration.getId()
						+ "' has no dataSet, skipping it");
				continue;
			}
			DataInput input = dataSet.getDataInput() != null ? dataSet.getDataInput() : service.getDataInput();
			if (input == null || input.getId() == null) {
				LOG.log(Level.WARNING, () -> "DataSet '" + dataSet.getId()
						+ "' resolves to no DataInput (neither own nor service default), skipping it");
				continue;
			}
			EObjectSource source = sources.get(input.getId());
			if (source == null) {
				return null; // required source not (yet) available
			}
			String path = configuration.getPath() != null ? configuration.getPath() : dataSet.getName();
			endpoints.put(path, new DataSetEndpoint(dataSet, configuration, source));
		}
		return endpoints.isEmpty() ? null : endpoints;
	}

	private ServiceRegistration<Application> register(String id, RestDataService service,
			Map<String, DataSetEndpoint> endpoints) {
		DataServiceResource resource = new DataServiceResource(service.getPaginationOffsetParameterName(),
				service.getPaginationSizeParameterName(), endpoints);
		String base = service.getUrlContext() == null || service.getUrlContext().isBlank() ? "/" + id
				: service.getUrlContext();
		if (!base.startsWith("/")) {
			base = "/" + base;
		}
		Dictionary<String, Object> props = new Hashtable<>();
		props.put(JakartarsWhiteboardConstants.JAKARTA_RS_APPLICATION_BASE, base);
		props.put(JakartarsWhiteboardConstants.JAKARTA_RS_NAME, "dataAtlas." + id);
		// let the fennec codec whiteboard extensions attach to this application
		props.put("emf", Boolean.TRUE);
		String logBase = base;
		LOG.log(Level.INFO, () -> "Registering REST application for service '" + id + "' at '" + logBase + "' with "
				+ endpoints.size() + " data set(s): " + endpoints.keySet());
		return bundleContext.registerService(Application.class, new DataAtlasRestApplication(resource), props);
	}
}
