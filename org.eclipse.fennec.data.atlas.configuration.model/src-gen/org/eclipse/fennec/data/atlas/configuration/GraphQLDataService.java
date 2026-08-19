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
 * A representation of the model object '<em><b>Graph QL Data Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * DataService that exposes DataSets via a GraphQL endpoint.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.GraphQLDataService#getConfiguration <em>Configuration</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGraphQLDataService()
 * @model
 * @generated
 */
@ProviderType
public interface GraphQLDataService extends DataService {
	/**
	 * Returns the value of the '<em><b>Configuration</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.GraphQLDataServiceConfiguration}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The per-DataSet GraphQL configurations provided by this service.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Configuration</em>' reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGraphQLDataService_Configuration()
	 * @model ordered="false"
	 *        annotation="http://www.eclipse.org/OCL/Collection nullFree='false'"
	 * @generated
	 */
	EList<GraphQLDataServiceConfiguration> getConfiguration();

} // GraphQLDataService
