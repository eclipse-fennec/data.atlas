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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.codec.geojson.annotation.RequireCodecGeoJson;
import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.GeoJsonDataService;
import org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.rest.GeoJsonServiceResource.GeoJsonEndpoint;
import org.eclipse.fennec.model.query.Query;
import org.eclipse.fennec.persistence.repository.RepositoryConstants;
import org.eclipse.fennec.persistence.repository.api.ReadRepository;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentServiceObjects;
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
 * GeoJSON endpoint configurator: tracks {@code GeoJsonDataService}
 * configuration services and the {@link ReadRepository}s realizing their
 * {@code DataInput}s, exactly like its REST sibling
 * {@link RestEndpointConfigurator}. As soon as every DataSet of a service is
 * resolvable — its repository present, its query prepare-validated, its
 * Feature mapping valid against the DataSet's type — one Jakarta-RS whiteboard
 * {@link Application} is registered under the service's {@code urlContext},
 * serving RFC 7946 {@code FeatureCollection}s/{@code Feature}s through the
 * fennec GeoJSON codec ({@code application/geo+json}).
 *
 * <p>
 * The GeoJSON codec bundle registers the content types the message body writer
 * resolves the factory by (emf.codec#168); {@code @RequireCodecGeoJson}
 * (emf.codec#201) turns that into a real OSGi requirement, so a runtime
 * missing the codec fails to resolve instead of failing per request.
 * </p>
 */
@Component(immediate = true)
@RequireJakartarsWhiteboard
@RequireHttpWhiteboard
@RequireCodecGeoJson
public class GeoJsonEndpointConfigurator {

	private static final Logger LOG = System.getLogger(GeoJsonEndpointConfigurator.class.getName());

	private final BundleContext bundleContext;

	// all access guarded by this
	private final Map<String, GeoJsonDataService> services = new HashMap<>();
	private final Map<String, ComponentServiceObjects<ReadRepository>> repositories = new HashMap<>();
	private final Map<String, ServiceRegistration<Application>> applications = new HashMap<>();

	@Activate
	public GeoJsonEndpointConfigurator(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	@Deactivate
	synchronized void deactivate() {
		applications.values().forEach(ServiceRegistration::unregister);
		applications.clear();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addGeoJsonDataService(GeoJsonDataService service, Map<String, Object> props) {
		String id = configObjectId(props, service.getId());
		if (id == null) {
			LOG.log(Level.WARNING, () -> "Ignoring GeoJsonDataService without id: " + service);
			return;
		}
		services.put(id, service);
		reconcile();
	}

	synchronized void removeGeoJsonDataService(GeoJsonDataService service, Map<String, Object> props) {
		String id = configObjectId(props, service.getId());
		if (id != null) {
			services.remove(id);
			reconcile();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addReadRepository(ComponentServiceObjects<ReadRepository> repository,
			Map<String, Object> props) {
		if (props.get(RepositoryConstants.REPOSITORY_ID) instanceof String id) {
			repositories.put(id, repository);
			reconcile();
		}
	}

	synchronized void removeReadRepository(ComponentServiceObjects<ReadRepository> repository,
			Map<String, Object> props) {
		if (props.get(RepositoryConstants.REPOSITORY_ID) instanceof String id && repositories.remove(id) != null) {
			reconcile();
		}
	}

	private String configObjectId(Map<String, Object> props, String fallback) {
		Object id = props.get(DataAtlasConstants.CONFIG_OBJECT_ID);
		return id instanceof String s ? s : fallback;
	}

	private void reconcile() {
		applications.entrySet().removeIf(entry -> {
			GeoJsonDataService service = services.get(entry.getKey());
			if (service == null || resolveEndpoints(service) == null) {
				entry.getValue().unregister();
				LOG.log(Level.INFO, () -> "Unregistered GeoJSON application for service '" + entry.getKey() + "'");
				return true;
			}
			return false;
		});
		services.forEach((id, service) -> {
			if (applications.containsKey(id)) {
				return;
			}
			Map<String, GeoJsonEndpoint> endpoints = resolveEndpoints(service);
			if (endpoints == null) {
				return;
			}
			applications.put(id, register(id, service, endpoints));
		});
	}

	/**
	 * Resolves the endpoint map of a service, or {@code null} while a required
	 * {@link ReadRepository} is missing. DataSets with configuration errors —
	 * no dataSet, no resolvable input, a refused query, an invalid Feature
	 * mapping — are skipped with a log message (fail-early gating).
	 */
	private Map<String, GeoJsonEndpoint> resolveEndpoints(GeoJsonDataService service) {
		Map<String, GeoJsonEndpoint> endpoints = new LinkedHashMap<>();
		for (GeoJsonDataServiceConfiguration configuration : service.getConfiguration()) {
			DataSet dataSet = configuration.getDataSet();
			if (dataSet == null) {
				LOG.log(Level.WARNING, () -> "GeoJsonDataServiceConfiguration '" + configuration.getId()
						+ "' has no dataSet, skipping it");
				continue;
			}
			DataInput input = dataSet.getDataInput() != null ? dataSet.getDataInput() : service.getDataInput();
			if (input == null || input.getId() == null) {
				LOG.log(Level.WARNING, () -> "DataSet '" + dataSet.getId()
						+ "' resolves to no DataInput (neither own nor service default), skipping it");
				continue;
			}
			ComponentServiceObjects<ReadRepository> repository = repositories.get(input.getId());
			if (repository == null) {
				return null; // required repository not (yet) available
			}
			if (dataSet.getQuery() != null && !validateQuery(dataSet, repository)) {
				continue;
			}
			GeoJsonFeatureMapper mapper;
			try {
				mapper = GeoJsonFeatureMapper.create(configuration, dataSet.getInputType());
			} catch (IllegalArgumentException invalid) {
				LOG.log(Level.ERROR, () -> "DataSet '" + dataSet.getId()
						+ "': invalid GeoJSON mapping, skipping the endpoint: " + invalid.getMessage());
				continue;
			}
			String path = configuration.getPath() != null ? configuration.getPath() : dataSet.getName();
			endpoints.put(path, new GeoJsonEndpoint(dataSet, configuration, repository, mapper));
		}
		return endpoints.isEmpty() ? null : endpoints;
	}

	/** The same fail-early query gate as the REST sibling. */
	private boolean validateQuery(DataSet dataSet, ComponentServiceObjects<ReadRepository> repository) {
		Query query = dataSet.getQuery();
		EClass from = query.getFrom();
		EClass inputType = dataSet.getInputType();
		boolean sameType = from != null && inputType != null && (from == inputType
				|| (from.getName().equals(inputType.getName())
						&& from.getEPackage() != null && inputType.getEPackage() != null
						&& from.getEPackage().getNsURI().equals(inputType.getEPackage().getNsURI())));
		if (!sameType) {
			LOG.log(Level.ERROR, () -> "DataSet '" + dataSet.getId()
					+ "': query.from does not match inputType, skipping the endpoint");
			return false;
		}
		ReadRepository instance = repository.getService();
		try {
			instance.prepare(EcoreUtil.copy(query));
			return true;
		} catch (Exception e) {
			LOG.log(Level.ERROR, () -> "DataSet '" + dataSet.getId()
					+ "': the backing repository refused the query, skipping the endpoint: " + e.getMessage());
			return false;
		} finally {
			repository.ungetService(instance);
		}
	}

	private ServiceRegistration<Application> register(String id, GeoJsonDataService service,
			Map<String, GeoJsonEndpoint> endpoints) {
		GeoJsonServiceResource resource = new GeoJsonServiceResource(service.getPaginationOffsetParameterName(),
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
		LOG.log(Level.INFO, () -> "Registering GeoJSON application for service '" + id + "' at '" + logBase
				+ "' with " + endpoints.size() + " data set(s): " + endpoints.keySet());
		return bundleContext.registerService(Application.class, new DataAtlasRestApplication(resource), props);
	}
}
