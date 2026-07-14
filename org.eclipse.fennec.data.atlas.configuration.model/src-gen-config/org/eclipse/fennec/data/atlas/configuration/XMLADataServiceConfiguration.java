/*
 * ******************************************************************
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
 * ******************************************************************
 */
package org.eclipse.fennec.data.atlas.configuration;

import org.eclipse.emf.ecore.EClass;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>XMLA Data Service Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Per-DataSet configuration for an XMLADataService.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration#getMapping <em>Mapping</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getXMLADataServiceConfiguration()
 * @model
 * @generated
 */
@ProviderType
public interface XMLADataServiceConfiguration extends DataServiceConfiguration {
	/**
	 * Returns the value of the '<em><b>Mapping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * TODO Mapping to the XMLA Mapping description
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Mapping</em>' reference.
	 * @see #setMapping(EClass)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getXMLADataServiceConfiguration_Mapping()
	 * @model required="true"
	 * @generated
	 */
	EClass getMapping();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration#getMapping <em>Mapping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mapping</em>' reference.
	 * @see #getMapping()
	 * @generated
	 */
	void setMapping(EClass value);

} // XMLADataServiceConfiguration
