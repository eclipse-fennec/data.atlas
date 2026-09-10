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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

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

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

/**
 * Milestone 10, OData end to end: three {@code ODataDataService}s become three
 * OData v4.01 service roots of the Fennec OData server, each publishing exactly
 * its configured entity set — the example persons from the file input, the
 * same persons from an H2 database (with a declared {@code $top} ceiling), and
 * the QVT-O-transformed {@code PublicPerson}s through the bridge — with
 * {@code $filter}/{@code $orderby}/{@code $top}/{@code $count}/{@code $select}/
 * {@code $apply} evaluated through the inputs' {@code ReadRepository}s (pushed
 * into the database for JPA, in memory for file and bridge). Writes are 405,
 * unpublished sets 404, and removing a service from the configuration takes
 * its root down (M4 lifecycle).
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class DataAtlasODataIntegrationTest {

	private static final int HTTP_PORT = 18093;
	private static final String HOST = "http://localhost:" + HTTP_PORT;
	private static final String FILE_ROOT = HOST + "/odata-file";
	private static final String JPA_ROOT = HOST + "/odata-jpa";
	private static final String PUBLIC_ROOT = HOST + "/odata-public";
	private static final String PERSON_NSURI = "https://eclipse.org/fennec/data/atlas/example/person/1.0.0";
	private static final String DATASOURCE_FILTER_PROPERTY = "dataatlas.test.ds";
	private static final long DEADLINE_MS = 90_000;

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration odataConfig;
	private static Configuration bootstrapConfig;
	private static Configuration seedMappingConfig;
	private static Configuration seedUnitConfig;
	private static Configuration seedRepositoryConfig;
	private static ServiceRegistration<javax.sql.DataSource> dataSourceRegistration;

	private static Path configFile;
	private static String v1;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path dataDir = extractTestData(bundleContext);
		configFile = dataDir.resolve("dataatlas-odata-roots.xmi");
		v1 = Files.readString(configFile, StandardCharsets.UTF_8);

		// the DataSource the fixture's JdbcDataSource filter selects: H2
		// in-memory, shared by name within this JVM, private to this test
		org.h2.jdbcx.JdbcDataSource h2 = new org.h2.jdbcx.JdbcDataSource();
		h2.setURL("jdbc:h2:mem:dataatlas-odata;DB_CLOSE_DELAY=-1");
		Dictionary<String, Object> dsProps = new Hashtable<>();
		dsProps.put(DATASOURCE_FILTER_PROPERTY, "odata-persons");
		dataSourceRegistration = bundleContext.registerService(javax.sql.DataSource.class, h2, dsProps);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "odataM10", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "odataM10");
		httpProps.put("org.apache.felix.http.runtime.init.id", "odataM10Http");
		httpConfig.update(httpProps);

		// the OData roots mount on this test's HTTP runtime only
		odataConfig = configAdmin.getConfiguration("org.eclipse.fennec.data.atlas.odata", "?");
		Dictionary<String, Object> odataProps = new Hashtable<>();
		odataProps.put("http.whiteboard.target", "(id=odataM10Http)");
		odataConfig.update(odataProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", configFile.toUri().toString());
		bootstrapConfig.update(bootProps);

		seed(bundleContext, configAdmin);
	}

	/**
	 * Seeds the database through a test-private writable repository over a
	 * second persistence unit with DDL generation (the same recipe as the JPA
	 * slice test); the Data Atlas unit stays read-only.
	 */
	private static void seed(BundleContext bundleContext, ConfigurationAdmin configAdmin) throws Exception {
		seedMappingConfig = configAdmin.getFactoryConfiguration("fennec.jpa.EORMMappingService", "odataSeed", "?");
		Dictionary<String, Object> mappingProps = new Hashtable<>();
		mappingProps.put("fennec.jpa.eorm.model.target", "(emf.nsURI=" + PERSON_NSURI + ")");
		mappingProps.put("fennec.jpa.eorm.eClasses", new String[] { "Person" });
		mappingProps.put("fennec.jpa.eorm.mappingName", "odataSeed");
		seedMappingConfig.update(mappingProps);

		seedUnitConfig = configAdmin.getFactoryConfiguration("fennec.jpa.EMPersistenceUnit", "odataSeed", "?");
		Dictionary<String, Object> unitProps = new Hashtable<>();
		unitProps.put("fennec.jpa.persistenceUnitName", "odataSeed");
		unitProps.put("fennec.jpa.dataSource.target", "(" + DATASOURCE_FILTER_PROPERTY + "=odata-persons)");
		unitProps.put("fennec.jpa.mapping.target", "(fennec.jpa.eorm.mapping=odataSeed)");
		unitProps.put("fennec.jpa.ext.eclipselink.ddl-generation", "create-or-extend-tables");
		seedUnitConfig.update(unitProps);

		seedRepositoryConfig = configAdmin.getFactoryConfiguration("fennec.repository.jpa", "odataSeed", "?");
		Dictionary<String, Object> repoProps = new Hashtable<>();
		repoProps.put("repositoryId", "dataatlas-odata-seed");
		repoProps.put("unit.target", "(osgi.unit.name=odataSeed)");
		seedRepositoryConfig.update(repoProps);

		ServiceReference<Repository> reference = waitForService(bundleContext, Repository.class,
				"(persistence.repository.id=dataatlas-odata-seed)");
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
				seedUnitConfig, seedMappingConfig, odataConfig, httpConfig }) {
			if (configuration != null) {
				try {
					configuration.delete();
				} catch (IllegalStateException e) {
					// already deleted
				}
			}
		}
		if (dataSourceRegistration != null) {
			dataSourceRegistration.unregister();
		}
	}

	@Test
	@Order(1)
	void serviceDocumentAndMetadataDescribeExactlyTheConfiguredModel() throws Exception {
		assertEquals(200, awaitStatus(FILE_ROOT + "/Persons", 200));

		JsonObject serviceDocument = json(get(FILE_ROOT + "/"));
		List<String> sets = serviceDocument.getJsonArray("value").stream().map(v -> (JsonObject) v)
				.map(v -> v.getString("name")).toList();
		assertEquals(List.of("Persons"), sets, serviceDocument.toString());

		// CSDL XML is the default representation; JSON only on request ($format=json / Accept)
		HttpResponse<String> metadata = get(FILE_ROOT + "/$metadata", "application/xml");
		assertEquals(200, metadata.statusCode());
		String csdl = metadata.body();
		assertTrue(csdl.contains("EntityType Name=\"Person\""), csdl);
		assertTrue(csdl.matches("(?s).*<edm:EntitySet[^>]*Name=\"Persons\".*"), csdl);
		// the root publishes ONE model: neither the Data Atlas configuration
		// package nor the other roots' types leak into it
		assertFalse(csdl.contains("DataAtlasConfiguration"), "the configuration model must not be published: " + csdl);
		assertFalse(csdl.contains("PublicPerson"), "another root's type must not be published: " + csdl);
	}

	@Test
	@Order(2)
	void filtersOrdersPagesAndCountsThroughTheFileInput() throws Exception {
		awaitStatus(FILE_ROOT + "/Persons", 200);

		JsonArray all = json(get(FILE_ROOT + "/Persons")).getJsonArray("value");
		assertEquals(3, all.size(), all.toString());

		JsonArray hopper = json(get(FILE_ROOT + "/Persons?$filter=lastName%20eq%20%27Hopper%27"))
				.getJsonArray("value");
		assertEquals(1, hopper.size(), hopper.toString());
		assertEquals("Grace", hopper.getJsonObject(0).getString("firstName"));

		JsonArray ordered = json(get(FILE_ROOT + "/Persons?$orderby=lastName%20desc&$top=1")).getJsonArray("value");
		assertEquals(1, ordered.size(), ordered.toString());
		assertEquals("Lovelace", ordered.getJsonObject(0).getString("lastName"));

		JsonObject counted = json(get(FILE_ROOT + "/Persons?$count=true&$top=1&$skip=1"));
		assertEquals(3, counted.getInt("@odata.count"), counted.toString());
		assertEquals(1, counted.getJsonArray("value").size(), counted.toString());

		JsonObject selected = json(get(FILE_ROOT + "/Persons?$select=lastName&$filter=id%20eq%20%27p3%27"));
		JsonObject hamilton = selected.getJsonArray("value").getJsonObject(0);
		assertEquals("Hamilton", hamilton.getString("lastName"));
		assertFalse(hamilton.containsKey("firstName"), "$select must project the entity: " + hamilton);

		JsonObject byKey = json(get(FILE_ROOT + "/Persons('p2')"));
		assertEquals("Grace", byKey.getString("firstName"), byKey.toString());
		assertEquals(404, get(FILE_ROOT + "/Persons('nope')").statusCode());

		JsonObject aggregated = json(get(FILE_ROOT + "/Persons?$apply=aggregate($count%20as%20Total)"));
		assertEquals(3, aggregated.getJsonArray("value").getJsonObject(0).getInt("Total"), aggregated.toString());
	}

	@Test
	@Order(3)
	void pushesDownIntoTheDatabaseWithTheDeclaredTopCeiling() throws Exception {
		assertEquals(200, awaitStatus(JPA_ROOT + "/Persons", 200));

		JsonArray hopper = json(get(JPA_ROOT + "/Persons?$filter=lastName%20eq%20%27Hopper%27")).getJsonArray("value");
		assertEquals(1, hopper.size(), hopper.toString());
		assertEquals("p2", hopper.getJsonObject(0).getString("id"));

		JsonArray ordered = json(get(JPA_ROOT + "/Persons?$orderby=lastName")).getJsonArray("value");
		assertEquals("Hamilton", ordered.getJsonObject(0).getString("lastName"), ordered.toString());

		// batchSizeLimit=2 on the configuration is the root's $top ceiling: a
		// plain collection GET answers a page of two plus a next link
		JsonObject page = json(get(JPA_ROOT + "/Persons"));
		assertEquals(2, page.getJsonArray("value").size(), page.toString());
		assertTrue(page.containsKey("@odata.nextLink"), "expected server-driven paging: " + page);

		JsonObject counted = json(get(JPA_ROOT + "/Persons?$count=true&$top=1"));
		assertEquals(3, counted.getInt("@odata.count"), counted.toString());
	}

	@Test
	@Order(4)
	void servesTransformedObjectsThroughTheBridge() throws Exception {
		assertEquals(200, awaitStatus(PUBLIC_ROOT + "/PublicPersons", 200));

		String csdl = get(PUBLIC_ROOT + "/$metadata", "application/xml").body();
		assertTrue(csdl.contains("EntityType Name=\"PublicPerson\""), csdl);
		assertTrue(csdl.matches("(?s).*<edm:EntitySet[^>]*Name=\"PublicPersons\".*"), csdl);

		JsonArray all = json(get(PUBLIC_ROOT + "/PublicPersons")).getJsonArray("value");
		assertEquals(3, all.size(), all.toString());

		// the predicate addresses the OUTPUT type: evaluated over the transformed objects
		JsonArray grace = json(get(PUBLIC_ROOT + "/PublicPersons?$filter=contains(displayName,%27Grace%27)"))
				.getJsonArray("value");
		assertEquals(1, grace.size(), grace.toString());
		assertEquals("p2", grace.getJsonObject(0).getString("id"));
		assertTrue(grace.getJsonObject(0).getString("displayName").contains("Hopper"), grace.toString());
	}

	@Test
	@Order(5)
	void isReadOnlyAndKnowsOnlyItsOwnSets() throws Exception {
		awaitStatus(FILE_ROOT + "/Persons", 200);

		HttpRequest post = HttpRequest.newBuilder(java.net.URI.create(FILE_ROOT + "/Persons"))
				.header("Content-Type", "application/json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"id\":\"p4\",\"firstName\":\"X\",\"lastName\":\"Y\"}"))
				.build();
		assertEquals(405, CLIENT.send(post, HttpResponse.BodyHandlers.ofString()).statusCode());

		// another root's set is not this root's set
		assertEquals(404, get(FILE_ROOT + "/PublicPersons").statusCode());
		assertEquals(404, get(FILE_ROOT + "/Nope").statusCode());
	}

	@Test
	@Order(6)
	void removingTheServiceTakesItsRootDown(@InjectBundleContext BundleContext bundleContext) throws Exception {
		awaitStatus(FILE_ROOT + "/Persons", 200);

		int start = v1.indexOf("  <services xsi:type=\"configuration:ODataDataService\" id=\"persons-odata\"");
		int end = v1.indexOf("</services>", start) + "</services>\n".length();
		assertTrue(start > 0 && end > start, "fixture layout changed");
		Files.writeString(configFile, v1.substring(0, start) + v1.substring(end), StandardCharsets.UTF_8);
		// re-applying a configuration re-registers the EPackages, which takes every
		// root's entity sets away for a moment - wait for the registrar to have
		// applied the diff before judging what is up and what is down
		awaitServiceGone(bundleContext, "(&(objectClass=org.eclipse.fennec.data.atlas.configuration.ODataDataService)"
				+ "(data.atlas.config.id=persons-odata))");
		assertEquals(404, awaitStatus(FILE_ROOT + "/Persons", 404));
		// the other roots survive the change
		assertEquals(200, awaitStatus(JPA_ROOT + "/Persons", 200));
		assertEquals(404, get(FILE_ROOT + "/Persons").statusCode());

		Files.writeString(configFile, v1, StandardCharsets.UTF_8);
		assertEquals(200, awaitStatus(FILE_ROOT + "/Persons", 200));
	}

	private static void awaitServiceGone(BundleContext bundleContext, String filter) throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		while (System.currentTimeMillis() < deadline) {
			ServiceReference<?>[] references = bundleContext.getAllServiceReferences(null, filter);
			if (references == null || references.length == 0) {
				return;
			}
			Thread.sleep(250);
		}
		throw new AssertionError("service matching " + filter + " still registered after " + DEADLINE_MS + " ms");
	}

	// --- helpers ---

	private static JsonObject json(HttpResponse<String> response) {
		assertEquals(200, response.statusCode(), response.body());
		return Json.createReader(new StringReader(response.body())).readObject();
	}

	private static HttpResponse<String> get(String url) throws Exception {
		return get(url, "application/json");
	}

	private static HttpResponse<String> get(String url, String accept) throws Exception {
		HttpRequest request = HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", accept)
				.timeout(Duration.ofSeconds(15))
				.GET()
				.build();
		return CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
	}

	private static int awaitStatus(String url, int wanted) throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		int last = -1;
		while (System.currentTimeMillis() < deadline) {
			try {
				last = get(url).statusCode();
				if (last == wanted) {
					return last;
				}
			} catch (Exception e) {
				// keep polling
			}
			Thread.sleep(500);
		}
		return last;
	}

	private static <T> ServiceReference<T> waitForService(BundleContext bundleContext, Class<T> type,
			String filter) throws Exception {
		long deadline = System.currentTimeMillis() + DEADLINE_MS;
		while (System.currentTimeMillis() < deadline) {
			Collection<ServiceReference<T>> references = bundleContext.getServiceReferences(type, filter);
			if (!references.isEmpty()) {
				return references.iterator().next();
			}
			Thread.sleep(250);
		}
		throw new AssertionError("no " + type.getSimpleName() + " matching " + filter + " within " + DEADLINE_MS + " ms");
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-odata");
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
		assertNotNull(target);
		return target;
	}
}
