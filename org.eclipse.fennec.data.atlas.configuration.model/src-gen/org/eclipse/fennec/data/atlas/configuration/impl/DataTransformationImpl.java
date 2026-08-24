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

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DataTransformation;

import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Transformation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataTransformationImpl#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataTransformationImpl#getSupportedEClasses <em>Supported EClasses</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataTransformationImpl#getResultEClasses <em>Result EClasses</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DataTransformationImpl extends TransformationImpl implements DataTransformation {
	/**
	 * The cached value of the '{@link #getTransformation() <em>Transformation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransformation()
	 * @generated
	 * @ordered
	 */
	protected OperationalTransformation transformation;

	/**
	 * The cached value of the '{@link #getSupportedEClasses() <em>Supported EClasses</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSupportedEClasses()
	 * @generated
	 * @ordered
	 */
	protected EList<EClass> supportedEClasses;

	/**
	 * The cached value of the '{@link #getResultEClasses() <em>Result EClasses</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultEClasses()
	 * @generated
	 * @ordered
	 */
	protected EList<EClass> resultEClasses;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataTransformationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.DATA_TRANSFORMATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OperationalTransformation getTransformation() {
		if (transformation != null && transformation.eIsProxy()) {
			InternalEObject oldTransformation = (InternalEObject)transformation;
			transformation = (OperationalTransformation)eResolveProxy(oldTransformation);
			if (transformation != oldTransformation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.DATA_TRANSFORMATION__TRANSFORMATION, oldTransformation, transformation));
			}
		}
		return transformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationalTransformation basicGetTransformation() {
		return transformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransformation(OperationalTransformation newTransformation) {
		OperationalTransformation oldTransformation = transformation;
		transformation = newTransformation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_TRANSFORMATION__TRANSFORMATION, oldTransformation, transformation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EClass> getSupportedEClasses() {
		if (supportedEClasses == null) {
			supportedEClasses = new EObjectResolvingEList<EClass>(EClass.class, this, DAConfigPackage.DATA_TRANSFORMATION__SUPPORTED_ECLASSES);
		}
		return supportedEClasses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EClass> getResultEClasses() {
		if (resultEClasses == null) {
			resultEClasses = new EObjectResolvingEList<EClass>(EClass.class, this, DAConfigPackage.DATA_TRANSFORMATION__RESULT_ECLASSES);
		}
		return resultEClasses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.DATA_TRANSFORMATION__TRANSFORMATION:
				if (resolve) return getTransformation();
				return basicGetTransformation();
			case DAConfigPackage.DATA_TRANSFORMATION__SUPPORTED_ECLASSES:
				return getSupportedEClasses();
			case DAConfigPackage.DATA_TRANSFORMATION__RESULT_ECLASSES:
				return getResultEClasses();
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
			case DAConfigPackage.DATA_TRANSFORMATION__TRANSFORMATION:
				setTransformation((OperationalTransformation)newValue);
				return;
			case DAConfigPackage.DATA_TRANSFORMATION__SUPPORTED_ECLASSES:
				getSupportedEClasses().clear();
				getSupportedEClasses().addAll((Collection<? extends EClass>)newValue);
				return;
			case DAConfigPackage.DATA_TRANSFORMATION__RESULT_ECLASSES:
				getResultEClasses().clear();
				getResultEClasses().addAll((Collection<? extends EClass>)newValue);
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
			case DAConfigPackage.DATA_TRANSFORMATION__TRANSFORMATION:
				setTransformation((OperationalTransformation)null);
				return;
			case DAConfigPackage.DATA_TRANSFORMATION__SUPPORTED_ECLASSES:
				getSupportedEClasses().clear();
				return;
			case DAConfigPackage.DATA_TRANSFORMATION__RESULT_ECLASSES:
				getResultEClasses().clear();
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
			case DAConfigPackage.DATA_TRANSFORMATION__TRANSFORMATION:
				return transformation != null;
			case DAConfigPackage.DATA_TRANSFORMATION__SUPPORTED_ECLASSES:
				return supportedEClasses != null && !supportedEClasses.isEmpty();
			case DAConfigPackage.DATA_TRANSFORMATION__RESULT_ECLASSES:
				return resultEClasses != null && !resultEClasses.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //DataTransformationImpl
