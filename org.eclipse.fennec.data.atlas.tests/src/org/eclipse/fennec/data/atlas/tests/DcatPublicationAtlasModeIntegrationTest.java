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

import static org.eclipse.fennec.data.atlas.tests.DcatPublicationIntegrationTest.PERSON_DOCUMENTATION;
import static org.eclipse.fennec.data.atlas.tests.DcatPublicationIntegrationTest.awaitState;
import static org.eclipse.fennec.data.atlas.tests.DcatPublicationIntegrationTest.statusReference;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import org.eclipse.fennec.data.atlas.api.PublicationStatus;
import org.eclipse.fennec.dcat.atlas.client.api.DcatAtlasClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import dcat.Dataset;
import dcat.Distribution;

/**
 * Milestone 8 in Model Atlas mode — the residual risk the file-mode
 * {@link DcatPublicationIntegrationTest} cannot cover: derivation v1 reads the
 * <b>GenModel documentation annotation</b> of the DataSet's output type when the
 * DataSet declares no description. In atlas mode that EClass is the one the
 * model.atlas client loaded from the schema registry, so the annotation has to
 * survive the Model Atlas round-trip (the model.atlas#213 class of problem, for
 * EAnnotations instead of ExtendedMetaData).
 *
 * <p>
 * A real {@code eclipsefennec/model.atlas:file-snapshot} container serves the
 * configuration; the portal is the recording double. The expected metadata is
 * the file-mode test's, literally — both modes must publish the same thing.
 * The second test walks a staged configuration update (draft → release) that
 * drops the publication declaration and expects the withdrawal, i.e. the
 * Milestone 4 lifecycle reaching the publication in atlas mode.
 * </p>
 *
 * <p>Skipped when docker (or the image) is not available.</p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class DcatPublicationAtlasModeIntegrationTest {

	private static final String IMAGE = "eclipsefennec/model.atlas:file-snapshot";
	private static final String CONTAINER = "dataatlas-it-dcat-modelatlas";
	private static final int MODEL_ATLAS_PORT = 18100;
	private static final String ATLAS_BASE = "http://localhost:" + MODEL_ATLAS_PORT + "/atlas/rest";
	private static final String PUBLIC_BASE = "https://data.example.org";
	private static final String PUBLICATION_PID = "org.eclipse.fennec.data.atlas.publication.dcat";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static RecordingDcatAtlasClient portal;
	private static ServiceRegistration<DcatAtlasClient> portalRegistration;
	private static Configuration publicationConfig;
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

		portal = new RecordingDcatAtlasClient();
		Dictionary<String, Object> portalProps = new Hashtable<>();
		portalProps.put("dcat.portal", "mock");
		portalRegistration = bundleContext.registerService(DcatAtlasClient.class, portal, portalProps);

		publicationConfig = configAdmin.getConfiguration(PUBLICATION_PID, "?");
		Dictionary<String, Object> publicationProps = new Hashtable<>();
		publicationProps.put("public.base.url", PUBLIC_BASE);
		publicationProps.put("retry.interval.ms", 2_000L);
		publicationConfig.update(publicationProps);

		clientConfig = configAdmin
				.getFactoryConfiguration("org.eclipse.fennec.model.atlas.rest.client", "dcatAtlasTest", "?");
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
		bootProps.put("refresh.interval.ms", 2000L);
		bootProps.put("scopeService.target", "(atlas.scope=dataatlas)");
		bootstrapConfig.update(bootProps);
	}

	@AfterAll
	static void tearDown() throws Exception {
		for (Configuration configuration : new Configuration[] { bootstrapConfig, clientConfig, publicationConfig }) {
			if (configuration != null) {
				configuration.delete();
			}
		}
		if (portalRegistration != null) {
			portalRegistration.unregister();
		}
		if (containerStarted) {
			docker("rm", "-f", CONTAINER);
		}
	}

	@Test
	@Order(1)
	void publishesTheSameMetadataAsFileMode(@InjectBundleContext BundleContext bundleContext) throws Exception {
		awaitState(bundleContext, "dcat-persons-rest", PublicationStatus.STATE_REGISTERED, 120_000);

		dcat.DataService service = portal.dataServices.get("dcat-persons-rest");
		assertNotNull(service, "expected the DataService to be registered");
		assertEquals(PUBLIC_BASE + "/dcat-example", service.getEndpointURL().get(0));
		assertEquals("Persons REST", service.getTitle().get(0).getValue());

		// THE assertion of this test: the description was derived from the
		// GenModel documentation of Person - an EClass the model.atlas client
		// loaded from the schema registry, not from a local ecore file
		Dataset dataset = portal.datasets.get("dcat-persons");
		assertNotNull(dataset, "expected the DataSet to be registered");
		assertEquals("persons", dataset.getTitle().get(0).getValue());
		assertEquals(PERSON_DOCUMENTATION, dataset.getDescription().get(0).getValue(),
				"the GenModel documentation annotation did not survive the Model Atlas round-trip");
		assertEquals(2, dataset.getKeyword().size());

		Distribution json = portal.distributions.get("dcat-persons/json");
		assertNotNull(json, "expected a distribution for the JSON default");
		assertEquals(PUBLIC_BASE + "/dcat-example/persons", json.getAccessURL().get(0));
		assertEquals("http://dcat-ap.de/def/licenses/dl-by-de/2.0", json.getLicense().getAbout());
		assertTrue(portal.links.contains("service:dcat-persons-rest<-dataset:dcat-persons"), portal.links.toString());
	}

	@Test
	@Order(2)
	void stagedUpdateWithoutTheDeclarationWithdraws(@InjectBundleContext BundleContext bundleContext)
			throws Exception {
		publishVersion(seededInstance.replace(" publication=\"dcat-open-data\"", ""));

		long deadline = System.currentTimeMillis() + 90_000;
		while (System.currentTimeMillis() < deadline
				&& (portal.dataServices.containsKey("dcat-persons-rest") || portal.datasets.containsKey("dcat-persons"))) {
			Thread.sleep(500);
		}
		assertTrue(portal.deletions.contains("datasets:dcat-persons"), portal.deletions.toString());
		assertTrue(portal.deletions.contains("data-services:dcat-persons-rest"), portal.deletions.toString());

		deadline = System.currentTimeMillis() + 30_000;
		while (System.currentTimeMillis() < deadline && statusReference(bundleContext, "dcat-persons-rest") != null) {
			Thread.sleep(500);
		}
		assertEquals(null, statusReference(bundleContext, "dcat-persons-rest"),
				"expected the PublicationStatus of the withdrawn provider to be unregistered");
	}

	// --- plumbing -------------------------------------------------------

	/** Extracts the /data and /atlas fixture trees of this bundle into a temp folder. */
	private static Path extract(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-dcat-atlas-it");
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

	/** Seeds the schemas and the atlas-mode DCAT fixture, mirroring the compose seeder. */
	private static void seed(Path dir) throws Exception {
		postSchema(dir.resolve("atlas/models/eorm.ecore"), "https://eclipse.org/fennec/persistence/eorm/1.0.0");
		postSchema(dir.resolve("atlas/models/configuration.ecore"),
				"https://eclipse.org/fennec/data/atlas/configuration/1.0.0");
		postSchema(dir.resolve("data/model/person.ecore"),
				"https://eclipse.org/fennec/data/atlas/example/person/1.0.0");

		seededInstance = Files.readString(dir.resolve("data/dataatlas-dcat-atlas.xmi"), StandardCharsets.UTF_8)
				.replace("/opt/dataatlas/runtime/data/data/persons.xmi",
						dir.resolve("data/data/persons.xmi").toUri().toString());
		HttpResponse<String> seeded = seedInstanceWithRetry(seededInstance);
		assertTrue(seeded.statusCode() == 201 || seeded.statusCode() == 409,
				() -> "instance seed failed: " + seeded.statusCode() + " " + seeded.body());
	}

	/**
	 * A freshly uploaded schema is not necessarily resolvable for an instance
	 * upload in the same breath — the stage's package view catches up
	 * asynchronously, and a too-early POST answers 500 "Error de-serializing
	 * incoming data". The compose seeder retries for the same reason.
	 */
	private static HttpResponse<String> seedInstanceWithRetry(String body) throws Exception {
		HttpResponse<String> response = null;
		for (int attempt = 0; attempt < 15; attempt++) {
			response = postInstance("release", body);
			if (response.statusCode() != 500) {
				return response;
			}
			Thread.sleep(2000);
		}
		return response;
	}

	private static HttpResponse<String> postInstance(String stage, String body) throws Exception {
		return CLIENT.send(HttpRequest
				.newBuilder(java.net.URI.create(ATLAS_BASE + "/dataatlas/registries/configurations/stages/" + stage
						+ "/dataatlas?name=dataatlas&override=true"))
				.header("Content-Type", "application/xmi")
				.header("Accept", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString(body))
				.build(), HttpResponse.BodyHandlers.ofString());
	}

	/** Publishes a new configuration version through the stage workflow (draft -> release). */
	private static void publishVersion(String body) throws Exception {
		HttpResponse<String> posted = postInstance("draft", body);
		assertTrue(posted.statusCode() == 201 || posted.statusCode() == 200,
				() -> "draft upload failed: " + posted.statusCode() + " " + posted.body());
		HttpResponse<String> transitioned = CLIENT.send(HttpRequest
				.newBuilder(java.net.URI.create(
						ATLAS_BASE + "/dataatlas/registries/configurations/stages/draft/actions/transition"))
				.header("Content-Type", "application/json")
				.header("Accept", "application/json")
				.POST(HttpRequest.BodyPublishers
						.ofString("{\"objectId\": \"dataatlas\", \"targetStage\": \"release\"}"))
				.build(), HttpResponse.BodyHandlers.ofString());
		assertTrue(transitioned.statusCode() >= 200 && transitioned.statusCode() < 300,
				() -> "transition failed: " + transitioned.statusCode() + " " + transitioned.body());
	}

	private static void postSchema(Path file, String nsUri) throws Exception {
		String enc = URLEncoder.encode(nsUri, StandardCharsets.UTF_8);
		// the instance lives in release, the staged update is uploaded to draft
		// first - each stage resolves against its own package view
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

	private static HttpResponse<String> awaitOk(String url, String accept, long timeoutMs) throws Exception {
		long deadline = System.currentTimeMillis() + timeoutMs;
		HttpResponse<String> response = null;
		Exception lastError = null;
		while (System.currentTimeMillis() < deadline) {
			try {
				response = CLIENT.send(HttpRequest.newBuilder(java.net.URI.create(url))
						.header("Accept", accept)
						.timeout(Duration.ofSeconds(10))
						.GET()
						.build(), HttpResponse.BodyHandlers.ofString());
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
