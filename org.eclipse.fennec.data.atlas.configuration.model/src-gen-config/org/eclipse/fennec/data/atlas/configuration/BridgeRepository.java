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

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bridge Repository</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A DataInput that can load data from another source and transform it if necessary.
 * BridgeRepository can be cascaded, so one original Entity can result in multiple intermediate formats if necessary.
 * 
 * A bridge can have an attached data transformation for the source objects it loads.
 * On the other hand, incoming queries might be transformed as well.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getDataTrafo <em>Data Trafo</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getQueryTrafo <em>Query Trafo</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getFilter <em>Filter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getBridgeRepository()
 * @model
 * @generated
 */
@ProviderType
public interface BridgeRepository extends DataInput {
	/**
	 * Returns the value of the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The underlying DataInput this bridge reads from before applying its transformations.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Source</em>' reference.
	 * @see #setSource(DataInput)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getBridgeRepository_Source()
	 * @model required="true"
	 * @generated
	 */
	DataInput getSource();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getSource <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(DataInput value);

	/**
	 * Returns the value of the '<em><b>Data Trafo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Transformation applied to the source EObjects when they are loaded.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data Trafo</em>' reference.
	 * @see #setDataTrafo(DataTransformation)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getBridgeRepository_DataTrafo()
	 * @model required="true"
	 * @generated
	 */
	DataTransformation getDataTrafo();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getDataTrafo <em>Data Trafo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Trafo</em>' reference.
	 * @see #getDataTrafo()
	 * @generated
	 */
	void setDataTrafo(DataTransformation value);

	/**
	 * Returns the value of the '<em><b>Query Trafo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Transformation applied to incoming queries so they can be executed against the source.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Query Trafo</em>' reference.
	 * @see #setQueryTrafo(QueryTransformation)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getBridgeRepository_QueryTrafo()
	 * @model required="true"
	 * @generated
	 */
	QueryTransformation getQueryTrafo();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getQueryTrafo <em>Query Trafo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Query Trafo</em>' reference.
	 * @see #getQueryTrafo()
	 * @generated
	 */
	void setQueryTrafo(QueryTransformation value);

	/**
	 * Returns the value of the '<em><b>Filter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A Placeholder for now. Will provide e.g. security filters or anonymization rules for the bridged dataset
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Filter</em>' reference.
	 * @see #setFilter(EObject)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getBridgeRepository_Filter()
	 * @model required="true"
	 * @generated
	 */
	EObject getFilter();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getFilter <em>Filter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Filter</em>' reference.
	 * @see #getFilter()
	 * @generated
	 */
	void setFilter(EObject value);

} // BridgeRepository
