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


import java.util.Collection;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;

/**
 * Hands out the file-mode bootstrap's singleton configuration to a test class.
 *
 * <p>
 * Every file-mode test class configures the same PID and deletes it in its
 * {@code @AfterAll}. Configuration Admin delivers that deletion to SCR
 * asynchronously; when the next class creates the PID again before SCR has
 * processed the deletion, SCR drops the deletion (a configuration exists) and
 * then the update (its change count is not newer than the one it has seen),
 * so the bootstrap keeps running on the PREVIOUS class's configuration file
 * and the new class waits for services that never come. {@link #fresh} waits
 * until SCR has really let go of the previous instance.
 * </p>
 */
final class BootstrapConfigs {

	static final String PID = "DataAtlasBootstrap";
	private static final long DEADLINE_MS = 60_000;

	private BootstrapConfigs() {
	}

	/**
	 * The bootstrap configuration for a new test class, created only after no
	 * bootstrap component instance of an earlier class is left.
	 */
	static Configuration fresh(ConfigurationAdmin configAdmin) throws Exception {
		Configuration[] leftovers = configAdmin.listConfigurations("(service.pid=" + PID + ")");
		if (leftovers != null) {
			for (Configuration leftover : leftovers) {
				leftover.delete();
			}
		}
		awaitNoBootstrapInstance();
		return configAdmin.getConfiguration(PID, "?");
	}

	private static void awaitNoBootstrapInstance() throws Exception {
		BundleContext context = FrameworkUtil.getBundle(BootstrapConfigs.class).getBundleContext();
		ServiceReference<ServiceComponentRuntime> reference = context.getServiceReference(ServiceComponentRuntime.class);
		if (reference == null) {
			return;
		}
		ServiceComponentRuntime scr = context.getService(reference);
		try {
			long deadline = System.currentTimeMillis() + DEADLINE_MS;
			while (System.currentTimeMillis() < deadline) {
				if (bootstrapInstances(scr) == 0) {
					return;
				}
				Thread.sleep(100);
			}
			throw new AssertionError("the bootstrap of a previous test class is still active after " + DEADLINE_MS
					+ " ms" + ThreadDumps.dump());
		} finally {
			context.ungetService(reference);
		}
	}

	private static int bootstrapInstances(ServiceComponentRuntime scr) {
		Collection<ComponentDescriptionDTO> descriptions = scr.getComponentDescriptionDTOs();
		return descriptions.stream().filter(description -> PID.equals(description.name))
				.mapToInt(description -> scr.getComponentConfigurationDTOs(description).size()).sum();
	}
}
