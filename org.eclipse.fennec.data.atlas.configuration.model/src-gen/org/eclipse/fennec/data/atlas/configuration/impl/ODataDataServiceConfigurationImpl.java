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
package org.eclipse.fennec.data.atlas.configuration.impl;

import java.math.BigInteger;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>OData Data Service Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.ODataDataServiceConfigurationImpl#getEntitySetName <em>Entity Set Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.ODataDataServiceConfigurationImpl#getBatchSizeLimit <em>Batch Size Limit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ODataDataServiceConfigurationImpl extends DataServiceConfigurationImpl implements ODataDataServiceConfiguration {
	/**
	 * The default value of the '{@link #getEntitySetName() <em>Entity Set Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntitySetName()
	 * @generated
	 * @ordered
	 */
	protected static final String ENTITY_SET_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEntitySetName() <em>Entity Set Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntitySetName()
	 * @generated
	 * @ordered
	 */
	protected String entitySetName = ENTITY_SET_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getBatchSizeLimit() <em>Batch Size Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchSizeLimit()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger BATCH_SIZE_LIMIT_EDEFAULT = new BigInteger("-1");

	/**
	 * The cached value of the '{@link #getBatchSizeLimit() <em>Batch Size Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchSizeLimit()
	 * @generated
	 * @ordered
	 */
	protected BigInteger batchSizeLimit = BATCH_SIZE_LIMIT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ODataDataServiceConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.ODATA_DATA_SERVICE_CONFIGURATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getEntitySetName() {
		return entitySetName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEntitySetName(String newEntitySetName) {
		String oldEntitySetName = entitySetName;
		entitySetName = newEntitySetName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION__ENTITY_SET_NAME, oldEntitySetName, entitySetName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BigInteger getBatchSizeLimit() {
		return batchSizeLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBatchSizeLimit(BigInteger newBatchSizeLimit) {
		BigInteger oldBatchSizeLimit = batchSizeLimit;
		batchSizeLimit = newBatchSizeLimit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT, oldBatchSizeLimit, batchSizeLimit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION__ENTITY_SET_NAME:
				return getEntitySetName();
			case DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				return getBatchSizeLimit();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION__ENTITY_SET_NAME:
				setEntitySetName((String)newValue);
				return;
			case DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				setBatchSizeLimit((BigInteger)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION__ENTITY_SET_NAME:
				setEntitySetName(ENTITY_SET_NAME_EDEFAULT);
				return;
			case DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				setBatchSizeLimit(BATCH_SIZE_LIMIT_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION__ENTITY_SET_NAME:
				return ENTITY_SET_NAME_EDEFAULT == null ? entitySetName != null : !ENTITY_SET_NAME_EDEFAULT.equals(entitySetName);
			case DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				return BATCH_SIZE_LIMIT_EDEFAULT == null ? batchSizeLimit != null : !BATCH_SIZE_LIMIT_EDEFAULT.equals(batchSizeLimit);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (entitySetName: ");
		result.append(entitySetName);
		result.append(", batchSizeLimit: ");
		result.append(batchSizeLimit);
		result.append(')');
		return result.toString();
	}

} //ODataDataServiceConfigurationImpl
