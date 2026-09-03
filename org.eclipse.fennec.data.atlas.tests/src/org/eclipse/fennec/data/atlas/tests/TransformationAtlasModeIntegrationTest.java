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
 * Milestone 6 in Model Atlas mode, both hosting conventions:
 *
 * <ol>
 * <li>the transformation's CompiledUnit document named by an <b>absolute local
 * URI</b> (the {@code FileDataInput} convention of this mode), and</li>
 * <li>the WP-DA-7 target picture (issue #7): the document lives in a Model
 * Atlas <b>{@code transformations} registry</b>, reaches the runtime through
 * the model.atlas {@code AtlasEObjectProvider} feeding a local emf.osgi
 * {@code EObjectRegistry}, and the configuration references it with an
 * {@code eobject-registry://transformations/<key>#//@unit} URI the bootstrap
 * resolves against that registry. The m2x metamodels are seeded into the
 * Model Atlas — possible since emf.m2x#246 made their cross-references
 * nsURI-based.</li>
 * </ol>
 *
 * <p>Skipped when docker (or the image) is not available.</p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@TestMethodOrder(OrderAnnotation.class)
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
	private static Configuration initialProviderConfig;
	private static Configuration eObjectRegistryConfig;
	private static Configuration atlasProviderConfig;
	private static boolean containerStarted;
	private static String seededInstance;
	private static Path fixtureDir;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		assumeTrue(docker("version") == 0, "docker is not available");

		Path dir = extract(bundleContext);
		fixtureDir = dir;

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
		for (Configuration configuration : new Configuration[] { bootstrapConfig, atlasProviderConfig,
				eObjectRegistryConfig, initialProviderConfig, clientConfig, whiteboardConfig, httpConfig }) {
			if (configuration != null) {
				configuration.delete();
			}
		}
		if (containerStarted) {
			docker("rm", "-f", CONTAINER);
		}
	}

	@Test
	@Order(1)
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

	/**
	 * The WP-DA-7 picture (issue #7): the CompiledUnit document is uploaded
	 * into the Model Atlas {@code transformations} registry (its m2x
	 * metamodels seeded first), synced into a local emf.osgi
	 * {@code EObjectRegistry} by the model.atlas {@code AtlasEObjectProvider},
	 * and a new configuration version referencing
	 * {@code eobject-registry://transformations/person-to-public#//@unit}
	 * reaches the running instance through the stage workflow — proven by the
	 * second endpoint that version adds, serving transformed objects.
	 */
	@Test
	@Order(2)
	void servesTransformationsFromTheModelAtlasRegistry(@InjectService ConfigurationAdmin configAdmin,
			@InjectBundleContext BundleContext bundleContext) throws Exception {
		awaitOk(BASE_URL, "application/json", 120_000);

		// the m2x schema closure, dependencies first (nsURI-based since emf.m2x#246)
		seedBundleSchema(bundleContext, "org.eclipse.fennec.m2x.qvt.model", "model/qvtbase.ecore");
		seedBundleSchema(bundleContext, "org.eclipse.fennec.m2x.ocl.model", "model/ocl.ecore");
		seedBundleSchema(bundleContext, "org.eclipse.fennec.m2x.qvto.model", "model/imperativeocl.ecore");
		seedBundleSchema(bundleContext, "org.eclipse.fennec.m2x.qvto.model", "model/qvtoperational.ecore");
		seedBundleSchema(bundleContext, "org.eclipse.fennec.m2x.unit", "model/compiledunit.ecore");

		// the unit document into the transformations registry (final stage)
		HttpResponse<String> stored = CLIENT.send(HttpRequest
				.newBuilder(java.net.URI.create(ATLAS_BASE
						+ "/dataatlas/registries/transformations/stages/release/person-to-public?name=person-to-public&override=true"))
				.header("Content-Type", "application/xmi")
				.header("Accept", "application/json")
				.POST(HttpRequest.BodyPublishers
						.ofByteArray(Files.readAllBytes(fixtureDir.resolve("data/trafo/person-to-public-atlas.xmi"))))
				.build(), HttpResponse.BodyHandlers.ofString());
		assertTrue(stored.statusCode() == 201 || stored.statusCode() == 409,
				() -> "unit upload failed: " + stored.statusCode() + " " + stored.body());

		// the local registry chain: empty initial provider -> EObjectRegistry
		// 'transformations' -> AtlasEObjectProvider syncing the atlas registry
		initialProviderConfig = configAdmin.getFactoryConfiguration("FileEObjectProvider", "trafoAtlasTest", "?");
		Dictionary<String, Object> initProps = new Hashtable<>();
		initProps.put("emf.eobject.provider.name", "trafoAtlasTestInit");
		initialProviderConfig.update(initProps);

		eObjectRegistryConfig = configAdmin.getFactoryConfiguration("EObjectRegistry", "trafoAtlasTest", "?");
		Dictionary<String, Object> registryProps = new Hashtable<>();
		registryProps.put("name", "transformations");
		registryProps.put("initialProvider.target", "(emf.eobject.provider.name=trafoAtlasTestInit)");
		eObjectRegistryConfig.update(registryProps);

		atlasProviderConfig = configAdmin.getFactoryConfiguration("AtlasEObjectProvider", "trafoAtlasTest", "?");
		Dictionary<String, Object> providerProps = new Hashtable<>();
		providerProps.put("emf.eobject.provider.name", "trafoAtlasTestSync");
		providerProps.put("registries", new String[] { "transformations" });
		providerProps.put("writer.target", "(emf.eobject.registry.name=transformations)");
		providerProps.put("atlasScope.target", "(atlas.scope=dataatlas)");
		providerProps.put("refresh.interval.ms", 2000L);
		atlasProviderConfig.update(providerProps);

		// the new configuration version: the transformation now comes from the
		// registry, and a second endpoint makes the applied version observable
		String registryVersion = seededInstance
				.replaceFirst("<transformation href=\"[^\"]+\"/>",
						"<transformation href=\"eobject-registry://transformations/person-to-public#//@unit\"/>")
				.replace("<configuration id=\"public-persons-rest-config\" dataSet=\"public-persons\" path=\"public-persons\"/>",
						"<configuration id=\"public-persons-rest-config\" dataSet=\"public-persons\" path=\"public-persons\"/>\n"
								+ "    <configuration id=\"public-persons2-config\" dataSet=\"public-persons\" path=\"public-persons2\"/>");
		assertTrue(registryVersion.contains("eobject-registry://"), "href rewrite failed:\n" + registryVersion);
		assertTrue(registryVersion.contains("path=\"public-persons2\""), "second endpoint insert failed:\n" + registryVersion);
		publishVersion(registryVersion);

		HttpResponse<String> added = awaitOk(BASE_URL + "2", "application/json", 120_000);
		assertEquals(200, added.statusCode());
		assertTrue(added.body().contains("Ada Lovelace"), added.body());
		assertFalse(added.body().contains("firstName"),
				() -> "source features must not leak through the bridge: " + added.body());
		// and the first endpoint keeps serving through the registry-resolved unit
		assertTrue(get(BASE_URL + "/p2", "application/json").body().contains("Grace Hopper"));
	}

	/** Uploads one metamodel taken from an installed bundle into both stages. */
	private static void seedBundleSchema(BundleContext bundleContext, String symbolicName, String entryPath)
			throws Exception {
		Bundle bundle = java.util.Arrays.stream(bundleContext.getBundles())
				.filter(b -> symbolicName.equals(b.getSymbolicName())).findFirst()
				.orElseThrow(() -> new AssertionError("bundle " + symbolicName + " is not installed"));
		URL entry = bundle.getEntry(entryPath);
		assertTrue(entry != null, () -> "bundle " + symbolicName + " has no entry " + entryPath);
		byte[] content;
		try (InputStream in = entry.openStream()) {
			content = in.readAllBytes();
		}
		String text = new String(content, StandardCharsets.UTF_8);
		java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("nsURI=\"([^\"]+)\"").matcher(text);
		assertTrue(matcher.find(), () -> entryPath + " declares no nsURI");
		postSchema(content, matcher.group(1), entryPath.replaceAll(".*/|\\.ecore$", ""));
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
		seededInstance = Files.readString(dir.resolve("data/dataatlas-trafo-atlas.xmi"), StandardCharsets.UTF_8)
				.replace("file:///DATA/", dataBase);
		HttpResponse<String> seeded = postInstance("release", seededInstance);
		assertTrue(seeded.statusCode() == 201 || seeded.statusCode() == 409,
				() -> "instance seed failed: " + seeded.statusCode() + " " + seeded.body());
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

	/** Publishes a new configuration version through the stage workflow. */
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
		postSchema(Files.readAllBytes(file), nsUri, String.valueOf(file.getFileName()));
	}

	private static void postSchema(byte[] content, String nsUri, String name) throws Exception {
		String enc = URLEncoder.encode(nsUri, StandardCharsets.UTF_8);
		for (String stage : new String[] { "release", "draft" }) {
			HttpResponse<String> response = CLIENT.send(HttpRequest
					.newBuilder(java.net.URI.create(ATLAS_BASE + "/dataatlas/schema/stages/" + stage + "?nsUri="
							+ enc + "&version=1.0.0"))
					.header("Content-Type", "application/xmi")
					.header("Accept", "application/json")
					.POST(HttpRequest.BodyPublishers.ofByteArray(content))
					.build(), HttpResponse.BodyHandlers.ofString());
			assertTrue(response.statusCode() == 201 || response.statusCode() == 409,
					() -> "schema seed " + name + " (" + stage + ") failed: " + response.statusCode()
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
