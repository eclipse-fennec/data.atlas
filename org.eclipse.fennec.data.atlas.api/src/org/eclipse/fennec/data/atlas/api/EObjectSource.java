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

import org.eclipse.emf.ecore.resource.Resource;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * Runtime counterpart of a configured {@code DataInput}: supplies the EObjects
 * of that input on demand.
 *
 * <p>
 * An input configurator (e.g. the file input bundle) registers one
 * {@code EObjectSource} service per {@code DataInput} configuration object,
 * carrying {@link DataAtlasConstants#INPUT_ID} so endpoint configurators can
 * correlate it with the {@code DataInput} referenced from their service
 * configuration.
 * </p>
 */
@ConsumerType
public interface EObjectSource {

	/**
	 * Loads the objects of this source into a fresh, caller-owned
	 * {@link Resource}. Each call produces an independent copy of the data;
	 * the caller may freely modify, detach or discard the returned resource
	 * and its contents.
	 *
	 * @return a fresh {@link Resource} holding the objects of this source;
	 *         never {@code null}
	 */
	Resource loadContents();
}
