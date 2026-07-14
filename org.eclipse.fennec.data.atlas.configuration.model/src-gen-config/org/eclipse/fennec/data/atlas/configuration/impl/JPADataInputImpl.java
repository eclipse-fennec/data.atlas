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

import emfmapping.EntityMappingsType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.JPADataInput;
import org.eclipse.fennec.data.atlas.configuration.JdbcDataSource;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>JPA Data Input</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.JPADataInputImpl#getPersistenceConfig <em>Persistence Config</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.JPADataInputImpl#getDataSource <em>Data Source</em>}</li>
 * </ul>
 *
 * @generated
 */
public class JPADataInputImpl extends DataInputImpl implements JPADataInput {
	/**
	 * The cached value of the '{@link #getPersistenceConfig() <em>Persistence Config</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPersistenceConfig()
	 * @generated
	 * @ordered
	 */
	protected EntityMappingsType persistenceConfig;

	/**
	 * The cached value of the '{@link #getDataSource() <em>Data Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataSource()
	 * @generated
	 * @ordered
	 */
	protected JdbcDataSource dataSource;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JPADataInputImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.JPA_DATA_INPUT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EntityMappingsType getPersistenceConfig() {
		if (persistenceConfig != null && persistenceConfig.eIsProxy()) {
			InternalEObject oldPersistenceConfig = (InternalEObject)persistenceConfig;
			persistenceConfig = (EntityMappingsType)eResolveProxy(oldPersistenceConfig);
			if (persistenceConfig != oldPersistenceConfig) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.JPA_DATA_INPUT__PERSISTENCE_CONFIG, oldPersistenceConfig, persistenceConfig));
			}
		}
		return persistenceConfig;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EntityMappingsType basicGetPersistenceConfig() {
		return persistenceConfig;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPersistenceConfig(EntityMappingsType newPersistenceConfig) {
		EntityMappingsType oldPersistenceConfig = persistenceConfig;
		persistenceConfig = newPersistenceConfig;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.JPA_DATA_INPUT__PERSISTENCE_CONFIG, oldPersistenceConfig, persistenceConfig));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public JdbcDataSource getDataSource() {
		if (dataSource != null && dataSource.eIsProxy()) {
			InternalEObject oldDataSource = (InternalEObject)dataSource;
			dataSource = (JdbcDataSource)eResolveProxy(oldDataSource);
			if (dataSource != oldDataSource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.JPA_DATA_INPUT__DATA_SOURCE, oldDataSource, dataSource));
			}
		}
		return dataSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JdbcDataSource basicGetDataSource() {
		return dataSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDataSource(JdbcDataSource newDataSource) {
		JdbcDataSource oldDataSource = dataSource;
		dataSource = newDataSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.JPA_DATA_INPUT__DATA_SOURCE, oldDataSource, dataSource));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.JPA_DATA_INPUT__PERSISTENCE_CONFIG:
				if (resolve) return getPersistenceConfig();
				return basicGetPersistenceConfig();
			case DAConfigPackage.JPA_DATA_INPUT__DATA_SOURCE:
				if (resolve) return getDataSource();
				return basicGetDataSource();
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
			case DAConfigPackage.JPA_DATA_INPUT__PERSISTENCE_CONFIG:
				setPersistenceConfig((EntityMappingsType)newValue);
				return;
			case DAConfigPackage.JPA_DATA_INPUT__DATA_SOURCE:
				setDataSource((JdbcDataSource)newValue);
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
			case DAConfigPackage.JPA_DATA_INPUT__PERSISTENCE_CONFIG:
				setPersistenceConfig((EntityMappingsType)null);
				return;
			case DAConfigPackage.JPA_DATA_INPUT__DATA_SOURCE:
				setDataSource((JdbcDataSource)null);
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
			case DAConfigPackage.JPA_DATA_INPUT__PERSISTENCE_CONFIG:
				return persistenceConfig != null;
			case DAConfigPackage.JPA_DATA_INPUT__DATA_SOURCE:
				return dataSource != null;
		}
		return super.eIsSet(featureID);
	}

} //JPADataInputImpl
