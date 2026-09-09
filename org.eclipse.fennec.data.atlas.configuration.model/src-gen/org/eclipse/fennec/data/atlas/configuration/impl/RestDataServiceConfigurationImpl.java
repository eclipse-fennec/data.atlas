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
import org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rest Data Service Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceConfigurationImpl#getPath <em>Path</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceConfigurationImpl#getBatchSize <em>Batch Size</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceConfigurationImpl#getBatchSizeLimit <em>Batch Size Limit</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceConfigurationImpl#getOffsetParameterName <em>Offset Parameter Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceConfigurationImpl#getLimitParameterName <em>Limit Parameter Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RestDataServiceConfigurationImpl extends DataServiceConfigurationImpl implements RestDataServiceConfiguration {
	/**
	 * The default value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected static final String PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected String path = PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #getBatchSize() <em>Batch Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchSize()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger BATCH_SIZE_EDEFAULT = new BigInteger("-1");

	/**
	 * The cached value of the '{@link #getBatchSize() <em>Batch Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchSize()
	 * @generated
	 * @ordered
	 */
	protected BigInteger batchSize = BATCH_SIZE_EDEFAULT;

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
	 * The default value of the '{@link #getOffsetParameterName() <em>Offset Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOffsetParameterName()
	 * @generated
	 * @ordered
	 */
	protected static final String OFFSET_PARAMETER_NAME_EDEFAULT = "offset";

	/**
	 * The cached value of the '{@link #getOffsetParameterName() <em>Offset Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOffsetParameterName()
	 * @generated
	 * @ordered
	 */
	protected String offsetParameterName = OFFSET_PARAMETER_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getLimitParameterName() <em>Limit Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLimitParameterName()
	 * @generated
	 * @ordered
	 */
	protected static final String LIMIT_PARAMETER_NAME_EDEFAULT = "limit";

	/**
	 * The cached value of the '{@link #getLimitParameterName() <em>Limit Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLimitParameterName()
	 * @generated
	 * @ordered
	 */
	protected String limitParameterName = LIMIT_PARAMETER_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RestDataServiceConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.REST_DATA_SERVICE_CONFIGURATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPath() {
		return path;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPath(String newPath) {
		String oldPath = path;
		path = newPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__PATH, oldPath, path));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BigInteger getBatchSize() {
		return batchSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBatchSize(BigInteger newBatchSize) {
		BigInteger oldBatchSize = batchSize;
		batchSize = newBatchSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE, oldBatchSize, batchSize));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT, oldBatchSizeLimit, batchSizeLimit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getOffsetParameterName() {
		return offsetParameterName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOffsetParameterName(String newOffsetParameterName) {
		String oldOffsetParameterName = offsetParameterName;
		offsetParameterName = newOffsetParameterName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__OFFSET_PARAMETER_NAME, oldOffsetParameterName, offsetParameterName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLimitParameterName() {
		return limitParameterName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLimitParameterName(String newLimitParameterName) {
		String oldLimitParameterName = limitParameterName;
		limitParameterName = newLimitParameterName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__LIMIT_PARAMETER_NAME, oldLimitParameterName, limitParameterName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__PATH:
				return getPath();
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE:
				return getBatchSize();
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				return getBatchSizeLimit();
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__OFFSET_PARAMETER_NAME:
				return getOffsetParameterName();
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__LIMIT_PARAMETER_NAME:
				return getLimitParameterName();
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
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__PATH:
				setPath((String)newValue);
				return;
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE:
				setBatchSize((BigInteger)newValue);
				return;
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				setBatchSizeLimit((BigInteger)newValue);
				return;
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__OFFSET_PARAMETER_NAME:
				setOffsetParameterName((String)newValue);
				return;
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__LIMIT_PARAMETER_NAME:
				setLimitParameterName((String)newValue);
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
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__PATH:
				setPath(PATH_EDEFAULT);
				return;
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE:
				setBatchSize(BATCH_SIZE_EDEFAULT);
				return;
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				setBatchSizeLimit(BATCH_SIZE_LIMIT_EDEFAULT);
				return;
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__OFFSET_PARAMETER_NAME:
				setOffsetParameterName(OFFSET_PARAMETER_NAME_EDEFAULT);
				return;
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__LIMIT_PARAMETER_NAME:
				setLimitParameterName(LIMIT_PARAMETER_NAME_EDEFAULT);
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
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__PATH:
				return PATH_EDEFAULT == null ? path != null : !PATH_EDEFAULT.equals(path);
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE:
				return BATCH_SIZE_EDEFAULT == null ? batchSize != null : !BATCH_SIZE_EDEFAULT.equals(batchSize);
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				return BATCH_SIZE_LIMIT_EDEFAULT == null ? batchSizeLimit != null : !BATCH_SIZE_LIMIT_EDEFAULT.equals(batchSizeLimit);
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__OFFSET_PARAMETER_NAME:
				return OFFSET_PARAMETER_NAME_EDEFAULT == null ? offsetParameterName != null : !OFFSET_PARAMETER_NAME_EDEFAULT.equals(offsetParameterName);
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION__LIMIT_PARAMETER_NAME:
				return LIMIT_PARAMETER_NAME_EDEFAULT == null ? limitParameterName != null : !LIMIT_PARAMETER_NAME_EDEFAULT.equals(limitParameterName);
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
		result.append(" (path: ");
		result.append(path);
		result.append(", batchSize: ");
		result.append(batchSize);
		result.append(", batchSizeLimit: ");
		result.append(batchSizeLimit);
		result.append(", offsetParameterName: ");
		result.append(offsetParameterName);
		result.append(", limitParameterName: ");
		result.append(limitParameterName);
		result.append(')');
		return result.toString();
	}

} //RestDataServiceConfigurationImpl
