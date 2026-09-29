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

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Ogc Features Data Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * DataService that exposes DataSets via the OGC API - Features standard (Part 1 Core, Part 3 Filtering with CQL2), served by the Fennec OGC API Features server (emf.ogc.features). One service is one API root with its own landing page at urlContext: /conformance, /collections, /collections/{collectionId}/items (GeoJSON, HTML), the OpenAPI document at /api, a QGIS project at /collections?f=qgs and, when the viewer bundle is present, a map viewer at {urlContext}/viewer/. The root publishes exactly the collections its configurations declare; every collection is served through the ReadRepository of its DataSet's DataInput, so bbox, datetime and CQL2 filters push down into the input. Read only.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService#getDefaultLimit <em>Default Limit</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService#getMaxLimit <em>Max Limit</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService#getConfiguration <em>Configuration</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataService()
 * @model
 * @generated
 */
@ProviderType
public interface OgcFeaturesDataService extends DataService {
	/**
	 * Returns the value of the '<em><b>Default Limit</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Page size of /items when a request carries no limit parameter. -1 means the server default (10).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Default Limit</em>' attribute.
	 * @see #setDefaultLimit(BigInteger)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataService_DefaultLimit()
	 * @model default="-1" required="true"
	 * @generated
	 */
	BigInteger getDefaultLimit();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService#getDefaultLimit <em>Default Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Limit</em>' attribute.
	 * @see #getDefaultLimit()
	 * @generated
	 */
	void setDefaultLimit(BigInteger value);

	/**
	 * Returns the value of the '<em><b>Max Limit</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Largest page size served; a larger limit is reduced to it. -1 means the server default (10000).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Max Limit</em>' attribute.
	 * @see #setMaxLimit(BigInteger)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataService_MaxLimit()
	 * @model default="-1" required="true"
	 * @generated
	 */
	BigInteger getMaxLimit();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService#getMaxLimit <em>Max Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Limit</em>' attribute.
	 * @see #getMaxLimit()
	 * @generated
	 */
	void setMaxLimit(BigInteger value);

	/**
	 * Returns the value of the '<em><b>Configuration</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The per-DataSet collection configurations provided by this service.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Configuration</em>' containment reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataService_Configuration()
	 * @model containment="true" ordered="false"
	 *        annotation="http://www.eclipse.org/OCL/Collection nullFree='false'"
	 * @generated
	 */
	EList<OgcFeaturesDataServiceConfiguration> getConfiguration();

} // OgcFeaturesDataService
