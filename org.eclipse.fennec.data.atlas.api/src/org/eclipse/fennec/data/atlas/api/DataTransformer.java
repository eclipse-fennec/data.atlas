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

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.osgi.annotation.versioning.ProviderType;

/**
 * A ready-to-execute data transformation: the runtime realization of one
 * {@code DataTransformation} configuration object, registered as an OSGi
 * service carrying {@link DataAtlasConstants#TRANSFORMATION_ID} = the
 * transformation's id.
 *
 * <p>
 * The Data Atlas transformation contract is <b>1:1</b>: one source object of
 * {@link #inputType()} maps to exactly one result object of
 * {@link #outputType()} carrying the same id, and the result list preserves
 * the source order. Implementations must be safe for concurrent calls.
 * </p>
 */
@ProviderType
public interface DataTransformer {

	/** The source EClass this transformer accepts. */
	EClass inputType();

	/** The result EClass this transformer produces. */
	EClass outputType();

	/**
	 * Transforms the given source objects (1:1, order-preserving). The
	 * returned objects are detached — the caller owns them.
	 *
	 * @param sourceObjects the source objects, instances of
	 *            {@link #inputType()}; never modified
	 * @return one result object per source object, in source order
	 * @throws TransformationException if the transformation fails or does not
	 *             produce exactly one result per source object
	 */
	List<EObject> transform(List<? extends EObject> sourceObjects) throws TransformationException;

	/** Failure of a {@link DataTransformer#transform(List)} execution. */
	class TransformationException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public TransformationException(String message) {
			super(message);
		}

		public TransformationException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
