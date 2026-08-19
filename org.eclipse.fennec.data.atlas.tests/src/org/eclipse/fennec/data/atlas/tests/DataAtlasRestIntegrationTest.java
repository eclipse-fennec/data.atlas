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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
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

/**
 * Boots the whole vertical slice inside the test framework: HTTP + Jakarta-RS
 * whiteboard + Data Atlas bootstrap fed with the example configuration, then
 * asserts the configured DataSet is served over REST.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class DataAtlasRestIntegrationTest {

	private static final int HTTP_PORT = 18086;
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest/example/persons";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration bootstrapConfig;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path dataDir = extractTestData(bundleContext);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "test", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "test");
		httpProps.put("org.apache.felix.http.runtime.init.id", "testHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "testRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "testRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=testHttp)");
		whiteboardConfig.update(wbProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", dataDir.resolve("dataatlas.xmi").toUri().toString());
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

	/**
	 * Extracts the example configuration and data shipped in this bundle
	 * (under /data) into a temp folder, preserving the relative structure the
	 * configuration's hrefs rely on.
	 */
	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-test");
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
	void servesDataSetAsJson() throws Exception {
		HttpResponse<String> response = getUntilOk(BASE_URL, "application/json");
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("Lovelace"), () -> "missing Lovelace in: " + response.body());
		assertTrue(response.body().contains("Hopper"), () -> "missing Hopper in: " + response.body());
	}

	@Test
	void servesDataSetAsXml() throws Exception {
		HttpResponse<String> response = getUntilOk(BASE_URL, "application/xml");
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("Lovelace"), () -> "missing Lovelace in: " + response.body());
	}

	@Test
	void servesSingleObjectById() throws Exception {
		getUntilOk(BASE_URL, "application/json");
		HttpResponse<String> response = get(BASE_URL + "/p1", "application/json");
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("Lovelace"), () -> "missing Lovelace in: " + response.body());
		assertTrue(!response.body().contains("Hopper"), () -> "unexpected Hopper in: " + response.body());
	}

	@Test
	void returns404ForUnknownObjects() throws Exception {
		getUntilOk(BASE_URL, "application/json");
		assertEquals(404, get(BASE_URL + "/does-not-exist", "application/json").statusCode());
		assertEquals(404, get("http://localhost:" + HTTP_PORT + "/rest/example/unknown", "application/json")
				.statusCode());
	}

	@Test
	void appliesPagination() throws Exception {
		getUntilOk(BASE_URL, "application/json");
		HttpResponse<String> response = get(BASE_URL + "?limit=1", "application/json");
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("Lovelace"), () -> "missing Lovelace in: " + response.body());
		assertTrue(!response.body().contains("Hopper"), () -> "unexpected Hopper in: " + response.body());

		HttpResponse<String> offsetResponse = get(BASE_URL + "?offset=1&limit=1", "application/json");
		assertEquals(200, offsetResponse.statusCode());
		assertTrue(offsetResponse.body().contains("Hopper"), () -> "missing Hopper in: " + offsetResponse.body());
	}

	private HttpResponse<String> get(String url, String accept) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", accept)
				.timeout(Duration.ofSeconds(10))
				.GET()
				.build();
		return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
	}

	/**
	 * The whiteboard and the Data Atlas services come up asynchronously; polls
	 * until the endpoint answers 200 (or times out after 30s).
	 */
	private HttpResponse<String> getUntilOk(String url, String accept) throws Exception {
		long deadline = System.currentTimeMillis() + 30_000;
		HttpResponse<String> response = null;
		Exception lastError = null;
		while (System.currentTimeMillis() < deadline) {
			try {
				response = get(url, accept);
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
