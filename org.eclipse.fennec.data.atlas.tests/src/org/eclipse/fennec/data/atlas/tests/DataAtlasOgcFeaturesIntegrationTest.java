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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.persistence.repository.api.Repository;
import org.geojson.Coordinates;
import org.geojson.GeoJsonFactory;
import org.geojson.Point;
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
import jakarta.json.JsonValue;

/**
 * OGC API Features end to end: two {@code OgcFeaturesDataService}s become two
 * API roots of the Fennec OGC API Features server, each publishing exactly its
 * configured collections — the annotated pools and slides plus the
 * configuration-declared benches from the example file, and the pools (under
 * an overriding id) plus every asset from an H2 database (with a declared page
 * size and ceiling) — with {@code bbox}, {@code datetime} and CQL2 filters
 * evaluated through the inputs' {@code ReadRepository}s (pushed into the
 * database for JPA, in memory for the file). The geometry of an asset is a
 * containment reference to the GeoJSON model (emf.ogc.features#14), read from
 * the XMI file and from the database (one CLOB column of the asset,
 * emf.persistence-jpa#363) and served as the feature's GeoJSON geometry.
 * Collections outside a root's allowlist are 404, writes 405, the QGIS project and the viewer are reachable,
 * and removing a service from the configuration takes its root down (M4
 * lifecycle).
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class DataAtlasOgcFeaturesIntegrationTest {

	private static final int HTTP_PORT = 18094;
	private static final String HOST = "http://localhost:" + HTTP_PORT;
	private static final String FILE_ROOT = HOST + "/ogc/assets";
	private static final String JPA_ROOT = HOST + "/ogc/db";
	private static final String ASSET_NSURI = "https://eclipse.org/fennec/data/atlas/example/asset/1.0.0";
	private static final String DATASOURCE_FILTER_PROPERTY = "dataatlas.test.ds";
	private static final long DEADLINE_MS = 90_000;

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration ogcConfig;
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
		configFile = dataDir.resolve("dataatlas-ogc-roots.xmi");
		v1 = Files.readString(configFile, StandardCharsets.UTF_8);

		// the DataSource the fixture's JdbcDataSource filter selects: H2
		// in-memory, shared by name within this JVM, private to this test
		org.h2.jdbcx.JdbcDataSource h2 = new org.h2.jdbcx.JdbcDataSource();
		h2.setURL("jdbc:h2:mem:dataatlas-ogc;DB_CLOSE_DELAY=-1");
		Dictionary<String, Object> dsProps = new Hashtable<>();
		dsProps.put(DATASOURCE_FILTER_PROPERTY, "ogc-assets");
		dataSourceRegistration = bundleContext.registerService(javax.sql.DataSource.class, h2, dsProps);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "ogcFeatures", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "ogcFeatures");
		httpProps.put("org.apache.felix.http.runtime.init.id", "ogcFeaturesHttp");
		httpConfig.update(httpProps);

		// the OGC roots mount on this test's HTTP runtime only
		ogcConfig = configAdmin.getConfiguration("org.eclipse.fennec.data.atlas.ogc", "?");
		Dictionary<String, Object> ogcProps = new Hashtable<>();
		ogcProps.put("http.whiteboard.target", "(id=ogcFeaturesHttp)");
		ogcConfig.update(ogcProps);

		bootstrapConfig = BootstrapConfigs.fresh(configAdmin);
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", configFile.toUri().toString());
		bootstrapConfig.update(bootProps);

		seed(bundleContext, configAdmin);
	}

	/**
	 * Seeds the database through a test-private writable repository over a
	 * second persistence unit with DDL generation (the same recipe as the OData
	 * test), the same single-table mapping and the same geojson converter
	 * binding the Data Atlas unit gets; the Data Atlas unit stays read-only.
	 */
	private static void seed(BundleContext bundleContext, ConfigurationAdmin configAdmin) throws Exception {
		seedMappingConfig = configAdmin.getFactoryConfiguration("fennec.jpa.EORMMappingService", "ogcSeed", "?");
		Dictionary<String, Object> mappingProps = new Hashtable<>();
		mappingProps.put("fennec.jpa.eorm.model.target", "(emf.nsURI=" + ASSET_NSURI + ")");
		mappingProps.put("fennec.jpa.eorm.eClasses", new String[] { "Asset", "Pool", "Slide", "Bench" });
		mappingProps.put("fennec.jpa.eorm.mappingName", "ogcSeed");
		seedMappingConfig.update(mappingProps);

		seedUnitConfig = configAdmin.getFactoryConfiguration("fennec.jpa.EMPersistenceUnit", "ogcSeed", "?");
		Dictionary<String, Object> unitProps = new Hashtable<>();
		unitProps.put("fennec.jpa.persistenceUnitName", "ogcSeed");
		unitProps.put("fennec.jpa.dataSource.target", "(" + DATASOURCE_FILTER_PROPERTY + "=ogc-assets)");
		unitProps.put("fennec.jpa.mapping.target", "(fennec.jpa.eorm.mapping=ogcSeed)");
		unitProps.put("fennec.jpa.converter.target", "(fennec.persistence.converter=geojson)");
		unitProps.put("fennec.jpa.ext.eclipselink.ddl-generation", "create-or-extend-tables");
		seedUnitConfig.update(unitProps);

		seedRepositoryConfig = configAdmin.getFactoryConfiguration("fennec.repository.jpa", "ogcSeed", "?");
		Dictionary<String, Object> repoProps = new Hashtable<>();
		repoProps.put("repositoryId", "dataatlas-ogc-seed");
		repoProps.put("unit.target", "(osgi.unit.name=ogcSeed)");
		seedRepositoryConfig.update(repoProps);

		ServiceReference<Repository> reference = waitForService(bundleContext, Repository.class,
				"(persistence.repository.id=dataatlas-ogc-seed)");
		Repository repository = bundleContext.getServiceObjects(reference).getService();
		try {
			EPackage assetPackage = bundleContext
					.getService(waitForService(bundleContext, EPackage.class, "(emf.nsURI=" + ASSET_NSURI + ")"));
			EClass pool = (EClass) assetPackage.getEClassifier("Pool");
			EClass slide = (EClass) assetPackage.getEClassifier("Slide");
			EClass bench = (EClass) assetPackage.getEClassifier("Bench");
			// the same assets as the example file, so both roots answer alike
			repository.save(asset(pool, "pool-kids", "Kids pool", 11.6170, 50.9052, "2026-06-15", "depthMax", 0.6));
			repository.save(asset(pool, "pool-sport", "Sports pool", 11.6182, 50.9058, "2026-05-02", "depthMax", 2.0));
			repository.save(asset(pool, "pool-outdoor", "Outdoor pool", 11.6195, 50.9065, "2025-09-20", "depthMax", 1.8));
			repository.save(asset(slide, "slide-black", "Black hole", 11.6175, 50.9060, "2026-04-11", "length", 82.0));
			repository.save(asset(slide, "slide-family", "Family slide", 11.6172, 50.9054, "2026-04-11", "length", 24.0));
			repository.save(asset(bench, "bench-1", "Bench at the kids pool", 11.6168, 50.9051, "2026-01-10", "seats", 4));
			repository.save(asset(bench, "bench-2", "Bench at the outdoor pool", 11.6198, 50.9067, "2026-01-10", "seats", 6));
		} finally {
			repository.dispose();
		}
	}

	/** An asset at a point: the GeoJSON point plus its bounding box in the bbox attributes. */
	private static EObject asset(EClass type, String id, String name, double lon, double lat, String inspected,
			String extraFeature, Object extraValue) {
		EObject asset = EcoreUtil.create(type);
		Coordinates coordinates = GeoJsonFactory.eINSTANCE.createCoordinates();
		coordinates.setLongitude(lon);
		coordinates.setLatitude(lat);
		Point point = GeoJsonFactory.eINSTANCE.createPoint();
		point.setCoordinates(coordinates);
		asset.eSet(type.getEStructuralFeature("geometry"), point);
		asset.eSet(type.getEStructuralFeature("id"), id);
		asset.eSet(type.getEStructuralFeature("name"), name);
		asset.eSet(type.getEStructuralFeature("minX"), lon);
		asset.eSet(type.getEStructuralFeature("minY"), lat);
		asset.eSet(type.getEStructuralFeature("maxX"), lon);
		asset.eSet(type.getEStructuralFeature("maxY"), lat);
		asset.eSet(type.getEStructuralFeature("accessible"), Boolean.TRUE);
		asset.eSet(type.getEStructuralFeature("lastInspection"),
				Date.from(java.time.LocalDate.parse(inspected).atStartOfDay(java.time.ZoneOffset.UTC).toInstant()));
		asset.eSet(type.getEStructuralFeature(extraFeature), extraValue);
		return asset;
	}

	@AfterAll
	static void tearDown() throws Exception {
		for (Configuration configuration : new Configuration[] { bootstrapConfig, seedRepositoryConfig,
				seedUnitConfig, seedMappingConfig, ogcConfig, httpConfig }) {
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
	void rootPublishesExactlyTheConfiguredCollections() throws Exception {
		assertEquals(200, awaitStatus(FILE_ROOT + "/collections", 200));

		JsonObject landing = json(get(FILE_ROOT));
		assertEquals("Leisure pool assets", landing.getString("title"), landing.toString());
		assertTrue(landing.getJsonArray("links").stream().map(v -> ((JsonObject) v).getString("href"))
				.allMatch(href -> href.startsWith(FILE_ROOT)), "links must resolve against the root: " + landing);

		List<String> conformance = json(get(FILE_ROOT + "/conformance")).getJsonArray("conformsTo").getValuesAs(
				JsonValue::toString);
		assertTrue(conformance.stream().anyMatch(c -> c.contains("ogcapi-features-1/1.0/conf/core")), conformance.toString());
		assertTrue(conformance.stream().anyMatch(c -> c.contains("cql2-text")), conformance.toString());

		// the annotated pools and slides plus the declared benches - and nothing
		// else: neither the annotated 'assets' collection of the abstract
		// supertype nor the other root's collections
		assertEquals(Set.of("pools", "slides", "benches"), collectionIds(FILE_ROOT));

		JsonObject benches = json(get(FILE_ROOT + "/collections/benches"));
		assertEquals("Benches", benches.getString("title"), benches.toString());
		assertEquals("Furniture", benches.getString("layerGroup"), benches.toString());
		JsonObject pools = json(get(FILE_ROOT + "/collections/pools"));
		assertEquals("Pools", pools.getString("title"), pools.toString());
	}

	@Test
	@Order(2)
	void servesFeaturesOfTheFileInput() throws Exception {
		awaitStatus(FILE_ROOT + "/collections", 200);

		JsonObject page = json(get(FILE_ROOT + "/collections/pools/items"));
		assertEquals("FeatureCollection", page.getString("type"), page.toString());
		assertEquals(3, page.getInt("numberMatched"), page.toString());
		JsonArray features = page.getJsonArray("features");
		assertEquals(3, features.size(), page.toString());
		JsonObject kids = features.stream().map(v -> (JsonObject) v)
				.filter(f -> "pool-kids".equals(f.getString("id"))).findFirst().orElseThrow();
		assertEquals("Kids pool", kids.getJsonObject("properties").getString("name"), kids.toString());
		assertEquals(0.6, kids.getJsonObject("properties").getJsonNumber("depthMax").doubleValue(), 1e-9);
		// the geometry is the asset's GeoJSON point (a reference to the GeoJSON model)
		assertPoint(kids, 11.6170, 50.9052);

		JsonObject single = json(get(FILE_ROOT + "/collections/benches/items/bench-2"));
		assertEquals("Feature", single.getString("type"), single.toString());
		assertEquals(6, single.getJsonObject("properties").getInt("seats"), single.toString());

		// CQL2 text over the file input, evaluated in memory
		JsonObject filtered = json(get(FILE_ROOT + "/collections/pools/items?filter="
				+ encode("waterTemperature > 25")));
		assertEquals(2, filtered.getInt("numberMatched"), filtered.toString());

		// bbox over the bbox attributes and the geometry: only the outdoor pool
		JsonObject boxed = json(get(FILE_ROOT + "/collections/pools/items?bbox=11.6190,50.9060,11.6200,50.9070"));
		assertEquals(1, boxed.getInt("numberMatched"), boxed.toString());
		assertEquals("pool-outdoor", boxed.getJsonArray("features").getJsonObject(0).getString("id"), boxed.toString());
	}

	@Test
	@Order(3)
	void pushesDownIntoTheDatabaseWithTheDeclaredLimits() throws Exception {
		assertEquals(200, awaitStatus(JPA_ROOT + "/collections", 200));
		assertEquals(Set.of("db-pools", "assets"), collectionIds(JPA_ROOT));

		JsonObject pools = json(get(JPA_ROOT + "/collections/db-pools"));
		assertEquals("Pools (DB)", pools.getString("title"), pools.toString());

		// defaultLimit=2 is the page size without a limit parameter: a page of
		// two plus a next link; maxLimit=5 caps a larger request
		JsonObject page = json(get(JPA_ROOT + "/collections/assets/items"));
		assertEquals(7, page.getInt("numberMatched"), page.toString());
		assertEquals(2, page.getInt("numberReturned"), page.toString());
		assertTrue(page.getJsonArray("links").stream().map(v -> ((JsonObject) v).getString("rel"))
				.anyMatch("next"::equals), "expected a next link: " + page);
		JsonObject capped = json(get(JPA_ROOT + "/collections/assets/items?limit=100"));
		assertEquals(5, capped.getInt("numberReturned"), capped.toString());

		// bbox pushed into the database: the outdoor pool and the bench next to it
		JsonObject boxed = json(get(JPA_ROOT + "/collections/assets/items?bbox=11.6190,50.9060,11.6200,50.9070"));
		assertEquals(2, boxed.getInt("numberMatched"), boxed.toString());

		JsonObject filtered = json(get(JPA_ROOT + "/collections/db-pools/items?filter=" + encode("depthMax > 1.5")));
		assertEquals(2, filtered.getInt("numberMatched"), filtered.toString());

		JsonObject recent = json(get(JPA_ROOT + "/collections/assets/items?datetime=2026-01-01T00:00:00Z/.."));
		assertEquals(6, recent.getInt("numberMatched"), recent.toString());

		JsonObject single = json(get(JPA_ROOT + "/collections/db-pools/items/pool-sport"));
		assertEquals("Sports pool", single.getJsonObject("properties").getString("name"), single.toString());
		// the GeoJSON child comes back from its CLOB column (emf.persistence-jpa#363)
		assertPoint(single, 11.6182, 50.9058);
	}

	@Test
	@Order(4)
	void isReadOnlyAndKnowsOnlyItsOwnCollections() throws Exception {
		awaitStatus(FILE_ROOT + "/collections", 200);

		HttpRequest post = HttpRequest.newBuilder(java.net.URI.create(FILE_ROOT + "/collections/pools/items"))
				.header("Content-Type", "application/geo+json")
				.POST(HttpRequest.BodyPublishers.ofString("{\"type\":\"Feature\",\"id\":\"pool-x\"}"))
				.build();
		assertEquals(405, CLIENT.send(post, HttpResponse.BodyHandlers.ofString()).statusCode());

		// annotated but not configured for this root, renamed in the other root, unknown
		assertEquals(404, get(FILE_ROOT + "/collections/assets").statusCode());
		assertEquals(404, get(FILE_ROOT + "/collections/assets/items").statusCode());
		assertEquals(404, get(JPA_ROOT + "/collections/pools").statusCode());
		assertEquals(404, get(FILE_ROOT + "/collections/nope").statusCode());
	}

	@Test
	@Order(5)
	void offersOpenApiQgisProjectAndViewer() throws Exception {
		awaitStatus(FILE_ROOT + "/collections", 200);

		assertEquals(200, get(FILE_ROOT + "/api").statusCode());

		HttpResponse<String> qgis = get(FILE_ROOT + "/collections?f=qgs", "*/*");
		assertEquals(200, qgis.statusCode(), qgis.body());
		assertTrue(qgis.body().contains("<qgis"), qgis.body().substring(0, Math.min(200, qgis.body().length())));

		HttpResponse<String> viewer = get(FILE_ROOT + "/viewer/", "text/html");
		assertEquals(200, viewer.statusCode(), viewer.body());
		assertTrue(viewer.headers().firstValue("Content-Type").orElse("").startsWith("text/html"),
				viewer.headers().toString());
	}

	@Test
	@Order(6)
	void removingTheServiceTakesItsRootDown(@InjectBundleContext BundleContext bundleContext) throws Exception {
		awaitStatus(FILE_ROOT + "/collections", 200);

		int start = v1.indexOf("  <services xsi:type=\"configuration:OgcFeaturesDataService\" id=\"assets-ogc\"");
		int end = v1.indexOf("</services>", start) + "</services>\n".length();
		assertTrue(start > 0 && end > start, "fixture layout changed");
		Files.writeString(configFile, v1.substring(0, start) + v1.substring(end), StandardCharsets.UTF_8);
		// re-applying a configuration re-registers the EPackages, which takes every
		// root's collections away for a moment - wait for the registrar to have
		// applied the diff before judging what is up and what is down
		awaitServiceGone(bundleContext,
				"(&(objectClass=org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService)"
						+ "(data.atlas.config.id=assets-ogc))");
		assertEquals(404, awaitStatus(FILE_ROOT + "/collections", 404));
		// the other root survives the change
		assertEquals(200, awaitStatus(JPA_ROOT + "/collections", 200));
		assertEquals(404, get(FILE_ROOT + "/collections").statusCode());

		Files.writeString(configFile, v1, StandardCharsets.UTF_8);
		assertEquals(200, awaitStatus(FILE_ROOT + "/collections", 200));
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

	private static void assertPoint(JsonObject feature, double lon, double lat) {
		assertTrue(feature.containsKey("geometry") && !feature.isNull("geometry"), "no geometry: " + feature);
		JsonObject geometry = feature.getJsonObject("geometry");
		assertEquals("Point", geometry.getString("type"), feature.toString());
		JsonArray coordinates = geometry.getJsonArray("coordinates");
		assertEquals(lon, coordinates.getJsonNumber(0).doubleValue(), 1e-9, feature.toString());
		assertEquals(lat, coordinates.getJsonNumber(1).doubleValue(), 1e-9, feature.toString());
	}

	private static Set<String> collectionIds(String root) throws Exception {
		JsonObject collections = json(get(root + "/collections"));
		return collections.getJsonArray("collections").stream().map(v -> ((JsonObject) v).getString("id"))
				.collect(java.util.stream.Collectors.toSet());
	}

	private static String encode(String value) {
		return URLEncoder.encode(value, StandardCharsets.UTF_8);
	}

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
		throw new AssertionError("no " + type.getSimpleName() + " matching " + filter + " within " + DEADLINE_MS
				+ " ms" + ThreadDumps.dump());
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-ogc");
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
