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
import static org.junit.jupiter.api.Assertions.fail;
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
import java.util.List;
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
 * The SensiNact history example in Model Atlas mode — the setup
 * {@code docker-compose-history.yml} runs by default, as a docker-gated test:
 * a real TimescaleDB seeded with the store's DDL, a real
 * {@code eclipsefennec/model.atlas:file-snapshot} holding the configuration,
 * and the atlas-mode bootstrap in this framework.
 *
 * <p>
 * What this adds to the file-mode {@link DataAtlasHistoryIntegrationTest}: the
 * configuration travels through the Model Atlas with its <b>inline eorm
 * mapping</b>. The eorm model uses ExtendedMetaData XML names
 * ({@code column-definition}), which the Model Atlas could store but not read
 * back until model.atlas#213 was fixed. The test pins both halves — the
 * read-back still carries the column definitions, and the mapping the client
 * loaded reaches the lower-case, schema-qualified tables — so the recordings
 * arrive exactly as in file mode.
 * </p>
 *
 * <p>Skipped where docker or an image is unavailable.</p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class DataAtlasHistoryAtlasModeIntegrationTest {

	private static final String DB_IMAGE = "timescale/timescaledb-ha:pg16";
	private static final String DB_CONTAINER = "dataatlas-it-history-atlas-db";
	private static final String ATLAS_IMAGE = "eclipsefennec/model.atlas:file-snapshot";
	private static final String ATLAS_CONTAINER = "dataatlas-it-history-modelatlas";
	private static final int DB_PORT = 18102;
	private static final int MODEL_ATLAS_PORT = 18101;
	private static final int HTTP_PORT = 18084;
	private static final String ATLAS_BASE = "http://localhost:" + MODEL_ATLAS_PORT + "/atlas/rest";
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest/history";
	private static final String DB = "sensinact";
	private static final String TEXT_CSV = "text/csv";
	private static final long DEADLINE_MS = 180_000;

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration dataSourceConfig;
	private static Configuration clientConfig;
	private static Configuration bootstrapConfig;
	private static boolean dbStarted;
	private static boolean atlasStarted;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		assumeTrue(docker("version") == 0, "docker is not available");

		Path dir = extract(bundleContext);

		docker("rm", "-f", DB_CONTAINER);
		int dbStart = docker("run", "-d", "--name", DB_CONTAINER,
				"-p", DB_PORT + ":5432",
				"-e", "POSTGRES_DB=" + DB,
				"-e", "POSTGRES_USER=" + DB,
				"-e", "POSTGRES_PASSWORD=" + DB,
				"-v", dir.resolve("history/init") + ":/docker-entrypoint-initdb.d:ro",
				DB_IMAGE);
		assumeTrue(dbStart == 0, "could not start " + DB_IMAGE);
		dbStarted = true;

		docker("rm", "-f", ATLAS_CONTAINER);
		int atlasStart = docker("run", "-d", "--name", ATLAS_CONTAINER, "-p", MODEL_ATLAS_PORT + ":8080",
				"-e", "JAVA_TOOL_OPTIONS=-Dconfigurator.initial=file:///opt/modelatlas/runtime/load/dataatlas.json",
				"-v", dir.resolve("atlas/load") + ":/opt/modelatlas/runtime/load:ro",
				ATLAS_IMAGE);
		assumeTrue(atlasStart == 0, "could not start " + ATLAS_IMAGE);
		atlasStarted = true;

		awaitOk(ATLAS_BASE + "/scopes/dataatlas", "application/json", 120_000);
		seed(dir);
		awaitSeededDatabase();

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "historyAtlas", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "historyAtlas");
		httpProps.put("org.apache.felix.http.runtime.init.id", "historyAtlasHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "historyAtlasRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "historyAtlasRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=historyAtlasHttp)");
		whiteboardConfig.update(wbProps);

		// the DataSource the configuration's DataSource filter selects
		// ((dataSourceName=historyDs)) - in the compose setup a mounted
		// Configurator file, here Config Admin directly
		dataSourceConfig = configAdmin
				.getFactoryConfiguration("daanse.jdbc.datasource.postgresql.DataSource", "historyAtlas", "?");
		Dictionary<String, Object> dsProps = new Hashtable<>();
		dsProps.put("host", "localhost");
		dsProps.put("port", Integer.valueOf(DB_PORT));
		dsProps.put("dbname", DB);
		dsProps.put("user", DB);
		dsProps.put(".password", DB);
		dsProps.put("dataSourceName", "historyDs");
		dataSourceConfig.update(dsProps);

		clientConfig = configAdmin
				.getFactoryConfiguration("org.eclipse.fennec.model.atlas.rest.client", "historyAtlasTest", "?");
		Dictionary<String, Object> clientProps = new Hashtable<>();
		clientProps.put("base.uri", ATLAS_BASE);
		clientProps.put("scope.allow.list", new String[] { "dataatlas" });
		clientProps.put("cache.ttl.ms", 1000L);
		clientConfig.update(clientProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasModelAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("atlas.registry", "configurations");
		bootProps.put("atlas.object.id", "dataatlas");
		bootProps.put("refresh.interval.ms", 5000L);
		bootProps.put("scopeService.target", "(atlas.scope=dataatlas)");
		bootstrapConfig.update(bootProps);
	}

	@AfterAll
	static void tearDown() throws Exception {
		for (Configuration configuration : new Configuration[] { bootstrapConfig, clientConfig, dataSourceConfig,
				whiteboardConfig, httpConfig }) {
			if (configuration != null) {
				configuration.delete();
			}
		}
		if (atlasStarted) {
			docker("rm", "-f", ATLAS_CONTAINER);
		}
		if (dbStarted) {
			docker("rm", "-f", DB_CONTAINER);
		}
	}

	/**
	 * The model.atlas#213 half: the configuration the Model Atlas hands back
	 * still carries the eorm column definitions — ExtendedMetaData-named
	 * attributes, which were stored but silently dropped on read before the fix.
	 */
	@Test
	void theModelAtlasHandsBackTheInlineMappingIntact() throws Exception {
		HttpResponse<String> response = awaitOk(ATLAS_BASE
				+ "/dataatlas/registries/configurations/stages/release/content?objectId=dataatlas",
				"application/xmi", 60_000);
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("columnDefinition=\"TIMESTAMPTZ\""),
				() -> "the read-back lost the column definitions: " + response.body());
		assertTrue(response.body().contains("schema=\"sensinact\""),
				() -> "the read-back lost the schema-qualified table: " + response.body());
	}

	@Test
	void servesTheNumericHypertableAsCsv() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL + "/numeric", TEXT_CSV, DEADLINE_MS);

		assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith(TEXT_CSV),
				"expected a text/csv response, got " + response.headers().firstValue("Content-Type"));
		List<String> rows = response.body().lines().filter(line -> !line.isBlank()).toList();
		String header = rows.get(0);
		assertTrue(header.contains(";"), "expected the configured ';' separator: " + header);
		// the EMF-side attribute names, not the lower-case column names — the
		// mapping the client loaded from the Model Atlas bridged the two
		assertTrue(header.contains("modelPackageUri"), "expected the model attribute names in the header: " + header);
		assertTrue(response.body().contains("temperature"),
				"expected the seeded numeric recordings: " + response.body());
		assertTrue(rows.size() > 1, "expected data rows, got only: " + rows);
	}

	@Test
	void servesTheGeographyColumnThroughTheProjectingView() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL + "/geo", TEXT_CSV, DEADLINE_MS);

		String header = response.body().lines().filter(line -> !line.isBlank()).findFirst().orElse("");
		assertTrue(header.contains("location") && header.contains("longitude") && header.contains("latitude"),
				"expected the projected geo columns in the header: " + header);
		assertTrue(response.body().contains("POINT(11.582 50.927)"),
				"expected the WKT of the seeded point: " + response.body());
	}

	@Test
	void servesTheSameDataAsJsonAndRefusesXml() throws Exception {
		HttpResponse<String> json = awaitOk(BASE_URL + "/text", "application/json", DEADLINE_MS);
		assertTrue(json.body().contains("maintenance"), "expected the seeded textual recordings: " + json.body());

		assertEquals(406, get(BASE_URL + "/text", "application/xml").statusCode(),
				"the example declares CSV and JSON only");
	}

	// --- plumbing -------------------------------------------------------

	private static void awaitSeededDatabase() throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		while (System.currentTimeMillis() < deadline) {
			if (docker("exec", DB_CONTAINER, "psql", "-U", DB, "-d", DB, "-tAc",
					"select count(*) from sensinact.numeric_data_recent") == 0) {
				return;
			}
			Thread.sleep(2000);
		}
		fail("timed out waiting for the seeded TimescaleDB container");
	}

	/** Seeds the schemas and the history configuration, mirroring the compose seeder. */
	private static void seed(Path dir) throws Exception {
		postSchema(dir.resolve("atlas/models/eorm.ecore"), "https://eclipse.org/fennec/persistence/eorm/1.0.0");
		postSchema(dir.resolve("atlas/models/configuration.ecore"),
				"https://eclipse.org/fennec/data/atlas/configuration/1.0.0");
		postSchema(dir.resolve("data/model/sensinact-history.ecore"),
				"https://eclipse.org/fennec/data/atlas/example/sensinact/history/1.0.0");

		// the example instance as the compose setup seeds it - nothing to
		// retarget, the JPA input selects its DataSource by filter
		String instance = Files.readString(dir.resolve("data/dataatlas-history-atlas.xmi"), StandardCharsets.UTF_8);
		HttpResponse<String> seeded = null;
		// a freshly uploaded schema is not necessarily resolvable in the same
		// breath - the stage's package view catches up asynchronously (see the
		// compose seeder), so the instance upload is retried a bounded number of times
		for (int attempt = 0; attempt < 15; attempt++) {
			seeded = CLIENT.send(HttpRequest
					.newBuilder(java.net.URI.create(ATLAS_BASE
							+ "/dataatlas/registries/configurations/stages/release/dataatlas?name=dataatlas&override=true"))
					.header("Content-Type", "application/xmi")
					.header("Accept", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(instance))
					.build(), HttpResponse.BodyHandlers.ofString());
			if (seeded.statusCode() == 201 || seeded.statusCode() == 200 || seeded.statusCode() == 409) {
				return;
			}
			Thread.sleep(2000);
		}
		fail("instance seed failed: " + seeded.statusCode() + " " + seeded.body());
	}

	private static void postSchema(Path file, String nsUri) throws Exception {
		String enc = URLEncoder.encode(nsUri, StandardCharsets.UTF_8);
		HttpResponse<String> response = CLIENT.send(HttpRequest
				.newBuilder(java.net.URI.create(
						ATLAS_BASE + "/dataatlas/schema/stages/release?nsUri=" + enc + "&version=1.0.0"))
				.header("Content-Type", "application/xmi")
				.header("Accept", "application/json")
				.POST(HttpRequest.BodyPublishers.ofByteArray(Files.readAllBytes(file)))
				.build(), HttpResponse.BodyHandlers.ofString());
		assertTrue(response.statusCode() == 201 || response.statusCode() == 409,
				() -> "schema seed " + file.getFileName() + " failed: " + response.statusCode() + " " + response.body());
	}

	private static Path extract(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-history-atlas-it");
		for (String root : new String[] { "data", "atlas", "history" }) {
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

	private static int docker(String... args) throws Exception {
		String[] command = new String[args.length + 1];
		command[0] = "docker";
		System.arraycopy(args, 0, command, 1, args.length);
		Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
		process.getInputStream().readAllBytes();
		return process.waitFor(600, TimeUnit.SECONDS) ? process.exitValue() : -1;
	}

	private static HttpResponse<String> get(String url, String accept) throws Exception {
		return CLIENT.send(HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", accept)
				.timeout(Duration.ofSeconds(20))
				.GET()
				.build(), HttpResponse.BodyHandlers.ofString());
	}

	private static HttpResponse<String> awaitOk(String url, String accept, long timeoutMs) throws Exception {
		long deadline = System.currentTimeMillis() + timeoutMs;
		HttpResponse<String> response = null;
		Exception lastError = null;
		while (System.currentTimeMillis() < deadline) {
			try {
				response = get(url, accept);
				lastError = null;
				if (response.statusCode() == 200 && !response.body().isBlank()) {
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
		fail("timed out waiting for " + url + " (" + accept + "); last: "
				+ (response == null ? "none" : response.statusCode() + " " + response.body()));
		return null;
	}
}
