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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.persistence.repository.api.Repository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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

/**
 * Docker-gated end-to-end test of the MongoDB slice: a MATERIALIZED
 * {@code MongoDataSource} (credentials as {@code $[secret:NAME]} placeholders
 * inside the connection string) becomes a fennec Mongo client + database, the
 * {@code MongoDataInput} over it becomes a read-only
 * {@code fennec.repository.mongo}, and the DataSet is served over REST. The
 * test seeds the collection through its own writable repository over the same
 * materialized database (the only writer - the Data Atlas stays read-only).
 *
 * <p>
 * Skipped where docker or the mongo image is unavailable.
 * </p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class DataAtlasMongoIntegrationTest {

	private static final String IMAGE = "mongo:7";
	private static final String CONTAINER = "dataatlas-it-mongo";
	private static final int MONGO_PORT = 18099;
	private static final int HTTP_PORT = 18097;
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest/mongo";
	private static final String PERSON_NSURI = "https://eclipse.org/fennec/data/atlas/example/person/1.0.0";
	/** The alias the datasource configurator gives the materialized database. */
	private static final String DATABASE_FILTER = "(mongo.database.alias=dataAtlas.mongo-db)";
	private static final long DEADLINE_MS = 120_000;

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration bootstrapConfig;
	private static Configuration seedRepositoryConfig;
	private static boolean containerStarted;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		assumeTrue(docker("version") == 0, "docker is not available");

		Path dataDir = extractTestData(bundleContext);

		// credentials match the checked-in test secrets the fixture references
		docker("rm", "-f", CONTAINER);
		int started = docker("run", "-d", "--name", CONTAINER,
				"-p", MONGO_PORT + ":27017",
				"-e", "MONGO_INITDB_ROOT_USERNAME=dataatlas",
				"-e", "MONGO_INITDB_ROOT_PASSWORD=dataatlas-secret",
				IMAGE);
		assumeTrue(started == 0, "could not start " + IMAGE);
		containerStarted = true;

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "mongo", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "mongo");
		httpProps.put("org.apache.felix.http.runtime.init.id", "mongoHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "mongoRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "mongoRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=mongoHttp)");
		whiteboardConfig.update(wbProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", dataDir.resolve("dataatlas-mongo.xmi").toUri().toString());
		bootstrapConfig.update(bootProps);

		seed(bundleContext, configAdmin);
	}

	/**
	 * Seeds the collection through a test-private writable repository over the
	 * materialized database: it appears only once the liveness-gated client
	 * has verified the connection - with the interpolated credentials.
	 */
	private static void seed(BundleContext bundleContext, ConfigurationAdmin configAdmin) throws Exception {
		waitForService(bundleContext, "com.mongodb.client.MongoDatabase", DATABASE_FILTER);

		seedRepositoryConfig = configAdmin.getFactoryConfiguration("fennec.repository.mongo", "mongoSeed", "?");
		Dictionary<String, Object> repoProps = new Hashtable<>();
		repoProps.put("repositoryId", "dataatlas-mongo-seed");
		repoProps.put("database.target", DATABASE_FILTER);
		seedRepositoryConfig.update(repoProps);

		ServiceReference<?> reference = waitForService(bundleContext, Repository.class.getName(),
				"(persistence.repository.id=dataatlas-mongo-seed)");
		Repository repository = (Repository) bundleContext.getServiceObjects(reference).getService();
		try {
			EPackage personPackage = (EPackage) bundleContext.getService(
					waitForService(bundleContext, EPackage.class.getName(), "(emf.nsURI=" + PERSON_NSURI + ")"));
			EClass personClass = (EClass) personPackage.getEClassifier("Person");
			repository.save(createPerson(personClass, "p1", "Ada", "Lovelace"));
			repository.save(createPerson(personClass, "p2", "Grace", "Hopper"));
		} finally {
			repository.dispose();
		}
	}

	private static EObject createPerson(EClass personClass, String id, String firstName, String lastName) {
		EObject person = EcoreUtil.create(personClass);
		person.eSet(personClass.getEStructuralFeature("id"), id);
		person.eSet(personClass.getEStructuralFeature("firstName"), firstName);
		person.eSet(personClass.getEStructuralFeature("lastName"), lastName);
		return person;
	}

	@AfterAll
	static void tearDown() throws Exception {
		for (Configuration configuration : new Configuration[] { bootstrapConfig, seedRepositoryConfig,
				whiteboardConfig, httpConfig }) {
			if (configuration != null) {
				try {
					configuration.delete();
				} catch (IllegalStateException e) {
					// already deleted by a test
				}
			}
		}
		if (containerStarted) {
			docker("rm", "-f", CONTAINER);
		}
	}

	@Test
	@Order(1)
	void materializesClientAndDatabase(@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Configuration client = findConfiguration(configAdmin, "persistence.mongo.client~dataAtlas.mongo-db");
		assertNotNull(client, "the materialized client configuration");
		assertEquals("mongodb://$[secret:dataatlas-db-user]:$[secret:dataatlas-db-password]@localhost:" + MONGO_PORT
				+ "/?authSource=admin", client.getProperties().get("connectionString"),
				"placeholders are stored verbatim inside the connection string");
		assertEquals("2", client.getProperties().get("liveness.checkInterval"), "connection properties pass through");
		Configuration database = findConfiguration(configAdmin, "persistence.mongo.database~dataAtlas.mongo-db");
		assertNotNull(database, "the materialized database configuration");
		assertEquals("dataatlas", database.getProperties().get("database"));
		assertEquals("(mongo.client.ident=dataAtlas.mongo-db)", database.getProperties().get("client.target"));
	}

	@Test
	@Order(2)
	void servesTheMongoBackedDataSet(@InjectBundleContext BundleContext bundleContext) throws Exception {
		HttpResponse<String> response = getUntil(BASE_URL + "/persons", body -> body.contains("Lovelace"));
		assertTrue(response.body().contains("Hopper"), () -> "missing Hopper in: " + response.body());

		ServiceReference<?> repository = waitForService(bundleContext, "org.eclipse.fennec.persistence.repository.api.ReadRepository",
				"(persistence.repository.id=persons-mongo-input)");
		assertEquals("mongodb", repository.getProperty("persistence.repository.backend"));
		assertEquals(Boolean.TRUE, repository.getProperty("persistence.repository.readOnly"),
				"the Data Atlas input is read-only");
	}

	@Test
	@Order(3)
	void deleteTearsEverythingDown(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		getUntil(BASE_URL + "/persons", body -> body.contains("Lovelace"));

		bootstrapConfig.delete();
		bootstrapConfig = null;

		waitUntil(() -> statusOf(BASE_URL + "/persons") == 404, "the endpoint survived the configuration delete");
		waitUntil(() -> countServices(bundleContext, DATABASE_FILTER) == 0,
				"the materialized MongoDatabase service survived the configuration delete");
		assertNull(findConfiguration(configAdmin, "persistence.mongo.client~dataAtlas.mongo-db"));
		assertNull(findConfiguration(configAdmin, "persistence.mongo.database~dataAtlas.mongo-db"));
		assertNull(findConfiguration(configAdmin, "fennec.repository.mongo~persons-mongo-input"));
	}

	// --- helpers ---

	private static Configuration findConfiguration(ConfigurationAdmin configAdmin, String pid) throws Exception {
		Configuration[] configurations = configAdmin.listConfigurations("(service.pid=" + pid + ")");
		return configurations == null ? null : configurations[0];
	}

	private static int countServices(BundleContext bundleContext, String filter) {
		try {
			ServiceReference<?>[] references = bundleContext.getAllServiceReferences(null, filter);
			return references == null ? 0 : references.length;
		} catch (Exception e) {
			throw new IllegalStateException("invalid service filter " + filter, e);
		}
	}

	/** By class name: the test bundle does not import the MongoDB driver packages. */
	private static ServiceReference<?> waitForService(BundleContext bundleContext, String type, String filter)
			throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		while (System.currentTimeMillis() < deadline) {
			ServiceReference<?>[] references = bundleContext.getAllServiceReferences(type, filter);
			if (references != null && references.length > 0) {
				return references[0];
			}
			Thread.sleep(200);
		}
		throw new AssertionError("timed out waiting for " + type + " " + filter);
	}

	private static void waitUntil(java.util.function.BooleanSupplier condition, String failureMessage)
			throws InterruptedException {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		while (System.currentTimeMillis() < deadline) {
			if (condition.getAsBoolean()) {
				return;
			}
			Thread.sleep(200);
		}
		assertTrue(condition.getAsBoolean(), failureMessage);
	}

	private static int docker(String... args) throws Exception {
		String[] command = new String[args.length + 1];
		command[0] = "docker";
		System.arraycopy(args, 0, command, 1, args.length);
		Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
		process.getInputStream().readAllBytes();
		return process.waitFor(300, TimeUnit.SECONDS) ? process.exitValue() : -1;
	}

	private static int statusOf(String url) {
		try {
			return get(url).statusCode();
		} catch (Exception e) {
			return -1;
		}
	}

	private static HttpResponse<String> get(String url) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", "application/json")
				.timeout(Duration.ofSeconds(10))
				.GET()
				.build();
		return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
	}

	private static HttpResponse<String> getUntil(String url, Predicate<String> body) throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		HttpResponse<String> response = null;
		Exception lastError = null;
		while (System.currentTimeMillis() < deadline) {
			try {
				response = get(url);
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
		Path target = Files.createTempDirectory("dataatlas-mongo-test");
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
