/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 */
package org.eclipse.fennec.data.atlas.tests.extension.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.atlas.configuration.impl.DataInputImpl;

import org.eclipse.fennec.data.atlas.tests.extension.ProbeInput;
import org.eclipse.fennec.data.atlas.tests.extension.ProbePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.tests.extension.impl.ProbeInputImpl#getMappingFile <em>Mapping File</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProbeInputImpl extends DataInputImpl implements ProbeInput {
	/**
	 * The default value of the '{@link #getMappingFile() <em>Mapping File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMappingFile()
	 * @generated
	 * @ordered
	 */
	protected static final String MAPPING_FILE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMappingFile() <em>Mapping File</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMappingFile()
	 * @generated
	 * @ordered
	 */
	protected String mappingFile = MAPPING_FILE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProbeInputImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ProbePackage.Literals.PROBE_INPUT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getMappingFile() {
		return mappingFile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMappingFile(String newMappingFile) {
		String oldMappingFile = mappingFile;
		mappingFile = newMappingFile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ProbePackage.PROBE_INPUT__MAPPING_FILE, oldMappingFile, mappingFile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ProbePackage.PROBE_INPUT__MAPPING_FILE:
				return getMappingFile();
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
			case ProbePackage.PROBE_INPUT__MAPPING_FILE:
				setMappingFile((String)newValue);
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
			case ProbePackage.PROBE_INPUT__MAPPING_FILE:
				setMappingFile(MAPPING_FILE_EDEFAULT);
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
			case ProbePackage.PROBE_INPUT__MAPPING_FILE:
				return MAPPING_FILE_EDEFAULT == null ? mappingFile != null : !MAPPING_FILE_EDEFAULT.equals(mappingFile);
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
		result.append(" (mappingFile: ");
		result.append(mappingFile);
		result.append(')');
		return result.toString();
	}

} //ProbeInputImpl
