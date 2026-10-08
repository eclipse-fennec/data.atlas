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
package org.eclipse.fennec.data.atlas.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import jakarta.json.Json;
import jakarta.json.JsonObject;

/**
 * The OpenAPI document of a {@code RestDataService} over two packages: both
 * served types are described in one schema section, nothing else of their
 * packages is (the REST counterpart of emf.odata#91), and a service with
 * {@code openAPI="false"} serves no document.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class DataAtlasOpenApiIntegrationTest {

	private static final int HTTP_PORT = 18103;
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration bootstrapConfig;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path dataDir = extractTestData(bundleContext);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "openapi", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "openapi");
		httpProps.put("org.apache.felix.http.runtime.init.id", "openapiHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "openapiRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "openapiRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=openapiHttp)");
		whiteboardConfig.update(wbProps);

		bootstrapConfig = BootstrapConfigs.fresh(configAdmin);
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", dataDir.resolve("dataatlas-openapi.xmi").toUri().toString());
		bootstrapConfig.update(bootProps);
	}

	@AfterAll
	static void tearDown() throws Exception {
		for (Configuration configuration : new Configuration[] { bootstrapConfig, whiteboardConfig, httpConfig }) {
			if (configuration != null) {
				configuration.delete();
			}
		}
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-openapi-test");
		Enumeration<URL> entries = bundle.findEntries("data", "*", true);
		while (entries != null && entries.hasMoreElements()) {
			URL url = entries.nextElement();
			String path = url.getPath();
			if (path.endsWith("/")) {
				continue;
			}
			Path file = target.resolve(path.substring("/data/".length()));
			Files.createDirectories(file.getParent());
			try (InputStream in = url.openStream()) {
				Files.copy(in, file);
			}
		}
		return target;
	}

	@Test
	void describesOnlyTheTypesTheServiceServes() throws Exception {
		assertEquals(200, awaitOk(BASE_URL + "/openapi-example/openings").statusCode());
		HttpResponse<String> response = get(BASE_URL + "/openapi-example/openapi.json");
		assertEquals(200, response.statusCode(), response::body);
		JsonObject document = Json.createReader(new StringReader(response.body())).readObject();

		JsonObject paths = document.getJsonObject("paths");
		assertTrue(paths.containsKey("/persons") && paths.containsKey("/openings"), paths::toString);

		// both packages in one schema section, every reference resolves
		JsonObject schemas = document.getJsonObject("components").getJsonObject("schemas");
		for (String path : new String[] { "/persons/{id}", "/openings/{id}" }) {
			String ref = paths.getJsonObject(path).getJsonObject("get").getJsonObject("responses")
					.getJsonObject("200").getJsonObject("content").getJsonObject("application/json")
					.getJsonObject("schema").getString("$ref");
			assertTrue(schemas.containsKey(ref.substring("#/components/schemas/".length())),
					() -> ref + " does not resolve in " + schemas);
		}
		assertEquals(2, schemas.size(), () -> "exactly the two served types expected: " + schemas.keySet());

		// the internal type of the ticketing package is not described
		assertFalse(response.body().contains("CardHolderVisit"), response::body);
		assertFalse(response.body().contains("email"), response::body);
	}

	@Test
	void aServiceWithoutOpenApiServesNoDocument() throws Exception {
		assertEquals(200, awaitOk(BASE_URL + "/openapi-off/persons").statusCode());
		assertEquals(404, get(BASE_URL + "/openapi-off/openapi.json").statusCode());
	}

	private HttpResponse<String> get(String url) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", "application/json")
				.timeout(Duration.ofSeconds(10))
				.GET()
				.build();
		return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
	}

	/** The services come up asynchronously; polls until 200 (or 30s). */
	private HttpResponse<String> awaitOk(String url) throws Exception {
		long deadline = System.currentTimeMillis() + 30_000;
		HttpResponse<String> response = null;
		Exception lastError = null;
		while (System.currentTimeMillis() < deadline) {
			try {
				response = get(url);
				lastError = null;
				if (response.statusCode() == 200) {
					return response;
				}
			} catch (Exception e) {
				lastError = e;
			}
			Thread.sleep(500);
		}
		if (lastError != null) {
			throw lastError;
		}
		return response;
	}
}
