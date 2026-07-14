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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.data.atlas.configuration.BridgeRepository;
import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.DataTransformation;
import org.eclipse.fennec.data.atlas.configuration.QueryTransformation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Bridge Repository</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.BridgeRepositoryImpl#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.BridgeRepositoryImpl#getDataTrafo <em>Data Trafo</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.BridgeRepositoryImpl#getQueryTrafo <em>Query Trafo</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.BridgeRepositoryImpl#getFilter <em>Filter</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BridgeRepositoryImpl extends DataInputImpl implements BridgeRepository {
	/**
	 * The cached value of the '{@link #getSource() <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected DataInput source;

	/**
	 * The cached value of the '{@link #getDataTrafo() <em>Data Trafo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataTrafo()
	 * @generated
	 * @ordered
	 */
	protected DataTransformation dataTrafo;

	/**
	 * The cached value of the '{@link #getQueryTrafo() <em>Query Trafo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQueryTrafo()
	 * @generated
	 * @ordered
	 */
	protected QueryTransformation queryTrafo;

	/**
	 * The cached value of the '{@link #getFilter() <em>Filter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFilter()
	 * @generated
	 * @ordered
	 */
	protected EObject filter;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BridgeRepositoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.BRIDGE_REPOSITORY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DataInput getSource() {
		if (source != null && source.eIsProxy()) {
			InternalEObject oldSource = (InternalEObject)source;
			source = (DataInput)eResolveProxy(oldSource);
			if (source != oldSource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.BRIDGE_REPOSITORY__SOURCE, oldSource, source));
			}
		}
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataInput basicGetSource() {
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSource(DataInput newSource) {
		DataInput oldSource = source;
		source = newSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.BRIDGE_REPOSITORY__SOURCE, oldSource, source));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DataTransformation getDataTrafo() {
		if (dataTrafo != null && dataTrafo.eIsProxy()) {
			InternalEObject oldDataTrafo = (InternalEObject)dataTrafo;
			dataTrafo = (DataTransformation)eResolveProxy(oldDataTrafo);
			if (dataTrafo != oldDataTrafo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.BRIDGE_REPOSITORY__DATA_TRAFO, oldDataTrafo, dataTrafo));
			}
		}
		return dataTrafo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataTransformation basicGetDataTrafo() {
		return dataTrafo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDataTrafo(DataTransformation newDataTrafo) {
		DataTransformation oldDataTrafo = dataTrafo;
		dataTrafo = newDataTrafo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.BRIDGE_REPOSITORY__DATA_TRAFO, oldDataTrafo, dataTrafo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public QueryTransformation getQueryTrafo() {
		if (queryTrafo != null && queryTrafo.eIsProxy()) {
			InternalEObject oldQueryTrafo = (InternalEObject)queryTrafo;
			queryTrafo = (QueryTransformation)eResolveProxy(oldQueryTrafo);
			if (queryTrafo != oldQueryTrafo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.BRIDGE_REPOSITORY__QUERY_TRAFO, oldQueryTrafo, queryTrafo));
			}
		}
		return queryTrafo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QueryTransformation basicGetQueryTrafo() {
		return queryTrafo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setQueryTrafo(QueryTransformation newQueryTrafo) {
		QueryTransformation oldQueryTrafo = queryTrafo;
		queryTrafo = newQueryTrafo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.BRIDGE_REPOSITORY__QUERY_TRAFO, oldQueryTrafo, queryTrafo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getFilter() {
		if (filter != null && filter.eIsProxy()) {
			InternalEObject oldFilter = (InternalEObject)filter;
			filter = eResolveProxy(oldFilter);
			if (filter != oldFilter) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.BRIDGE_REPOSITORY__FILTER, oldFilter, filter));
			}
		}
		return filter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetFilter() {
		return filter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFilter(EObject newFilter) {
		EObject oldFilter = filter;
		filter = newFilter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.BRIDGE_REPOSITORY__FILTER, oldFilter, filter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.BRIDGE_REPOSITORY__SOURCE:
				if (resolve) return getSource();
				return basicGetSource();
			case DAConfigPackage.BRIDGE_REPOSITORY__DATA_TRAFO:
				if (resolve) return getDataTrafo();
				return basicGetDataTrafo();
			case DAConfigPackage.BRIDGE_REPOSITORY__QUERY_TRAFO:
				if (resolve) return getQueryTrafo();
				return basicGetQueryTrafo();
			case DAConfigPackage.BRIDGE_REPOSITORY__FILTER:
				if (resolve) return getFilter();
				return basicGetFilter();
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
			case DAConfigPackage.BRIDGE_REPOSITORY__SOURCE:
				setSource((DataInput)newValue);
				return;
			case DAConfigPackage.BRIDGE_REPOSITORY__DATA_TRAFO:
				setDataTrafo((DataTransformation)newValue);
				return;
			case DAConfigPackage.BRIDGE_REPOSITORY__QUERY_TRAFO:
				setQueryTrafo((QueryTransformation)newValue);
				return;
			case DAConfigPackage.BRIDGE_REPOSITORY__FILTER:
				setFilter((EObject)newValue);
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
			case DAConfigPackage.BRIDGE_REPOSITORY__SOURCE:
				setSource((DataInput)null);
				return;
			case DAConfigPackage.BRIDGE_REPOSITORY__DATA_TRAFO:
				setDataTrafo((DataTransformation)null);
				return;
			case DAConfigPackage.BRIDGE_REPOSITORY__QUERY_TRAFO:
				setQueryTrafo((QueryTransformation)null);
				return;
			case DAConfigPackage.BRIDGE_REPOSITORY__FILTER:
				setFilter((EObject)null);
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
			case DAConfigPackage.BRIDGE_REPOSITORY__SOURCE:
				return source != null;
			case DAConfigPackage.BRIDGE_REPOSITORY__DATA_TRAFO:
				return dataTrafo != null;
			case DAConfigPackage.BRIDGE_REPOSITORY__QUERY_TRAFO:
				return queryTrafo != null;
			case DAConfigPackage.BRIDGE_REPOSITORY__FILTER:
				return filter != null;
		}
		return super.eIsSet(featureID);
	}

} //BridgeRepositoryImpl
