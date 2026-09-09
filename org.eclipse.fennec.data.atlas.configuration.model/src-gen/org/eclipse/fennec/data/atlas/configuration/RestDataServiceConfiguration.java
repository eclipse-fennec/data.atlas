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
 * Everything the REST runtime needs to serve one DataSet of a RestDataService: its path under the service's urlContext, the paging defaults and bounds, and the names of the HTTP query parameters the paging is driven by. Nothing per-DataSet is configured on the service itself.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getPath <em>Path</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getBatchSize <em>Batch Size</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getBatchSizeLimit <em>Batch Size Limit</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getOffsetParameterName <em>Offset Parameter Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getLimitParameterName <em>Limit Parameter Name</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Offset Parameter Name</b></em>' attribute.
	 * The default value is <code>"offset"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the HTTP query parameter carrying the start offset for pagination of this DataSet.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Offset Parameter Name</em>' attribute.
	 * @see #setOffsetParameterName(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataServiceConfiguration_OffsetParameterName()
	 * @model default="offset" required="true"
	 * @generated
	 */
	String getOffsetParameterName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getOffsetParameterName <em>Offset Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Offset Parameter Name</em>' attribute.
	 * @see #getOffsetParameterName()
	 * @generated
	 */
	void setOffsetParameterName(String value);

	/**
	 * Returns the value of the '<em><b>Limit Parameter Name</b></em>' attribute.
	 * The default value is <code>"limit"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the HTTP query parameter carrying the page size for pagination of this DataSet.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Limit Parameter Name</em>' attribute.
	 * @see #setLimitParameterName(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getRestDataServiceConfiguration_LimitParameterName()
	 * @model default="limit" required="true"
	 * @generated
	 */
	String getLimitParameterName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getLimitParameterName <em>Limit Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Limit Parameter Name</em>' attribute.
	 * @see #getLimitParameterName()
	 * @generated
	 */
	void setLimitParameterName(String value);

} // RestDataServiceConfiguration
