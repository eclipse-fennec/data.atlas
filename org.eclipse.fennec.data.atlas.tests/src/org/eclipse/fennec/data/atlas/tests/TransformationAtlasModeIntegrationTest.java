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
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
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
import java.util.concurrent.TimeUnit;

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
 * Milestone 6 in Model Atlas mode: the configuration — including the
 * {@code DataTransformation} and the bridge — travels through the Model Atlas,
 * and the served objects are transformed. The transformation's CompiledUnit
 * document is named by an absolute URI the runtime resolves locally, exactly
 * like every {@code FileDataInput} in this mode; publishing the document into
 * a Model Atlas registry additionally is blocked upstream — the m2x metamodels
 * carry relative cross-repository references a Model Atlas cannot serve (the
 * configuration.ecore lesson of the history example, this time upstream).
 *
 * <p>Skipped when docker (or the image) is not available.</p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class TransformationAtlasModeIntegrationTest {

	private static final String IMAGE = "eclipsefennec/model.atlas:file-snapshot";
	private static final String CONTAINER = "dataatlas-it-trafo-modelatlas";
	private static final int MODEL_ATLAS_PORT = 18094;
	private static final int HTTP_PORT = 18085;
	private static final String ATLAS_BASE = "http://localhost:" + MODEL_ATLAS_PORT + "/atlas/rest";
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest/example-public/public-persons";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration clientConfig;
	private static Configuration bootstrapConfig;
	private static boolean containerStarted;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		assumeTrue(docker("version") == 0, "docker is not available");

		Path dir = extract(bundleContext);

		docker("rm", "-f", CONTAINER);
		int started = docker("run", "-d", "--name", CONTAINER, "-p", MODEL_ATLAS_PORT + ":8080",
				"-e", "JAVA_TOOL_OPTIONS=-Dconfigurator.initial=file:///opt/modelatlas/runtime/load/dataatlas.json",
				"-v", dir.resolve("atlas/load") + ":/opt/modelatlas/runtime/load:ro",
				IMAGE);
		assumeTrue(started == 0, "could not start " + IMAGE);
		containerStarted = true;

		awaitOk(ATLAS_BASE + "/scopes/dataatlas", "application/json", 120_000);
		seed(dir);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "trafoAtlasTest", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "trafoAtlasTest");
		httpProps.put("org.apache.felix.http.runtime.init.id", "trafoAtlasTestHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "trafoAtlasTestRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "trafoAtlasTestRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=trafoAtlasTestHttp)");
		whiteboardConfig.update(wbProps);

		clientConfig = configAdmin
				.getFactoryConfiguration("org.eclipse.fennec.model.atlas.rest.client", "trafoAtlasTest", "?");
		Dictionary<String, Object> clientProps = new Hashtable<>();
		clientProps.put("base.uri", ATLAS_BASE);
		clientProps.put("scope.allow.list", new String[] { "dataatlas" });
		clientProps.put("cache.ttl.ms", 1000L);
		clientConfig.update(clientProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasModelAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("atlas.registry", "configurations");
		bootProps.put("atlas.object.id", "dataatlas");
		bootProps.put("refresh.interval.ms", 2000L);
		bootProps.put("scopeService.target", "(atlas.scope=dataatlas)");
		bootstrapConfig.update(bootProps);
	}

	@AfterAll
	static void tearDown() throws Exception {
		for (Configuration configuration : new Configuration[] { bootstrapConfig, clientConfig, whiteboardConfig,
				httpConfig }) {
			if (configuration != null) {
				configuration.delete();
			}
		}
		if (containerStarted) {
			docker("rm", "-f", CONTAINER);
		}
	}

	@Test
	void servesTransformedObjectsFromAtlasConfiguration() throws Exception {
		HttpResponse<String> list = awaitOk(BASE_URL, "application/json", 120_000);
		assertEquals(200, list.statusCode());
		assertTrue(list.body().contains("Ada Lovelace"), list.body());
		assertTrue(list.body().contains("displayName"), list.body());
		assertFalse(list.body().contains("firstName"),
				() -> "source features must not leak through the bridge: " + list.body());

		HttpResponse<String> byId = get(BASE_URL + "/p3", "application/json");
		assertEquals(200, byId.statusCode());
		assertTrue(byId.body().contains("Margaret Hamilton"), byId.body());
	}

	// --- plumbing (the ModelAtlasModeIntegrationTest pattern) ---------------

	private static Path extract(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-trafo-atlas-it");
		for (String root : new String[] { "data", "atlas" }) {
			Enumeration<URL> entries = bundle.findEntries(root, "*", true);
			while (entries != null && entries.hasMoreElements()) {
				URL url = entries.nextElement();
				String path = url.getPath();
				if (path.endsWith("/")) {
					continue;
				}
				Path file = target.resolve(path.substring(1));
				Files.createDirectories(file.getParent());
				try (InputStream in = url.openStream()) {
					Files.copy(in, file);
				}
			}
		}
		return target;
	}

	/**
	 * Seeds the schemas (both stages) and the transformation instance, with the
	 * absolute-URI placeholders retargeted to the extracted fixture tree.
	 */
	private static void seed(Path dir) throws Exception {
		postSchema(dir.resolve("atlas/models/eorm.ecore"), "https://eclipse.org/fennec/persistence/eorm/1.0.0");
		postSchema(dir.resolve("atlas/models/configuration.ecore"),
				"https://eclipse.org/fennec/data/atlas/configuration/1.0.0");
		postSchema(dir.resolve("data/model/person.ecore"),
				"https://eclipse.org/fennec/data/atlas/example/person/1.0.0");
		postSchema(dir.resolve("data/model/person-public.ecore"),
				"https://eclipse.org/fennec/data/atlas/example/person/public/1.0.0");

		String dataBase = dir.resolve("data").toUri().toString();
		String instance = Files.readString(dir.resolve("data/dataatlas-trafo-atlas.xmi"), StandardCharsets.UTF_8)
				.replace("file:///DATA/", dataBase);
		HttpResponse<String> seeded = postInstance(instance);
		assertTrue(seeded.statusCode() == 201 || seeded.statusCode() == 409,
				() -> "instance seed failed: " + seeded.statusCode() + " " + seeded.body());
	}

	private static HttpResponse<String> postInstance(String body) throws Exception {
		return CLIENT.send(HttpRequest
				.newBuilder(java.net.URI.create(ATLAS_BASE
						+ "/dataatlas/registries/configurations/stages/release/dataatlas?name=dataatlas&override=true"))
				.header("Content-Type", "application/xmi")
				.header("Accept", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.build(), HttpResponse.BodyHandlers.ofString());
	}

	private static void postSchema(Path file, String nsUri) throws Exception {
		String enc = URLEncoder.encode(nsUri, StandardCharsets.UTF_8);
		for (String stage : new String[] { "release", "draft" }) {
			HttpResponse<String> response = CLIENT.send(HttpRequest
					.newBuilder(java.net.URI.create(ATLAS_BASE + "/dataatlas/schema/stages/" + stage + "?nsUri="
							+ enc + "&version=1.0.0"))
					.header("Content-Type", "application/xmi")
					.header("Accept", "application/json")
					.POST(HttpRequest.BodyPublishers.ofByteArray(Files.readAllBytes(file)))
					.build(), HttpResponse.BodyHandlers.ofString());
			assertTrue(response.statusCode() == 201 || response.statusCode() == 409,
					() -> "schema seed " + file.getFileName() + " (" + stage + ") failed: " + response.statusCode()
							+ " " + response.body());
		}
	}

	private static int docker(String... args) throws Exception {
		String[] command = new String[args.length + 1];
		command[0] = "docker";
		System.arraycopy(args, 0, command, 1, args.length);
		Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
		process.getInputStream().readAllBytes();
		return process.waitFor(300, TimeUnit.SECONDS) ? process.exitValue() : -1;
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
			Thread.sleep(1000);
		}
		if (lastError != null) {
			throw lastError;
		}
		return response;
	}
}
