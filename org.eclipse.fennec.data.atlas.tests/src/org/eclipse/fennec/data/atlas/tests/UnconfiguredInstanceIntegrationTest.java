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
import java.nio.file.StandardCopyOption;
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
 * The out-of-the-box state: the docker images ship **no** configuration, model
 * or data, so a Data Atlas that was started without a mount points at a
 * configuration file that is not there.
 *
 * <p>
 * That is not an error — it is an unconfigured instance. The bootstrap must come
 * up, publish nothing, and still watch the location, so mounting or creating the
 * configuration afterwards brings the instance up without a restart. This test
 * starts with the file absent, then puts it in place and expects the endpoint to
 * appear; removing it again takes the instance back down.
 * </p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class UnconfiguredInstanceIntegrationTest {

	private static final int HTTP_PORT = 18091;
	private static final String BASE = "http://localhost:" + HTTP_PORT + "/rest/example";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration bootstrapConfig;
	private static Path configFile;
	private static Path stashedConfig;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path dataDir = extractTestData(bundleContext);
		configFile = dataDir.resolve("dataatlas.xmi");

		// the model and data stay where the configuration's relative hrefs
		// expect them; only the configuration itself is moved out of the way,
		// which is exactly the shape of an image without a mount
		stashedConfig = dataDir.resolve("stashed-dataatlas.xmi");
		Files.move(configFile, stashedConfig, StandardCopyOption.REPLACE_EXISTING);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "unconfigured", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "unconfigured");
		httpProps.put("org.apache.felix.http.runtime.init.id", "unconfiguredHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "unconfiguredRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "unconfiguredRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=unconfiguredHttp)");
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
	void anAbsentConfigurationIsPickedUpWhenItAppears() throws Exception {
		// nothing published, and no crash: the framework is up and answering
		assertEquals(404, awaitStatus(BASE + "/persons", 404, 30_000),
				"an unconfigured instance must publish nothing");

		// the configuration is mounted / created afterwards
		Files.copy(stashedConfig, configFile, StandardCopyOption.REPLACE_EXISTING);
		assertEquals(200, awaitStatus(BASE + "/persons", 200, 60_000),
				"the bootstrap must watch the location even while the file is absent");
		assertTrue(get(BASE + "/persons").body().contains("Lovelace"),
				"the mounted configuration must actually be served");

		// and taking it away again tears the instance down
		Files.delete(configFile);
		assertEquals(404, awaitStatus(BASE + "/persons", 404, 60_000),
				"deleting the configuration must unpublish the instance");
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-unconfigured");
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
