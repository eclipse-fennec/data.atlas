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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ogc Features Data Service Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceConfigurationImpl#getCollectionId <em>Collection Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceConfigurationImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceConfigurationImpl#getIdFeature <em>Id Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceConfigurationImpl#getGeometryFeature <em>Geometry Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceConfigurationImpl#getBboxFeatures <em>Bbox Features</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceConfigurationImpl#getTemporalFeature <em>Temporal Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceConfigurationImpl#getLayerGroup <em>Layer Group</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceConfigurationImpl#getStyle <em>Style</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OgcFeaturesDataServiceConfigurationImpl extends DataServiceConfigurationImpl implements OgcFeaturesDataServiceConfiguration {
	/**
	 * The default value of the '{@link #getCollectionId() <em>Collection Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionId()
	 * @generated
	 * @ordered
	 */
	protected static final String COLLECTION_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCollectionId() <em>Collection Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionId()
	 * @generated
	 * @ordered
	 */
	protected String collectionId = COLLECTION_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected static final String TITLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected String title = TITLE_EDEFAULT;

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
	 * The cached value of the '{@link #getBboxFeatures() <em>Bbox Features</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBboxFeatures()
	 * @generated
	 * @ordered
	 */
	protected EList<String> bboxFeatures;

	/**
	 * The default value of the '{@link #getTemporalFeature() <em>Temporal Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemporalFeature()
	 * @generated
	 * @ordered
	 */
	protected static final String TEMPORAL_FEATURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTemporalFeature() <em>Temporal Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemporalFeature()
	 * @generated
	 * @ordered
	 */
	protected String temporalFeature = TEMPORAL_FEATURE_EDEFAULT;

	/**
	 * The default value of the '{@link #getLayerGroup() <em>Layer Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLayerGroup()
	 * @generated
	 * @ordered
	 */
	protected static final String LAYER_GROUP_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLayerGroup() <em>Layer Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLayerGroup()
	 * @generated
	 * @ordered
	 */
	protected String layerGroup = LAYER_GROUP_EDEFAULT;

	/**
	 * The default value of the '{@link #getStyle() <em>Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStyle()
	 * @generated
	 * @ordered
	 */
	protected static final String STYLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStyle() <em>Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStyle()
	 * @generated
	 * @ordered
	 */
	protected String style = STYLE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OgcFeaturesDataServiceConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.OGC_FEATURES_DATA_SERVICE_CONFIGURATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCollectionId() {
		return collectionId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCollectionId(String newCollectionId) {
		String oldCollectionId = collectionId;
		collectionId = newCollectionId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__COLLECTION_ID, oldCollectionId, collectionId));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTitle(String newTitle) {
		String oldTitle = title;
		title = newTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TITLE, oldTitle, title));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__ID_FEATURE, oldIdFeature, idFeature));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE, oldGeometryFeature, geometryFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<String> getBboxFeatures() {
		if (bboxFeatures == null) {
			bboxFeatures = new EDataTypeUniqueEList<String>(String.class, this, DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__BBOX_FEATURES);
		}
		return bboxFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getTemporalFeature() {
		return temporalFeature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTemporalFeature(String newTemporalFeature) {
		String oldTemporalFeature = temporalFeature;
		temporalFeature = newTemporalFeature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TEMPORAL_FEATURE, oldTemporalFeature, temporalFeature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLayerGroup() {
		return layerGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLayerGroup(String newLayerGroup) {
		String oldLayerGroup = layerGroup;
		layerGroup = newLayerGroup;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__LAYER_GROUP, oldLayerGroup, layerGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getStyle() {
		return style;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStyle(String newStyle) {
		String oldStyle = style;
		style = newStyle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__STYLE, oldStyle, style));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__COLLECTION_ID:
				return getCollectionId();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TITLE:
				return getTitle();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__ID_FEATURE:
				return getIdFeature();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE:
				return getGeometryFeature();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__BBOX_FEATURES:
				return getBboxFeatures();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TEMPORAL_FEATURE:
				return getTemporalFeature();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__LAYER_GROUP:
				return getLayerGroup();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__STYLE:
				return getStyle();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__COLLECTION_ID:
				setCollectionId((String)newValue);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TITLE:
				setTitle((String)newValue);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__ID_FEATURE:
				setIdFeature((String)newValue);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE:
				setGeometryFeature((String)newValue);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__BBOX_FEATURES:
				getBboxFeatures().clear();
				getBboxFeatures().addAll((Collection<? extends String>)newValue);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TEMPORAL_FEATURE:
				setTemporalFeature((String)newValue);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__LAYER_GROUP:
				setLayerGroup((String)newValue);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__STYLE:
				setStyle((String)newValue);
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
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__COLLECTION_ID:
				setCollectionId(COLLECTION_ID_EDEFAULT);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__ID_FEATURE:
				setIdFeature(ID_FEATURE_EDEFAULT);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE:
				setGeometryFeature(GEOMETRY_FEATURE_EDEFAULT);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__BBOX_FEATURES:
				getBboxFeatures().clear();
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TEMPORAL_FEATURE:
				setTemporalFeature(TEMPORAL_FEATURE_EDEFAULT);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__LAYER_GROUP:
				setLayerGroup(LAYER_GROUP_EDEFAULT);
				return;
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__STYLE:
				setStyle(STYLE_EDEFAULT);
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
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__COLLECTION_ID:
				return COLLECTION_ID_EDEFAULT == null ? collectionId != null : !COLLECTION_ID_EDEFAULT.equals(collectionId);
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TITLE:
				return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__ID_FEATURE:
				return ID_FEATURE_EDEFAULT == null ? idFeature != null : !ID_FEATURE_EDEFAULT.equals(idFeature);
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE:
				return GEOMETRY_FEATURE_EDEFAULT == null ? geometryFeature != null : !GEOMETRY_FEATURE_EDEFAULT.equals(geometryFeature);
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__BBOX_FEATURES:
				return bboxFeatures != null && !bboxFeatures.isEmpty();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TEMPORAL_FEATURE:
				return TEMPORAL_FEATURE_EDEFAULT == null ? temporalFeature != null : !TEMPORAL_FEATURE_EDEFAULT.equals(temporalFeature);
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__LAYER_GROUP:
				return LAYER_GROUP_EDEFAULT == null ? layerGroup != null : !LAYER_GROUP_EDEFAULT.equals(layerGroup);
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE_CONFIGURATION__STYLE:
				return STYLE_EDEFAULT == null ? style != null : !STYLE_EDEFAULT.equals(style);
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
		result.append(" (collectionId: ");
		result.append(collectionId);
		result.append(", title: ");
		result.append(title);
		result.append(", idFeature: ");
		result.append(idFeature);
		result.append(", geometryFeature: ");
		result.append(geometryFeature);
		result.append(", bboxFeatures: ");
		result.append(bboxFeatures);
		result.append(", temporalFeature: ");
		result.append(temporalFeature);
		result.append(", layerGroup: ");
		result.append(layerGroup);
		result.append(", style: ");
		result.append(style);
		result.append(')');
		return result.toString();
	}

} //OgcFeaturesDataServiceConfigurationImpl
