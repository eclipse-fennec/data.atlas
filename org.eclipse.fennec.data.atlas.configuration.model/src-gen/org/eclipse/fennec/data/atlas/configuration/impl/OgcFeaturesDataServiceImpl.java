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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ogc Features Data Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceImpl#getDefaultLimit <em>Default Limit</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceImpl#getMaxLimit <em>Max Limit</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceImpl#getConfiguration <em>Configuration</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OgcFeaturesDataServiceImpl extends DataServiceImpl implements OgcFeaturesDataService {
	/**
	 * The default value of the '{@link #getDefaultLimit() <em>Default Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultLimit()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger DEFAULT_LIMIT_EDEFAULT = new BigInteger("-1");

	/**
	 * The cached value of the '{@link #getDefaultLimit() <em>Default Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultLimit()
	 * @generated
	 * @ordered
	 */
	protected BigInteger defaultLimit = DEFAULT_LIMIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxLimit() <em>Max Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxLimit()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger MAX_LIMIT_EDEFAULT = new BigInteger("-1");

	/**
	 * The cached value of the '{@link #getMaxLimit() <em>Max Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxLimit()
	 * @generated
	 * @ordered
	 */
	protected BigInteger maxLimit = MAX_LIMIT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getConfiguration() <em>Configuration</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfiguration()
	 * @generated
	 * @ordered
	 */
	protected EList<OgcFeaturesDataServiceConfiguration> configuration;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OgcFeaturesDataServiceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.OGC_FEATURES_DATA_SERVICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BigInteger getDefaultLimit() {
		return defaultLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultLimit(BigInteger newDefaultLimit) {
		BigInteger oldDefaultLimit = defaultLimit;
		defaultLimit = newDefaultLimit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.OGC_FEATURES_DATA_SERVICE__DEFAULT_LIMIT, oldDefaultLimit, defaultLimit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BigInteger getMaxLimit() {
		return maxLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMaxLimit(BigInteger newMaxLimit) {
		BigInteger oldMaxLimit = maxLimit;
		maxLimit = newMaxLimit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.OGC_FEATURES_DATA_SERVICE__MAX_LIMIT, oldMaxLimit, maxLimit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OgcFeaturesDataServiceConfiguration> getConfiguration() {
		if (configuration == null) {
			configuration = new EObjectContainmentEList<OgcFeaturesDataServiceConfiguration>(OgcFeaturesDataServiceConfiguration.class, this, DAConfigPackage.OGC_FEATURES_DATA_SERVICE__CONFIGURATION);
		}
		return configuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__CONFIGURATION:
				return ((InternalEList<?>)getConfiguration()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__DEFAULT_LIMIT:
				return getDefaultLimit();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__MAX_LIMIT:
				return getMaxLimit();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__CONFIGURATION:
				return getConfiguration();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__DEFAULT_LIMIT:
				setDefaultLimit((BigInteger)newValue);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__MAX_LIMIT:
				setMaxLimit((BigInteger)newValue);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__CONFIGURATION:
				getConfiguration().clear();
				getConfiguration().addAll((Collection<? extends OgcFeaturesDataServiceConfiguration>)newValue);
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
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__DEFAULT_LIMIT:
				setDefaultLimit(DEFAULT_LIMIT_EDEFAULT);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__MAX_LIMIT:
				setMaxLimit(MAX_LIMIT_EDEFAULT);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__CONFIGURATION:
				getConfiguration().clear();
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
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__DEFAULT_LIMIT:
				return DEFAULT_LIMIT_EDEFAULT == null ? defaultLimit != null : !DEFAULT_LIMIT_EDEFAULT.equals(defaultLimit);
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__MAX_LIMIT:
				return MAX_LIMIT_EDEFAULT == null ? maxLimit != null : !MAX_LIMIT_EDEFAULT.equals(maxLimit);
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE__CONFIGURATION:
				return configuration != null && !configuration.isEmpty();
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
		result.append(" (defaultLimit: ");
		result.append(defaultLimit);
		result.append(", maxLimit: ");
		result.append(maxLimit);
		result.append(')');
		return result.toString();
	}

} //OgcFeaturesDataServiceImpl
