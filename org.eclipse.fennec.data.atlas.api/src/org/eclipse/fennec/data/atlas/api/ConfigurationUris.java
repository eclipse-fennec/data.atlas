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

import java.util.Map;

import org.eclipse.emf.common.util.URI;

/**
 * Resolves URI-valued attributes of configuration objects against the
 * location of the configuration document they came from.
 *
 * <p>
 * The bootstrap hands configuration objects to configurators as detached
 * copies without a resource, so a configurator cannot ask the object for its
 * location. Instead the bootstrap publishes it as the
 * {@link DataAtlasConstants#CONFIG_BASE_URI} service property; a configurator
 * of a custom input type passes its configuration object's service properties
 * here:
 * </p>
 *
 * <pre>
 * &#64;Reference(policy = ReferencePolicy.DYNAMIC, cardinality = ReferenceCardinality.MULTIPLE)
 * void bindInput(MyDataInput input, Map&lt;String, Object&gt; properties) {
 * 	String mappingFile = ConfigurationUris.resolve(input.getMappingFile(), properties);
 * 	...
 * }
 * </pre>
 */
public final class ConfigurationUris {

	private ConfigurationUris() {
	}

	/**
	 * Resolves a URI-valued configuration attribute against the configuration
	 * document's location.
	 *
	 * @param value      the attribute value, may be {@code null}
	 * @param properties the service properties of the configuration object
	 * @return {@code value} resolved against
	 *         {@link DataAtlasConstants#CONFIG_BASE_URI}; {@code value}
	 *         unchanged when it is {@code null}, already absolute, or no base is
	 *         known
	 */
	public static String resolve(String value, Map<String, ?> properties) {
		if (value == null || properties == null) {
			return value;
		}
		Object base = properties.get(DataAtlasConstants.CONFIG_BASE_URI);
		if (base == null) {
			return value;
		}
		URI uri = URI.createURI(value);
		if (!uri.isRelative()) {
			return value;
		}
		return uri.resolve(URI.createURI(base.toString())).toString();
	}
}
