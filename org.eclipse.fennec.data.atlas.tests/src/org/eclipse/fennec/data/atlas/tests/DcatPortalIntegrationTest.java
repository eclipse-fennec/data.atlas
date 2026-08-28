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
import java.util.function.Supplier;

import org.eclipse.fennec.dcat.atlas.client.api.DcatAtlasClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

import dcat.Catalog;
import dcat.DcatFactory;
import foaf.Agent;
import foaf.FoafFactory;
import rdf.PlainLiteral;
import rdf.RdfFactory;

/**
 * Milestone 8 against a real DCAT.Atlas (the published container image): the
 * acceptance walk of data.atlas#4 — a declared DataSet reaches the portal with
 * a distribution URL pointing at the serving endpoint, removing the
 * declaration withdraws it, re-adding it re-publishes it, and taking the
 * portal down does not disturb the Data Atlas.
 *
 * <p>
 * The portal runs without the AGPL SHACL shapes (deliberately not shipped nor
 * fetched here): SHACL enforcement is off, the model validation stays on — so
 * the portal still refuses entities missing title/description/publisher/
 * license, which is what this test needs a real portal for. Skipped where
 * docker or the image is unavailable, like the other container-backed tests.
 * </p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class DcatPortalIntegrationTest {

	private static final String IMAGE = "eclipsefennec/dcat.atlas:snapshot";
	private static final String CONTAINER = "dataatlas-dcat-portal-it";
	private static final int PORTAL_PORT = 18098;
	private static final int HTTP_PORT = 18099;
	private static final String PORTAL_BASE = "http://localhost:" + PORTAL_PORT;
	private static final String PUBLIC_BASE = "http://localhost:" + HTTP_PORT + "/rest";
	private static final String PORTAL_NAME = "it-portal";

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration clientConfig;
	private static Configuration publicationConfig;
	private static Configuration bootstrapConfig;
	private static Path configFile;
	private static String v1;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		assumeTrue(docker("version") == 0, "docker is not available");
		docker("rm", "-f", CONTAINER);
		int started = docker("run", "-d", "--name", CONTAINER,
				"-p", PORTAL_PORT + ":8080",
				"-e", "SHACL_SHAPES_DIR=",
				"-e", "SHACL_VOCAB_DIR=",
				"-e", "SHACL_ENFORCE=false",
				"-e", "PUBLIC_BASE_URL=" + PORTAL_BASE + "/rest/",
				IMAGE);
		assumeTrue(started == 0, "could not start " + IMAGE);
		assumeTrue(awaitStatus(PORTAL_BASE + "/health/ready", 200, 120_000) == 200,
				"the portal never became ready");

		Path dataDir = extractTestData(bundleContext);
		configFile = dataDir.resolve("dataatlas-dcat.xmi");
		v1 = Files.readString(configFile, StandardCharsets.UTF_8);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "dcatPortalIT", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "dcatPortalIT");
		httpProps.put("org.apache.felix.http.runtime.init.id", "dcatPortalITHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "dcatPortalITRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "dcatPortalITRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=dcatPortalITHttp)");
		whiteboardConfig.update(wbProps);

		clientConfig = configAdmin.getFactoryConfiguration("org.eclipse.fennec.dcat.atlas.client", "dcatPortalIT",
				"?");
		Dictionary<String, Object> clientProps = new Hashtable<>();
		clientProps.put("dcat.portal", PORTAL_NAME);
		clientProps.put("base.uri", PORTAL_BASE + "/rest/");
		clientConfig.update(clientProps);

		publicationConfig = configAdmin.getConfiguration("org.eclipse.fennec.data.atlas.publication.dcat", "?");
		Dictionary<String, Object> publicationProps = new Hashtable<>();
		publicationProps.put("public.base.url", PUBLIC_BASE);
		publicationProps.put("retry.interval.ms", 2_000L);
		publicationConfig.update(publicationProps);

		// the target catalog is expected to exist (catalog creation is out of
		// scope of the publication) - seed it through the same client stack
		DcatAtlasClient client = awaitClient(bundleContext, 30_000);
		client.registerCatalog("test-catalog", catalog());

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", configFile.toUri().toString());
		bootstrapConfig.update(bootProps);
	}

	@AfterAll
	static void tearDown() throws Exception {
		for (Configuration configuration : new Configuration[] { bootstrapConfig, publicationConfig, clientConfig,
				whiteboardConfig, httpConfig }) {
			if (configuration != null) {
				configuration.delete();
			}
		}
		docker("rm", "-f", CONTAINER);
	}

	@Test
	void acceptanceWalk(@InjectBundleContext BundleContext bundleContext) throws Exception {
		DcatAtlasClient client = awaitClient(bundleContext, 30_000);

		// declared -> the portal holds service, dataset and distributions, and
		// the distribution URL points at the endpoint the Data Atlas serves
		await(() -> client.dataService("dcat-persons-rest").isPresent(), 120_000,
				"the DataService never reached the portal");
		await(() -> client.dataset("dcat-persons").isPresent(), 30_000, "the Dataset never reached the portal");
		await(() -> client.distribution("dcat-persons", "json").isPresent(), 30_000,
				"the JSON distribution never reached the portal");
		dcat.DataService portalService = client.dataService("dcat-persons-rest").orElseThrow();
		assertEquals(PUBLIC_BASE + "/dcat-example", portalService.getEndpointURL().get(0));
		String accessUrl = client.distribution("dcat-persons", "json").orElseThrow().getAccessURL().get(0);
		assertEquals(PUBLIC_BASE + "/dcat-example/persons", accessUrl);

		// ... and that URL actually resolves to the exported data
		assertEquals(200, awaitStatus(accessUrl, 200, 60_000));
		assertTrue(get(accessUrl, "application/json").body().contains("Lovelace"));

		// removing the declaration withdraws service and dataset
		Files.writeString(configFile, v1.replace(" publication=\"dcat-open-data\"", ""), StandardCharsets.UTF_8);
		await(() -> client.dataset("dcat-persons").isEmpty(), 120_000, "the Dataset was never withdrawn");
		await(() -> client.dataService("dcat-persons-rest").isEmpty(), 30_000,
				"the DataService was never withdrawn");

		// re-adding it publishes again (the loop is idempotent and repeatable)
		Files.writeString(configFile, v1, StandardCharsets.UTF_8);
		await(() -> client.dataset("dcat-persons").isPresent(), 120_000, "the Dataset never came back");

		// the portal is not on the critical path: kill it, the data serves on
		docker("rm", "-f", CONTAINER);
		assertEquals(200, awaitStatus(PUBLIC_BASE + "/dcat-example/persons", 200, 30_000));
		assertTrue(get(PUBLIC_BASE + "/dcat-example/persons", "application/json").body().contains("Hopper"));
	}

	// --- helpers ------------------------------------------------------------

	private static Catalog catalog() {
		Catalog catalog = DcatFactory.eINSTANCE.createCatalog();
		catalog.getTitle().add(literal("Test catalog"));
		catalog.getDescription().add(literal("Catalog seeded by the Data Atlas integration suite"));
		Agent publisher = FoafFactory.eINSTANCE.createAgent();
		publisher.getName().add(literal("Data Atlas integration suite"));
		catalog.setPublisher(publisher);
		return catalog;
	}

	private static PlainLiteral literal(String value) {
		PlainLiteral literal = RdfFactory.eINSTANCE.createPlainLiteral();
		literal.setValue(value);
		literal.setLang("en");
		return literal;
	}

	private static DcatAtlasClient awaitClient(BundleContext bundleContext, long timeoutMs) throws Exception {
		long deadline = System.currentTimeMillis() + timeoutMs;
		while (System.currentTimeMillis() < deadline) {
			var references = bundleContext.getServiceReferences(DcatAtlasClient.class,
					"(dcat.portal=" + PORTAL_NAME + ")");
			if (!references.isEmpty()) {
				ServiceReference<DcatAtlasClient> reference = references.iterator().next();
				DcatAtlasClient client = bundleContext.getService(reference);
				if (client != null) {
					return client;
				}
			}
			Thread.sleep(500);
		}
		throw new AssertionError("no DcatAtlasClient for portal '" + PORTAL_NAME + "' appeared");
	}

	/** Polls a portal read until it holds (portal I/O may transiently fail). */
	private static void await(Supplier<Boolean> condition, long timeoutMs, String message) throws Exception {
		long deadline = System.currentTimeMillis() + timeoutMs;
		while (System.currentTimeMillis() < deadline) {
			try {
				if (Boolean.TRUE.equals(condition.get())) {
					return;
				}
			} catch (RuntimeException portalNotReachable) {
				// keep polling
			}
			Thread.sleep(500);
		}
		throw new AssertionError(message);
	}

	private static HttpResponse<String> get(String url, String accept) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", accept)
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
				last = get(url, "*/*").statusCode();
				if (last == wanted) {
					return last;
				}
			} catch (Exception e) {
				last = -1;
			}
			Thread.sleep(1000);
		}
		return last;
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-dcat-portal");
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

	private static int docker(String... args) throws Exception {
		String[] command = new String[args.length + 1];
		command[0] = "docker";
		System.arraycopy(args, 0, command, 1, args.length);
		Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
		process.getInputStream().readAllBytes();
		return process.waitFor();
	}
}
