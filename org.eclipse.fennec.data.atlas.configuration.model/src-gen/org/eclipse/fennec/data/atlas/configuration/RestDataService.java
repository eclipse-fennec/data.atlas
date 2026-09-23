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

import org.eclipse.emf.common.util.EList;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rest Data Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * DataService definition for a REST endpoint over HTTP(s), serialized to several mimetypes like XML, CSV, JSON etc.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#isOpenAPI <em>Open API</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataService()
 * @model
 * @generated
 */
@ProviderType
public interface RestDataService extends DataService {
	/**
	 * Returns the value of the '<em><b>Configuration</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Definition of the DataSets that should be provided by this RestDataService.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Configuration</em>' containment reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataService_Configuration()
	 * @model containment="true" ordered="false"
	 *        annotation="http://www.eclipse.org/OCL/Collection nullFree='false'"
	 * @generated
	 */
	EList<RestDataServiceConfiguration> getConfiguration();

	/**
	 * Returns the value of the '<em><b>Open API</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Marker, if an openAPI definition should be provided
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Open API</em>' attribute.
	 * @see #setOpenAPI(boolean)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataService_OpenAPI()
	 * @model required="true"
	 *        annotation="https://eclipse.org/fennec/data/atlas/configuration/status implementation='not implemented yet' note='no OpenAPI document is generated; the value is ignored'"
	 * @generated
	 */
	boolean isOpenAPI();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#isOpenAPI <em>Open API</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Open API</em>' attribute.
	 * @see #isOpenAPI()
	 * @generated
	 */
	void setOpenAPI(boolean value);

} // RestDataService
