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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
 * Milestone 6, the transformation slice end to end in file mode: a DataSet on
 * a {@code BridgeRepository} with a QVT-O {@code DataTransformation} (the
 * compiled AST referenced as an EObject from the configuration) serves
 * transformed objects over REST — list, by id and paginated, all through the
 * 1:1 bridge — and a configuration whose transformation reference breaks keeps
 * the instance down until a corrected version recovers it (M4 semantics).
 *
 * <p>
 * The fixture is the shipped example {@code dataatlas-transformation.xmi}
 * itself, so this test also keeps the example honest: Person(firstName,
 * lastName) is projected onto PublicPerson(displayName) by
 * {@code trafo/person-to-public.qvto}, whose compiled document
 * {@code trafo/person-to-public.xmi} the configuration references.
 * </p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class DataAtlasTransformationIntegrationTest {

	private static final int HTTP_PORT = 18093;
	private static final String BASE = "http://localhost:" + HTTP_PORT + "/rest/example-public";

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
		configFile = dataDir.resolve("dataatlas-transformation.xmi");
		v1 = Files.readString(configFile, StandardCharsets.UTF_8);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "trafoM6", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "trafoM6");
		httpProps.put("org.apache.felix.http.runtime.init.id", "trafoM6Http");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "trafoM6Rest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "trafoM6Rest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=trafoM6Http)");
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
	@Order(1)
	void bridgeServesTransformedObjects() throws Exception {
		assertEquals(200, awaitStatus(BASE + "/public-persons", 200, 60_000));

		// the list is transformed: displayName carries the projection, the
		// source features are gone
		String list = get(BASE + "/public-persons").body();
		assertTrue(list.contains("Ada Lovelace"), list);
		assertTrue(list.contains("Grace Hopper"), list);
		assertTrue(list.contains("Margaret Hamilton"), list);
		assertTrue(list.contains("displayName"), list);
		assertFalse(list.contains("firstName"), "source features must not leak through the bridge: " + list);

		// by id: the 1:1 contract keeps the source id on the result
		String byId = get(BASE + "/public-persons/p2").body();
		assertTrue(byId.contains("Grace Hopper"), byId);
		assertFalse(byId.contains("Lovelace"), byId);
		assertEquals(404, get(BASE + "/public-persons/nope").statusCode());

		// pagination pushes down through the bridge (order-preserving 1:1)
		String page = get(BASE + "/public-persons?offset=1&limit=1").body();
		assertTrue(page.contains("Grace Hopper"), page);
		assertFalse(page.contains("Ada Lovelace"), page);
		assertFalse(page.contains("Margaret Hamilton"), page);
	}

	@Test
	@Order(2)
	void brokenTransformationReferenceFailsHardAndRecovers() throws Exception {
		// break the AST reference: an unresolvable transformation must keep
		// the whole instance down (fail hard), not serve untransformed data
		Files.writeString(configFile,
				v1.replace("trafo/person-to-public.xmi#//@unit", "trafo/does-not-exist.xmi#//@unit"),
				StandardCharsets.UTF_8);
		assertEquals(404, awaitStatus(BASE + "/public-persons", 404, 60_000));

		// the corrected version recovers the instance without a restart
		Files.writeString(configFile, v1, StandardCharsets.UTF_8);
		assertEquals(200, awaitStatus(BASE + "/public-persons", 200, 60_000));
		assertTrue(get(BASE + "/public-persons").body().contains("Ada Lovelace"));
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-trafo");
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
