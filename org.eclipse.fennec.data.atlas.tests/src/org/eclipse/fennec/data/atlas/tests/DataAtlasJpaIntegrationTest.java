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
import java.util.List;
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
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * The JPA vertical slice: a {@code JPADataInput} + {@code JdbcDataSource} in
 * the configuration turn into the fennec persistence factory configurations
 * and a read-only repository, served over REST — including a query-defined
 * DataSet with a bound parameter. H2 (in-memory, shared by name within the
 * JVM) is the database; the test seeds it through its own writable repository
 * over a second persistence unit with DDL generation enabled.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class DataAtlasJpaIntegrationTest {

	private static final int HTTP_PORT = 18088;
	private static final String BASE_URL = "http://localhost:" + HTTP_PORT + "/rest/jpa";
	private static final String PERSON_NSURI = "https://eclipse.org/fennec/data/atlas/example/person/1.0.0";
	private static final String DATASOURCE_FILTER_PROPERTY = "dataatlas.test.ds";
	private static final long DEADLINE_MS = 60_000;

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration whiteboardConfig;
	private static Configuration bootstrapConfig;
	private static Configuration seedMappingConfig;
	private static Configuration seedUnitConfig;
	private static Configuration seedRepositoryConfig;
	private static ServiceRegistration<javax.sql.DataSource> dataSourceRegistration;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path dataDir = extractTestData(bundleContext);

		// the DataSource the fixture's JdbcDataSource filter selects: H2
		// in-memory, shared by name within this JVM
		org.h2.jdbcx.JdbcDataSource h2 = new org.h2.jdbcx.JdbcDataSource();
		h2.setURL("jdbc:h2:mem:dataatlas;DB_CLOSE_DELAY=-1");
		Dictionary<String, Object> dsProps = new Hashtable<>();
		dsProps.put(DATASOURCE_FILTER_PROPERTY, "persons");
		dataSourceRegistration = bundleContext.registerService(javax.sql.DataSource.class, h2, dsProps);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "jpa", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "jpa");
		httpProps.put("org.apache.felix.http.runtime.init.id", "jpaHttp");
		httpConfig.update(httpProps);

		whiteboardConfig = configAdmin
				.getFactoryConfiguration("JakartarsServletWhiteboardRuntimeComponent", "jpaRest", "?");
		Dictionary<String, Object> wbProps = new Hashtable<>();
		wbProps.put("jersey.jaxrs.whiteboard.name", "jpaRest");
		wbProps.put("jersey.context.path", "rest");
		wbProps.put("osgi.http.whiteboard.target", "(id=jpaHttp)");
		whiteboardConfig.update(wbProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", dataDir.resolve("dataatlas-jpa.xmi").toUri().toString());
		bootstrapConfig.update(bootProps);

		seed(bundleContext, configAdmin);
	}

	/**
	 * Seeds the database through a test-private writable repository: a second
	 * persistence unit over the same DataSource and EClasses, with DDL
	 * generation enabled — the Data Atlas unit itself stays read-only with the
	 * upstream default of no DDL.
	 */
	private static void seed(BundleContext bundleContext, ConfigurationAdmin configAdmin) throws Exception {
		seedMappingConfig = configAdmin.getFactoryConfiguration("fennec.jpa.EORMMappingService", "seed", "?");
		Dictionary<String, Object> mappingProps = new Hashtable<>();
		mappingProps.put("fennec.jpa.eorm.model.target", "(emf.nsURI=" + PERSON_NSURI + ")");
		mappingProps.put("fennec.jpa.eorm.eClasses", new String[] { "Person" });
		mappingProps.put("fennec.jpa.eorm.mappingName", "seed");
		seedMappingConfig.update(mappingProps);

		seedUnitConfig = configAdmin.getFactoryConfiguration("fennec.jpa.EMPersistenceUnit", "seed", "?");
		Dictionary<String, Object> unitProps = new Hashtable<>();
		unitProps.put("fennec.jpa.persistenceUnitName", "seed");
		unitProps.put("fennec.jpa.dataSource.target", "(" + DATASOURCE_FILTER_PROPERTY + "=persons)");
		unitProps.put("fennec.jpa.mapping.target", "(fennec.jpa.eorm.mapping=seed)");
		unitProps.put("fennec.jpa.ext.eclipselink.ddl-generation", "create-or-extend-tables");
		seedUnitConfig.update(unitProps);

		seedRepositoryConfig = configAdmin.getFactoryConfiguration("fennec.repository.jpa", "seed", "?");
		Dictionary<String, Object> repoProps = new Hashtable<>();
		repoProps.put("repositoryId", "dataatlas-seed");
		repoProps.put("unit.target", "(osgi.unit.name=seed)");
		seedRepositoryConfig.update(repoProps);

		ServiceReference<Repository> reference = waitForService(bundleContext, Repository.class,
				"(persistence.repository.id=dataatlas-seed)");
		Repository repository = bundleContext.getServiceObjects(reference).getService();
		try {
			EPackage personPackage = bundleContext
					.getService(waitForService(bundleContext, EPackage.class, "(emf.nsURI=" + PERSON_NSURI + ")"));
			EClass personClass = (EClass) personPackage.getEClassifier("Person");
			repository.save(createPerson(personClass, "p1", "Ada", "Lovelace"));
			repository.save(createPerson(personClass, "p2", "Grace", "Hopper"));
			repository.save(createPerson(personClass, "p3", "Margaret", "Hamilton"));
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
		if (dataSourceRegistration != null) {
			dataSourceRegistration.unregister();
		}
	}

	@Test
	@Order(1)
	void servesDatabaseBackedDataSet() throws Exception {
		HttpResponse<String> response = getUntil(BASE_URL + "/persons", body -> body.contains("Lovelace"));
		assertTrue(response.body().contains("Hopper"), () -> "missing Hopper in: " + response.body());
		assertTrue(response.body().contains("Hamilton"), () -> "missing Hamilton in: " + response.body());
	}

	@Test
	@Order(2)
	void servesSingleObjectById() throws Exception {
		getUntil(BASE_URL + "/persons", body -> body.contains("Lovelace"));
		HttpResponse<String> response = get(BASE_URL + "/persons/p2");
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("Hopper"), () -> "missing Hopper in: " + response.body());
		assertFalse(response.body().contains("Lovelace"), () -> "unexpected Lovelace in: " + response.body());
		assertEquals(404, get(BASE_URL + "/persons/does-not-exist").statusCode());
	}

	@Test
	@Order(3)
	void pushesPaginationDownToTheDatabase() throws Exception {
		getUntil(BASE_URL + "/persons", body -> body.contains("Lovelace"));
		// without an orderBy the row order is backend-defined, so assert page
		// sizes and that the pages cover the whole data set
		List<String> names = List.of("Lovelace", "Hopper", "Hamilton");
		java.util.Set<String> seen = new java.util.HashSet<>();
		for (int offset = 0; offset < 3; offset++) {
			HttpResponse<String> page = get(BASE_URL + "/persons?offset=" + offset + "&limit=1");
			assertEquals(200, page.statusCode());
			String body = page.body();
			List<String> onPage = names.stream().filter(body::contains).toList();
			assertEquals(1, onPage.size(), () -> "expected exactly one person on the page: " + body);
			seen.addAll(onPage);
		}
		assertEquals(3, seen.size(), () -> "the three one-person pages should cover all persons, got: " + seen);
	}

	@Test
	@Order(4)
	void queryDataSetBindsItsParameter() throws Exception {
		getUntil(BASE_URL + "/persons", body -> body.contains("Lovelace"));
		HttpResponse<String> response = get(BASE_URL + "/by-lastname?lastName=Hopper");
		assertEquals(200, response.statusCode());
		assertTrue(response.body().contains("Hopper"), () -> "missing Hopper in: " + response.body());
		assertFalse(response.body().contains("Lovelace"), () -> "unexpected Lovelace in: " + response.body());
		assertFalse(response.body().contains("Hamilton"), () -> "unexpected Hamilton in: " + response.body());

		assertEquals(400, get(BASE_URL + "/by-lastname").statusCode(),
				"a missing declared query parameter is a client error");
	}

	@Test
	@Order(5)
	void deleteRemovesThePersistenceConfigurations(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		getUntil(BASE_URL + "/persons", body -> body.contains("Lovelace"));
		assertNotNull(findConfiguration(configAdmin, "fennec.jpa.EMPersistenceUnit~persons-jpa"),
				"expected the input's persistence unit configuration while the configuration exists");

		bootstrapConfig.delete();
		bootstrapConfig = null;

		waitUntil(() -> statusOf(BASE_URL + "/persons") == 404, "the JPA endpoint survived the configuration delete");
		waitUntil(() -> countServices(bundleContext, "(persistence.repository.id=persons-jpa)") == 0,
				"the input's repository service survived the configuration delete");
		assertNull(findConfiguration(configAdmin, "fennec.jpa.EORMMappingService~persons-jpa"));
		assertNull(findConfiguration(configAdmin, "fennec.jpa.EMPersistenceUnit~persons-jpa"));
		assertNull(findConfiguration(configAdmin, "fennec.repository.jpa~persons-jpa"));
		// the seed configurations are the test's own and must still exist
		assertNotNull(findConfiguration(configAdmin, "fennec.jpa.EMPersistenceUnit~seed"));
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

	/**
	 * The whiteboard, the persistence stack and the seeded data come up
	 * asynchronously; polls until a 200 response satisfies the predicate.
	 */
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
		Path target = Files.createTempDirectory("dataatlas-jpa-test");
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
