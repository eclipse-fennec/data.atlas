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
import java.nio.charset.StandardCharsets;
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
 * Milestone 4 lifecycle in file mode: changes to the configuration file are
 * picked up by the io.fs.watcher listener and applied as a diff — an added
 * DataSet appears, a removed one 404s, an unchanged one keeps serving; a
 * broken configuration fails hard (endpoints down) and a corrected version
 * recovers the instance.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class FileConfigLifecycleIntegrationTest {

	private static final int HTTP_PORT = 18088;
	private static final String BASE = "http://localhost:" + HTTP_PORT + "/rest/example";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration bootstrapConfig;

	private static Path configFile;
	private static String v1;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path dataDir = extractTestData(bundleContext);
		configFile = dataDir.resolve("dataatlas.xmi");
		v1 = Files.readString(configFile, StandardCharsets.UTF_8);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "lifecycleM4", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "lifecycleM4");
		httpProps.put("org.apache.felix.http.runtime.init.id", "lifecycleM4Http");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "lifecycleM4Rest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "lifecycleM4Rest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=lifecycleM4Http)");
		whiteboardConfig.update(wbProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", configFile.toUri().toString());
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
	void configFileChangesReachTheRunningInstance() throws Exception {
		// v1 serves
		assertEquals(200, awaitStatus(BASE + "/persons", 200, 60_000));

		// v2: a second endpoint for the same DataSet is ADDED -> it appears,
		// and the unchanged one keeps serving
		Files.writeString(configFile, v1.replace(
				"<configuration id=\"persons-rest-config\" dataSet=\"persons\" path=\"persons\"/>",
				"<configuration id=\"persons-rest-config\" dataSet=\"persons\" path=\"persons\"/>\n"
						+ "    <configuration id=\"persons2-config\" dataSet=\"persons\" path=\"persons2\"/>"),
				StandardCharsets.UTF_8);
		assertEquals(200, awaitStatus(BASE + "/persons2", 200, 60_000));
		assertEquals(200, get(BASE + "/persons").statusCode());
		assertTrue(get(BASE + "/persons2").body().contains("Lovelace"));

		// back to v1: the added endpoint is REMOVED -> 404, the rest serves on
		// (polled: the service application is briefly down during the swap)
		Files.writeString(configFile, v1, StandardCharsets.UTF_8);
		assertEquals(404, awaitStatus(BASE + "/persons2", 404, 60_000));
		assertEquals(200, awaitStatus(BASE + "/persons", 200, 60_000));

		// broken version: unresolvable EClass reference -> fail hard, all down
		Files.writeString(configFile, v1.replace(
				"model/person.ecore#//Person\"/>",
				"https://eclipse.org/fennec/does/not/exist/1.0.0#//Nope\"/>"), StandardCharsets.UTF_8);
		assertEquals(404, awaitStatus(BASE + "/persons", 404, 60_000));

		// corrected version recovers the instance
		Files.writeString(configFile, v1, StandardCharsets.UTF_8);
		assertEquals(200, awaitStatus(BASE + "/persons", 200, 60_000));
		assertTrue(get(BASE + "/persons").body().contains("Hopper"));
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-lifecycle");
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

	private HttpResponse<String> get(String url) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", "application/json")
				.timeout(Duration.ofSeconds(10))
				.GET()
				.build();
		return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
	}

	/** Polls until the URL answers with the wanted status (or times out). */
	private int awaitStatus(String url, int wanted, long timeoutMs) throws Exception {
		long deadline = System.currentTimeMillis() + timeoutMs;
		int last = -1;
		while (System.currentTimeMillis() < deadline) {
			try {
				last = get(url).statusCode();
				if (last == wanted) {
					return last;
				}
			} catch (Exception e) {
				last = -1;
			}
			Thread.sleep(500);
		}
		return last;
	}
}
