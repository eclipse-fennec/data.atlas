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

import org.eclipse.fennec.codec.openapi.OpenApiResourceFactoryImpl;
import org.eclipse.fennec.codec.rest.annotations.ResourceOverwriteContentType;
import org.eclipse.fennec.model.openapi.OpenAPI;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

/**
 * Serves the OpenAPI document of one {@code RestDataService} at
 * {@code {urlContext}/openapi.json}.
 *
 * <p>
 * The document names the URL the request reached the application at as its
 * server, so it is completed per request; a DataSet whose path is
 * {@code openapi.json} is shadowed by it (the literal path wins over the
 * DataSet template). The codec's message body writer serializes it through
 * the OpenAPI resource factory, selected by its content type — also when the
 * client asks for plain {@code application/json}.
 * </p>
 */
@Path(OpenApiResource.PATH)
public class OpenApiResource {

	/** The path of the document below the application base. */
	public static final String PATH = "openapi.json";

	private final RestOpenApi openApi;

	/**
	 * @param openApi the description of the service
	 */
	public OpenApiResource(RestOpenApi openApi) {
		this.openApi = openApi;
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, OpenApiResourceFactoryImpl.CONTENT_TYPE_OPENAPI_JSON })
	@ResourceOverwriteContentType(OpenApiResourceFactoryImpl.CONTENT_TYPE_OPENAPI_JSON)
	public OpenAPI document(@Context UriInfo uriInfo) {
		String base = uriInfo.getBaseUri().toString();
		return openApi.document(base.endsWith("/") ? base.substring(0, base.length() - 1) : base);
	}
}
