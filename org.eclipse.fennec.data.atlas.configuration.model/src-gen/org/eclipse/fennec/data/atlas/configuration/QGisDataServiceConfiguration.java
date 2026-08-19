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
 * A representation of the model object '<em><b>QGis Data Service Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Per-DataSet configuration for a QGisDataService; maps a DataSet to a QGis layer.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.QGisDataServiceConfiguration#getLayer <em>Layer</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getQGisDataServiceConfiguration()
 * @model
 * @generated
 */
@ProviderType
public interface QGisDataServiceConfiguration extends DataServiceConfiguration {
	/**
	 * Returns the value of the '<em><b>Layer</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * mapping for the QGis configuration
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Layer</em>' reference.
	 * @see #setLayer(EClass)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getQGisDataServiceConfiguration_Layer()
	 * @model required="true"
	 * @generated
	 */
	EClass getLayer();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.QGisDataServiceConfiguration#getLayer <em>Layer</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Layer</em>' reference.
	 * @see #getLayer()
	 * @generated
	 */
	void setLayer(EClass value);

} // QGisDataServiceConfiguration
