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
package org.eclipse.fennec.data.atlas.configuration;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>CSV Distribution Export</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * DistributionExport template for CSV serialization.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#getSeparator <em>Separator</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#isCompressed <em>Compressed</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#isIncludeTypeHeader <em>Include Type Header</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getCSVDistributionExport()
 * @model
 * @generated
 */
@ProviderType
public interface CSVDistributionExport extends DistributionExport {
	/**
	 * Returns the value of the '<em><b>Separator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Column separator character (e.g. ',' or ';'). Only the first character is used - it maps to the fennec codec option 'codec.csv.delimiter', which takes a single char.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Separator</em>' attribute.
	 * @see #setSeparator(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getCSVDistributionExport_Separator()
	 * @model
	 * @generated
	 */
	String getSeparator();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#getSeparator <em>Separator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Separator</em>' attribute.
	 * @see #getSeparator()
	 * @generated
	 */
	void setSeparator(String value);

	/**
	 * Returns the value of the '<em><b>Compressed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether the CSV output is delivered compressed. This is NOT a gzip of a single CSV: it selects the fennec codec's zipped multi-table CSV (media type 'application/x-csv-zip'), a ZIP containing one CSV per serialized EClass.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Compressed</em>' attribute.
	 * @see #setCompressed(boolean)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getCSVDistributionExport_Compressed()
	 * @model
	 * @generated
	 */
	boolean isCompressed();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#isCompressed <em>Compressed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Compressed</em>' attribute.
	 * @see #isCompressed()
	 * @generated
	 */
	void setCompressed(boolean value);

	/**
	 * Returns the value of the '<em><b>Include Type Header</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether to emit an additional row carrying each column's SQL type between the header row and the data rows. The column header row itself is ALWAYS written and cannot be switched off. Maps to the fennec codec option 'codec.csv.dataTypeInSecondRow'.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Include Type Header</em>' attribute.
	 * @see #setIncludeTypeHeader(boolean)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getCSVDistributionExport_IncludeTypeHeader()
	 * @model
	 * @generated
	 */
	boolean isIncludeTypeHeader();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#isIncludeTypeHeader <em>Include Type Header</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Include Type Header</em>' attribute.
	 * @see #isIncludeTypeHeader()
	 * @generated
	 */
	void setIncludeTypeHeader(boolean value);

} // CSVDistributionExport
