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

import java.time.Instant;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Observable outcome of publishing one DataProvider to an external target
 * (e.g. a DCAT portal): what was published, when, and with what result — so an
 * operator can tell whether a provider actually reached the target without
 * reading the target (data.atlas issue #4, DA-DCAT-20).
 *
 * <p>
 * A publication handler registers one service per published provider, carrying
 * {@link DataAtlasConstants#CONFIG_OBJECT_ID} (the provider id) and
 * {@link #STATE} as service properties so consumers can select and await
 * states with a plain service filter. This interface is deliberately free of
 * any publication-kind dependency — the kinds live in their omittable handler
 * bundles.
 * </p>
 */
@ProviderType
public interface PublicationStatus {

	/** Service property carrying {@link #state()}, for LDAP-filterable awaits. */
	String STATE = "data.atlas.publication.state";

	/** The provider is waiting for its target (portal client missing or not ready). */
	String STATE_PENDING = "PENDING";
	/** The provider is published; the target holds the current configuration. */
	String STATE_REGISTERED = "REGISTERED";
	/** The last attempt failed transiently; the handler retries. */
	String STATE_RETRYING = "RETRYING";
	/**
	 * The publication declaration is wrong (missing mandatory metadata, refused
	 * by the target's validation). Retrying will not help — only a configuration
	 * change clears this state.
	 */
	String STATE_ERROR = "ERROR";

	/** The id of the published DataProvider (the DataService in v1). */
	String providerId();

	/** One of the {@code STATE_*} constants. */
	String state();

	/** Human-readable detail: the last error, or what was published where. */
	String message();

	/** When the state last changed. */
	Instant lastChange();
}
