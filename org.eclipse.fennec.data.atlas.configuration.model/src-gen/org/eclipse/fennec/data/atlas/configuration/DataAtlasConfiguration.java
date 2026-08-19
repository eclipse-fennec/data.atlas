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
 * A representation of the model object '<em><b>Data Atlas Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Root of the configuration model: the complete description of exactly one Data-Atlas instance.
 * Multiple Data-Atlas instances can run side by side (e.g. to spread load); each instance is fed its own DataAtlasConfiguration, obtained either from the file system or by retrieving it from the Model-Atlas.
 * 
 * The root acts as a set of registries: data sources, data inputs, transformations and distribution-export templates are defined here exactly once and only referenced from DataServices and DataSets, so the same service definition can be re-applied to another data source (tenant/test system) and export settings are reusable templates rather than per-provider copies.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDataSources <em>Data Sources</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDataInputs <em>Data Inputs</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDataSets <em>Data Sets</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getServices <em>Services</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getExports <em>Exports</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getTransformations <em>Transformations</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasConfiguration()
 * @model
 * @generated
 */
@ProviderType
public interface DataAtlasConfiguration extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the Data-Atlas instance this configuration describes.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasConfiguration_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getName <em>Name</em>}' attribute.
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
	 * Optional description of this Data-Atlas instance.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasConfiguration_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Data Sources</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.JdbcDataSource}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Registry of reusable data source definitions, referenced from DataInputs.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data Sources</em>' containment reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasConfiguration_DataSources()
	 * @model containment="true"
	 * @generated
	 */
	EList<JdbcDataSource> getDataSources();

	/**
	 * Returns the value of the '<em><b>Data Inputs</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.DataInput}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Registry of the DataInputs of this instance, referenced from DataServices and DataSets.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data Inputs</em>' containment reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasConfiguration_DataInputs()
	 * @model containment="true"
	 * @generated
	 */
	EList<DataInput> getDataInputs();

	/**
	 * Returns the value of the '<em><b>Data Sets</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.DataSet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Registry of the DataSets of this instance, referenced from the DataServiceConfigurations.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data Sets</em>' containment reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasConfiguration_DataSets()
	 * @model containment="true"
	 * @generated
	 */
	EList<DataSet> getDataSets();

	/**
	 * Returns the value of the '<em><b>Services</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.DataService}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The DataServices (endpoints) this instance publishes.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Services</em>' containment reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasConfiguration_Services()
	 * @model containment="true"
	 * @generated
	 */
	EList<DataService> getServices();

	/**
	 * Returns the value of the '<em><b>Exports</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.DistributionExport}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Registry of reusable DistributionExport templates, referenced from DataServices and DataSets.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Exports</em>' containment reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasConfiguration_Exports()
	 * @model containment="true"
	 * @generated
	 */
	EList<DistributionExport> getExports();

	/**
	 * Returns the value of the '<em><b>Transformations</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.Transformation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Registry of the Transformations of this instance, referenced from DataProviders and BridgeRepositories.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transformations</em>' containment reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasConfiguration_Transformations()
	 * @model containment="true"
	 * @generated
	 */
	EList<Transformation> getTransformations();

} // DataAtlasConfiguration
