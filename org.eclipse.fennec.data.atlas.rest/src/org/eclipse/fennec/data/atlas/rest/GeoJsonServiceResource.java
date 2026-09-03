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

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration;
import org.eclipse.fennec.model.query.ParameterDecl;
import org.eclipse.fennec.model.query.Query;
import org.eclipse.fennec.model.query.builder.QueryBuilder;
import org.eclipse.fennec.persistence.query.api.QueryResult;
import org.eclipse.fennec.persistence.repository.api.ReadRepository;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonFactory;
import org.osgi.service.component.ComponentServiceObjects;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * Jakarta-RS resource of one {@code GeoJsonDataService}: serves the configured
 * DataSets as RFC 7946 GeoJSON — {@code GET {path}} returns a
 * {@code FeatureCollection}, {@code GET {path}/{id}} a single {@code Feature} —
 * under the media type {@code application/geo+json} (plus the pre-RFC alias).
 * The domain objects are mapped by the endpoint's {@link GeoJsonFeatureMapper};
 * serialization happens in the fennec codec's message body writer, which
 * resolves the GeoJSON resource factory through the negotiated content type.
 *
 * <p>
 * The read plumbing mirrors {@link DataServiceResource}: per-request repository
 * leases, the DataSet's canonical query with the REST pagination overlaid on
 * {@code skip}/{@code top} of a per-request copy, and declared query parameters
 * bound from HTTP query parameters.
 * </p>
 */
@Path("/")
@Produces({ GeoJsonServiceResource.APPLICATION_GEO_JSON, GeoJsonServiceResource.APPLICATION_GEO_JSON_LEGACY })
public class GeoJsonServiceResource {

	/** {@code application/geo+json} — the RFC 7946 media type. */
	public static final String APPLICATION_GEO_JSON = "application/geo+json";
	/** {@code application/vnd.geo+json} — the pre-RFC alias still found in clients. */
	public static final String APPLICATION_GEO_JSON_LEGACY = "application/vnd.geo+json";

	private static final Logger LOG = System.getLogger(GeoJsonServiceResource.class.getName());

	/** Everything needed to serve one configured DataSet as GeoJSON. */
	public record GeoJsonEndpoint(DataSet dataSet, GeoJsonDataServiceConfiguration configuration,
			ComponentServiceObjects<ReadRepository> repository, GeoJsonFeatureMapper mapper) {
	}

	private final String offsetParameterName;
	private final String sizeParameterName;
	private final Map<String, GeoJsonEndpoint> endpoints;

	public GeoJsonServiceResource(String offsetParameterName, String sizeParameterName,
			Map<String, GeoJsonEndpoint> endpoints) {
		this.offsetParameterName = offsetParameterName;
		this.sizeParameterName = sizeParameterName;
		this.endpoints = Map.copyOf(endpoints);
	}

	@GET
	@Path("{dataSetPath}")
	public Response list(@PathParam("dataSetPath") String dataSetPath, @Context UriInfo uriInfo) {
		GeoJsonEndpoint endpoint = endpoint(dataSetPath);
		FeatureCollection collection = GeoJsonFactory.eINSTANCE.createFeatureCollection();
		int offset = queryParameter(uriInfo, offsetParameterName, 0);
		int size = effectiveSize(uriInfo, endpoint.configuration());
		if (size == 0) {
			return Response.ok(collection).build();
		}
		Query query = endpoint.dataSet().getQuery() != null ? EcoreUtil.copy(endpoint.dataSet().getQuery())
				: QueryBuilder.from(endpoint.dataSet().getInputType()).build();
		if (offset > 0) {
			query.setSkip(offset);
		}
		if (size > 0) {
			query.setTop(size);
		}
		Map<String, Object> parameters = bindParameters(query, uriInfo);
		List<Feature> features = lease(endpoint, dataSetPath, repository -> {
			try (QueryResult result = repository.find(query, parameters.isEmpty() ? null : parameters, null)) {
				// map before the lease is released: the source objects may live
				// in the repository instance's ResourceSet, disposed on unget
				return result.objects().map(endpoint.mapper()::map).toList();
			}
		});
		collection.getFeatures().addAll(features);
		return Response.ok(collection).build();
	}

	@GET
	@Path("{dataSetPath}/{id}")
	public Response byId(@PathParam("dataSetPath") String dataSetPath, @PathParam("id") String id) {
		GeoJsonEndpoint endpoint = endpoint(dataSetPath);
		Feature feature = lease(endpoint, dataSetPath, repository -> {
			EObject match = repository.getEObject(endpoint.dataSet().getInputType(), id);
			return match == null ? null : endpoint.mapper().map(match);
		});
		if (feature == null) {
			throw new NotFoundException("No object '" + id + "' in data set '" + dataSetPath + "'");
		}
		return Response.ok(feature).build();
	}

	private interface RepositoryCall<T> {
		T call(ReadRepository repository) throws IOException;
	}

	/** Leases a repository instance for one call and always releases it (see {@link DataServiceResource}). */
	private <T> T lease(GeoJsonEndpoint endpoint, String dataSetPath, RepositoryCall<T> call) {
		ReadRepository repository = endpoint.repository().getService();
		if (repository == null) {
			LOG.log(Level.ERROR, () -> "Data set '" + dataSetPath
					+ "': the backing repository yielded no instance - refusing the request");
			throw new InternalServerErrorException(
					"Data set '" + dataSetPath + "' has no usable repository instance");
		}
		try {
			return call.call(repository);
		} catch (IOException e) {
			LOG.log(Level.ERROR, "Reading data set '" + dataSetPath + "' failed", e);
			throw new InternalServerErrorException(
					"Reading data set '" + dataSetPath + "' failed: " + e.getMessage(), e);
		} catch (RuntimeException e) {
			LOG.log(Level.ERROR, "Reading data set '" + dataSetPath + "' failed", e);
			throw e;
		} finally {
			endpoint.repository().ungetService(repository);
		}
	}

	private GeoJsonEndpoint endpoint(String dataSetPath) {
		GeoJsonEndpoint endpoint = endpoints.get(dataSetPath);
		if (endpoint == null) {
			throw new NotFoundException("Unknown data set '" + dataSetPath + "'");
		}
		return endpoint;
	}

	private int effectiveSize(UriInfo uriInfo, GeoJsonDataServiceConfiguration configuration) {
		int size = queryParameter(uriInfo, sizeParameterName, intValue(configuration.getBatchSize(), -1));
		int limit = intValue(configuration.getBatchSizeLimit(), -1);
		if (limit >= 0 && (size < 0 || size > limit)) {
			size = limit;
		}
		return size;
	}

	private Map<String, Object> bindParameters(Query query, UriInfo uriInfo) {
		Map<String, Object> parameters = new HashMap<>();
		for (ParameterDecl declaration : query.getParameters()) {
			String value = uriInfo.getQueryParameters().getFirst(declaration.getName());
			if (value == null) {
				throw new BadRequestException("Missing query parameter '" + declaration.getName() + "'");
			}
			parameters.put(declaration.getName(), convert(declaration, value));
		}
		return parameters;
	}

	private Object convert(ParameterDecl declaration, String value) {
		if (declaration.getTypeHint() instanceof EDataType dataType) {
			try {
				return EcoreUtil.createFromString(dataType, value);
			} catch (RuntimeException e) {
				throw new BadRequestException("Query parameter '" + declaration.getName() + "' is not a valid "
						+ dataType.getName() + ": " + value);
			}
		}
		return value;
	}

	private int queryParameter(UriInfo uriInfo, String name, int defaultValue) {
		if (name == null) {
			return defaultValue;
		}
		String value = uriInfo.getQueryParameters().getFirst(name);
		if (value == null || value.isBlank()) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new BadRequestException("Query parameter '" + name + "' is not a number: " + value);
		}
	}

	private int intValue(BigInteger value, int defaultValue) {
		return value == null ? defaultValue : value.intValue();
	}
}
