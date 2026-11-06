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

import org.eclipse.fennec.atlas.configuration.DataProvider;
import org.eclipse.fennec.atlas.configuration.Transformation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Atlas Provider</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Data-Atlas refinement of the shared DataProvider (id, name, description, dataInput): anything that provides data and can be distributed - a DataService (via DataAtlasService) or a concrete DataSet. Adds the rest of the data configuration trias besides the dataInput - transformation and distribution exports - plus the opt-in publication.
 * 
 * A DataSet refines its enclosing DataService: if it sets one of the trias features (dataInput, transformation, distributionExport) itself, that value overrides the one of the DataService; if it leaves the feature unset, the value of the DataService applies (override-else-default). Runtime constraint: a DataService that does not contain any DataSet must define a dataInput.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasProvider#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasProvider#getDistributionExport <em>Distribution Export</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasProvider#getPublication <em>Publication</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasProvider()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface DataAtlasProvider extends DataProvider {
	/**
	 * Returns the value of the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional transformation applied to the data of this provider. On a DataSet it overrides the transformation of the enclosing DataService; if left unset, the service's transformation applies.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transformation</em>' reference.
	 * @see #setTransformation(Transformation)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasProvider_Transformation()
	 * @model
	 * @generated
	 */
	Transformation getTransformation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasProvider#getTransformation <em>Transformation</em>}' reference.
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
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasProvider_DistributionExport()
	 * @model
	 * @generated
	 */
	EList<DistributionExport> getDistributionExport();

	/**
	 * Returns the value of the '<em><b>Publication</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Opt-in open-data publication of this provider: references a DcatPublication declaration (defined in the publications registry). Absent means the provider is not published - nothing is published implicitly.
	 * Follows the override-else-default rule: what a DataService declares applies to its DataSets unless a DataSet references a DcatPublication of its own.
	 * Deliberately plain data - the mapping to a DCAT model lives in the omittable publication bundle, never in this model (see data.atlas issue #4, DA-DCAT-1).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Publication</em>' reference.
	 * @see #setPublication(DcatPublication)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasProvider_Publication()
	 * @model
	 * @generated
	 */
	DcatPublication getPublication();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasProvider#getPublication <em>Publication</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Publication</em>' reference.
	 * @see #getPublication()
	 * @generated
	 */
	void setPublication(DcatPublication value);

} // DataAtlasProvider
