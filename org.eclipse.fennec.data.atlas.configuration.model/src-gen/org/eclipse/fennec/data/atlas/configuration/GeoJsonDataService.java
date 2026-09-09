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
 * A representation of the model object '<em><b>Geo Json Data Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * DataService that publishes DataSets as RFC 7946 GeoJSON: GET {path} returns a FeatureCollection, GET {path}/{id} a single Feature, media type application/geo+json.
 * A dedicated service kind rather than a media type on RestDataService, because serving GeoJSON needs mapping configuration (which features carry the geometry, what becomes the Feature id) that has no place on the generic REST service. Coordinates are assumed to be WGS 84 (RFC 7946 mandates it); coordinate transformation is a Transformation concern, not a serving one.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataService#getConfiguration <em>Configuration</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataService()
 * @model
 * @generated
 */
@ProviderType
public interface GeoJsonDataService extends DataService {
	/**
	 * Returns the value of the '<em><b>Configuration</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The per-DataSet GeoJSON configurations provided by this service.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Configuration</em>' containment reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataService_Configuration()
	 * @model containment="true" ordered="false"
	 *        annotation="http://www.eclipse.org/OCL/Collection nullFree='false'"
	 * @generated
	 */
	EList<GeoJsonDataServiceConfiguration> getConfiguration();

} // GeoJsonDataService
