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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.emf.osgi.ResourceSetFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * How a schema loaded at runtime (a dynamic EPackage, as the Data Atlas loads
 * them from a file or a Model Atlas) resolves a cross-model reference into a
 * generated model deployed in the runtime — here the {@code Geometry} class of
 * the GeoJSON model ({@code org.geojson.model}), the geometry of OGC API
 * Features collections. The reference must resolve to the class of the
 * deployed, generated package, or the server, the JPA reader and the codec
 * (which create generated geometries) clash with the schema.
 *
 * <p>
 * Two spellings of the same reference: the nsURI (resolved through the package
 * registry) and the {@code platform:/plugin/<bundle>/<ecore>} location that
 * the bundle declares in its {@code org.eclipse.emf.ecore.generated_package}
 * capability ({@code ecore="/model/geojson.ecore"}) — in Eclipse EMF maps such
 * a location of a generated model to its nsURI. The emf.ogc.features example
 * models use the second form (the codegen cannot resolve the first,
 * eclipse-fennec/emf.osgi#113). Since eclipse-fennec/emf.osgi#114 the
 * location resolves to the generated package as well — on purpose unlike
 * Eclipse, where it loads the model file as a dynamic copy.
 * </p>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class CrossModelReferenceResolutionIntegrationTest {

	private static final String GEOJSON_NSURI = "https://geojson.org/model/2016";

	@Test
	void nsUriReferenceResolvesToTheDeployedGeneratedPackage(@InjectService ResourceSetFactory resourceSetFactory,
			@InjectService(filter = "(emf.nsURI=" + GEOJSON_NSURI + ")") EPackage geoJson) throws Exception {
		assertResolvesToDeployed(resourceSetFactory, geoJson, GEOJSON_NSURI + "#//Geometry");
	}

	@Test
	void platformPluginReferenceResolvesToTheDeployedGeneratedPackage(
			@InjectService ResourceSetFactory resourceSetFactory,
			@InjectService(filter = "(emf.nsURI=" + GEOJSON_NSURI + ")") EPackage geoJson) throws Exception {
		assertResolvesToDeployed(resourceSetFactory, geoJson,
				"platform:/plugin/org.geojson.model/model/geojson.ecore#//Geometry");
	}

	/**
	 * The manual way emf.osgi offers: a {@code DefaultUriMapProvider} mapping
	 * the model location to the nsURI, which every ResourceSet's URI converter
	 * receives; the location normalizes to the nsURI, which resolves to the
	 * registered package (emf.osgi#114).
	 */
	@Test
	void platformPluginReferenceWithAConfiguredUriMap(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin, @InjectService ResourceSetFactory resourceSetFactory,
			@InjectService(filter = "(emf.nsURI=" + GEOJSON_NSURI + ")") EPackage geoJson) throws Exception {
		Configuration uriMap = configAdmin.getFactoryConfiguration("DefaultUriMapProvider", "geojsonLocation", "?");
		try {
			Dictionary<String, Object> props = new Hashtable<>();
			props.put("uri.map.src", "platform:/plugin/org.geojson.model/model/geojson.ecore");
			props.put("uri.map.dest", GEOJSON_NSURI);
			uriMap.update(props);
			awaitService(bundleContext, "(&(objectClass=org.eclipse.fennec.emf.osgi.UriMapProvider)"
					+ "(service.pid=DefaultUriMapProvider~geojsonLocation))");
			assertResolvesToDeployed(resourceSetFactory, geoJson,
					"platform:/plugin/org.geojson.model/model/geojson.ecore#//Geometry");
		} finally {
			uriMap.delete();
		}
	}

	private static void awaitService(BundleContext bundleContext, String filter) throws Exception {
		long deadline = System.currentTimeMillis() + 30_000;
		while (System.currentTimeMillis() < deadline) {
			ServiceReference<?>[] references = bundleContext.getAllServiceReferences(null, filter);
			if (references != null && references.length > 0) {
				return;
			}
			Thread.sleep(100);
		}
		throw new AssertionError("no service matching " + filter);
	}

	private static void assertResolvesToDeployed(ResourceSetFactory resourceSetFactory, EPackage geoJson, String href)
			throws Exception {
		EClass deployed = (EClass) geoJson.getEClassifier("Geometry");
		assertNotNull(deployed, "the deployed GeoJSON package has no Geometry class");

		ResourceSet resourceSet = resourceSetFactory.createResourceSet();
		Resource schema = resourceSet.createResource(URI.createURI("file:/probe/shapes.ecore"));
		schema.load(new ByteArrayInputStream(probeSchema(href).getBytes(StandardCharsets.UTF_8)), Map.of());
		EPackage shapes = (EPackage) schema.getContents().get(0);
		EReference geometry = (EReference) ((EClass) shapes.getEClassifier("Shape")).getEStructuralFeature("geometry");

		EClass resolved = geometry.getEReferenceType();
		String context = "href " + href + " -> " + resolved + "; normalized "
				+ resourceSet.getURIConverter().normalize(URI.createURI(href).trimFragment()) + "; resources in set: "
				+ resourceSet.getResources().stream().map(r -> String.valueOf(r.getURI()))
						.collect(Collectors.joining(", "));
		assertFalse(resolved.eIsProxy(), "the reference stays an unresolved proxy: " + context);
		assertSame(deployed, resolved, "the reference resolves to another Geometry than the deployed one: " + context);
	}

	private static String probeSchema(String href) {
		return """
				<?xml version="1.0" encoding="UTF-8"?>
				<ecore:EPackage xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI"
				    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
				    xmlns:ecore="http://www.eclipse.org/emf/2002/Ecore" name="shapes"
				    nsURI="https://eclipse.org/fennec/data/atlas/test/shapes/1.0" nsPrefix="shapes">
				  <eClassifiers xsi:type="ecore:EClass" name="Shape">
				    <eStructuralFeatures xsi:type="ecore:EReference" name="geometry" containment="true"
				        eType="ecore:EClass %s"/>
				  </eClassifiers>
				</ecore:EPackage>
				""".formatted(href);
	}
}
