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
import org.eclipse.fennec.data.atlas.configuration.GeoJsonDataService;
import org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geo Json Data Service</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceImpl#getConfiguration <em>Configuration</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceImpl#getPaginationOffsetParameterName <em>Pagination Offset Parameter Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceImpl#getPaginationSizeParameterName <em>Pagination Size Parameter Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GeoJsonDataServiceImpl extends DataServiceImpl implements GeoJsonDataService {
	/**
	 * The cached value of the '{@link #getConfiguration() <em>Configuration</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfiguration()
	 * @generated
	 * @ordered
	 */
	protected EList<GeoJsonDataServiceConfiguration> configuration;

	/**
	 * The default value of the '{@link #getPaginationOffsetParameterName() <em>Pagination Offset Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaginationOffsetParameterName()
	 * @generated
	 * @ordered
	 */
	protected static final String PAGINATION_OFFSET_PARAMETER_NAME_EDEFAULT = "offset";

	/**
	 * The cached value of the '{@link #getPaginationOffsetParameterName() <em>Pagination Offset Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaginationOffsetParameterName()
	 * @generated
	 * @ordered
	 */
	protected String paginationOffsetParameterName = PAGINATION_OFFSET_PARAMETER_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPaginationSizeParameterName() <em>Pagination Size Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaginationSizeParameterName()
	 * @generated
	 * @ordered
	 */
	protected static final String PAGINATION_SIZE_PARAMETER_NAME_EDEFAULT = "limit";

	/**
	 * The cached value of the '{@link #getPaginationSizeParameterName() <em>Pagination Size Parameter Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaginationSizeParameterName()
	 * @generated
	 * @ordered
	 */
	protected String paginationSizeParameterName = PAGINATION_SIZE_PARAMETER_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeoJsonDataServiceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.GEO_JSON_DATA_SERVICE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<GeoJsonDataServiceConfiguration> getConfiguration() {
		if (configuration == null) {
			configuration = new EObjectContainmentEList<GeoJsonDataServiceConfiguration>(GeoJsonDataServiceConfiguration.class, this, DAConfigPackage.GEO_JSON_DATA_SERVICE__CONFIGURATION);
		}
		return configuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPaginationOffsetParameterName() {
		return paginationOffsetParameterName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPaginationOffsetParameterName(String newPaginationOffsetParameterName) {
		String oldPaginationOffsetParameterName = paginationOffsetParameterName;
		paginationOffsetParameterName = newPaginationOffsetParameterName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.GEO_JSON_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME, oldPaginationOffsetParameterName, paginationOffsetParameterName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPaginationSizeParameterName() {
		return paginationSizeParameterName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPaginationSizeParameterName(String newPaginationSizeParameterName) {
		String oldPaginationSizeParameterName = paginationSizeParameterName;
		paginationSizeParameterName = newPaginationSizeParameterName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.GEO_JSON_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME, oldPaginationSizeParameterName, paginationSizeParameterName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__CONFIGURATION:
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
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__CONFIGURATION:
				return getConfiguration();
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME:
				return getPaginationOffsetParameterName();
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME:
				return getPaginationSizeParameterName();
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
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__CONFIGURATION:
				getConfiguration().clear();
				getConfiguration().addAll((Collection<? extends GeoJsonDataServiceConfiguration>)newValue);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME:
				setPaginationOffsetParameterName((String)newValue);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME:
				setPaginationSizeParameterName((String)newValue);
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
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__CONFIGURATION:
				getConfiguration().clear();
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME:
				setPaginationOffsetParameterName(PAGINATION_OFFSET_PARAMETER_NAME_EDEFAULT);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME:
				setPaginationSizeParameterName(PAGINATION_SIZE_PARAMETER_NAME_EDEFAULT);
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
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__CONFIGURATION:
				return configuration != null && !configuration.isEmpty();
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME:
				return PAGINATION_OFFSET_PARAMETER_NAME_EDEFAULT == null ? paginationOffsetParameterName != null : !PAGINATION_OFFSET_PARAMETER_NAME_EDEFAULT.equals(paginationOffsetParameterName);
			case DAConfigPackage.GEO_JSON_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME:
				return PAGINATION_SIZE_PARAMETER_NAME_EDEFAULT == null ? paginationSizeParameterName != null : !PAGINATION_SIZE_PARAMETER_NAME_EDEFAULT.equals(paginationSizeParameterName);
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
		result.append(" (paginationOffsetParameterName: ");
		result.append(paginationOffsetParameterName);
		result.append(", paginationSizeParameterName: ");
		result.append(paginationSizeParameterName);
		result.append(')');
		return result.toString();
	}

} //GeoJsonDataServiceImpl
