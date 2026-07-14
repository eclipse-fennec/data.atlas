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

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract base for anything that provides data and can be distributed: a generic DataService or a concrete DataSet.
 * Carries the common identity (id/name/description) and the data configuration trias: data source (dataInput), transformation and distribution exports.
 * 
 * A DataSet refines its enclosing DataService: if it sets one of the trias features itself, that value overrides the one of the DataService; if it leaves the feature unset, the value of the DataService applies (override-else-default).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getDataInput <em>Data Input</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getDistributionExport <em>Distribution Export</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataProvider()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface DataProvider extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unique identifier of this data provider (DataService or DataSet).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataProvider_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Derives from the model annotation by default
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataProvider_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Derives from the model annotation by default
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataProvider_Description()
	 * @model required="true"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The DataInput (data source) used by this provider.
	 * On a standalone DataService this is the source served generically. On a DataSet it overrides the DataInput of the enclosing DataService; if left unset, the service's DataInput applies.
	 * Runtime constraint: a DataService that does not contain any DataSet must define a dataInput here.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data Input</em>' reference.
	 * @see #setDataInput(DataInput)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataProvider_DataInput()
	 * @model
	 * @generated
	 */
	DataInput getDataInput();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getDataInput <em>Data Input</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Input</em>' reference.
	 * @see #getDataInput()
	 * @generated
	 */
	void setDataInput(DataInput value);

	/**
	 * Returns the value of the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional transformation applied to the data of this provider. On a DataSet it overrides the transformation of the enclosing DataService; if left unset, the service's transformation applies.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transformation</em>' reference.
	 * @see #setTransformation(Transformation)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataProvider_Transformation()
	 * @model
	 * @generated
	 */
	Transformation getTransformation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getTransformation <em>Transformation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transformation</em>' reference.
	 * @see #getTransformation()
	 * @generated
	 */
	void setTransformation(Transformation value);

	/**
	 * Returns the value of the '<em><b>Distribution Export</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.DistributionExport}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * References reusable DistributionExport templates (defined in the export registry) describing the formats this provider can be serialized to.
	 * On a DataService this is the set of available/default exports. On a DataSet, if it defines its own exports they fully replace the service's; if left empty, the service's exports apply (override-else-default).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Distribution Export</em>' reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataProvider_DistributionExport()
	 * @model
	 * @generated
	 */
	EList<DistributionExport> getDistributionExport();

} // DataProvider
