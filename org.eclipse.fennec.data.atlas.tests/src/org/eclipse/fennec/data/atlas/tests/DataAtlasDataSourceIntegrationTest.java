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
 * Modeled data sources: a {@code JdbcDataSource} with connection coordinates
 * is MATERIALIZED by the datasource configurator into a daanse H2
 * {@code DataSource} factory configuration, its {@code $[secret:NAME]}
 * credential placeholders are resolved by the interpolation plugin against the
 * test secrets directory, and the {@code JPADataInput} over it serves through
 * the service the configurator created (targeted by
 * {@code data.atlas.datasource.id}). Definitions that violate the rules - a
 * literal credential, a filter combined with coordinates - are refused, and
 * their inputs stay down. The bound (filter) path stays covered by
 * {@link DataAtlasJpaIntegrationTest}.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class DataAtlasDataSourceIntegrationTest {

	private static final int HTTP_PORT = 18098;
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest/ds";
	private static final String REFUSED_URL = "http://localhost:" + HTTP_PORT + "/rest/ds-refused";
	private static final String PERSON_NSURI = "https://eclipse.org/fennec/data/atlas/example/person/1.0.0";
	private static final String MATERIALIZED_FILTER = "(data.atlas.datasource.id=h2-materialized)";
	private static final long DEADLINE_MS = 60_000;

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration bootstrapConfig;
	private static Configuration seedMappingConfig;
	private static Configuration seedUnitConfig;
	private static Configuration seedRepositoryConfig;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path dataDir = extractTestData(bundleContext);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "ds", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "ds");
		httpProps.put("org.apache.felix.http.runtime.init.id", "dsHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "dsRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "dsRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=dsHttp)");
		whiteboardConfig.update(wbProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", dataDir.resolve("dataatlas-datasources.xmi").toUri().toString());
		bootstrapConfig.update(bootProps);

		seed(bundleContext, configAdmin);
	}

	/**
	 * Seeds the materialized database through a test-private writable
	 * repository over the SAME DataSource service the configurator created -
	 * which is the first proof that it exists and connects.
	 */
	private static void seed(BundleContext bundleContext, ConfigurationAdmin configAdmin) throws Exception {
		waitForService(bundleContext, javax.sql.DataSource.class, MATERIALIZED_FILTER);

		seedMappingConfig = configAdmin.getFactoryConfiguration("fennec.jpa.EORMMappingService", "dsSeed", "?");
		Dictionary<String, Object> mappingProps = new Hashtable<>();
		mappingProps.put("fennec.jpa.eorm.model.target", "(emf.nsURI=" + PERSON_NSURI + ")");
		mappingProps.put("fennec.jpa.eorm.eClasses", new String[] { "Person" });
		mappingProps.put("fennec.jpa.eorm.mappingName", "dsSeed");
		seedMappingConfig.update(mappingProps);

		seedUnitConfig = configAdmin.getFactoryConfiguration("fennec.jpa.EMPersistenceUnit", "dsSeed", "?");
		Dictionary<String, Object> unitProps = new Hashtable<>();
		unitProps.put("fennec.jpa.persistenceUnitName", "dsSeed");
		unitProps.put("fennec.jpa.dataSource.target", MATERIALIZED_FILTER);
		unitProps.put("fennec.jpa.mapping.target", "(fennec.jpa.eorm.mapping=dsSeed)");
		unitProps.put("fennec.jpa.ext.eclipselink.ddl-generation", "create-or-extend-tables");
		seedUnitConfig.update(unitProps);

		seedRepositoryConfig = configAdmin.getFactoryConfiguration("fennec.repository.jpa", "dsSeed", "?");
		Dictionary<String, Object> repoProps = new Hashtable<>();
		repoProps.put("repositoryId", "dataatlas-ds-seed");
		repoProps.put("unit.target", "(osgi.unit.name=dsSeed)");
		seedRepositoryConfig.update(repoProps);

		ServiceReference<Repository> reference = waitForService(bundleContext, Repository.class,
				"(persistence.repository.id=dataatlas-ds-seed)");
		Repository repository = bundleContext.getServiceObjects(reference).getService();
		try {
			EPackage personPackage = bundleContext
					.getService(waitForService(bundleContext, EPackage.class, "(emf.nsURI=" + PERSON_NSURI + ")"));
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
				seedUnitConfig, seedMappingConfig, whiteboardConfig, httpConfig }) {
			if (configuration != null) {
				try {
					configuration.delete();
				} catch (IllegalStateException e) {
					// already deleted by a test
				}
			}
		}
	}

	@Test
	@Order(1)
	void materializesTheDefinitionWithResolvedPlaceholders(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		ServiceReference<javax.sql.DataSource> dataSource = waitForService(bundleContext,
				javax.sql.DataSource.class, MATERIALIZED_FILTER);
		// the daanse component publishes its non-dot configuration keys as
		// service properties, AFTER interpolation: the placeholder is gone
		assertEquals("dataatlas", dataSource.getProperty("username"),
				"the $[secret:...] user placeholder should have been resolved against the secrets directory");
		assertEquals("dataatlas-materialized", dataSource.getProperty("identifier"));
		assertNull(dataSource.getProperty(".password"), "a dot-key never becomes a service property");

		Configuration configuration = findConfiguration(configAdmin,
				"daanse.jdbc.datasource.h2.DataSource~dataAtlas.h2-materialized");
		assertNotNull(configuration, "the materialized factory configuration");
		// Config Admin hands the test the stored, uninterpolated value
		assertEquals("$[secret:dataatlas-db-password]", configuration.getProperties().get(".password"),
				"the model's placeholder is stored verbatim; only the consumer sees the secret");
		assertEquals("mem", configuration.getProperties().get("plugableFilesystem"),
				"connection properties pass through");
	}

	@Test
	@Order(2)
	void servesThroughTheMaterializedDataSource() throws Exception {
		HttpResponse<String> response = getUntil(BASE_URL + "/persons", body -> body.contains("Lovelace"));
		assertTrue(response.body().contains("Hopper"), () -> "missing Hopper in: " + response.body());
	}

	@Test
	@Order(3)
	void refusesDefinitionsViolatingTheRules(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		getUntil(BASE_URL + "/persons", body -> body.contains("Lovelace"));
		// the configurator handled all three definitions by now: the refused
		// ones have neither a factory configuration nor a service
		assertNull(findConfiguration(configAdmin, "daanse.jdbc.datasource.h2.DataSource~dataAtlas.bad-literal"),
				"a literal credential must not be materialized");
		assertNull(findConfiguration(configAdmin, "daanse.jdbc.datasource.postgresql.DataSource~dataAtlas.bad-both"),
				"filter plus coordinates must not be materialized");
		assertEquals(0, countServices(bundleContext, "(data.atlas.datasource.id=bad-literal)"));
		assertEquals(0, countServices(bundleContext, "(data.atlas.datasource.id=bad-both)"));
		// the inputs over them are configured (the input configurator does not
		// judge the definition) but never come up: no DataSource, no unit, no
		// repository, no endpoint
		assertEquals(0, countServices(bundleContext, "(persistence.repository.id=persons-bad-literal-jpa)"));
		assertEquals(0, countServices(bundleContext, "(persistence.repository.id=persons-bad-both-jpa)"));
		assertEquals(404, statusOf(REFUSED_URL + "/bad-literal"));
		assertEquals(404, statusOf(REFUSED_URL + "/bad-both"));
	}

	@Test
	@Order(4)
	void deleteTearsTheMaterializationDown(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		getUntil(BASE_URL + "/persons", body -> body.contains("Lovelace"));

		bootstrapConfig.delete();
		bootstrapConfig = null;

		waitUntil(() -> statusOf(BASE_URL + "/persons") == 404, "the endpoint survived the configuration delete");
		waitUntil(() -> countServices(bundleContext, MATERIALIZED_FILTER) == 0,
				"the materialized DataSource service survived the configuration delete");
		assertNull(findConfiguration(configAdmin, "daanse.jdbc.datasource.h2.DataSource~dataAtlas.h2-materialized"));
		assertNull(findConfiguration(configAdmin, "fennec.jpa.EMPersistenceUnit~persons-materialized-jpa"));
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

	private static <T> ServiceReference<T> waitForService(BundleContext bundleContext, Class<T> type, String filter)
			throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		while (System.currentTimeMillis() < deadline) {
			var references = bundleContext.getServiceReferences(type, filter);
			if (!references.isEmpty()) {
				return references.iterator().next();
			}
			Thread.sleep(200);
		}
		throw new AssertionError("timed out waiting for " + type.getSimpleName() + " " + filter);
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
		Path target = Files.createTempDirectory("dataatlas-ds-test");
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
