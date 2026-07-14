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
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.DataProvider;
import org.eclipse.fennec.data.atlas.configuration.DistributionExport;
import org.eclipse.fennec.data.atlas.configuration.Transformation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Provider</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataProviderImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataProviderImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataProviderImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataProviderImpl#getDataInput <em>Data Input</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataProviderImpl#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataProviderImpl#getDistributionExport <em>Distribution Export</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class DataProviderImpl extends MinimalEObjectImpl.Container implements DataProvider {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDataInput() <em>Data Input</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataInput()
	 * @generated
	 * @ordered
	 */
	protected DataInput dataInput;

	/**
	 * The cached value of the '{@link #getTransformation() <em>Transformation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransformation()
	 * @generated
	 * @ordered
	 */
	protected Transformation transformation;

	/**
	 * The cached value of the '{@link #getDistributionExport() <em>Distribution Export</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistributionExport()
	 * @generated
	 * @ordered
	 */
	protected EList<DistributionExport> distributionExport;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataProviderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.DATA_PROVIDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_PROVIDER__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_PROVIDER__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_PROVIDER__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DataInput getDataInput() {
		if (dataInput != null && dataInput.eIsProxy()) {
			InternalEObject oldDataInput = (InternalEObject)dataInput;
			dataInput = (DataInput)eResolveProxy(oldDataInput);
			if (dataInput != oldDataInput) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.DATA_PROVIDER__DATA_INPUT, oldDataInput, dataInput));
			}
		}
		return dataInput;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DataInput basicGetDataInput() {
		return dataInput;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDataInput(DataInput newDataInput) {
		DataInput oldDataInput = dataInput;
		dataInput = newDataInput;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_PROVIDER__DATA_INPUT, oldDataInput, dataInput));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Transformation getTransformation() {
		if (transformation != null && transformation.eIsProxy()) {
			InternalEObject oldTransformation = (InternalEObject)transformation;
			transformation = (Transformation)eResolveProxy(oldTransformation);
			if (transformation != oldTransformation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.DATA_PROVIDER__TRANSFORMATION, oldTransformation, transformation));
			}
		}
		return transformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Transformation basicGetTransformation() {
		return transformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransformation(Transformation newTransformation) {
		Transformation oldTransformation = transformation;
		transformation = newTransformation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_PROVIDER__TRANSFORMATION, oldTransformation, transformation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DistributionExport> getDistributionExport() {
		if (distributionExport == null) {
			distributionExport = new EObjectResolvingEList<DistributionExport>(DistributionExport.class, this, DAConfigPackage.DATA_PROVIDER__DISTRIBUTION_EXPORT);
		}
		return distributionExport;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.DATA_PROVIDER__ID:
				return getId();
			case DAConfigPackage.DATA_PROVIDER__NAME:
				return getName();
			case DAConfigPackage.DATA_PROVIDER__DESCRIPTION:
				return getDescription();
			case DAConfigPackage.DATA_PROVIDER__DATA_INPUT:
				if (resolve) return getDataInput();
				return basicGetDataInput();
			case DAConfigPackage.DATA_PROVIDER__TRANSFORMATION:
				if (resolve) return getTransformation();
				return basicGetTransformation();
			case DAConfigPackage.DATA_PROVIDER__DISTRIBUTION_EXPORT:
				return getDistributionExport();
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
			case DAConfigPackage.DATA_PROVIDER__ID:
				setId((String)newValue);
				return;
			case DAConfigPackage.DATA_PROVIDER__NAME:
				setName((String)newValue);
				return;
			case DAConfigPackage.DATA_PROVIDER__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DAConfigPackage.DATA_PROVIDER__DATA_INPUT:
				setDataInput((DataInput)newValue);
				return;
			case DAConfigPackage.DATA_PROVIDER__TRANSFORMATION:
				setTransformation((Transformation)newValue);
				return;
			case DAConfigPackage.DATA_PROVIDER__DISTRIBUTION_EXPORT:
				getDistributionExport().clear();
				getDistributionExport().addAll((Collection<? extends DistributionExport>)newValue);
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
			case DAConfigPackage.DATA_PROVIDER__ID:
				setId(ID_EDEFAULT);
				return;
			case DAConfigPackage.DATA_PROVIDER__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DAConfigPackage.DATA_PROVIDER__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DAConfigPackage.DATA_PROVIDER__DATA_INPUT:
				setDataInput((DataInput)null);
				return;
			case DAConfigPackage.DATA_PROVIDER__TRANSFORMATION:
				setTransformation((Transformation)null);
				return;
			case DAConfigPackage.DATA_PROVIDER__DISTRIBUTION_EXPORT:
				getDistributionExport().clear();
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
			case DAConfigPackage.DATA_PROVIDER__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case DAConfigPackage.DATA_PROVIDER__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DAConfigPackage.DATA_PROVIDER__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DAConfigPackage.DATA_PROVIDER__DATA_INPUT:
				return dataInput != null;
			case DAConfigPackage.DATA_PROVIDER__TRANSFORMATION:
				return transformation != null;
			case DAConfigPackage.DATA_PROVIDER__DISTRIBUTION_EXPORT:
				return distributionExport != null && !distributionExport.isEmpty();
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
		result.append(" (id: ");
		result.append(id);
		result.append(", name: ");
		result.append(name);
		result.append(", description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

} //DataProviderImpl
