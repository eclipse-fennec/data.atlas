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
package org.eclipse.fennec.data.atlas.api;

/**
 * Shared service property names of the Data Atlas runtime.
 *
 * <p>
 * The bootstrap registers the configuration objects of a
 * {@code DataAtlasConfiguration} as OSGi services carrying these properties;
 * per-technology configurators use them to correlate configuration objects
 * with the runtime services realized for them.
 * </p>
 */
public final class DataAtlasConstants {

	/** Name of the Data Atlas instance a service belongs to. */
	public static final String ATLAS_NAME = "data.atlas.name";

	/** The {@code id} of the configuration object a service represents. */
	public static final String CONFIG_OBJECT_ID = "data.atlas.config.id";

	/** The EClass name of the configuration object a service represents. */
	public static final String CONFIG_OBJECT_TYPE = "data.atlas.config.type";

	private DataAtlasConstants() {
	}
}
