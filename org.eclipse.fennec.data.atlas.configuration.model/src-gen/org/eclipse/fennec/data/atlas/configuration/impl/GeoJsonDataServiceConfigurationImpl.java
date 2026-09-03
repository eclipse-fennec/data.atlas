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
package org.eclipse.fennec.data.atlas.configuration.impl;

import java.math.BigInteger;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Geo Json Data Service Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceConfigurationImpl#getPath <em>Path</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceConfigurationImpl#getBatchSize <em>Batch Size</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceConfigurationImpl#getBatchSizeLimit <em>Batch Size Limit</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceConfigurationImpl#getLongitudeFeature <em>Longitude Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceConfigurationImpl#getLatitudeFeature <em>Latitude Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceConfigurationImpl#getElevationFeature <em>Elevation Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceConfigurationImpl#getGeometryFeature <em>Geometry Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.GeoJsonDataServiceConfigurationImpl#getIdFeature <em>Id Feature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GeoJsonDataServiceConfigurationImpl extends DataServiceConfigurationImpl implements GeoJsonDataServiceConfiguration {
	/**
	 * The default value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected static final String PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected String path = PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #getBatchSize() <em>Batch Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchSize()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger BATCH_SIZE_EDEFAULT = new BigInteger("-1");

	/**
	 * The cached value of the '{@link #getBatchSize() <em>Batch Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchSize()
	 * @generated
	 * @ordered
	 */
	protected BigInteger batchSize = BATCH_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBatchSizeLimit() <em>Batch Size Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchSizeLimit()
	 * @generated
	 * @ordered
	 */
	protected static final BigInteger BATCH_SIZE_LIMIT_EDEFAULT = new BigInteger("-1");

	/**
	 * The cached value of the '{@link #getBatchSizeLimit() <em>Batch Size Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBatchSizeLimit()
	 * @generated
	 * @ordered
	 */
	protected BigInteger batchSizeLimit = BATCH_SIZE_LIMIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getLongitudeFeature() <em>Longitude Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLongitudeFeature()
	 * @generated
	 * @ordered
	 */
	protected static final String LONGITUDE_FEATURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLongitudeFeature() <em>Longitude Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLongitudeFeature()
	 * @generated
	 * @ordered
	 */
	protected String longitudeFeature = LONGITUDE_FEATURE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLatitudeFeature() <em>Latitude Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatitudeFeature()
	 * @generated
	 * @ordered
	 */
	protected static final String LATITUDE_FEATURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLatitudeFeature() <em>Latitude Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatitudeFeature()
	 * @generated
	 * @ordered
	 */
	protected String latitudeFeature = LATITUDE_FEATURE_EDEFAULT;

	/**
	 * The default value of the '{@link #getElevationFeature() <em>Elevation Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElevationFeature()
	 * @generated
	 * @ordered
	 */
	protected static final String ELEVATION_FEATURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getElevationFeature() <em>Elevation Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElevationFeature()
	 * @generated
	 * @ordered
	 */
	protected String elevationFeature = ELEVATION_FEATURE_EDEFAULT;

	/**
	 * The default value of the '{@link #getGeometryFeature() <em>Geometry Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeometryFeature()
	 * @generated
	 * @ordered
	 */
	protected static final String GEOMETRY_FEATURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGeometryFeature() <em>Geometry Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeometryFeature()
	 * @generated
	 * @ordered
	 */
	protected String geometryFeature = GEOMETRY_FEATURE_EDEFAULT;

	/**
	 * The default value of the '{@link #getIdFeature() <em>Id Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdFeature()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_FEATURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIdFeature() <em>Id Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdFeature()
	 * @generated
	 * @ordered
	 */
	protected String idFeature = ID_FEATURE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeoJsonDataServiceConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.GEO_JSON_DATA_SERVICE_CONFIGURATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPath() {
		return path;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPath(String newPath) {
		String oldPath = path;
		path = newPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__PATH, oldPath, path));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BigInteger getBatchSize() {
		return batchSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBatchSize(BigInteger newBatchSize) {
		BigInteger oldBatchSize = batchSize;
		batchSize = newBatchSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE, oldBatchSize, batchSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BigInteger getBatchSizeLimit() {
		return batchSizeLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBatchSizeLimit(BigInteger newBatchSizeLimit) {
		BigInteger oldBatchSizeLimit = batchSizeLimit;
		batchSizeLimit = newBatchSizeLimit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT, oldBatchSizeLimit, batchSizeLimit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLongitudeFeature() {
		return longitudeFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLongitudeFeature(String newLongitudeFeature) {
		String oldLongitudeFeature = longitudeFeature;
		longitudeFeature = newLongitudeFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__LONGITUDE_FEATURE, oldLongitudeFeature, longitudeFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLatitudeFeature() {
		return latitudeFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLatitudeFeature(String newLatitudeFeature) {
		String oldLatitudeFeature = latitudeFeature;
		latitudeFeature = newLatitudeFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__LATITUDE_FEATURE, oldLatitudeFeature, latitudeFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getElevationFeature() {
		return elevationFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setElevationFeature(String newElevationFeature) {
		String oldElevationFeature = elevationFeature;
		elevationFeature = newElevationFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__ELEVATION_FEATURE, oldElevationFeature, elevationFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getGeometryFeature() {
		return geometryFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGeometryFeature(String newGeometryFeature) {
		String oldGeometryFeature = geometryFeature;
		geometryFeature = newGeometryFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE, oldGeometryFeature, geometryFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getIdFeature() {
		return idFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIdFeature(String newIdFeature) {
		String oldIdFeature = idFeature;
		idFeature = newIdFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__ID_FEATURE, oldIdFeature, idFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__PATH:
				return getPath();
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE:
				return getBatchSize();
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				return getBatchSizeLimit();
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__LONGITUDE_FEATURE:
				return getLongitudeFeature();
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__LATITUDE_FEATURE:
				return getLatitudeFeature();
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__ELEVATION_FEATURE:
				return getElevationFeature();
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE:
				return getGeometryFeature();
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__ID_FEATURE:
				return getIdFeature();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__PATH:
				setPath((String)newValue);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE:
				setBatchSize((BigInteger)newValue);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				setBatchSizeLimit((BigInteger)newValue);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__LONGITUDE_FEATURE:
				setLongitudeFeature((String)newValue);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__LATITUDE_FEATURE:
				setLatitudeFeature((String)newValue);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__ELEVATION_FEATURE:
				setElevationFeature((String)newValue);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE:
				setGeometryFeature((String)newValue);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__ID_FEATURE:
				setIdFeature((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__PATH:
				setPath(PATH_EDEFAULT);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE:
				setBatchSize(BATCH_SIZE_EDEFAULT);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				setBatchSizeLimit(BATCH_SIZE_LIMIT_EDEFAULT);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__LONGITUDE_FEATURE:
				setLongitudeFeature(LONGITUDE_FEATURE_EDEFAULT);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__LATITUDE_FEATURE:
				setLatitudeFeature(LATITUDE_FEATURE_EDEFAULT);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__ELEVATION_FEATURE:
				setElevationFeature(ELEVATION_FEATURE_EDEFAULT);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE:
				setGeometryFeature(GEOMETRY_FEATURE_EDEFAULT);
				return;
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__ID_FEATURE:
				setIdFeature(ID_FEATURE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__PATH:
				return PATH_EDEFAULT == null ? path != null : !PATH_EDEFAULT.equals(path);
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE:
				return BATCH_SIZE_EDEFAULT == null ? batchSize != null : !BATCH_SIZE_EDEFAULT.equals(batchSize);
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT:
				return BATCH_SIZE_LIMIT_EDEFAULT == null ? batchSizeLimit != null : !BATCH_SIZE_LIMIT_EDEFAULT.equals(batchSizeLimit);
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__LONGITUDE_FEATURE:
				return LONGITUDE_FEATURE_EDEFAULT == null ? longitudeFeature != null : !LONGITUDE_FEATURE_EDEFAULT.equals(longitudeFeature);
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__LATITUDE_FEATURE:
				return LATITUDE_FEATURE_EDEFAULT == null ? latitudeFeature != null : !LATITUDE_FEATURE_EDEFAULT.equals(latitudeFeature);
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__ELEVATION_FEATURE:
				return ELEVATION_FEATURE_EDEFAULT == null ? elevationFeature != null : !ELEVATION_FEATURE_EDEFAULT.equals(elevationFeature);
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE:
				return GEOMETRY_FEATURE_EDEFAULT == null ? geometryFeature != null : !GEOMETRY_FEATURE_EDEFAULT.equals(geometryFeature);
			case DAConfigPackage.GEO_JSON_DATA_SERVICE_CONFIGURATION__ID_FEATURE:
				return ID_FEATURE_EDEFAULT == null ? idFeature != null : !ID_FEATURE_EDEFAULT.equals(idFeature);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (path: ");
		result.append(path);
		result.append(", batchSize: ");
		result.append(batchSize);
		result.append(", batchSizeLimit: ");
		result.append(batchSizeLimit);
		result.append(", longitudeFeature: ");
		result.append(longitudeFeature);
		result.append(", latitudeFeature: ");
		result.append(latitudeFeature);
		result.append(", elevationFeature: ");
		result.append(elevationFeature);
		result.append(", geometryFeature: ");
		result.append(geometryFeature);
		result.append(", idFeature: ");
		result.append(idFeature);
		result.append(')');
		return result.toString();
	}

} //GeoJsonDataServiceConfigurationImpl
