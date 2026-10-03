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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

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
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.persistence.repository.api.Repository;
import org.geojson.Coordinates;
import org.geojson.GeoJsonFactory;
import org.geojson.GeoJsonPackage;
import org.geojson.Point;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
 * OGC API Features in Model Atlas mode, with the GeoJSON geometry — the case the
 * file-mode {@link DataAtlasOgcFeaturesIntegrationTest} cannot see. The asset
 * schema comes from a real {@code eclipsefennec/model.atlas:file-snapshot}
 * container and references the GeoJSON model by nsURI; the GeoJSON model is
 * seeded into the Atlas as well (dependencies first, model.atlas#329) while the
 * runtime also carries it as the generated {@code org.geojson.model}.
 *
 * <p>
 * The schema the model.atlas client loads must bind its geometry reference to
 * the deployed, generated {@code Geometry} — not to a dynamic copy of the
 * Atlas's GeoJSON schema (model.atlas#330). Otherwise the type identity splits:
 * writing a generated {@code Point} into an asset fails, the JPA read decodes
 * the geometry column into a dynamic object, and the OGC server serves
 * {@code geometry: null} without a word (DataInMotion/infrastructure#67).
 * </p>
 *
 * <p>Skipped when docker (or the image) is not available.</p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class OgcFeaturesAtlasModeIntegrationTest {

	private static final String IMAGE = "eclipsefennec/model.atlas:file-snapshot";
	private static final String CONTAINER = "dataatlas-it-ogc-modelatlas";
	private static final int MODEL_ATLAS_PORT = 18101;
	private static final String ATLAS_BASE = "http://localhost:" + MODEL_ATLAS_PORT + "/atlas/rest";
	private static final int HTTP_PORT = 18095;
	private static final String HOST = "http://localhost:" + HTTP_PORT;
	private static final String GEOJSON_NSURI = "https://geojson.org/model/2016";
	private static final String ASSET_NSURI = "https://eclipse.org/fennec/data/atlas/example/asset/1.0.0";
	private static final String DATASOURCE_FILTER_PROPERTY = "dataatlas.test.ds";
	private static final long DEADLINE_MS = 120_000;

	private static final HttpClient CLIENT = HttpClient.newHttpClient();

	private static Configuration httpConfig;
	private static Configuration ogcConfig;
	private static Configuration clientConfig;
	private static Configuration bootstrapConfig;
	private static Configuration seedMappingConfig;
	private static Configuration seedUnitConfig;
	private static Configuration seedRepositoryConfig;
	private static ServiceRegistration<javax.sql.DataSource> dataSourceRegistration;
	private static boolean containerStarted;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		assumeTrue(docker("version") == 0, "docker is not available");

		Path dir = extract(bundleContext);

		docker("rm", "-f", CONTAINER);
		int started = docker("run", "-d", "--name", CONTAINER, "-p", MODEL_ATLAS_PORT + ":8080",
				"-e", "JAVA_TOOL_OPTIONS=-Dconfigurator.initial=file:///opt/modelatlas/runtime/load/dataatlas.json",
				"-v", dir.resolve("atlas/load") + ":/opt/modelatlas/runtime/load:ro",
				IMAGE);
		assumeTrue(started == 0, "could not start " + IMAGE);
		containerStarted = true;

		awaitOk(ATLAS_BASE + "/scopes/dataatlas", 120_000);
		seed(bundleContext, dir);

		org.h2.jdbcx.JdbcDataSource h2 = new org.h2.jdbcx.JdbcDataSource();
		h2.setURL("jdbc:h2:mem:dataatlas-ogc-atlas;DB_CLOSE_DELAY=-1");
		Dictionary<String, Object> dsProps = new Hashtable<>();
		dsProps.put(DATASOURCE_FILTER_PROPERTY, "ogc-atlas-assets");
		dataSourceRegistration = bundleContext.registerService(javax.sql.DataSource.class, h2, dsProps);

		httpConfig = configAdmin.getFactoryConfiguration("org.apache.felix.http", "ogcAtlas", "?");
		Dictionary<String, Object> httpProps = new Hashtable<>();
		httpProps.put("org.osgi.service.http.port", HTTP_PORT);
		httpProps.put("org.apache.felix.http.name", "ogcAtlas");
		httpProps.put("org.apache.felix.http.runtime.init.id", "ogcAtlasHttp");
		httpConfig.update(httpProps);

		ogcConfig = configAdmin.getConfiguration("org.eclipse.fennec.data.atlas.ogc", "?");
		Dictionary<String, Object> ogcProps = new Hashtable<>();
		ogcProps.put("http.whiteboard.target", "(id=ogcAtlasHttp)");
		ogcConfig.update(ogcProps);

		clientConfig = configAdmin
				.getFactoryConfiguration("org.eclipse.fennec.model.atlas.rest.client", "ogcAtlasTest", "?");
		Dictionary<String, Object> clientProps = new Hashtable<>();
		clientProps.put("base.uri", ATLAS_BASE);
		clientProps.put("scope.allow.list", new String[] { "dataatlas" });
		clientProps.put("cache.ttl.ms", 1000L);
		clientConfig.update(clientProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasModelAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("atlas.registry", "configurations");
		bootProps.put("atlas.object.id", "dataatlas");
		bootProps.put("refresh.interval.ms", 2000L);
		bootProps.put("scopeService.target", "(atlas.scope=dataatlas)");
		bootstrapConfig.update(bootProps);

		seedDatabase(bundleContext, configAdmin);
	}

	@AfterAll
	static void tearDown() throws Exception {
		for (Configuration configuration : new Configuration[] { bootstrapConfig, clientConfig, seedRepositoryConfig,
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
		if (containerStarted) {
			docker("rm", "-f", CONTAINER);
		}
	}

	/**
	 * The asset schema the Data Atlas got from the Atlas binds its geometry
	 * reference to the deployed, generated GeoJSON {@code Geometry}.
	 */
	@Test
	void theAtlasSchemaBindsTheDeployedGeoJsonModel(@InjectBundleContext BundleContext bundleContext)
			throws Exception {
		EPackage assets = bundleContext
				.getService(waitForService(bundleContext, EPackage.class, "(emf.nsURI=" + ASSET_NSURI + ")"));
		EReference geometry = (EReference) ((EClass) assets.getEClassifier("Asset")).getEStructuralFeature("geometry");
		assertSame(GeoJsonPackage.eINSTANCE.getGeometry(), geometry.getEReferenceType(),
				"the geometry reference is bound to " + geometry.getEReferenceType() + " of "
						+ geometry.getEReferenceType().getEPackage() + ", not to the deployed GeoJSON model "
						+ "(model.atlas#330)");
	}

	@Test
	void servesTheGeometriesOfTheFileAndTheDatabase() throws Exception {
		assertEquals(200, awaitStatus(HOST + "/ogc/atlas-file/collections/pools/items", 200));
		JsonObject pools = json(get(HOST + "/ogc/atlas-file/collections/pools/items"));
		assertEquals(3, pools.getInt("numberMatched"), pools.toString());
		JsonObject kids = pools.getJsonArray("features").stream().map(v -> (JsonObject) v)
				.filter(f -> "pool-kids".equals(f.getString("id"))).findFirst().orElseThrow();
		assertPoint(kids, 11.6170, 50.9052);

		assertEquals(200, awaitStatus(HOST + "/ogc/atlas-db/collections/assets/items/pool-sport", 200));
		// the GeoJSON child comes back from its CLOB column, decoded against the
		// deployed model (emf.persistence-jpa#363)
		assertPoint(json(get(HOST + "/ogc/atlas-db/collections/assets/items/pool-sport")), 11.6182, 50.9058);
		JsonObject boxed = json(get(HOST + "/ogc/atlas-db/collections/assets/items?bbox=11.6190,50.9060,11.6200,50.9070"));
		assertEquals(2, boxed.getInt("numberMatched"), boxed.toString());
	}

	// --- seeding ------------------------------------------------------------

	/**
	 * GeoJSON first (straight from the deployed bundle, so Atlas and runtime
	 * hold the same model), then the configuration model and the asset schema,
	 * then the configuration instance.
	 */
	private static void seed(BundleContext bundleContext, Path dir) throws Exception {
		Bundle geoJsonBundle = java.util.Arrays.stream(bundleContext.getBundles())
				.filter(b -> "org.geojson.model".equals(b.getSymbolicName())).findFirst().orElseThrow();
		byte[] geoJson;
		try (InputStream in = geoJsonBundle.getEntry("model/geojson.ecore").openStream()) {
			geoJson = in.readAllBytes();
		}
		postSchema(geoJson, GEOJSON_NSURI);
		postSchema(Files.readAllBytes(dir.resolve("atlas/models/eorm.ecore")),
				"https://eclipse.org/fennec/persistence/eorm/1.0.0");
		postSchema(Files.readAllBytes(dir.resolve("atlas/models/configuration.ecore")),
				"https://eclipse.org/fennec/data/atlas/configuration/1.0.0");
		postSchema(Files.readAllBytes(dir.resolve("data/model/asset.ecore")), ASSET_NSURI);

		String instance = Files.readString(dir.resolve("data/dataatlas-ogc-atlas.xmi"), StandardCharsets.UTF_8)
				.replace("/opt/dataatlas/runtime/data/data/assets.xmi",
						dir.resolve("data/data/assets.xmi").toUri().toString());
		HttpResponse<String> seeded = null;
		for (int attempt = 0; attempt < 15; attempt++) {
			seeded = CLIENT.send(HttpRequest
					.newBuilder(java.net.URI.create(ATLAS_BASE
							+ "/dataatlas/registries/configurations/stages/release/dataatlas?name=dataatlas&override=true"))
					.header("Content-Type", "application/xmi")
					.header("Accept", "application/json")
					.POST(HttpRequest.BodyPublishers.ofString(instance))
					.build(), HttpResponse.BodyHandlers.ofString());
			if (seeded.statusCode() != 500) {
				break;
			}
			Thread.sleep(2000);
		}
		HttpResponse<String> result = seeded;
		assertTrue(result.statusCode() == 201 || result.statusCode() == 409,
				() -> "instance seed failed: " + result.statusCode() + " " + result.body());
	}

	private static void postSchema(byte[] content, String nsUri) throws Exception {
		String enc = URLEncoder.encode(nsUri, StandardCharsets.UTF_8);
		for (String stage : new String[] { "release", "draft" }) {
			HttpResponse<String> response = CLIENT.send(HttpRequest
					.newBuilder(java.net.URI.create(
							ATLAS_BASE + "/dataatlas/schema/stages/" + stage + "?nsUri=" + enc + "&version=1.0.0"))
					.header("Content-Type", "application/xmi")
					.header("Accept", "application/json")
					.POST(HttpRequest.BodyPublishers.ofByteArray(content))
					.build(), HttpResponse.BodyHandlers.ofString());
			assertTrue(response.statusCode() == 201 || response.statusCode() == 409,
					() -> "schema seed " + nsUri + " (" + stage + ") failed: " + response.statusCode() + " "
							+ response.body());
		}
	}

	/**
	 * Seeds the database through a test-private writable repository over the
	 * asset package the Data Atlas got from the Atlas — writing generated
	 * {@code Point}s into it is the first thing a split type identity breaks.
	 */
	private static void seedDatabase(BundleContext bundleContext, ConfigurationAdmin configAdmin) throws Exception {
		EPackage assetPackage = bundleContext
				.getService(waitForService(bundleContext, EPackage.class, "(emf.nsURI=" + ASSET_NSURI + ")"));

		seedMappingConfig = configAdmin.getFactoryConfiguration("fennec.jpa.EORMMappingService", "ogcAtlasSeed", "?");
		Dictionary<String, Object> mappingProps = new Hashtable<>();
		mappingProps.put("fennec.jpa.eorm.model.target", "(emf.nsURI=" + ASSET_NSURI + ")");
		mappingProps.put("fennec.jpa.eorm.eClasses", new String[] { "Asset", "Pool", "Slide", "Bench" });
		mappingProps.put("fennec.jpa.eorm.mappingName", "ogcAtlasSeed");
		seedMappingConfig.update(mappingProps);

		seedUnitConfig = configAdmin.getFactoryConfiguration("fennec.jpa.EMPersistenceUnit", "ogcAtlasSeed", "?");
		Dictionary<String, Object> unitProps = new Hashtable<>();
		unitProps.put("fennec.jpa.persistenceUnitName", "ogcAtlasSeed");
		unitProps.put("fennec.jpa.dataSource.target", "(" + DATASOURCE_FILTER_PROPERTY + "=ogc-atlas-assets)");
		unitProps.put("fennec.jpa.mapping.target", "(fennec.jpa.eorm.mapping=ogcAtlasSeed)");
		unitProps.put("fennec.jpa.ext.eclipselink.ddl-generation", "create-or-extend-tables");
		seedUnitConfig.update(unitProps);

		seedRepositoryConfig = configAdmin.getFactoryConfiguration("fennec.repository.jpa", "ogcAtlasSeed", "?");
		Dictionary<String, Object> repoProps = new Hashtable<>();
		repoProps.put("repositoryId", "dataatlas-ogc-atlas-seed");
		repoProps.put("unit.target", "(osgi.unit.name=ogcAtlasSeed)");
		seedRepositoryConfig.update(repoProps);

		ServiceReference<Repository> reference = waitForService(bundleContext, Repository.class,
				"(persistence.repository.id=dataatlas-ogc-atlas-seed)");
		Repository repository = bundleContext.getServiceObjects(reference).getService();
		try {
			EClass pool = (EClass) assetPackage.getEClassifier("Pool");
			EClass bench = (EClass) assetPackage.getEClassifier("Bench");
			repository.save(asset(pool, "pool-sport", "Sports pool", 11.6182, 50.9058));
			repository.save(asset(pool, "pool-outdoor", "Outdoor pool", 11.6195, 50.9065));
			repository.save(asset(bench, "bench-2", "Bench at the outdoor pool", 11.6198, 50.9067));
			repository.save(asset(bench, "bench-1", "Bench at the kids pool", 11.6168, 50.9051));
		} finally {
			repository.dispose();
		}
	}

	private static EObject asset(EClass type, String id, String name, double lon, double lat) {
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
		asset.eSet(type.getEStructuralFeature("lastInspection"), new Date());
		return asset;
	}

	// --- helpers ------------------------------------------------------------

	private static void assertPoint(JsonObject feature, double lon, double lat) {
		assertTrue(feature.containsKey("geometry") && !feature.isNull("geometry"), "no geometry: " + feature);
		JsonObject geometry = feature.getJsonObject("geometry");
		assertEquals("Point", geometry.getString("type"), feature.toString());
		JsonArray coordinates = geometry.getJsonArray("coordinates");
		assertEquals(lon, coordinates.getJsonNumber(0).doubleValue(), 1e-9, feature.toString());
		assertEquals(lat, coordinates.getJsonNumber(1).doubleValue(), 1e-9, feature.toString());
	}

	private static JsonObject json(HttpResponse<String> response) {
		assertEquals(200, response.statusCode(), response.body());
		return Json.createReader(new StringReader(response.body())).readObject();
	}

	private static HttpResponse<String> get(String url) throws Exception {
		return CLIENT.send(HttpRequest.newBuilder(java.net.URI.create(url))
				.header("Accept", "application/json")
				.timeout(Duration.ofSeconds(15))
				.GET()
				.build(), HttpResponse.BodyHandlers.ofString());
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

	private static void awaitOk(String url, long timeoutMs) throws Exception {
		long deadline = System.currentTimeMillis() + timeoutMs;
		while (System.currentTimeMillis() < deadline) {
			try {
				if (get(url).statusCode() == 200) {
					return;
				}
			} catch (Exception e) {
				// keep polling
			}
			Thread.sleep(1000);
		}
		throw new AssertionError(url + " did not answer 200 within " + timeoutMs + " ms");
	}

	private static <T> ServiceReference<T> waitForService(BundleContext bundleContext, Class<T> type, String filter)
			throws Exception {
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

	/** Extracts the /data and /atlas fixture trees of this bundle into a temp folder. */
	private static Path extract(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-ogc-atlas-it");
		for (String root : new String[] { "data", "atlas" }) {
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
		assertNotNull(target);
		return target;
	}

	private static int docker(String... args) throws Exception {
		String[] command = new String[args.length + 1];
		command[0] = "docker";
		System.arraycopy(args, 0, command, 1, args.length);
		Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
		process.getInputStream().readAllBytes();
		return process.waitFor(300, TimeUnit.SECONDS) ? process.exitValue() : -1;
	}
}
