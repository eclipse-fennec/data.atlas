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
 * Docker-gated end-to-end test of the Model Atlas config mode: starts a real
 * {@code eclipsefennec/model.atlas:file-snapshot} container (custom scope +
 * EObject-rooted registry via {@code configurator.initial}), seeds the schemas
 * and the example {@code DataAtlasConfiguration} over REST, boots the
 * atlas-mode bootstrap in this framework via the model.atlas client, and
 * asserts the configured DataSet is served exactly like in file mode.
 *
 * <p>Skipped when docker (or the image) is not available.</p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class ModelAtlasModeIntegrationTest {

	private static final String IMAGE = "eclipsefennec/model.atlas:file-snapshot";
	private static final String CONTAINER = "dataatlas-it-modelatlas";
	private static final int MODEL_ATLAS_PORT = 18095;
	private static final int HTTP_PORT = 18087;
	private static final String ATLAS_BASE = "http://localhost:" + MODEL_ATLAS_PORT + "/atlas/rest";
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest/example/persons";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration clientConfig;
	private static Configuration bootstrapConfig;
	private static boolean containerStarted;
	private static String seededInstance;

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

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "atlasModeTest", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "atlasModeTest");
		httpProps.put("org.apache.felix.http.runtime.init.id", "atlasModeTestHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "atlasModeTestRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "atlasModeTestRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=atlasModeTestHttp)");
		whiteboardConfig.update(wbProps);

		clientConfig = configAdmin
				.getFactoryConfiguration("org.eclipse.fennec.model.atlas.rest.client", "atlasModeTest", "?");
		Dictionary<String, Object> clientProps = new Hashtable<>();
		clientProps.put("base.uri", ATLAS_BASE);
		clientProps.put("scope.allow.list", new String[] { "dataatlas" });
		// without a TTL the client cache never revalidates and the refresh
		// poll would keep seeing the same instance forever
		clientProps.put("cache.ttl.ms", 1000L);
		clientConfig.update(clientProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasModelAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("atlas.registry", "configurations");
		bootProps.put("atlas.object.id", "dataatlas");
		// short refresh so the staged-update lifecycle test completes quickly
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
	void servesDataSetAsJson() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL, "application/json", 120_000);
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("Lovelace"), () -> "missing Lovelace in: " + response.body());
		assertTrue(response.body().contains("Hopper"), () -> "missing Hopper in: " + response.body());
		// EClass URIs are canonical nsURI-based, independent of the config source
		assertTrue(response.body().contains("https://eclipse.org/fennec/data/atlas/example/person/1.0.0#//Person"),
				() -> "non-canonical _type in: " + response.body());
	}

	@Test
	void servesSingleObjectAsXml() throws Exception {
		awaitOk(BASE_URL, "application/json", 120_000);
		HttpResponse<String> response = get(BASE_URL + "/p2", "application/xml");
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("Hopper"), () -> "missing Hopper in: " + response.body());
		assertTrue(response.body().contains("https://eclipse.org/fennec/data/atlas/example/person/1.0.0"),
				() -> "missing person namespace in: " + response.body());
	}

	/**
	 * Milestone 4 lifecycle: a new configuration version published through the
	 * stage workflow (draft -> release) reaches the running instance within the
	 * refresh interval; a broken version fails hard; a corrected version
	 * recovers the instance.
	 */
	@Test
	void appliesStagedConfigurationUpdates() throws Exception {
		awaitOk(BASE_URL, "application/json", 120_000);

		// v2 ADDS a second endpoint for the same DataSet
		publishVersion(seededInstance.replace(
				"<configuration id=\"persons-rest-config\" dataSet=\"persons\" path=\"persons\"/>",
				"<configuration id=\"persons-rest-config\" dataSet=\"persons\" path=\"persons\"/>\n"
						+ "    <configuration id=\"persons2-config\" dataSet=\"persons\" path=\"persons2\"/>"));
		HttpResponse<String> added = awaitOk(BASE_URL.replace("/persons", "/persons2"), "application/json", 60_000);
		assertEquals(200, added.statusCode());
		assertTrue(added.body().contains("Lovelace"), () -> "missing Lovelace in: " + added.body());
		assertEquals(200, get(BASE_URL, "application/json").statusCode());

		// a broken version (unresolvable EClass reference) fails hard
		publishVersion(seededInstance.replace(
				"https://eclipse.org/fennec/data/atlas/example/person/1.0.0#//Person",
				"https://eclipse.org/fennec/does/not/exist/1.0.0#//Nope"));
		assertEquals(404, awaitStatus(BASE_URL, 404, 60_000));

		// the corrected version recovers the instance
		publishVersion(seededInstance);
		assertEquals(200, awaitOk(BASE_URL, "application/json", 60_000).statusCode());
		assertEquals(404, get(BASE_URL.replace("/persons", "/persons2"), "application/json").statusCode());
	}

	/** Polls until the URL answers with the wanted status (or times out). */
	private int awaitStatus(String url, int wanted, long timeoutMs) throws Exception {
		long deadline = System.currentTimeMillis() + timeoutMs;
		int last = -1;
		while (System.currentTimeMillis() < deadline) {
			try {
				last = get(url, "application/json").statusCode();
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

	@Test
	void appliesPaginationAndAnswers404() throws Exception {
		awaitOk(BASE_URL, "application/json", 120_000);
		HttpResponse<String> paged = get(BASE_URL + "?offset=1&limit=1", "application/json");
		assertEquals(200, paged.statusCode());
		assertTrue(paged.body().contains("Hopper"), () -> "missing Hopper in: " + paged.body());
		assertTrue(!paged.body().contains("Lovelace"), () -> "unexpected Lovelace in: " + paged.body());
		assertEquals(404, get("http://localhost:" + HTTP_PORT + "/rest/example/unknown", "application/json")
				.statusCode());
	}

	// --- plumbing -------------------------------------------------------

	/**
	 * Extracts the /data and /atlas fixture trees of this bundle into a temp
	 * folder (the configurator config is mounted into the container, the
	 * schemas are seeded over REST).
	 */
	private static Path extract(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-atlas-it");
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

	/** Seeds the schemas and the example instance, mirroring the compose seeder. */
	private static void seed(Path dir) throws Exception {
		postSchema(dir.resolve("atlas/models/eorm.ecore"), "https://eclipse.org/fennec/persistence/eorm/1.0.0");
		postSchema(dir.resolve("atlas/models/configuration.ecore"),
				"https://eclipse.org/fennec/data/atlas/configuration/1.0.0");
		postSchema(dir.resolve("data/model/person.ecore"),
				"https://eclipse.org/fennec/data/atlas/example/person/1.0.0");

		// the example instance, with the data location retargeted to the
		// extracted persons.xmi (the atlas-mode bootstrap uses URIs as-is)
		seededInstance = Files.readString(dir.resolve("data/dataatlas-atlas.xmi"), StandardCharsets.UTF_8)
				.replace("/opt/dataatlas/runtime/data/data/persons.xmi",
						dir.resolve("data/data/persons.xmi").toUri().toString());
		HttpResponse<String> seeded = postInstance("release", seededInstance);
		assertTrue(seeded.statusCode() == 201 || seeded.statusCode() == 409,
				() -> "instance seed failed: " + seeded.statusCode() + " " + seeded.body());
	}

	/** Uploads a DataAtlasConfiguration instance version into the given stage. */
	private static HttpResponse<String> postInstance(String stage, String body) throws Exception {
		return CLIENT.send(HttpRequest
				.newBuilder(java.net.URI.create(ATLAS_BASE + "/dataatlas/registries/configurations/stages/" + stage
						+ "/dataatlas?name=dataatlas&override=true"))
				.header("Content-Type", "application/xmi")
				.header("Accept", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.build(), HttpResponse.BodyHandlers.ofString());
	}

	/** Transitions the configuration object from draft to release. */
	private static int transitionToRelease() throws Exception {
		HttpResponse<String> response = CLIENT.send(HttpRequest
				.newBuilder(java.net.URI.create(
						ATLAS_BASE + "/dataatlas/registries/configurations/stages/draft/actions/transition"))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.POST(HttpRequest.BodyPublishers
						.ofString("{\"objectId\": \"dataatlas\", \"targetStage\": \"release\"}"))
				.build(), HttpResponse.BodyHandlers.ofString());
		return response.statusCode();
	}

	/** Publishes a new configuration version through the stage workflow. */
	private static void publishVersion(String body) throws Exception {
		HttpResponse<String> posted = postInstance("draft", body);
		assertTrue(posted.statusCode() == 201 || posted.statusCode() == 200,
				() -> "draft upload failed: " + posted.statusCode() + " " + posted.body());
		int transitioned = transitionToRelease();
		assertTrue(transitioned >= 200 && transitioned < 300, () -> "transition failed: " + transitioned);
	}

	private static void postSchema(Path file, String nsUri) throws Exception {
		String enc = URLEncoder.encode(nsUri, StandardCharsets.UTF_8);
		// the instance lives in release, but staged updates are uploaded to
		// draft first - each stage resolves against its own package view, so
		// the schemas are seeded into both stages
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
