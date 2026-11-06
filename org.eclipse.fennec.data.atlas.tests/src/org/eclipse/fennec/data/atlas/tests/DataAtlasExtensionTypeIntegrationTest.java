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

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
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

/**
 * data.atlas#34: a configuration type defined by a third-party bundle
 * ({@code ProbeInput} of {@code org.eclipse.fennec.data.atlas.tests.extension},
 * an EPackage the bootstrap is not wired to) reaches the DS references of its
 * own configurator. The bootstrap registers configuration objects with the
 * context of the bundle defining their type; registered by the bootstrap
 * itself, the framework would hide the service from every other bundle
 * ({@code ServiceReference.isAssignableTo} is {@code false} when the registrant
 * has no source for the type's package).
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class DataAtlasExtensionTypeIntegrationTest {

	private static final String EXTENSION_BUNDLE = "org.eclipse.fennec.data.atlas.tests.extension";

	private static Configuration bootstrapConfig;
	private static Path dataDir;

	@BeforeAll
	static void setup(@InjectBundleContext BundleContext bundleContext,
			@InjectService ConfigurationAdmin configAdmin) throws Exception {
		dataDir = extractTestData(bundleContext);
		bootstrapConfig = BootstrapConfigs.fresh(configAdmin);
		Dictionary<String, Object> bootProps = new Hashtable<>();
		bootProps.put("config.uri", dataDir.resolve("dataatlas-extension.xmi").toUri().toString());
		bootstrapConfig.update(bootProps);
	}

	@AfterAll
	static void tearDown() throws Exception {
		if (bootstrapConfig != null) {
			bootstrapConfig.delete();
		}
	}

	@Test
	void thirdPartyConfigurationTypeReachesItsConfigurator(@InjectBundleContext BundleContext bundleContext)
			throws Exception {
		// the extension's DS component bound the ProbeInput and published its marker
		ServiceReference<?> marker = await(bundleContext, "(probe.bound=probe-1)", 60_000);
		assertNotNull(marker, "the extension bundle's configurator never bound its ProbeInput");
		assertEquals(dataDir.resolve("mapping").resolve("probe.csv").toAbsolutePath().normalize(),
				Path.of(java.net.URI.create((String) marker.getProperty("probe.mappingFile"))).toAbsolutePath()
						.normalize());

		// registered with the context of the bundle that defines the type
		ServiceReference<?> configObject = await(bundleContext,
				"(" + DataAtlasConstants.CONFIG_OBJECT_ID + "=probe-1)", 10_000);
		assertNotNull(configObject);
		assertEquals(EXTENSION_BUNDLE, configObject.getBundle().getSymbolicName());
	}

	private static ServiceReference<?> await(BundleContext bundleContext, String filter, long timeoutMs)
			throws Exception {
		long deadline = System.currentTimeMillis() + timeoutMs;
		while (System.currentTimeMillis() < deadline) {
			ServiceReference<?>[] references = bundleContext.getAllServiceReferences(null, filter);
			if (references != null && references.length > 0) {
				return references[0];
			}
			Thread.sleep(250);
		}
		return null;
	}

	private static Path extractTestData(BundleContext bundleContext) throws Exception {
		Bundle bundle = bundleContext.getBundle();
		Path target = Files.createTempDirectory("dataatlas-extension");
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
