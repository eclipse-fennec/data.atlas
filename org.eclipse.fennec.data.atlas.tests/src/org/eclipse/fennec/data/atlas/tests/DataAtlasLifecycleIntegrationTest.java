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
import java.util.function.BooleanSupplier;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * Configuration lifecycle of the vertical slice: what happens when the
 * bootstrap configuration comes, goes, comes back or is pointed at a different
 * {@code DataAtlasConfiguration} — plus the override-else-default trias
 * semantics and the pagination attributes the static happy-path test leaves at
 * their defaults.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class DataAtlasLifecycleIntegrationTest {

	private static final int HTTP_PORT = 18087;
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest";
	private static final String PERSON_NSURI = "https://eclipse.org/fennec/data/atlas/example/person/1.0.0";
	private static final long DEADLINE_MS = 30_000;

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Path dataDir;
	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;

	private Configuration bootstrapConfig;

	@BeforeAll
	static void setupInfrastructure(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		dataDir = extractTestData(bundleContext);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "lifecycle", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "lifecycle");
		httpProps.put("org.apache.felix.http.runtime.init.id", "lifecycleHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "lifecycleRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "lifecycleRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=lifecycleHttp)");
		whiteboardConfig.update(wbProps);
	}

	@AfterAll
	static void tearDownInfrastructure() throws Exception {
		for (Configuration configuration : new Configuration[] { whiteboardConfig, httpConfig }) {
			if (configuration != null) {
				configuration.delete();
			}
		}
	}

	@AfterEach
	void tearDownBootstrap(@InjectBundleContext BundleContext bundleContext) throws Exception {
		if (bootstrapConfig != null) {
			bootstrapConfig.delete();
			bootstrapConfig = null;
		}
		// isolate the tests: the next one must start from a torn-down slice
		waitUntil(() -> countServices(bundleContext, null, "(data.atlas.config.id=*)") == 0,
				"configuration object services still registered");
	}

	@Test
	void deleteTearsEverythingDown(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		activateBootstrap(configAdmin, "dataatlas.xmi");
		getUntilStatus(BASE_URL + "/example/persons", 200);

		// the slice's services are up while the configuration exists
		assertTrue(countServices(bundleContext, null, "(data.atlas.config.id=*)") > 0,
				"expected configuration object services while the configuration exists");
		assertTrue(countServices(bundleContext, "org.eclipse.emf.ecore.EPackage", "(emf.nsURI=" + PERSON_NSURI + ")") > 0,
				"expected the person EPackage service while the configuration exists");

		bootstrapConfig.delete();
		bootstrapConfig = null;

		getUntilStatus(BASE_URL + "/example/persons", 404);
		waitUntil(() -> countServices(bundleContext, null, "(data.atlas.config.id=*)") == 0,
				"configuration object services survived the configuration delete");
		waitUntil(() -> countServices(bundleContext, null, "(data.atlas.input.id=*)") == 0,
				"EObjectSource services survived the configuration delete");
		waitUntil(() -> countServices(bundleContext, "org.eclipse.emf.ecore.EPackage",
				"(emf.nsURI=" + PERSON_NSURI + ")") == 0,
				"the person EPackage service survived the configuration delete");
	}

	@Test
	void reAddedConfigurationServesAgain(@InjectService ConfigurationAdmin configAdmin) throws Exception {
		activateBootstrap(configAdmin, "dataatlas.xmi");
		getUntilStatus(BASE_URL + "/example/persons", 200);

		bootstrapConfig.delete();
		getUntilStatus(BASE_URL + "/example/persons", 404);

		activateBootstrap(configAdmin, "dataatlas.xmi");
		HttpResponse<String> response = getUntilStatus(BASE_URL + "/example/persons", 200);
		assertTrue(response.body().contains("Lovelace"), () -> "missing Lovelace in: " + response.body());
	}

	@Test
	void configurationUpdateMovesTheEndpoint(@InjectService ConfigurationAdmin configAdmin) throws Exception {
		activateBootstrap(configAdmin, "dataatlas.xmi");
		getUntilStatus(BASE_URL + "/example/persons", 200);

		// no @Modified on the bootstrap: the update restarts the component,
		// which must fully replace the old slice with the new one
		updateBootstrap("dataatlas-moved.xmi");

		HttpResponse<String> moved = getUntilStatus(BASE_URL + "/moved/people", 200);
		assertTrue(moved.body().contains("Hopper"), () -> "missing Hopper in: " + moved.body());
		getUntilStatus(BASE_URL + "/example/persons", 404);
	}

	@Test
	void dataSetInheritsServiceDataInputAndNameAsPath(@InjectService ConfigurationAdmin configAdmin) throws Exception {
		activateBootstrap(configAdmin, "dataatlas-servicedefault.xmi");

		// dataInput comes from the service (the DataSet sets none), the path
		// from the DataSet name (the configuration sets none)
		HttpResponse<String> response = getUntilStatus(BASE_URL + "/svcdefault/people", 200);
		assertTrue(response.body().contains("Lovelace"), () -> "missing Lovelace in: " + response.body());
		assertTrue(response.body().contains("Hamilton"), () -> "missing Hamilton in: " + response.body());
	}

	@Test
	void customPaginationParametersAndBatchLimits(@InjectService ConfigurationAdmin configAdmin) throws Exception {
		activateBootstrap(configAdmin, "dataatlas-pagination.xmi");
		String base = BASE_URL + "/paged/persons";

		// batchSize=2 is the default page size (file order: Lovelace, Hopper, Hamilton)
		HttpResponse<String> firstPage = getUntilStatus(base, 200);
		assertTrue(firstPage.body().contains("Hopper"), () -> "missing Hopper in: " + firstPage.body());
		assertFalse(firstPage.body().contains("Hamilton"), () -> "unexpected Hamilton in: " + firstPage.body());

		// the configured parameter names page through the data
		HttpResponse<String> secondPage = get(base + "?start=2&count=1");
		assertEquals(200, secondPage.statusCode());
		assertTrue(secondPage.body().contains("Hamilton"), () -> "missing Hamilton in: " + secondPage.body());
		assertFalse(secondPage.body().contains("Lovelace"), () -> "unexpected Lovelace in: " + secondPage.body());

		// batchSizeLimit=2 caps a larger requested page size
		HttpResponse<String> capped = get(base + "?count=5");
		assertEquals(200, capped.statusCode());
		assertFalse(capped.body().contains("Hamilton"), () -> "batchSizeLimit not applied in: " + capped.body());

		// the default parameter names are replaced, not additional
		HttpResponse<String> defaultNames = get(base + "?limit=1");
		assertEquals(200, defaultNames.statusCode());
		assertTrue(defaultNames.body().contains("Hopper"),
				() -> "default parameter name 'limit' should be ignored in: " + defaultNames.body());
	}

	private void activateBootstrap(ConfigurationAdmin configAdmin, String configFile) throws Exception {
		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		updateBootstrap(configFile);
	}

	private void updateBootstrap(String configFile) throws Exception {
		Dictionary<String, Object> props = new Hashtable<>();
		props.put("config.uri", dataDir.resolve(configFile).toUri().toString());
		bootstrapConfig.update(props);
	}

	private static int countServices(BundleContext bundleContext, String objectClass, String filter) {
		try {
			ServiceReference<?>[] references = bundleContext.getAllServiceReferences(objectClass, filter);
			return references == null ? 0 : references.length;
		} catch (Exception e) {
			throw new IllegalStateException("invalid service filter " + filter, e);
		}
	}

	private static void waitUntil(BooleanSupplier condition, String failureMessage) throws InterruptedException {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		while (System.currentTimeMillis() < deadline) {
			if (condition.getAsBoolean()) {
				return;
			}
			Thread.sleep(200);
		}
		assertTrue(condition.getAsBoolean(), failureMessage);
	}

	private static HttpResponse<String> get(String url) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", "application/json")
				.timeout(Duration.ofSeconds(10))
				.GET()
				.build();
		return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
	}

	/**
	 * The whiteboard and the Data Atlas services change asynchronously; polls
	 * until the URL answers with the expected status (or times out).
	 */
	private static HttpResponse<String> getUntilStatus(String url, int expectedStatus) throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		HttpResponse<String> response = null;
		Exception lastError = null;
		while (System.currentTimeMillis() < deadline) {
			try {
				response = get(url);
				lastError = null;
				if (response.statusCode() == expectedStatus) {
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
		assertEquals(expectedStatus, response == null ? -1 : response.statusCode(),
				() -> "timed out waiting for " + expectedStatus + " from " + url);
		return response;
	}

	/**
	 * Extracts the configurations and data shipped in this bundle (under /data)
	 * into a temp folder, preserving the relative structure the configurations'
	 * hrefs rely on.
	 */
	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-lifecycle-test");
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
}
