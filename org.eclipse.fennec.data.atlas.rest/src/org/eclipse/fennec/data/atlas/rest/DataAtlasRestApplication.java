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
package org.eclipse.fennec.data.atlas.rest;

import java.util.Set;

import jakarta.ws.rs.core.Application;

/**
 * Jakarta-RS {@link Application} registered per configured
 * {@code RestDataService}; the service's {@code urlContext} becomes the
 * whiteboard application base.
 */
public class DataAtlasRestApplication extends Application {

	private final Set<Object> singletons;

	public DataAtlasRestApplication(Object... resources) {
		this.singletons = Set.of(resources);
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}
}
