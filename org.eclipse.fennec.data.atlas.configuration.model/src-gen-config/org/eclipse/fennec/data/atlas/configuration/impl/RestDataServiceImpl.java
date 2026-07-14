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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

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
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceImpl#getPaginationOffsetParameterName <em>Pagination Offset Parameter Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceImpl#getPaginationSizeParameterName <em>Pagination Size Parameter Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RestDataServiceImpl extends DataServiceImpl implements RestDataService {
	/**
	 * The cached value of the '{@link #getConfiguration() <em>Configuration</em>}' reference list.
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
			configuration = new EObjectResolvingEList<RestDataServiceConfiguration>(RestDataServiceConfiguration.class, this, DAConfigPackage.REST_DATA_SERVICE__CONFIGURATION);
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
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.REST_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME, oldPaginationOffsetParameterName, paginationOffsetParameterName));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.REST_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME, oldPaginationSizeParameterName, paginationSizeParameterName));
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
			case DAConfigPackage.REST_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME:
				return getPaginationOffsetParameterName();
			case DAConfigPackage.REST_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME:
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
			case DAConfigPackage.REST_DATA_SERVICE__CONFIGURATION:
				getConfiguration().clear();
				getConfiguration().addAll((Collection<? extends RestDataServiceConfiguration>)newValue);
				return;
			case DAConfigPackage.REST_DATA_SERVICE__OPEN_API:
				setOpenAPI((Boolean)newValue);
				return;
			case DAConfigPackage.REST_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME:
				setPaginationOffsetParameterName((String)newValue);
				return;
			case DAConfigPackage.REST_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME:
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
			case DAConfigPackage.REST_DATA_SERVICE__CONFIGURATION:
				getConfiguration().clear();
				return;
			case DAConfigPackage.REST_DATA_SERVICE__OPEN_API:
				setOpenAPI(OPEN_API_EDEFAULT);
				return;
			case DAConfigPackage.REST_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME:
				setPaginationOffsetParameterName(PAGINATION_OFFSET_PARAMETER_NAME_EDEFAULT);
				return;
			case DAConfigPackage.REST_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME:
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
			case DAConfigPackage.REST_DATA_SERVICE__CONFIGURATION:
				return configuration != null && !configuration.isEmpty();
			case DAConfigPackage.REST_DATA_SERVICE__OPEN_API:
				return openAPI != OPEN_API_EDEFAULT;
			case DAConfigPackage.REST_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME:
				return PAGINATION_OFFSET_PARAMETER_NAME_EDEFAULT == null ? paginationOffsetParameterName != null : !PAGINATION_OFFSET_PARAMETER_NAME_EDEFAULT.equals(paginationOffsetParameterName);
			case DAConfigPackage.REST_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME:
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
		result.append(" (openAPI: ");
		result.append(openAPI);
		result.append(", paginationOffsetParameterName: ");
		result.append(paginationOffsetParameterName);
		result.append(", paginationSizeParameterName: ");
		result.append(paginationSizeParameterName);
		result.append(')');
		return result.toString();
	}

} //RestDataServiceImpl
