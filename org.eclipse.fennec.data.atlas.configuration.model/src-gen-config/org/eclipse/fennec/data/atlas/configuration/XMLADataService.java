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

import org.eclipse.emf.common.util.EList;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>XMLA Data Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * DataService that exposes DataSets via the XML for Analysis (XMLA) protocol, e.g. for OLAP / Daanse cubes.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.XMLADataService#getConfiguration <em>Configuration</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getXMLADataService()
 * @model
 * @generated
 */
@ProviderType
public interface XMLADataService extends DataService {
	/**
	 * Returns the value of the '<em><b>Configuration</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The per-DataSet XMLA configurations provided by this service.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Configuration</em>' reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getXMLADataService_Configuration()
	 * @model ordered="false"
	 *        annotation="http://www.eclipse.org/OCL/Collection nullFree='false'"
	 * @generated
	 */
	EList<XMLADataServiceConfiguration> getConfiguration();

} // XMLADataService
