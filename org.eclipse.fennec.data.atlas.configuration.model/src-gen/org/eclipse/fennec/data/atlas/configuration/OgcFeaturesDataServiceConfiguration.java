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
 * A representation of the model object '<em><b>Ogc Features Data Service Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Everything the OGC API Features runtime needs to serve one DataSet of an OgcFeaturesDataService as a feature collection. The feature type is the DataSet's outputType. Override-else-default: an EClass carrying the https://eclipse.org/fennec/ogc/features annotation of emf.ogc.features (collection=true with id, geometry, bbox, temporal, idAttribute, layerGroup, style) is served as annotated; every feature set here replaces the annotation's value, and a schema without the annotation (e.g. one resolved from a Model Atlas) is served through these features alone. The geometry is an attribute whose EDataType has the instance class org.geojson.Geometry; a DataSet with neither an annotated nor a configured geometry attribute is a diagnosed configuration error and the collection stays down, as is a DataSet with a query (the base predicate cannot be composed with the collection filters). The feature id is idFeature, else the type's EMF id attribute - a collection needs one.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getCollectionId <em>Collection Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getIdFeature <em>Id Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getGeometryFeature <em>Geometry Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getBboxFeatures <em>Bbox Features</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getTemporalFeature <em>Temporal Feature</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getLayerGroup <em>Layer Group</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getStyle <em>Style</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataServiceConfiguration()
 * @model
 * @generated
 */
@ProviderType
public interface OgcFeaturesDataServiceConfiguration extends DataServiceConfiguration {
	/**
	 * Returns the value of the '<em><b>Collection Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The collection id under the root (GET {urlContext}/collections/{collectionId}). Defaults to the annotation's id, else the outputType's EClass name; must be unique within the service.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Collection Id</em>' attribute.
	 * @see #setCollectionId(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataServiceConfiguration_CollectionId()
	 * @model
	 * @generated
	 */
	String getCollectionId();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getCollectionId <em>Collection Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Collection Id</em>' attribute.
	 * @see #getCollectionId()
	 * @generated
	 */
	void setCollectionId(String value);

	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Title of the collection. Defaults to the annotation's title, else the DataSet's name.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataServiceConfiguration_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Id Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the attribute used as the feature id. Defaults to the annotation's idAttribute, else the EMF id attribute of the type.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id Feature</em>' attribute.
	 * @see #setIdFeature(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataServiceConfiguration_IdFeature()
	 * @model
	 * @generated
	 */
	String getIdFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getIdFeature <em>Id Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id Feature</em>' attribute.
	 * @see #getIdFeature()
	 * @generated
	 */
	void setIdFeature(String value);

	/**
	 * Returns the value of the '<em><b>Geometry Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the attribute holding the feature geometry (EDataType with instance class org.geojson.Geometry, persisted as GeoJSON text). Defaults to the annotation's geometry.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Geometry Feature</em>' attribute.
	 * @see #setGeometryFeature(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataServiceConfiguration_GeometryFeature()
	 * @model
	 * @generated
	 */
	String getGeometryFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getGeometryFeature <em>Geometry Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Geometry Feature</em>' attribute.
	 * @see #getGeometryFeature()
	 * @generated
	 */
	void setGeometryFeature(String value);

	/**
	 * Returns the value of the '<em><b>Bbox Features</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The four attributes minX, minY, maxX, maxY holding the persisted bounding box of a feature, in this order; lets bbox requests push down into the input instead of testing every geometry. Defaults to the annotation's bbox.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Bbox Features</em>' attribute list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataServiceConfiguration_BboxFeatures()
	 * @model upper="4"
	 * @generated
	 */
	EList<String> getBboxFeatures();

	/**
	 * Returns the value of the '<em><b>Temporal Feature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the date/time attribute the datetime parameter filters on. Defaults to the annotation's temporal.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Temporal Feature</em>' attribute.
	 * @see #setTemporalFeature(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataServiceConfiguration_TemporalFeature()
	 * @model
	 * @generated
	 */
	String getTemporalFeature();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getTemporalFeature <em>Temporal Feature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temporal Feature</em>' attribute.
	 * @see #getTemporalFeature()
	 * @generated
	 */
	void setTemporalFeature(String value);

	/**
	 * Returns the value of the '<em><b>Layer Group</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Group the map viewer and the QGIS project put the collection's layer in. Defaults to the annotation's layerGroup.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Layer Group</em>' attribute.
	 * @see #setLayerGroup(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataServiceConfiguration_LayerGroup()
	 * @model
	 * @generated
	 */
	String getLayerGroup();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getLayerGroup <em>Layer Group</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Layer Group</em>' attribute.
	 * @see #getLayerGroup()
	 * @generated
	 */
	void setLayerGroup(String value);

	/**
	 * Returns the value of the '<em><b>Style</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Display style of the layer for the viewer and the QGIS project: a CSS color or a JSON object, as in the annotation. Defaults to the annotation's style.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Style</em>' attribute.
	 * @see #setStyle(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getOgcFeaturesDataServiceConfiguration_Style()
	 * @model
	 * @generated
	 */
	String getStyle();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration#getStyle <em>Style</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Style</em>' attribute.
	 * @see #getStyle()
	 * @generated
	 */
	void setStyle(String value);

} // OgcFeaturesDataServiceConfiguration
