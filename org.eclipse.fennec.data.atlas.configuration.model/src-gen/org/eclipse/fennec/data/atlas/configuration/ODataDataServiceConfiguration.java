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

import java.math.BigInteger;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>OData Data Service Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Everything the OData runtime needs to serve one DataSet of an ODataDataService as an entity set: the DataSet reference, the entity set's name under the service root and the server-side $top ceiling. The entity type is the DataSet's outputType; it needs an identity (an iD attribute or the idFeatures annotation of the fennec persistence stack), and within one service every outputType is served by exactly one configuration - OData addresses entity sets by type. A DataSet with a query cannot be served as OData (the base predicate would have to be composed with $filter) and keeps the entity set down.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration#getEntitySetName <em>Entity Set Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration#getBatchSizeLimit <em>Batch Size Limit</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getODataDataServiceConfiguration()
 * @model
 * @generated
 */
@ProviderType
public interface ODataDataServiceConfiguration extends DataServiceConfiguration {
	/**
	 * Returns the value of the '<em><b>Entity Set Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The entity set name under the service root (GET {urlContext}/{entitySetName}). Defaults to the name of the DataSet's outputType EClass; must be unique within the service.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Entity Set Name</em>' attribute.
	 * @see #setEntitySetName(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getODataDataServiceConfiguration_EntitySetName()
	 * @model
	 * @generated
	 */
	String getEntitySetName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration#getEntitySetName <em>Entity Set Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity Set Name</em>' attribute.
	 * @see #getEntitySetName()
	 * @generated
	 */
	void setEntitySetName(String value);

	/**
	 * Returns the value of the '<em><b>Batch Size Limit</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Server-side $top ceiling (applies even without a client $top). -1 means the runtime default (1000). The OData server enforces the ceiling per service root, so the smallest positive limit declared by the service's configurations applies to all of its entity sets.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Batch Size Limit</em>' attribute.
	 * @see #setBatchSizeLimit(BigInteger)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getODataDataServiceConfiguration_BatchSizeLimit()
	 * @model default="-1" required="true"
	 * @generated
	 */
	BigInteger getBatchSizeLimit();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration#getBatchSizeLimit <em>Batch Size Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Batch Size Limit</em>' attribute.
	 * @see #getBatchSizeLimit()
	 * @generated
	 */
	void setBatchSizeLimit(BigInteger value);

} // ODataDataServiceConfiguration
