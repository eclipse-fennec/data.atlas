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
import java.util.function.Predicate;

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
 * Export formats are configuration, not code: this boots the vertical slice
 * with a fixture whose {@code exports} registry holds a
 * {@code CSVDistributionExport} (semicolon, no SQL-type row) and a plain
 * {@code DistributionExport} carrying {@code mediaType=application/json}, and
 * asserts what the three DataSets of that fixture serve —
 *
 * <ul>
 * <li>{@code both} references both exports: CSV and JSON, nothing else;</li>
 * <li>{@code csv-only} references only CSV: JSON is a 406;</li>
 * <li>{@code default} references none: the runtime defaults still apply, which
 * is what keeps every pre-export configuration working.</li>
 * </ul>
 *
 * It also covers who wins over the CSV settings: by default the configuration,
 * because the codec's client-option filter is not targeted at our applications —
 * and the client, once a deployment points that filter at them through Config
 * Admin. Both are asserted, so the default and the documented knob stay honest.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class DataAtlasExportFormatIntegrationTest {

	private static final int HTTP_PORT = 18089;
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest/export";
	private static final String TEXT_CSV = "text/csv";
	private static final long DEADLINE_MS = 60_000;

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration bootstrapConfig;
	private static Configuration clientOptionsFilterConfig;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path dataDir = extractTestData(bundleContext);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "export", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "export");
		httpProps.put("org.apache.felix.http.runtime.init.id", "exportHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "exportRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "exportRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=exportHttp)");
		whiteboardConfig.update(wbProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", dataDir.resolve("dataatlas-csv.xmi").toUri().toString());
		bootstrapConfig.update(bootProps);
	}

	@AfterAll
	static void tearDown() throws Exception {
		for (Configuration configuration : new Configuration[] { clientOptionsFilterConfig, bootstrapConfig,
				whiteboardConfig, httpConfig }) {
			if (configuration != null) {
				configuration.delete();
			}
		}
	}

	@Test
	void csvExportIsServedWithTheConfiguredSeparatorAndNoTypeRow() throws Exception {
		HttpResponse<String> response = getUntil(BASE_URL + "/both", TEXT_CSV, body -> body.contains("Lovelace"));

		assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith(TEXT_CSV),
				"expected a text/csv response, got " + response.headers().firstValue("Content-Type"));

		List<String> rows = response.body().lines().filter(line -> !line.isBlank()).toList();
		String header = rows.get(0);
		assertTrue(header.contains(";"), "expected the configured ';' separator in the header row: " + header);
		assertFalse(header.contains(","), "the configured separator replaces the codec default: " + header);
		assertTrue(header.contains("firstName") && header.contains("lastName"),
				"expected the attribute names as column headers: " + header);

		// includeTypeHeader=false: the row after the header is already data.
		// The codec's own default for that row is true, so this also proves the
		// configured value is what reaches the codec.
		assertEquals(4, rows.size(), "expected a header row plus the three example persons, got: " + rows);
		assertTrue(rows.stream().skip(1).anyMatch(row -> row.contains("Ada") && row.contains(";")),
				"expected the person rows semicolon separated: " + rows);
	}

	@Test
	void jsonIsStillServedWhenItIsDeclaredAsAnExport() throws Exception {
		HttpResponse<String> response = getUntil(BASE_URL + "/both", "application/json",
				body -> body.contains("Lovelace"));

		assertTrue(response.headers().firstValue("Content-Type").orElse("").startsWith("application/json"),
				"a plain DistributionExport with mediaType=application/json must be served as JSON");
	}

	@Test
	void aFormatThatIsNotConfiguredIsNotAcceptable() throws Exception {
		// bring the endpoint up first, so a 406 cannot be confused with a 404
		getUntil(BASE_URL + "/csv-only", TEXT_CSV, body -> body.contains("Lovelace"));

		assertEquals(406, status(BASE_URL + "/csv-only", "application/json"),
				"a DataSet that declares CSV only must refuse JSON");
		assertEquals(406, status(BASE_URL + "/csv-only", "application/xml"),
				"a DataSet that declares CSV only must refuse XMI");
		assertEquals(406, status(BASE_URL + "/both", "application/x-csv-zip"),
				"the compressed CSV variant is a different export and was not declared");
	}

	@Test
	void byIdHonoursTheConfiguredFormatsToo() throws Exception {
		getUntil(BASE_URL + "/csv-only", TEXT_CSV, body -> body.contains("Lovelace"));

		HttpResponse<String> csv = get(BASE_URL + "/csv-only/p1", TEXT_CSV);
		assertEquals(200, csv.statusCode(), "expected the single person as CSV");
		assertTrue(csv.body().contains("Ada"), "expected the requested person in the CSV body: " + csv.body());
		assertEquals(406, status(BASE_URL + "/csv-only/p1", "application/json"),
				"by-id negotiates against the same configured formats");
		assertEquals(404, status(BASE_URL + "/csv-only/nope", TEXT_CSV),
				"an unknown id is still a 404, not a 406");
	}

	@Test
	void aDataSetWithoutExportsKeepsTheRuntimeDefaults() throws Exception {
		HttpResponse<String> json = getUntil(BASE_URL + "/default", "application/json",
				body -> body.contains("Lovelace"));
		assertTrue(json.headers().firstValue("Content-Type").orElse("").startsWith("application/json"),
				"a DataSet without exports must still serve JSON");

		assertEquals(200, status(BASE_URL + "/default", "application/xml"),
				"a DataSet without exports must still serve XMI");
		assertEquals(406, status(BASE_URL + "/default", TEXT_CSV),
				"the defaults do not silently include every format the runtime can write");
	}

	/**
	 * The default: the codec's client-side override (the {@code Codec-Options}
	 * header of
	 * <a href="https://github.com/eclipse-fennec/emf.codec/issues/33">emf.codec#33</a>)
	 * does not reach a Data Atlas endpoint, so the configured settings are
	 * authoritative out of the box.
	 *
	 * <p>
	 * The reason is a defaulting asymmetry upstream: the codec's message body
	 * handlers select {@code (|(emf=true)(osgi.jakartars.name=.default))}, its
	 * {@code ClientCodecOptionsFilter} selects nothing at all, so the whiteboard
	 * attaches it to the {@code .default} application only — while every Data
	 * Atlas service is its own application. That default is a deployment
	 * decision, not a dead end: see
	 * {@link #aClientCodecOptionWinsOnceTheFilterIsTargetedAtOurApplications()}.
	 * </p>
	 */
	@Test
	@Order(1)
	void aClientCodecOptionIsIgnoredByDefault() throws Exception {
		getUntil(BASE_URL + "/both", TEXT_CSV, body -> body.contains("Lovelace"));

		String header = firstRowWithClientDelimiter();
		assertTrue(header.contains(";"),
				"by default the configured separator stands: " + header);
		assertFalse(header.contains("|"), "the client header is not honoured by default: " + header);
	}

	/**
	 * The knob: {@code ClientCodecOptionsFilter} is a DS component, so its
	 * whiteboard target is a component property and can be set through Config
	 * Admin like any other — no upstream change and no downstream reimplementation
	 * needed. Pointing it at the same applications the codec's message body
	 * handlers already select makes the client override work, and our own option
	 * publishing puts the configured values *underneath* whatever the filter
	 * deposited, so the client legitimately wins.
	 *
	 * <p>
	 * A deployment that wants the configuration to stay authoritative simply does
	 * not set this; one that wants clients to tune the whitelisted options sets it
	 * broadly ({@code (osgi.jakartars.name=*)}) or per application.
	 * </p>
	 */
	@Test
	@Order(2)
	void aClientCodecOptionWinsOnceTheFilterIsTargetedAtOurApplications(
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		getUntil(BASE_URL + "/both", TEXT_CSV, body -> body.contains("Lovelace"));

		clientOptionsFilterConfig = configAdmin
				.getConfiguration("org.eclipse.fennec.codec.rest.jakartas.filter.ClientCodecOptionsFilter", "?");
		Dictionary<String, Object> props = new Hashtable<>();
		props.put("osgi.jakartars.application.select", "(|(emf=true)(osgi.jakartars.name=.default))");
		clientOptionsFilterConfig.update(props);

		// the filter is re-registered and the applications rebuilt asynchronously
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		String header = "";
		while (System.currentTimeMillis() < deadline) {
			header = firstRowWithClientDelimiter();
			if (header.contains("|")) {
				break;
			}
			Thread.sleep(500);
		}
		assertTrue(header.contains("|"),
				"with the filter targeted at our applications the whitelisted client option must win: " + header);
		assertFalse(header.contains(";"), "the configured separator must be replaced, not combined: " + header);
	}

	/** First CSV row of {@code /both} requested with a client delimiter override. */
	private static String firstRowWithClientDelimiter() throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(BASE_URL + "/both"))
				.header("Accept", TEXT_CSV)
				.header("Codec-Options", "codec.csv.delimiter=|")
				.timeout(Duration.ofSeconds(10))
				.GET()
				.build();
		HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
		assertEquals(200, response.statusCode());
		return response.body().lines().filter(line -> !line.isBlank()).findFirst().orElse("");
	}

	// --- helpers ---

	private static HttpResponse<String> get(String url, String accept) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", accept)
				.timeout(Duration.ofSeconds(10))
				.GET()
				.build();
		return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
	}

	private static int status(String url, String accept) {
		try {
			return get(url, accept).statusCode();
		} catch (Exception e) {
			return -1;
		}
	}

	/** The whiteboard and the input come up asynchronously; polls for a match. */
	private static HttpResponse<String> getUntil(String url, String accept, Predicate<String> body) throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		HttpResponse<String> response = null;
		Exception lastError = null;
		while (System.currentTimeMillis() < deadline) {
			try {
				response = get(url, accept);
				lastError = null;
				if (response.statusCode() == 200 && body.test(response.body())) {
					return response;
				}
			} catch (Exception e) {
				lastError = e;
			}
			Thread.sleep(500);
		}
		if (lastError != null) {
			throw lastError;
		}
		fail("timed out waiting for the expected response from " + url + "; last: "
				+ (response == null ? "none" : response.statusCode() + " " + response.body()));
		return null;
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-export-test");
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
}
