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

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.api.EObjectSource;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

/**
 * Generic Jakarta-RS resource of one {@code RestDataService}: serves the
 * configured DataSets under their configured paths, list and by-id, with the
 * pagination parameter names configured on the service. Serialization happens
 * in the fennec codec message body writers via content negotiation.
 */
@Path("/")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class DataServiceResource {

	/** Everything needed to serve one configured DataSet. */
	public record DataSetEndpoint(DataSet dataSet, RestDataServiceConfiguration configuration, EObjectSource source) {
	}

	private final String offsetParameterName;
	private final String sizeParameterName;
	private final Map<String, DataSetEndpoint> endpoints;

	public DataServiceResource(String offsetParameterName, String sizeParameterName,
			Map<String, DataSetEndpoint> endpoints) {
		this.offsetParameterName = offsetParameterName;
		this.sizeParameterName = sizeParameterName;
		this.endpoints = Map.copyOf(endpoints);
	}

	@GET
	@Path("{dataSetPath}")
	public Resource list(@PathParam("dataSetPath") String dataSetPath, @Context UriInfo uriInfo) {
		DataSetEndpoint endpoint = endpoint(dataSetPath);
		Resource contents = endpoint.source().loadContents();
		List<EObject> objects = select(contents, endpoint);
		objects = paginate(objects, endpoint.configuration(), uriInfo);
		List<EObject> result = List.copyOf(objects);
		contents.getContents().clear();
		contents.getContents().addAll(result);
		return contents;
	}

	@GET
	@Path("{dataSetPath}/{id}")
	public EObject byId(@PathParam("dataSetPath") String dataSetPath, @PathParam("id") String id) {
		DataSetEndpoint endpoint = endpoint(dataSetPath);
		Resource contents = endpoint.source().loadContents();
		EObject match = select(contents, endpoint).stream()
				.filter(o -> Objects.equals(EcoreUtil.getID(o), id))
				.findFirst()
				.orElseThrow(() -> new NotFoundException(
						"No object '" + id + "' in data set '" + dataSetPath + "'"));
		// detach so the writer serializes the single object, not its resource
		EcoreUtil.remove(match);
		return match;
	}

	private DataSetEndpoint endpoint(String dataSetPath) {
		DataSetEndpoint endpoint = endpoints.get(dataSetPath);
		if (endpoint == null) {
			throw new NotFoundException("Unknown data set '" + dataSetPath + "'");
		}
		return endpoint;
	}

	private List<EObject> select(Resource contents, DataSetEndpoint endpoint) {
		EClass type = endpoint.dataSet().getInputType();
		return contents.getContents().stream()
				.filter(o -> type == null || type.isInstance(o))
				.toList();
	}

	private List<EObject> paginate(List<EObject> objects, RestDataServiceConfiguration configuration,
			UriInfo uriInfo) {
		int offset = queryParameter(uriInfo, offsetParameterName, 0);
		int size = queryParameter(uriInfo, sizeParameterName, intValue(configuration.getBatchSize(), -1));
		int limit = intValue(configuration.getBatchSizeLimit(), -1);
		if (limit >= 0 && (size < 0 || size > limit)) {
			size = limit;
		}
		if (offset > 0) {
			objects = offset >= objects.size() ? List.of() : objects.subList(offset, objects.size());
		}
		if (size >= 0 && size < objects.size()) {
			objects = objects.subList(0, size);
		}
		return objects;
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
			throw new jakarta.ws.rs.BadRequestException("Query parameter '" + name + "' is not a number: " + value);
		}
	}

	private int intValue(BigInteger value, int defaultValue) {
		return value == null ? defaultValue : value.intValue();
	}
}
