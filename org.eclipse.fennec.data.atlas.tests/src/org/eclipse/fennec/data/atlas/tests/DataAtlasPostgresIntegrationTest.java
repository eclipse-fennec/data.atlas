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
 * Docker-gated end-to-end test of the Postgres + CSV example: starts a real
 * PostgreSQL, initialised with the very SQL the compose setup mounts, lets the
 * daanse Postgres DataSource component publish the {@code javax.sql.DataSource}
 * the configuration's {@code JdbcDataSource} filter selects, and asserts the
 * {@code JPADataInput} is served as configured — CSV with the configured
 * separator, JSON alongside it, and 406 for anything else.
 *
 * <p>
 * The point beyond "it works": nothing generates the schema here. The Data
 * Atlas persistence unit stays at the upstream default
 * {@code eclipselink.ddl-generation=none}, so this run only succeeds if the
 * committed {@code 01-schema.sql} matches what the <em>derived</em> eorm mapping
 * expects — uppercased table name, verbatim column names, both unquoted and
 * hence folded to lower case by PostgreSQL. That asymmetry is undocumented
 * upstream (emf.persistence-jpa#252), so this test is what keeps the SQL and
 * the mapping from drifting apart.
 * </p>
 *
 * <p>
 * Skipped where docker or the postgres image is unavailable.
 * </p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class DataAtlasPostgresIntegrationTest {

	private static final String IMAGE = "postgres:16-alpine";
	private static final String CONTAINER = "dataatlas-it-postgres";
	private static final int POSTGRES_PORT = 18096;
	private static final int HTTP_PORT = 18090;
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest/pg/persons";
	private static final String DB = "dataatlas";
	private static final String TEXT_CSV = "text/csv";
	private static final long DEADLINE_MS = 120_000;

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
				"-p", POSTGRES_PORT + ":5432",
				"-e", "POSTGRES_DB=" + DB,
				"-e", "POSTGRES_USER=" + DB,
				"-e", "POSTGRES_PASSWORD=" + DB,
				"-v", dir.resolve("postgres/init") + ":/docker-entrypoint-initdb.d:ro",
				IMAGE);
		assumeTrue(started == 0, "could not start " + IMAGE);
		containerStarted = true;
		// polls a query against the seeded table: it only succeeds once the
		// init scripts have run and the server accepts connections
		awaitSeededDatabase();

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "pg", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "pg");
		httpProps.put("org.apache.felix.http.runtime.init.id", "pgHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "pgRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "pgRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=pgHttp)");
		whiteboardConfig.update(wbProps);

		// the DataSource service the configuration's JdbcDataSource filter
		// selects; dataSourceName is not a key of the daanse component - it is
		// an extra config key, and every non-dot key also becomes a service
		// property, which is exactly what makes the filter work
		dataSourceConfig = configAdmin
				.getFactoryConfiguration("daanse.jdbc.datasource.postgresql.DataSource", "persons", "?");
		Dictionary<String, Object> dsProps = new Hashtable<>();
		dsProps.put("host", "localhost");
		dsProps.put("port", Integer.valueOf(POSTGRES_PORT));
		dsProps.put("dbname", DB);
		dsProps.put("user", DB);
		dsProps.put(".password", DB);
		dsProps.put("dataSourceName", "personsDs");
		dataSourceConfig.update(dsProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", dir.resolve("dataatlas-postgres.xmi").toUri().toString());
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
	void servesThePostgresTableAsConfiguredCsv() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL, TEXT_CSV);

		assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith(TEXT_CSV),
				"expected a text/csv response, got " + response.headers().firstValue("Content-Type"));

		List<String> rows = response.body().lines().filter(line -> !line.isBlank()).toList();
		assertTrue(rows.get(0).contains(";"), "expected the configured ';' separator: " + rows.get(0));
		assertEquals(4, rows.size(), "expected a header row plus the three seeded persons, got: " + rows);
		assertTrue(response.body().contains("Lovelace") && response.body().contains("Hamilton"),
				"expected the rows seeded by 02-data.sql: " + response.body());
	}

	@Test
	void servesTheSameDataSetAsJson() throws Exception {
		HttpResponse<String> response = awaitOk(BASE_URL, "application/json");

		assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("application/json"),
				"the example declares a JSON export next to the CSV one");
		assertTrue(response.body().contains("Lovelace"), "expected the seeded persons as JSON: " + response.body());
	}

	@Test
	void refusesAFormatTheConfigurationDoesNotDeclare() throws Exception {
		awaitOk(BASE_URL, TEXT_CSV);

		assertEquals(406, statusOf(BASE_URL, "application/xml"),
				"the example declares CSV and JSON only");
	}

	@Test
	void byIdReadsThroughToTheDatabase() throws Exception {
		awaitOk(BASE_URL, TEXT_CSV);

		HttpResponse<String> response = get(BASE_URL + "/p2", TEXT_CSV);
		assertEquals(200, response.statusCode(), "expected the single person as CSV");
		assertTrue(response.body().contains("Hopper"), "expected the requested row: " + response.body());
		assertFalse(response.body().contains("Lovelace"), "by-id must not return the whole table");
		assertEquals(404, statusOf(BASE_URL + "/nope", TEXT_CSV), "an unknown id is a 404");
	}

	// --- helpers ---

	/**
	 * Waits until the container answers a query against the seeded table — the
	 * init scripts run inside the entrypoint before the server accepts TCP
	 * connections, so a successful select is the honest readiness signal.
	 */
	private static void awaitSeededDatabase() throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		while (System.currentTimeMillis() < deadline) {
			if (docker("exec", CONTAINER, "psql", "-U", DB, "-d", DB, "-tAc",
					"select count(*) from person") == 0) {
				return;
			}
			Thread.sleep(1000);
		}
		fail("timed out waiting for the seeded PostgreSQL container");
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
				.timeout(Duration.ofSeconds(15))
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

	/** The whiteboard and the whole persistence stack come up asynchronously. */
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
		Path target = Files.createTempDirectory("dataatlas-pg-test");
		for (String root : new String[] { "data", "postgres" }) {
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
