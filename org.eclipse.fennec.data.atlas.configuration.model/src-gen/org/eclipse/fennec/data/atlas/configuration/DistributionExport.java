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
 * A representation of the model object '<em><b>Distribution Export</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A reusable export/serialization template for a distribution (e.g. CSV settings). Defined once in the export registry and referenced from DataServices and DataSets (via DataProvider.distributionExport), so the same export settings are not duplicated per provider.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getMediaType <em>Media Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDistributionExport()
 * @model
 * @generated
 */
@ProviderType
public interface DistributionExport extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unique identifier of this export template.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDistributionExport_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getId <em>Id</em>}' attribute.
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
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDistributionExport_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getName <em>Name</em>}' attribute.
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
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDistributionExport_Description()
	 * @model required="true"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Media Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The HTTP media type this export is served as, e.g. 'text/csv' or 'application/json'.
	 * Unset means the kind-specific default of the concrete export: CSVDistributionExport serves 'text/csv', or 'application/x-csv-zip' when compressed is set. A plain DistributionExport therefore needs this attribute to be meaningful - it is how a configuration keeps a format that has no dedicated subclass (e.g. JSON) alongside a specialized one.
	 * Resolution at runtime: a DataProvider that resolves to no export at all is served in the runtime default formats; as soon as it resolves to at least one export, exactly the media types of those exports are served and any other Accept is answered with 406.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Media Type</em>' attribute.
	 * @see #setMediaType(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDistributionExport_MediaType()
	 * @model
	 * @generated
	 */
	String getMediaType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getMediaType <em>Media Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Media Type</em>' attribute.
	 * @see #getMediaType()
	 * @generated
	 */
	void setMediaType(String value);

} // DistributionExport
