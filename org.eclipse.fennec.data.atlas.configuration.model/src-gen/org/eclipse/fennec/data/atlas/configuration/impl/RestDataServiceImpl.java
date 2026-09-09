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
import org.eclipse.fennec.data.atlas.configuration.RestDataService;
import org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Rest Data Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceImpl#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceImpl#isOpenAPI <em>Open API</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RestDataServiceImpl extends DataServiceImpl implements RestDataService {
	/**
	 * The cached value of the '{@link #getConfiguration() <em>Configuration</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfiguration()
	 * @generated
	 * @ordered
	 */
	protected EList<RestDataServiceConfiguration> configuration;

	/**
	 * The default value of the '{@link #isOpenAPI() <em>Open API</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOpenAPI()
	 * @generated
	 * @ordered
	 */
	protected static final boolean OPEN_API_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isOpenAPI() <em>Open API</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isOpenAPI()
	 * @generated
	 * @ordered
	 */
	protected boolean openAPI = OPEN_API_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RestDataServiceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.REST_DATA_SERVICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<RestDataServiceConfiguration> getConfiguration() {
		if (configuration == null) {
			configuration = new EObjectContainmentEList<RestDataServiceConfiguration>(RestDataServiceConfiguration.class, this, DAConfigPackage.REST_DATA_SERVICE__CONFIGURATION);
		}
		return configuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isOpenAPI() {
		return openAPI;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOpenAPI(boolean newOpenAPI) {
		boolean oldOpenAPI = openAPI;
		openAPI = newOpenAPI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.REST_DATA_SERVICE__OPEN_API, oldOpenAPI, openAPI));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DAConfigPackage.REST_DATA_SERVICE__CONFIGURATION:
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
			case DAConfigPackage.REST_DATA_SERVICE__CONFIGURATION:
				return getConfiguration();
			case DAConfigPackage.REST_DATA_SERVICE__OPEN_API:
				return isOpenAPI();
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
			case DAConfigPackage.REST_DATA_SERVICE__CONFIGURATION:
				getConfiguration().clear();
				getConfiguration().addAll((Collection<? extends RestDataServiceConfiguration>)newValue);
				return;
			case DAConfigPackage.REST_DATA_SERVICE__OPEN_API:
				setOpenAPI((Boolean)newValue);
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
			case DAConfigPackage.REST_DATA_SERVICE__CONFIGURATION:
				getConfiguration().clear();
				return;
			case DAConfigPackage.REST_DATA_SERVICE__OPEN_API:
				setOpenAPI(OPEN_API_EDEFAULT);
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
			case DAConfigPackage.REST_DATA_SERVICE__CONFIGURATION:
				return configuration != null && !configuration.isEmpty();
			case DAConfigPackage.REST_DATA_SERVICE__OPEN_API:
				return openAPI != OPEN_API_EDEFAULT;
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
		result.append(" (openAPI: ");
		result.append(openAPI);
		result.append(')');
		return result.toString();
	}

} //RestDataServiceImpl
