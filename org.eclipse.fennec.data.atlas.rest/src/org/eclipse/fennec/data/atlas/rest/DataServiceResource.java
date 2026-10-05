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

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.fennec.codec.rest.jakartas.JakartaRestConstants;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration;
import org.eclipse.fennec.model.query.ParameterDecl;
import org.eclipse.fennec.model.query.Query;
import org.eclipse.fennec.model.query.builder.QueryBuilder;
import org.eclipse.fennec.persistence.query.api.QueryResult;
import org.eclipse.fennec.persistence.repository.api.ReadRepository;
import org.osgi.service.component.ComponentServiceObjects;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.NotAcceptableException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Request;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.Variant;

/**
 * Generic Jakarta-RS resource of one {@code RestDataService}: serves the
 * configured DataSets under their configured paths, list and by-id, reading
 * through the {@link ReadRepository} backing each DataSet's input. A DataSet's
 * canonical query (if any) defines its content; REST pagination is overlaid on
 * {@code skip}/{@code top} of a per-request copy and declared query parameters
 * bind from HTTP query parameters.
 *
 * <p>
 * Serialization happens in the fennec codec message body writers. Which formats
 * a DataSet offers is configuration, not code: the {@link ExportFormats}
 * resolved from its {@code DistributionExport}s define the negotiable variants,
 * a request for anything else is a {@code 406}, and the export's codec options
 * (CSV delimiter, SQL-type row) reach the writer through the codec's
 * per-request option property. Because {@code @Produces} is static while the
 * DataSets are not, it lists everything the runtime <em>can</em> write and the
 * per-DataSet restriction is applied here.
 * </p>
 *
 * <p>
 * Repositories are prototype-scoped and own a non-thread-safe ResourceSet, so
 * every request leases its own instance via {@link ComponentServiceObjects}
 * and releases it after detaching the results.
 * </p>
 */
@Path("/")
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, ExportFormats.TEXT_CSV,
		ExportFormats.APPLICATION_CSV_ZIP })
public class DataServiceResource {

	private static final Logger LOG = System.getLogger(DataServiceResource.class.getName());

	/** Everything needed to serve one configured DataSet. */
	public record DataSetEndpoint(DataSet dataSet, RestDataServiceConfiguration configuration,
			ComponentServiceObjects<ReadRepository> repository, ExportFormats formats) {
	}

	private final Map<String, DataSetEndpoint> endpoints;

	public DataServiceResource(Map<String, DataSetEndpoint> endpoints) {
		this.endpoints = Map.copyOf(endpoints);
	}

	@GET
	@Path("{dataSetPath}")
	public Response list(@PathParam("dataSetPath") String dataSetPath, @Context UriInfo uriInfo,
			@Context Request request, @Context ContainerRequestContext requestContext) {
		DataSetEndpoint endpoint = endpoint(dataSetPath);
		MediaType mediaType = negotiate(endpoint, dataSetPath, request, requestContext);
		Resource container = newContainer(dataSetPath);
		// the parameter names are per DataSet configuration, not per service
		int offset = queryParameter(uriInfo, endpoint.configuration().getOffsetParameterName(), 0);
		int size = effectiveSize(uriInfo, endpoint.configuration());
		if (size == 0) {
			return Response.ok(container).type(mediaType).build();
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
		List<EObject> objects = lease(endpoint, dataSetPath, repository -> {
			try (QueryResult result = repository.find(query, parameters.isEmpty() ? null : parameters, null)) {
				// copy before the lease is released: the objects may live in
				// the repository instance's ResourceSet, disposed on unget
				return List.copyOf(EcoreUtil.copyAll(result.objects().toList()));
			}
		});
		container.getContents().addAll(objects);
		return Response.ok(container).type(mediaType).build();
	}

	@GET
	@Path("{dataSetPath}/{id}")
	public Response byId(@PathParam("dataSetPath") String dataSetPath, @PathParam("id") String id,
			@Context Request request, @Context ContainerRequestContext requestContext) {
		DataSetEndpoint endpoint = endpoint(dataSetPath);
		MediaType mediaType = negotiate(endpoint, dataSetPath, request, requestContext);
		EObject copy = lease(endpoint, dataSetPath, repository -> {
			EObject match = repository.getEObject(endpoint.dataSet().getInputType(), id);
			return match == null ? null : EcoreUtil.copy(match);
		});
		if (copy == null) {
			throw new NotFoundException("No object '" + id + "' in data set '" + dataSetPath + "'");
		}
		return Response.ok(copy).type(mediaType).build();
	}

	/**
	 * Negotiates the response media type against the DataSet's configured
	 * export formats and publishes that export's codec options for this
	 * request.
	 *
	 * @return the selected media type
	 * @throws NotAcceptableException if the client accepts none of the
	 *                                configured formats
	 */
	private MediaType negotiate(DataSetEndpoint endpoint, String dataSetPath, Request request,
			ContainerRequestContext requestContext) {
		ExportFormats formats = endpoint.formats();
		Variant variant = request.selectVariant(formats.variants());
		if (variant == null || variant.getMediaType() == null) {
			throw new NotAcceptableException(
					"Data set '" + dataSetPath + "' is served as " + formats.mediaTypes() + " only");
		}
		MediaType mediaType = preferJson(variant.getMediaType(), formats, requestContext);
		publishCodecOptions(formats.optionsFor(mediaType), requestContext);
		return mediaType;
	}

	/**
	 * Makes JSON the default format of every DataSet that offers it.
	 *
	 * <p>
	 * {@link Request#selectVariant} breaks ties between equally acceptable
	 * variants in an implementation-defined way — in practice by declaration
	 * order, so a DataSet exporting CSV before JSON answered {@code Accept: *}{@code /*}
	 * (or no {@code Accept} at all) with CSV. JSON replaces the selected variant
	 * whenever the client accepts it at least as well: with an equal or higher
	 * quality and an equally or more specific media range. An explicit
	 * preference is never overridden — {@code text/csv, *}{@code /*} still gets
	 * CSV, because {@code text/csv} is the more specific range.
	 * </p>
	 */
	private static MediaType preferJson(MediaType selected, ExportFormats formats,
			ContainerRequestContext requestContext) {
		if (requestContext == null || MediaType.APPLICATION_JSON_TYPE.isCompatible(selected)
				|| !formats.offers(MediaType.APPLICATION_JSON_TYPE)) {
			return selected;
		}
		List<MediaType> acceptable = requestContext.getAcceptableMediaTypes();
		double[] json = acceptance(MediaType.APPLICATION_JSON_TYPE, acceptable);
		double[] current = acceptance(selected, acceptable);
		if (json == null || json[0] <= 0) {
			return selected;
		}
		if (current == null || json[0] > current[0] || (json[0] == current[0] && json[1] >= current[1])) {
			return MediaType.APPLICATION_JSON_TYPE;
		}
		return selected;
	}

	/**
	 * How well the client accepts {@code type}: the quality and specificity
	 * ({@code 0} = {@code *}{@code /*}, {@code 1} = {@code type/*}, {@code 2} =
	 * exact) of the most specific matching media range, or {@code null} if no
	 * range matches.
	 */
	private static double[] acceptance(MediaType type, List<MediaType> acceptable) {
		double[] best = null;
		for (MediaType range : acceptable) {
			if (!range.isCompatible(type)) {
				continue;
			}
			int specificity = range.isWildcardType() ? 0 : range.isWildcardSubtype() ? 1 : 2;
			if (best == null || specificity > best[1]) {
				best = new double[] { quality(range), specificity };
			}
		}
		return best;
	}

	private static double quality(MediaType range) {
		String q = range.getParameters().get("q");
		if (q == null) {
			return 1.0;
		}
		try {
			return Double.parseDouble(q);
		} catch (NumberFormatException e) {
			return 1.0;
		}
	}

	/**
	 * Hands the configured codec save options to the codec's message body
	 * writer, which reads them from
	 * {@link JakartaRestConstants#CLIENT_CODEC_OPTIONS} at serialization time.
	 *
	 * <p>
	 * Anything already in that property keeps precedence — the configured values
	 * go in underneath — so a client override would win once it can reach us. It
	 * cannot today: the codec's {@code ClientCodecOptionsFilter} declares no
	 * application select, so the whiteboard attaches it to the {@code .default}
	 * application only, never to the per-service applications the Data Atlas
	 * registers. Both that and the missing server-side channel are
	 * <a href="https://github.com/eclipse-fennec/emf.codec/issues/170">emf.codec#170</a>;
	 * until it is resolved this public constant is the only per-request channel
	 * there is.
	 * </p>
	 */
	private void publishCodecOptions(Map<String, Object> configured, ContainerRequestContext requestContext) {
		if (configured.isEmpty() || requestContext == null) {
			return;
		}
		Map<String, Object> merged = new HashMap<>(configured);
		if (requestContext.getProperty(JakartaRestConstants.CLIENT_CODEC_OPTIONS) instanceof Map<?, ?> client) {
			client.forEach((key, value) -> merged.put(String.valueOf(key), value));
		}
		requestContext.setProperty(JakartaRestConstants.CLIENT_CODEC_OPTIONS, merged);
	}

	private interface RepositoryCall<T> {
		T call(ReadRepository repository) throws IOException;
	}

	/**
	 * Leases a repository instance for one call and always releases it.
	 *
	 * <p>
	 * Both failure paths are logged with their cause before the request is
	 * answered: a 500 that leaves no trace on the server is not diagnosable, and
	 * Jersey does not log a {@link InternalServerErrorException} it is handed.
	 * </p>
	 */
	private <T> T lease(DataSetEndpoint endpoint, String dataSetPath, RepositoryCall<T> call) {
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

	private DataSetEndpoint endpoint(String dataSetPath) {
		DataSetEndpoint endpoint = endpoints.get(dataSetPath);
		if (endpoint == null) {
			throw new NotFoundException("Unknown data set '" + dataSetPath + "'");
		}
		return endpoint;
	}

	/** A detached container resource for the codec response writers. */
	private Resource newContainer(String dataSetPath) {
		return new XMIResourceImpl(URI.createURI("dataatlas:/" + dataSetPath));
	}

	/**
	 * The requested page size, defaulted by {@code batchSize} and capped by
	 * {@code batchSizeLimit}; -1 means unlimited.
	 */
	private int effectiveSize(UriInfo uriInfo, RestDataServiceConfiguration configuration) {
		int size = queryParameter(uriInfo, configuration.getLimitParameterName(),
				intValue(configuration.getBatchSize(), -1));
		int limit = intValue(configuration.getBatchSizeLimit(), -1);
		if (limit >= 0 && (size < 0 || size > limit)) {
			size = limit;
		}
		return size;
	}

	/**
	 * Binds the query's declared parameters from HTTP query parameters,
	 * converting via the declared type hint; a missing parameter is a client
	 * error.
	 */
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
