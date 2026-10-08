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
import java.util.function.Supplier;

import org.eclipse.emf.ecore.resource.Resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.ServiceUnavailableException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

/**
 * Serves the OpenAPI document of one {@code RestDataService} at
 * {@code {urlContext}/openapi.json}.
 *
 * <p>
 * The document names the URL the request reached the application at as its
 * server, so it is rendered per request; a DataSet whose path is
 * {@code openapi.json} is shadowed by it (the literal path wins over the
 * DataSet template).
 * </p>
 */
@Path(OpenApiResource.PATH)
public class OpenApiResource {

	/** The path of the document below the application base. */
	public static final String PATH = "openapi.json";

	private static final Logger LOG = System.getLogger(OpenApiResource.class.getName());

	private final RestOpenApi openApi;
	private final Supplier<Resource.Factory> resourceFactory;

	/**
	 * @param openApi         the description of the service
	 * @param resourceFactory the codec's OpenAPI resource factory, {@code null}
	 *                        while it is not available
	 */
	public OpenApiResource(RestOpenApi openApi, Supplier<Resource.Factory> resourceFactory) {
		this.openApi = openApi;
		this.resourceFactory = resourceFactory;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response document(@Context UriInfo uriInfo) {
		Resource.Factory factory = resourceFactory.get();
		if (factory == null) {
			LOG.log(Level.WARNING, "OpenAPI document requested, but no OpenAPI resource factory "
					+ "(org.eclipse.fennec.codec.openapi) is available");
			throw new ServiceUnavailableException("The OpenAPI document is currently not available");
		}
		String base = uriInfo.getBaseUri().toString();
		String serverUrl = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
		try {
			return Response.ok(openApi.render(factory, serverUrl), MediaType.APPLICATION_JSON_TYPE).build();
		} catch (IOException | RuntimeException e) {
			LOG.log(Level.ERROR, "Writing the OpenAPI document failed", e);
			throw new InternalServerErrorException("Writing the OpenAPI document failed", e);
		}
	}
}
