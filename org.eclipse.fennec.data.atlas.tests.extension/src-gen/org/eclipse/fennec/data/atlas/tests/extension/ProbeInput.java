/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 */
package org.eclipse.fennec.data.atlas.tests.extension;

import org.eclipse.fennec.atlas.configuration.DataInput;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.tests.extension.ProbeInput#getMappingFile <em>Mapping File</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.tests.extension.ProbePackage#getProbeInput()
 * @model
 * @generated
 */
@ProviderType
public interface ProbeInput extends DataInput {
	/**
	 * Returns the value of the '<em><b>Mapping File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mapping File</em>' attribute.
	 * @see #setMappingFile(String)
	 * @see org.eclipse.fennec.data.atlas.tests.extension.ProbePackage#getProbeInput_MappingFile()
	 * @model
	 * @generated
	 */
	String getMappingFile();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.tests.extension.ProbeInput#getMappingFile <em>Mapping File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mapping File</em>' attribute.
	 * @see #getMappingFile()
	 * @generated
	 */
	void setMappingFile(String value);

} // ProbeInput
