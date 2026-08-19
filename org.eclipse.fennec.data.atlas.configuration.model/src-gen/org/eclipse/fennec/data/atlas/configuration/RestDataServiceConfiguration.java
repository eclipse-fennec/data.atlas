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
 * A representation of the model object '<em><b>Rest Data Service Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A set of Configuration parameters for a specific DataSet
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getPath <em>Path</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getBatchSize <em>Batch Size</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getBatchSizeLimit <em>Batch Size Limit</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataServiceConfiguration()
 * @model
 * @generated
 */
@ProviderType
public interface RestDataServiceConfiguration extends DataServiceConfiguration {
	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * derives by default from the Dataset name
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataServiceConfiguration_Path()
	 * @model required="true"
	 * @generated
	 */
	String getPath();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getPath <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
	void setPath(String value);

	/**
	 * Returns the value of the '<em><b>Batch Size</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * the page size for pagination. -1 means no pagination
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Batch Size</em>' attribute.
	 * @see #setBatchSize(BigInteger)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataServiceConfiguration_BatchSize()
	 * @model default="-1" required="true"
	 * @generated
	 */
	BigInteger getBatchSize();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getBatchSize <em>Batch Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Batch Size</em>' attribute.
	 * @see #getBatchSize()
	 * @generated
	 */
	void setBatchSize(BigInteger value);

	/**
	 * Returns the value of the '<em><b>Batch Size Limit</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * the page size limit for pagination, so the server will not be overtaxed
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Batch Size Limit</em>' attribute.
	 * @see #setBatchSizeLimit(BigInteger)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataServiceConfiguration_BatchSizeLimit()
	 * @model default="-1" required="true"
	 * @generated
	 */
	BigInteger getBatchSizeLimit();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getBatchSizeLimit <em>Batch Size Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Batch Size Limit</em>' attribute.
	 * @see #getBatchSizeLimit()
	 * @generated
	 */
	void setBatchSizeLimit(BigInteger value);

} // RestDataServiceConfiguration
