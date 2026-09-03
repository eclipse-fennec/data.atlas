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
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/**
 * Milestone 5, GeoJSON end to end: a {@code GeoJsonDataService} serves the
 * example points of interest as RFC 7946 GeoJSON — the list as a
 * {@code FeatureCollection}, by id as a single {@code Feature}, media type
 * {@code application/geo+json} — with the mapping (longitude/latitude
 * attributes → Point geometry, remaining attributes → properties, EMF id →
 * Feature id) taken from the configuration. The response is <em>parsed</em>
 * and verified structurally (the WP-DA-12 evidence criterion), not
 * string-matched.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class DataAtlasGeoJsonIntegrationTest {

	private static final int HTTP_PORT = 18082;
	private static final String BASE = "http://localhost:" + HTTP_PORT + "/rest/geo/pois";
	private static final String GEO_JSON = "application/geo+json";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration bootstrapConfig;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path dataDir = extractTestData(bundleContext);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "geoM5", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "geoM5");
		httpProps.put("org.apache.felix.http.runtime.init.id", "geoM5Http");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "geoM5Rest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "geoM5Rest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=geoM5Http)");
		whiteboardConfig.update(wbProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", dataDir.resolve("dataatlas-geo.xmi").toUri().toString());
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

	@Test
	void servesASpecValidFeatureCollection() throws Exception {
		HttpResponse<String> response = awaitOk(BASE, GEO_JSON, 60_000);
		assertEquals(200, response.statusCode());
		assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith(GEO_JSON),
				"expected " + GEO_JSON + ", got " + response.headers().firstValue("Content-Type"));

		JsonObject collection = Json.createReader(new StringReader(response.body())).readObject();
		assertEquals("FeatureCollection", collection.getString("type"), response.body());
		JsonArray features = collection.getJsonArray("features");
		assertEquals(3, features.size(), response.body());

		JsonObject tower = features.stream().map(v -> (JsonObject) v)
				.filter(f -> "jentower".equals(f.getString("id", null)))
				.findFirst().orElseThrow(() -> new AssertionError("no feature 'jentower' in " + response.body()));
		assertEquals("Feature", tower.getString("type"));
		JsonObject geometry = tower.getJsonObject("geometry");
		assertEquals("Point", geometry.getString("type"));
		JsonArray coordinates = geometry.getJsonArray("coordinates");
		assertEquals(11.5858, coordinates.getJsonNumber(0).doubleValue(), 1e-9);
		assertEquals(50.9296, coordinates.getJsonNumber(1).doubleValue(), 1e-9);
		JsonObject properties = tower.getJsonObject("properties");
		assertEquals("JenTower", properties.getString("name"));
		assertEquals("landmark", properties.getString("category"));
		assertTrue(!properties.containsKey("longitude"),
				"the geometry-consumed attributes must not appear as properties: " + properties);
	}

	@Test
	void servesASingleFeatureById() throws Exception {
		awaitOk(BASE, GEO_JSON, 60_000);
		HttpResponse<String> response = get(BASE + "/planetarium", GEO_JSON);
		assertEquals(200, response.statusCode());
		JsonObject feature = Json.createReader(new StringReader(response.body())).readObject();
		assertEquals("Feature", feature.getString("type"), response.body());
		assertEquals("planetarium", feature.getString("id"));
		assertEquals("Zeiss-Planetarium", feature.getJsonObject("properties").getString("name"));
		assertEquals("Point", feature.getJsonObject("geometry").getString("type"));

		assertEquals(404, get(BASE + "/nope", GEO_JSON).statusCode());
	}

	@Test
	void paginatesAndNegotiates() throws Exception {
		awaitOk(BASE, GEO_JSON, 60_000);
		JsonObject page = Json.createReader(
				new StringReader(get(BASE + "?offset=1&limit=1", GEO_JSON).body())).readObject();
		assertEquals(1, page.getJsonArray("features").size(), page.toString());

		// only GeoJSON is served - a JSON accept is refused
		assertEquals(406, get(BASE, "application/json").statusCode());
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-geo");
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

	private static HttpResponse<String> get(String url, String accept) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", accept)
				.timeout(Duration.ofSeconds(10))
				.GET()
				.build();
		return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
	}

	private static HttpResponse<String> awaitOk(String url, String accept, long timeoutMs) throws Exception {
		long deadline = System.currentTimeMillis() + timeoutMs;
		HttpResponse<String> response = null;
		while (System.currentTimeMillis() < deadline) {
			try {
				response = get(url, accept);
				if (response.statusCode() == 200) {
					return response;
				}
			} catch (Exception e) {
				// keep polling
			}
			Thread.sleep(500);
		}
		return response;
	}
}
