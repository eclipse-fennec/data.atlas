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

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.MongoDataSource;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mongo Data Source</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.MongoDataSourceImpl#getAuthSource <em>Auth Source</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.MongoDataSourceImpl#getFlavor <em>Flavor</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MongoDataSourceImpl extends DatabaseDataSourceImpl implements MongoDataSource {
	/**
	 * The default value of the '{@link #getAuthSource() <em>Auth Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthSource()
	 * @generated
	 * @ordered
	 */
	protected static final String AUTH_SOURCE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAuthSource() <em>Auth Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAuthSource()
	 * @generated
	 * @ordered
	 */
	protected String authSource = AUTH_SOURCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getFlavor() <em>Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlavor()
	 * @generated
	 * @ordered
	 */
	protected static final String FLAVOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFlavor() <em>Flavor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFlavor()
	 * @generated
	 * @ordered
	 */
	protected String flavor = FLAVOR_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MongoDataSourceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.MONGO_DATA_SOURCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getAuthSource() {
		return authSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAuthSource(String newAuthSource) {
		String oldAuthSource = authSource;
		authSource = newAuthSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.MONGO_DATA_SOURCE__AUTH_SOURCE, oldAuthSource, authSource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getFlavor() {
		return flavor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFlavor(String newFlavor) {
		String oldFlavor = flavor;
		flavor = newFlavor;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.MONGO_DATA_SOURCE__FLAVOR, oldFlavor, flavor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.MONGO_DATA_SOURCE__AUTH_SOURCE:
				return getAuthSource();
			case DAConfigPackage.MONGO_DATA_SOURCE__FLAVOR:
				return getFlavor();
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
			case DAConfigPackage.MONGO_DATA_SOURCE__AUTH_SOURCE:
				setAuthSource((String)newValue);
				return;
			case DAConfigPackage.MONGO_DATA_SOURCE__FLAVOR:
				setFlavor((String)newValue);
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
			case DAConfigPackage.MONGO_DATA_SOURCE__AUTH_SOURCE:
				setAuthSource(AUTH_SOURCE_EDEFAULT);
				return;
			case DAConfigPackage.MONGO_DATA_SOURCE__FLAVOR:
				setFlavor(FLAVOR_EDEFAULT);
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
			case DAConfigPackage.MONGO_DATA_SOURCE__AUTH_SOURCE:
				return AUTH_SOURCE_EDEFAULT == null ? authSource != null : !AUTH_SOURCE_EDEFAULT.equals(authSource);
			case DAConfigPackage.MONGO_DATA_SOURCE__FLAVOR:
				return FLAVOR_EDEFAULT == null ? flavor != null : !FLAVOR_EDEFAULT.equals(flavor);
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
		result.append(" (authSource: ");
		result.append(authSource);
		result.append(", flavor: ");
		result.append(flavor);
		result.append(')');
		return result.toString();
	}

} //MongoDataSourceImpl
