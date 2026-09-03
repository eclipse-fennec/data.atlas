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

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Geo Json Data Service Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Per-DataSet configuration for a GeoJsonDataService: the path plus the mapping of the domain type onto a GeoJSON Feature.
 * The geometry source is EITHER geometryFeature (a feature already holding a org.geojson.model Geometry, passed through) OR the longitudeFeature/latitudeFeature pair of numeric attributes (mapped to a Point, elevationFeature optionally third) - declaring both, or neither, is a configuration error and the endpoint stays down. All attributes not consumed by the mapping become the Feature's properties.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getPath <em>Path</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getBatchSize <em>Batch Size</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getBatchSizeLimit <em>Batch Size Limit</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getLongitudeFeature <em>Longitude Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getLatitudeFeature <em>Latitude Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getElevationFeature <em>Elevation Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getGeometryFeature <em>Geometry Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getIdFeature <em>Id Feature</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataServiceConfiguration()
 * @model
 * @generated
 */
@ProviderType
public interface GeoJsonDataServiceConfiguration extends DataServiceConfiguration {
	/**
	 * Returns the value of the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * derives by default from the Dataset name
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Path</em>' attribute.
	 * @see #setPath(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataServiceConfiguration_Path()
	 * @model required="true"
	 * @generated
	 */
	String getPath();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getPath <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Path</em>' attribute.
	 * @see #getPath()
	 * @generated
	 */
	void setPath(String value);

	/**
	 * Returns the value of the '<em><b>Batch Size</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * the page size for pagination. -1 means no pagination
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Batch Size</em>' attribute.
	 * @see #setBatchSize(BigInteger)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataServiceConfiguration_BatchSize()
	 * @model default="-1" required="true"
	 * @generated
	 */
	BigInteger getBatchSize();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getBatchSize <em>Batch Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Batch Size</em>' attribute.
	 * @see #getBatchSize()
	 * @generated
	 */
	void setBatchSize(BigInteger value);

	/**
	 * Returns the value of the '<em><b>Batch Size Limit</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * the page size limit for pagination, so the server will not be overtaxed
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Batch Size Limit</em>' attribute.
	 * @see #setBatchSizeLimit(BigInteger)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataServiceConfiguration_BatchSizeLimit()
	 * @model default="-1" required="true"
	 * @generated
	 */
	BigInteger getBatchSizeLimit();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getBatchSizeLimit <em>Batch Size Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Batch Size Limit</em>' attribute.
	 * @see #getBatchSizeLimit()
	 * @generated
	 */
	void setBatchSizeLimit(BigInteger value);

	/**
	 * Returns the value of the '<em><b>Longitude Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the numeric attribute of the DataSet's type carrying the WGS 84 longitude. Used together with latitudeFeature to build a Point geometry; mutually exclusive with geometryFeature.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Longitude Feature</em>' attribute.
	 * @see #setLongitudeFeature(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataServiceConfiguration_LongitudeFeature()
	 * @model
	 * @generated
	 */
	String getLongitudeFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getLongitudeFeature <em>Longitude Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Longitude Feature</em>' attribute.
	 * @see #getLongitudeFeature()
	 * @generated
	 */
	void setLongitudeFeature(String value);

	/**
	 * Returns the value of the '<em><b>Latitude Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the numeric attribute carrying the WGS 84 latitude; see longitudeFeature.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Latitude Feature</em>' attribute.
	 * @see #setLatitudeFeature(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataServiceConfiguration_LatitudeFeature()
	 * @model
	 * @generated
	 */
	String getLatitudeFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getLatitudeFeature <em>Latitude Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Latitude Feature</em>' attribute.
	 * @see #getLatitudeFeature()
	 * @generated
	 */
	void setLatitudeFeature(String value);

	/**
	 * Returns the value of the '<em><b>Elevation Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional name of the numeric attribute carrying the elevation, emitted as the Point's third coordinate.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Elevation Feature</em>' attribute.
	 * @see #setElevationFeature(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataServiceConfiguration_ElevationFeature()
	 * @model
	 * @generated
	 */
	String getElevationFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getElevationFeature <em>Elevation Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Elevation Feature</em>' attribute.
	 * @see #getElevationFeature()
	 * @generated
	 */
	void setElevationFeature(String value);

	/**
	 * Returns the value of the '<em><b>Geometry Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the feature already holding a org.geojson.model Geometry, passed through as the Feature's geometry; mutually exclusive with longitudeFeature/latitudeFeature.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Geometry Feature</em>' attribute.
	 * @see #setGeometryFeature(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataServiceConfiguration_GeometryFeature()
	 * @model
	 * @generated
	 */
	String getGeometryFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getGeometryFeature <em>Geometry Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Geometry Feature</em>' attribute.
	 * @see #getGeometryFeature()
	 * @generated
	 */
	void setGeometryFeature(String value);

	/**
	 * Returns the value of the '<em><b>Id Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional name of the attribute used as the Feature id. Unset falls back to the type's EMF id attribute; without either the Feature carries no id.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id Feature</em>' attribute.
	 * @see #setIdFeature(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getGeoJsonDataServiceConfiguration_IdFeature()
	 * @model
	 * @generated
	 */
	String getIdFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration#getIdFeature <em>Id Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id Feature</em>' attribute.
	 * @see #getIdFeature()
	 * @generated
	 */
	void setIdFeature(String value);

} // GeoJsonDataServiceConfiguration
