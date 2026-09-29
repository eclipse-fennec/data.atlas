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
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
 * Docker-gated end-to-end test of the SensiNact history example: a real
 * TimescaleDB, initialised with the very SQL the compose setup mounts — the
 * unified {@code sensinact.history} table transcribed verbatim from SensiNact's
 * {@code TimescaleSql}, plus our per-kind views over it — served as CSV through
 * the Data Atlas.
 *
 * <p>
 * This is the first case in which the Data Atlas reads a schema it does not own,
 * so the test guards three things the Postgres example could not:
 * </p>
 * <ul>
 * <li>the <b>explicit</b> eorm mapping actually reaches lower-case,
 * schema-qualified tables that the derived naming could never address;</li>
 * <li>the <b>composite id</b> over {@code (time, provider, service, resource)}
 * works — the tables have no primary key, exactly as upstream defines them;</li>
 * <li>the committed DDL does not drift from what the store really creates. The
 * reference is upstream and can change, so this is the whole point: a drift that
 * breaks the mapping fails the build instead of production.</li>
 * </ul>
 *
 * <p>
 * Skipped where docker or the image is unavailable.
 * </p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class DataAtlasHistoryIntegrationTest {

	private static final String IMAGE = "timescale/timescaledb-ha:pg16";
	private static final String CONTAINER = "dataatlas-it-history";
	private static final int DB_PORT = 18097;
	private static final int HTTP_PORT = 18092;
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest/history";
	private static final String DB = "sensinact";
	private static final String TEXT_CSV = "text/csv";
	private static final long DEADLINE_MS = 180_000;

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration dataSourceConfig;
	private static Configuration bootstrapConfig;
	private static boolean containerStarted;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		assumeTrue(docker("version") == 0, "docker is not available");

		Path dir = extractTestData(bundleContext);

		docker("rm", "-f", CONTAINER);
		int started = docker("run", "-d", "--name", CONTAINER,
				"-p", DB_PORT + ":5432",
				"-e", "POSTGRES_DB=" + DB,
				"-e", "POSTGRES_USER=" + DB,
				"-e", "POSTGRES_PASSWORD=" + DB,
				"-v", dir.resolve("history/init") + ":/docker-entrypoint-initdb.d:ro",
				IMAGE);
		assumeTrue(started == 0, "could not start " + IMAGE);
		containerStarted = true;
		awaitSeededDatabase();

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "history", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "history");
		httpProps.put("org.apache.felix.http.runtime.init.id", "historyHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "historyRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "historyRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=historyHttp)");
		whiteboardConfig.update(wbProps);

		dataSourceConfig = configAdmin
				.getFactoryConfiguration("daanse.jdbc.datasource.postgresql.DataSource", "history", "?");
		Dictionary<String, Object> dsProps = new Hashtable<>();
		dsProps.put("host", "localhost");
		dsProps.put("port", Integer.valueOf(DB_PORT));
		dsProps.put("dbname", DB);
		dsProps.put("user", DB);
		dsProps.put(".password", DB);
		dsProps.put("dataSourceName", "historyDs");
		dataSourceConfig.update(dsProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", dir.resolve("dataatlas-history.xmi").toUri().toString());
		bootstrapConfig.update(bootProps);
	}

	@AfterAll
	static void tearDown() throws Exception {
		for (Configuration configuration : new Configuration[] { bootstrapConfig, dataSourceConfig, whiteboardConfig,
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
	void servesTheNumericKindAsCsv() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL + "/numeric", TEXT_CSV);

		assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith(TEXT_CSV),
				"expected a text/csv response, got " + response.headers().firstValue("Content-Type"));

		List<String> rows = response.body().lines().filter(line -> !line.isBlank()).toList();
		String header = rows.get(0);
		assertTrue(header.contains(";"), "expected the configured ';' separator: " + header);
		// the EMF-side attribute names, not the lower-case column names — proof
		// that the explicit mapping bridged the two
		assertTrue(header.contains("modelPackageUri"), "expected the model attribute names in the header: " + header);
		// the CSV lists the set attributes: a NUMBER row has valueNum, while its
		// valueKind is the EInt default 0 and valueJson is null
		assertTrue(header.contains("valueNum"), "expected the numeric value column in the header: " + header);
		assertTrue(response.body().contains("temperature"),
				"expected the seeded numeric recordings: " + response.body());
		assertTrue(rows.size() > 1, "expected data rows, got only: " + rows);
		// the DataSet's query (valueKind = 0) is pushed down: no other kind leaks in
		assertFalse(response.body().contains("maintenance") || response.body().contains("location"),
				"expected only NUMBER rows behind /numeric: " + response.body());
	}

	@Test
	void servesTheTextKindsAsCsv() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL + "/text", TEXT_CSV);

		// STRING, BOOLEAN and OBJECT share the text DataSet (valueKind IN (1, 2, 4));
		// their value is the JSON text of value_json
		String header = response.body().lines().filter(line -> !line.isBlank()).findFirst().orElse("");
		assertTrue(header.contains("valueKind") && header.contains("valueJson"),
				"expected the kind and the JSON value column in the header: " + header);
		assertTrue(response.body().contains("maintenance"),
				"expected the seeded textual recordings: " + response.body());
		assertTrue(response.body().contains("online") && response.body().contains("true"),
				"expected the seeded boolean recording: " + response.body());
		assertFalse(response.body().contains("temperature"),
				"expected no NUMBER rows behind /text: " + response.body());
	}

	@Test
	void servesEveryKindAtEntries() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL + "/entries", TEXT_CSV);

		long dataRows = response.body().lines().filter(line -> !line.isBlank()).count() - 1;
		// 02-data.sql seeds 6 NUMBER, 4 text-like and 2 GEOJSON rows
		assertTrue(dataRows >= 12, "expected every seeded row behind /entries, got " + dataRows);
	}

	/**
	 * The geo case: a location is a GeoJSON document in the JSONB column
	 * {@code value_json} (a bare geometry or a Feature), and the Data Atlas
	 * serves it as that JSON text — no PostGIS, no view, no type converter. Both
	 * seeded shapes must arrive: the bare Point of station-1 and the
	 * Feature-wrapped Point of station-2.
	 */
	@Test
	void servesLocationsAsGeoJsonText() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL + "/geo", TEXT_CSV);

		List<String> rows = response.body().lines().filter(line -> !line.isBlank()).toList();
		String header = rows.get(0);
		assertTrue(header.contains("valueJson"), "expected the JSON value column in the header: " + header);
		assertTrue(response.body().contains("11.582") && response.body().contains("50.927"),
				"expected the coordinates of the seeded point: " + response.body());
		assertTrue(response.body().contains("Point"), "expected the GeoJSON geometry type: " + response.body());
		assertTrue(response.body().contains("Feature") && response.body().contains("11.606"),
				"expected the Feature-wrapped point as it was recorded: " + response.body());
		assertFalse(response.body().contains("temperature"), "expected only GEOJSON rows behind /geo: " + response.body());
	}

	@Test
	void servesTheSameDataAsJson() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL + "/numeric", "application/json");

		assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("application/json"),
				"the example declares a JSON export next to the CSV one");
		assertTrue(response.body().contains("temperature"), "expected the recordings as JSON: " + response.body());
	}

	@Test
	void refusesAFormatTheConfigurationDoesNotDeclare() throws Exception {
		awaitOk(BASE_URL + "/numeric", TEXT_CSV);

		assertEquals(406, statusOf(BASE_URL + "/numeric", "application/xml"),
				"the example declares CSV and JSON only");
	}

	@Test
	void pagingBoundsWhatTheEndpointServes() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL + "/numeric", TEXT_CSV);

		// no view bounds the hypertable any more: the DataSet configuration's
		// batchSize/batchSizeLimit (1000) do, and offset/limit page through it
		List<String> rows = response.body().lines().filter(line -> !line.isBlank()).toList();
		long dataRows = rows.size() - 1;
		assertTrue(dataRows >= 6, "expected all six seeded numeric rows, got " + dataRows);
		assertTrue(dataRows <= 1000, "batchSizeLimit caps at 1000 rows, so a response can never be unbounded");
		// the query orders by time, newest first: the 25-hour-old recording comes last
		assertTrue(rows.get(rows.size() - 1).contains("18.2"), "expected the oldest row last: " + rows.get(rows.size() - 1));

		HttpResponse<String> page = awaitOk(BASE_URL + "/numeric?limit=2", TEXT_CSV);
		long pageRows = page.body().lines().filter(line -> !line.isBlank()).count() - 1;
		assertEquals(2, pageRows, "expected a page of two: " + page.body());
	}

	// --- helpers ---

	private static void awaitSeededDatabase() throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		while (System.currentTimeMillis() < deadline) {
			if (docker("exec", CONTAINER, "psql", "-U", DB, "-d", DB, "-tAc",
					"select count(*) from sensinact.history") == 0) {
				return;
			}
			Thread.sleep(2000);
		}
		fail("timed out waiting for the seeded TimescaleDB container");
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
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", accept)
				.timeout(Duration.ofSeconds(20))
				.GET()
				.build();
		return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
	}

	private static int statusOf(String url, String accept) {
		try {
			return get(url, accept).statusCode();
		} catch (Exception e) {
			return -1;
		}
	}

	private static HttpResponse<String> awaitOk(String url, String accept) throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
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

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-history-test");
		for (String root : new String[] { "data", "history" }) {
			Enumeration<URL> entries = bundle.findEntries(root, "*", true);
			while (entries != null && entries.hasMoreElements()) {
				URL url = entries.nextElement();
				String path = url.getPath();
				if (path.endsWith("/")) {
					continue;
				}
				Path file = "data".equals(root)
						? target.resolve(path.substring("/data/".length()))
						: target.resolve(path.substring(1));
				Files.createDirectories(file.getParent());
				try (InputStream in = url.openStream()) {
					Files.copy(in, file);
				}
			}
		}
		return target;
	}
}
