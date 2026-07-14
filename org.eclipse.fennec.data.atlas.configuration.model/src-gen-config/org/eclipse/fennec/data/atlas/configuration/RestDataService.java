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
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#getPaginationOffsetParameterName <em>Pagination Offset Parameter Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#getPaginationSizeParameterName <em>Pagination Size Parameter Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataService()
 * @model
 * @generated
 */
@ProviderType
public interface RestDataService extends DataService {
	/**
	 * Returns the value of the '<em><b>Configuration</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Definition of the DataSets that should be provided by this RestDataService.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Configuration</em>' reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataService_Configuration()
	 * @model ordered="false"
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

	/**
	 * Returns the value of the '<em><b>Pagination Offset Parameter Name</b></em>' attribute.
	 * The default value is <code>"offset"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * configurable parameter names for the start offset for pagination
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Pagination Offset Parameter Name</em>' attribute.
	 * @see #setPaginationOffsetParameterName(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataService_PaginationOffsetParameterName()
	 * @model default="offset" required="true"
	 * @generated
	 */
	String getPaginationOffsetParameterName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#getPaginationOffsetParameterName <em>Pagination Offset Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pagination Offset Parameter Name</em>' attribute.
	 * @see #getPaginationOffsetParameterName()
	 * @generated
	 */
	void setPaginationOffsetParameterName(String value);

	/**
	 * Returns the value of the '<em><b>Pagination Size Parameter Name</b></em>' attribute.
	 * The default value is <code>"limit"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * configurable parameter names for the batch limit for pagination
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Pagination Size Parameter Name</em>' attribute.
	 * @see #setPaginationSizeParameterName(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataService_PaginationSizeParameterName()
	 * @model default="limit" required="true"
	 * @generated
	 */
	String getPaginationSizeParameterName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#getPaginationSizeParameterName <em>Pagination Size Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pagination Size Parameter Name</em>' attribute.
	 * @see #getPaginationSizeParameterName()
	 * @generated
	 */
	void setPaginationSizeParameterName(String value);

} // RestDataService
