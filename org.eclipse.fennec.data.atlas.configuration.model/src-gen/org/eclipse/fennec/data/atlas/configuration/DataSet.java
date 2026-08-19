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

import org.eclipse.emf.ecore.EClass;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Conforms to the DCAT Dataset. A concrete realization of a DataService: it refines the service for a specific case (e.g. a concrete query) by overriding the inherited dataInput, transformation and distributionExport where needed.
 * Inherits id, name, description and the data configuration trias from DataProvider.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getInputType <em>Input Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getOutputType <em>Output Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getChildDataSet <em>Child Data Set</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getParentDataSet <em>Parent Data Set</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataSet()
 * @model
 * @generated
 */
@ProviderType
public interface DataSet extends DataProvider {
	/**
	 * Returns the value of the '<em><b>Input Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The model type (EClass) that is read from the dataInput. Might be too simple. We may need an approach like a genmodel, to properly resolve e.g. non containment references to other Datasets or Distributions or DataServices.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Input Type</em>' reference.
	 * @see #setInputType(EClass)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataSet_InputType()
	 * @model required="true"
	 * @generated
	 */
	EClass getInputType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getInputType <em>Input Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input Type</em>' reference.
	 * @see #getInputType()
	 * @generated
	 */
	void setInputType(EClass value);

	/**
	 * Returns the value of the '<em><b>Output Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The model type (EClass) that is published by this DataSet after an optional transformation. Might be too simple. We may need an approach like a genmodel, to properly resolve e.g. non containment references to other Datasets or Distributions or DataServices.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Output Type</em>' reference.
	 * @see #setOutputType(EClass)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataSet_OutputType()
	 * @model required="true"
	 * @generated
	 */
	EClass getOutputType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getOutputType <em>Output Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output Type</em>' reference.
	 * @see #getOutputType()
	 * @generated
	 */
	void setOutputType(EClass value);

	/**
	 * Returns the value of the '<em><b>Child Data Set</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.DataSet}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getParentDataSet <em>Parent Data Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Child DataSets within a DCAT dataset hierarchy/series; this DataSet acts as the parent of the referenced ones.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Child Data Set</em>' reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataSet_ChildDataSet()
	 * @see org.eclipse.fennec.data.atlas.configuration.DataSet#getParentDataSet
	 * @model opposite="parentDataSet"
	 * @generated
	 */
	EList<DataSet> getChildDataSet();

	/**
	 * Returns the value of the '<em><b>Parent Data Set</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getChildDataSet <em>Child Data Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Parent DataSet within a DCAT dataset hierarchy/series; opposite of childDataSet.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Parent Data Set</em>' reference.
	 * @see #setParentDataSet(DataSet)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataSet_ParentDataSet()
	 * @see org.eclipse.fennec.data.atlas.configuration.DataSet#getChildDataSet
	 * @model opposite="childDataSet"
	 * @generated
	 */
	DataSet getParentDataSet();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getParentDataSet <em>Parent Data Set</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Data Set</em>' reference.
	 * @see #getParentDataSet()
	 * @generated
	 */
	void setParentDataSet(DataSet value);

} // DataSet
