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

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract definition of an Endpoint to publish DataSets.
 * As a DataProvider a DataService can also act standalone - serving its own dataInput generically in the formats given by its distributionExport - or it can contain refined DataSets (via its configurations).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataService#getUrlContext <em>Url Context</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataService()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface DataService extends DataProvider {
	/**
	 * Returns the value of the '<em><b>Url Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Base URL context (path prefix) under which this service is published.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Url Context</em>' attribute.
	 * @see #setUrlContext(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataService_UrlContext()
	 * @model required="true"
	 * @generated
	 */
	String getUrlContext();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataService#getUrlContext <em>Url Context</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Url Context</em>' attribute.
	 * @see #getUrlContext()
	 * @generated
	 */
	void setUrlContext(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Marker operation. Each configuration of a DataService must result in a DCAT Distribution.
	 * <!-- end-model-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	void getDistributions();

} // DataService
