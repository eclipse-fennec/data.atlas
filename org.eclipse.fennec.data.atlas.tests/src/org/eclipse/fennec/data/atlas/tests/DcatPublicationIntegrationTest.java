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
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.api.PublicationStatus;
import org.eclipse.fennec.dcat.atlas.client.api.DcatAtlasClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
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

import dcat.Dataset;
import dcat.Distribution;

/**
 * Milestone 8 (data.atlas#4), portal-independent half: a configuration whose
 * DataService declares a {@code DcatPublication} is registered with the portal
 * client — DataService-first, its DataSets and their Distributions along, the
 * membership links asserted — and removing the declaration withdraws it. The
 * portal is a recording double here; what a real portal accepts and refuses is
 * the docker-gated {@code DcatPortalIntegrationTest}.
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@TestMethodOrder(OrderAnnotation.class)
public class DcatPublicationIntegrationTest {

	private static final String PUBLIC_BASE = "https://data.example.org";
	private static final String PUBLICATION_PID = "org.eclipse.fennec.data.atlas.publication.dcat";

	private static RecordingDcatAtlasClient portal;
	private static ServiceRegistration<DcatAtlasClient> portalRegistration;
	private static Configuration publicationConfig;
	private static Configuration bootstrapConfig;
	private static Path configFile;
	private static String v1;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		Path dataDir = extractTestData(bundleContext);
		configFile = dataDir.resolve("dataatlas-dcat.xmi");
		v1 = Files.readString(configFile, StandardCharsets.UTF_8);

		portal = new RecordingDcatAtlasClient();
		Dictionary<String, Object> portalProps = new Hashtable<>();
		portalProps.put("dcat.portal", "mock");
		portalRegistration = bundleContext.registerService(DcatAtlasClient.class, portal, portalProps);

		publicationConfig = configAdmin.getConfiguration(PUBLICATION_PID, "?");
		Dictionary<String, Object> publicationProps = new Hashtable<>();
		publicationProps.put("public.base.url", PUBLIC_BASE);
		publicationProps.put("retry.interval.ms", 2_000L);
		publicationConfig.update(publicationProps);

		bootstrapConfig = configAdmin.getConfiguration("DataAtlasBootstrap", "?");
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", configFile.toUri().toString());
		bootstrapConfig.update(bootProps);
	}

	@AfterAll
	static void tearDown() throws Exception {
		if (bootstrapConfig != null) {
			bootstrapConfig.delete();
		}
		if (publicationConfig != null) {
			publicationConfig.delete();
		}
		if (portalRegistration != null) {
			portalRegistration.unregister();
		}
	}

	@Test
	@Order(1)
	void declaredServiceIsPublishedServiceFirst(@InjectBundleContext BundleContext bundleContext) throws Exception {
		awaitState(bundleContext, "dcat-persons-rest", PublicationStatus.STATE_REGISTERED, 60_000);

		// the service, DataService-first: the endpoint under the public base
		dcat.DataService service = portal.dataServices.get("dcat-persons-rest");
		assertNotNull(service, "expected the DataService to be registered");
		assertEquals(PUBLIC_BASE + "/dcat-example", service.getEndpointURL().get(0));
		assertEquals("Persons REST", service.getTitle().get(0).getValue());
		assertNotNull(service.getPublisher(), "expected a publisher");
		assertEquals("Data Atlas integration suite", service.getPublisher().getName().get(0).getValue());

		// its dataset, with derived metadata and the declared keywords
		Dataset dataset = portal.datasets.get("dcat-persons");
		assertNotNull(dataset, "expected the DataSet to be registered");
		assertEquals("persons", dataset.getTitle().get(0).getValue());
		assertEquals(2, dataset.getKeyword().size());

		// no exports declared: the runtime defaults JSON and XMI become the
		// distributions, each pointing at the endpoint that serves them
		Distribution json = portal.distributions.get("dcat-persons/json");
		Distribution xml = portal.distributions.get("dcat-persons/xml");
		assertNotNull(json, "expected a distribution for the JSON default");
		assertNotNull(xml, "expected a distribution for the XMI default");
		assertEquals(PUBLIC_BASE + "/dcat-example/persons", json.getAccessURL().get(0));
		assertEquals("http://www.iana.org/assignments/media-types/application/json", json.getMediaType());
		assertNotNull(json.getLicense(), "expected the declared license on the distribution");
		assertEquals("http://dcat-ap.de/def/licenses/dl-by-de/2.0", json.getLicense().getAbout());

		// the membership links a PUT replace would have dropped
		assertTrue(portal.links.contains("catalog:test-catalog<-service:dcat-persons-rest"), portal.links.toString());
		assertTrue(portal.links.contains("catalog:test-catalog<-dataset:dcat-persons"), portal.links.toString());
		assertTrue(portal.links.contains("service:dcat-persons-rest<-dataset:dcat-persons"), portal.links.toString());
		assertTrue(portal.links.contains("distribution:dcat-persons/json<-service:dcat-persons-rest"),
				portal.links.toString());
	}

	@Test
	@Order(2)
	void removedDeclarationWithdrawsFromThePortal(@InjectBundleContext BundleContext bundleContext) throws Exception {
		Files.writeString(configFile, v1.replace(" publication=\"dcat-open-data\"", ""), StandardCharsets.UTF_8);

		long deadline = System.currentTimeMillis() + 60_000;
		while (System.currentTimeMillis() < deadline
				&& (portal.dataServices.containsKey("dcat-persons-rest") || portal.datasets.containsKey("dcat-persons"))) {
			Thread.sleep(500);
		}
		assertTrue(portal.deletions.contains("datasets:dcat-persons"), portal.deletions.toString());
		assertTrue(portal.deletions.contains("data-services:dcat-persons-rest"), portal.deletions.toString());

		// the status service of the withdrawn provider is gone
		deadline = System.currentTimeMillis() + 30_000;
		while (System.currentTimeMillis() < deadline && statusReference(bundleContext, "dcat-persons-rest") != null) {
			Thread.sleep(500);
		}
		assertEquals(null, statusReference(bundleContext, "dcat-persons-rest"),
				"expected the PublicationStatus of the withdrawn provider to be unregistered");
	}

	private static void awaitState(BundleContext bundleContext, String providerId, String state, long timeoutMs)
			throws Exception {
		long deadline = System.currentTimeMillis() + timeoutMs;
		String last = "<no status service>";
		while (System.currentTimeMillis() < deadline) {
			ServiceReference<PublicationStatus> reference = statusReference(bundleContext, providerId);
			if (reference != null) {
				PublicationStatus status = bundleContext.getService(reference);
				try {
					if (status != null) {
						if (state.equals(status.state())) {
							return;
						}
						last = status.state() + " (" + status.message() + ")";
					}
				} finally {
					bundleContext.ungetService(reference);
				}
			}
			Thread.sleep(500);
		}
		throw new AssertionError("publication of '" + providerId + "' never reached " + state + ", last: " + last);
	}

	private static ServiceReference<PublicationStatus> statusReference(BundleContext bundleContext,
			String providerId) throws Exception {
		var references = bundleContext.getServiceReferences(PublicationStatus.class,
				"(" + DataAtlasConstants.CONFIG_OBJECT_ID + "=" + providerId + ")");
		return references.isEmpty() ? null : references.iterator().next();
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-dcat");
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
