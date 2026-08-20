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

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DataSet;

import org.eclipse.fennec.model.query.Query;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataSetImpl#getInputType <em>Input Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataSetImpl#getOutputType <em>Output Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataSetImpl#getQuery <em>Query</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataSetImpl#getChildDataSet <em>Child Data Set</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataSetImpl#getParentDataSet <em>Parent Data Set</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DataSetImpl extends DataProviderImpl implements DataSet {
	/**
	 * The cached value of the '{@link #getInputType() <em>Input Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputType()
	 * @generated
	 * @ordered
	 */
	protected EClass inputType;

	/**
	 * The cached value of the '{@link #getOutputType() <em>Output Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputType()
	 * @generated
	 * @ordered
	 */
	protected EClass outputType;

	/**
	 * The cached value of the '{@link #getQuery() <em>Query</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuery()
	 * @generated
	 * @ordered
	 */
	protected Query query;

	/**
	 * The cached value of the '{@link #getChildDataSet() <em>Child Data Set</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildDataSet()
	 * @generated
	 * @ordered
	 */
	protected EList<DataSet> childDataSet;

	/**
	 * The cached value of the '{@link #getParentDataSet() <em>Parent Data Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParentDataSet()
	 * @generated
	 * @ordered
	 */
	protected DataSet parentDataSet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.DATA_SET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getInputType() {
		if (inputType != null && inputType.eIsProxy()) {
			InternalEObject oldInputType = (InternalEObject)inputType;
			inputType = (EClass)eResolveProxy(oldInputType);
			if (inputType != oldInputType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.DATA_SET__INPUT_TYPE, oldInputType, inputType));
			}
		}
		return inputType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass basicGetInputType() {
		return inputType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setInputType(EClass newInputType) {
		EClass oldInputType = inputType;
		inputType = newInputType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_SET__INPUT_TYPE, oldInputType, inputType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOutputType() {
		if (outputType != null && outputType.eIsProxy()) {
			InternalEObject oldOutputType = (InternalEObject)outputType;
			outputType = (EClass)eResolveProxy(oldOutputType);
			if (outputType != oldOutputType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.DATA_SET__OUTPUT_TYPE, oldOutputType, outputType));
			}
		}
		return outputType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass basicGetOutputType() {
		return outputType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOutputType(EClass newOutputType) {
		EClass oldOutputType = outputType;
		outputType = newOutputType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_SET__OUTPUT_TYPE, oldOutputType, outputType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Query getQuery() {
		return query;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetQuery(Query newQuery, NotificationChain msgs) {
		Query oldQuery = query;
		query = newQuery;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_SET__QUERY, oldQuery, newQuery);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setQuery(Query newQuery) {
		if (newQuery != query) {
			NotificationChain msgs = null;
			if (query != null)
				msgs = ((InternalEObject)query).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - DAConfigPackage.DATA_SET__QUERY, null, msgs);
			if (newQuery != null)
				msgs = ((InternalEObject)newQuery).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - DAConfigPackage.DATA_SET__QUERY, null, msgs);
			msgs = basicSetQuery(newQuery, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_SET__QUERY, newQuery, newQuery));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DataSet> getChildDataSet() {
		if (childDataSet == null) {
			childDataSet = new EObjectWithInverseResolvingEList<DataSet>(DataSet.class, this, DAConfigPackage.DATA_SET__CHILD_DATA_SET, DAConfigPackage.DATA_SET__PARENT_DATA_SET);
		}
		return childDataSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DataSet getParentDataSet() {
		if (parentDataSet != null && parentDataSet.eIsProxy()) {
			InternalEObject oldParentDataSet = (InternalEObject)parentDataSet;
			parentDataSet = (DataSet)eResolveProxy(oldParentDataSet);
			if (parentDataSet != oldParentDataSet) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.DATA_SET__PARENT_DATA_SET, oldParentDataSet, parentDataSet));
			}
		}
		return parentDataSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataSet basicGetParentDataSet() {
		return parentDataSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParentDataSet(DataSet newParentDataSet, NotificationChain msgs) {
		DataSet oldParentDataSet = parentDataSet;
		parentDataSet = newParentDataSet;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_SET__PARENT_DATA_SET, oldParentDataSet, newParentDataSet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setParentDataSet(DataSet newParentDataSet) {
		if (newParentDataSet != parentDataSet) {
			NotificationChain msgs = null;
			if (parentDataSet != null)
				msgs = ((InternalEObject)parentDataSet).eInverseRemove(this, DAConfigPackage.DATA_SET__CHILD_DATA_SET, DataSet.class, msgs);
			if (newParentDataSet != null)
				msgs = ((InternalEObject)newParentDataSet).eInverseAdd(this, DAConfigPackage.DATA_SET__CHILD_DATA_SET, DataSet.class, msgs);
			msgs = basicSetParentDataSet(newParentDataSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_SET__PARENT_DATA_SET, newParentDataSet, newParentDataSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DAConfigPackage.DATA_SET__CHILD_DATA_SET:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildDataSet()).basicAdd(otherEnd, msgs);
			case DAConfigPackage.DATA_SET__PARENT_DATA_SET:
				if (parentDataSet != null)
					msgs = ((InternalEObject)parentDataSet).eInverseRemove(this, DAConfigPackage.DATA_SET__CHILD_DATA_SET, DataSet.class, msgs);
				return basicSetParentDataSet((DataSet)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DAConfigPackage.DATA_SET__QUERY:
				return basicSetQuery(null, msgs);
			case DAConfigPackage.DATA_SET__CHILD_DATA_SET:
				return ((InternalEList<?>)getChildDataSet()).basicRemove(otherEnd, msgs);
			case DAConfigPackage.DATA_SET__PARENT_DATA_SET:
				return basicSetParentDataSet(null, msgs);
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
			case DAConfigPackage.DATA_SET__INPUT_TYPE:
				if (resolve) return getInputType();
				return basicGetInputType();
			case DAConfigPackage.DATA_SET__OUTPUT_TYPE:
				if (resolve) return getOutputType();
				return basicGetOutputType();
			case DAConfigPackage.DATA_SET__QUERY:
				return getQuery();
			case DAConfigPackage.DATA_SET__CHILD_DATA_SET:
				return getChildDataSet();
			case DAConfigPackage.DATA_SET__PARENT_DATA_SET:
				if (resolve) return getParentDataSet();
				return basicGetParentDataSet();
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
			case DAConfigPackage.DATA_SET__INPUT_TYPE:
				setInputType((EClass)newValue);
				return;
			case DAConfigPackage.DATA_SET__OUTPUT_TYPE:
				setOutputType((EClass)newValue);
				return;
			case DAConfigPackage.DATA_SET__QUERY:
				setQuery((Query)newValue);
				return;
			case DAConfigPackage.DATA_SET__CHILD_DATA_SET:
				getChildDataSet().clear();
				getChildDataSet().addAll((Collection<? extends DataSet>)newValue);
				return;
			case DAConfigPackage.DATA_SET__PARENT_DATA_SET:
				setParentDataSet((DataSet)newValue);
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
			case DAConfigPackage.DATA_SET__INPUT_TYPE:
				setInputType((EClass)null);
				return;
			case DAConfigPackage.DATA_SET__OUTPUT_TYPE:
				setOutputType((EClass)null);
				return;
			case DAConfigPackage.DATA_SET__QUERY:
				setQuery((Query)null);
				return;
			case DAConfigPackage.DATA_SET__CHILD_DATA_SET:
				getChildDataSet().clear();
				return;
			case DAConfigPackage.DATA_SET__PARENT_DATA_SET:
				setParentDataSet((DataSet)null);
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
			case DAConfigPackage.DATA_SET__INPUT_TYPE:
				return inputType != null;
			case DAConfigPackage.DATA_SET__OUTPUT_TYPE:
				return outputType != null;
			case DAConfigPackage.DATA_SET__QUERY:
				return query != null;
			case DAConfigPackage.DATA_SET__CHILD_DATA_SET:
				return childDataSet != null && !childDataSet.isEmpty();
			case DAConfigPackage.DATA_SET__PARENT_DATA_SET:
				return parentDataSet != null;
		}
		return super.eIsSet(featureID);
	}

} //DataSetImpl
