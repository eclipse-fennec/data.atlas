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

import org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport;
import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CSV Distribution Export</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.CSVDistributionExportImpl#getSeparator <em>Separator</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.CSVDistributionExportImpl#isCompressed <em>Compressed</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.CSVDistributionExportImpl#isIncludeTypeHeader <em>Include Type Header</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CSVDistributionExportImpl extends DistributionExportImpl implements CSVDistributionExport {
	/**
	 * The default value of the '{@link #getSeparator() <em>Separator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeparator()
	 * @generated
	 * @ordered
	 */
	protected static final String SEPARATOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSeparator() <em>Separator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeparator()
	 * @generated
	 * @ordered
	 */
	protected String separator = SEPARATOR_EDEFAULT;

	/**
	 * The default value of the '{@link #isCompressed() <em>Compressed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCompressed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean COMPRESSED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCompressed() <em>Compressed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCompressed()
	 * @generated
	 * @ordered
	 */
	protected boolean compressed = COMPRESSED_EDEFAULT;

	/**
	 * The default value of the '{@link #isIncludeTypeHeader() <em>Include Type Header</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeTypeHeader()
	 * @generated
	 * @ordered
	 */
	protected static final boolean INCLUDE_TYPE_HEADER_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIncludeTypeHeader() <em>Include Type Header</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIncludeTypeHeader()
	 * @generated
	 * @ordered
	 */
	protected boolean includeTypeHeader = INCLUDE_TYPE_HEADER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CSVDistributionExportImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.CSV_DISTRIBUTION_EXPORT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSeparator() {
		return separator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSeparator(String newSeparator) {
		String oldSeparator = separator;
		separator = newSeparator;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.CSV_DISTRIBUTION_EXPORT__SEPARATOR, oldSeparator, separator));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isCompressed() {
		return compressed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCompressed(boolean newCompressed) {
		boolean oldCompressed = compressed;
		compressed = newCompressed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.CSV_DISTRIBUTION_EXPORT__COMPRESSED, oldCompressed, compressed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIncludeTypeHeader() {
		return includeTypeHeader;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIncludeTypeHeader(boolean newIncludeTypeHeader) {
		boolean oldIncludeTypeHeader = includeTypeHeader;
		includeTypeHeader = newIncludeTypeHeader;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.CSV_DISTRIBUTION_EXPORT__INCLUDE_TYPE_HEADER, oldIncludeTypeHeader, includeTypeHeader));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__SEPARATOR:
				return getSeparator();
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__COMPRESSED:
				return isCompressed();
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__INCLUDE_TYPE_HEADER:
				return isIncludeTypeHeader();
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
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__SEPARATOR:
				setSeparator((String)newValue);
				return;
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__COMPRESSED:
				setCompressed((Boolean)newValue);
				return;
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__INCLUDE_TYPE_HEADER:
				setIncludeTypeHeader((Boolean)newValue);
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
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__SEPARATOR:
				setSeparator(SEPARATOR_EDEFAULT);
				return;
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__COMPRESSED:
				setCompressed(COMPRESSED_EDEFAULT);
				return;
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__INCLUDE_TYPE_HEADER:
				setIncludeTypeHeader(INCLUDE_TYPE_HEADER_EDEFAULT);
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
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__SEPARATOR:
				return SEPARATOR_EDEFAULT == null ? separator != null : !SEPARATOR_EDEFAULT.equals(separator);
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__COMPRESSED:
				return compressed != COMPRESSED_EDEFAULT;
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT__INCLUDE_TYPE_HEADER:
				return includeTypeHeader != INCLUDE_TYPE_HEADER_EDEFAULT;
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
		result.append(" (separator: ");
		result.append(separator);
		result.append(", compressed: ");
		result.append(compressed);
		result.append(", includeTypeHeader: ");
		result.append(includeTypeHeader);
		result.append(')');
		return result.toString();
	}

} //CSVDistributionExportImpl
