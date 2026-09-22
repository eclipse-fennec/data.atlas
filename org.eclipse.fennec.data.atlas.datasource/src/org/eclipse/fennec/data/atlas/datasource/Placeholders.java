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

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The placeholder syntax of the Felix Config Admin interpolation plugin as far
 * as the credential rule of the configuration model uses it.
 *
 * <p>
 * The model never carries a credential value: {@code user} and
 * {@code password} of a data source definition are placeholders that the
 * interpolation plugin resolves in the consuming runtime when Config Admin
 * delivers the materialized configuration - {@code $[env:NAME]} against an
 * environment variable, {@code $[secret:NAME]} against a file in the runtime's
 * secrets directory. Nothing else is accepted: no literal, no default value
 * (a default IS a literal), no other placeholder type.
 * </p>
 */
final class Placeholders {

	/** Framework/system property the interpolation plugin reads its secrets directory from. */
	static final String SECRETS_DIR_PROPERTY = "org.apache.felix.configadmin.plugin.interpolation.secretsdir";

	static final String TYPE_ENV = "env";
	static final String TYPE_SECRET = "secret";

	private static final Pattern CREDENTIAL = Pattern.compile("^\\$\\[(env|secret):([A-Za-z0-9_.\\-]+)\\]$");
	private static final Pattern ANY = Pattern.compile("\\$\\[[^\\]]*\\]");

	/** A credential reference: the placeholder type and the referenced name. */
	record Reference(String type, String name) {
	}

	private Placeholders() {
	}

	/**
	 * The credential reference a value denotes, or empty when the value is not
	 * exactly one {@code $[env:NAME]} / {@code $[secret:NAME]} placeholder.
	 */
	static Optional<Reference> credential(String value) {
		if (value == null) {
			return Optional.empty();
		}
		Matcher matcher = CREDENTIAL.matcher(value.trim());
		return matcher.matches() ? Optional.of(new Reference(matcher.group(1), matcher.group(2)))
				: Optional.empty();
	}

	/** Whether the value contains any interpolation placeholder at all. */
	static boolean containsPlaceholder(String value) {
		return value != null && ANY.matcher(value).find();
	}
}
