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
package org.eclipse.fennec.data.atlas.publication.dcat;

/**
 * A publication declaration that cannot be published as configured: missing
 * mandatory metadata, an unsupported provider kind, or a missing public base
 * URL. Retrying does not help — only a configuration change clears it
 * (data.atlas issue #4, DA-DCAT-9/13).
 */
class PublicationConfigException extends Exception {

	private static final long serialVersionUID = 1L;

	PublicationConfigException(String message) {
		super(message);
	}
}
