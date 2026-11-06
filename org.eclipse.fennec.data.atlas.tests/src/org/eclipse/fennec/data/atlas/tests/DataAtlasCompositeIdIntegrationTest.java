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
 * data.atlas#31: a type without a single iD attribute, identified by a
 * composite key declared with the fennec persistence {@code idFeatures}
 * annotation (here {@code systemName,deviceID}, the way DMTF CIM identifies
 * instances by several key properties). The file input identifies its objects
 * the way the persistence stack does ({@code CompositeIds}), so a by-id lookup
 * takes the canonical {@code k1=v1,k2=v2} key.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class DataAtlasCompositeIdIntegrationTest {

	private static final int HTTP_PORT = 18110;
	private static final String BASE = "http://localhost:" + HTTP_PORT + "/rest/inventory/ports";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration bootstrapConfig;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path configFile = extractTestData(bundleContext).resolve("dataatlas-composite.xmi");

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "compositeIds", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "compositeIds");
		httpProps.put("org.apache.felix.http.runtime.init.id", "compositeIdsHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "compositeIdsRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "compositeIdsRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=compositeIdsHttp)");
		whiteboardConfig.update(wbProps);

		bootstrapConfig = BootstrapConfigs.fresh(configAdmin);
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
	void servesAndLooksUpObjectsByTheirCompositeKey() throws Exception {
		assertEquals(200, awaitStatus(BASE, 200, 60_000));
		String list = get(BASE).body();
		assertTrue(list.contains("switch-2"), list);

		// the canonical composite key selects exactly one port: the same deviceID
		// exists on both switches, the same systemName on two ports
		HttpResponse<String> byKey = get(BASE + "/systemName=switch-1,deviceID=port-2");
		assertEquals(200, byKey.statusCode(), byKey.body());
		assertTrue(byKey.body().contains("10000"), byKey.body());
		assertFalse(byKey.body().contains("switch-2"), byKey.body());

		HttpResponse<String> otherSwitch = get(BASE + "/systemName=switch-2,deviceID=port-1");
		assertEquals(200, otherSwitch.statusCode(), otherSwitch.body());
		assertTrue(otherSwitch.body().contains("switch-2"), otherSwitch.body());
		assertFalse(otherSwitch.body().contains("switch-1"), otherSwitch.body());

		// a partial or unknown key matches nothing
		assertEquals(404, get(BASE + "/port-1").statusCode());
		assertEquals(404, get(BASE + "/systemName=switch-3,deviceID=port-1").statusCode());
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-composite");
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

	private static HttpResponse<String> get(String url) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", "application/json")
				.timeout(Duration.ofSeconds(10))
				.GET()
				.build();
		return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
	}

	private static int awaitStatus(String url, int wanted, long timeoutMs) throws Exception {
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
