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
package org.eclipse.fennec.data.atlas.datasource;

/**
 * A {@code DataSource} definition that cannot be realized: mode conflict,
 * missing coordinates, a credential that is not a placeholder, a placeholder
 * that does not resolve in this runtime, or a host outside the allow-list. The
 * message names the definition and the reason; the definition is skipped.
 */
class DataSourceDefinitionException extends Exception {

	private static final long serialVersionUID = 1L;

	DataSourceDefinitionException(String message) {
		super(message);
	}
}
