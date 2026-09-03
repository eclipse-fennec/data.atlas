/**
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
 */
package org.eclipse.fennec.data.atlas.rest;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration;
import org.geojson.Coordinates;
import org.geojson.Feature;
import org.geojson.GeoJsonFactory;
import org.geojson.Geometry;
import org.geojson.Point;

/**
 * Maps domain EObjects of one DataSet onto RFC 7946 {@code Feature}s, driven by
 * the {@code GeoJsonDataServiceConfiguration}: the geometry comes either from a
 * feature already holding a {@code org.geojson.model} {@link Geometry}
 * (passthrough, copied) or from the configured longitude/latitude attribute
 * pair (a {@link Point}, elevation optionally third); the {@code Feature} id
 * comes from the configured attribute, else the type's EMF id attribute; the
 * whole source object — minus the geometry-consumed features — becomes the
 * {@code properties}. Coordinates are WGS 84 by RFC 7946 §4; transforming them
 * is a {@code Transformation} concern, not a mapping one.
 *
 * <p>
 * The mapping is validated once against the DataSet's type at endpoint
 * registration ({@link #create}); a misconfigured mapping never becomes an
 * endpoint (the established fail-early gating).
 * </p>
 */
final class GeoJsonFeatureMapper {

	private final EAttribute longitude;
	private final EAttribute latitude;
	private final EAttribute elevation;
	private final EReference geometry;
	private final EAttribute id;

	private GeoJsonFeatureMapper(EAttribute longitude, EAttribute latitude, EAttribute elevation,
			EReference geometry, EAttribute id) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.elevation = elevation;
		this.geometry = geometry;
		this.id = id;
	}

	/**
	 * Builds and validates the mapper for one DataSet type.
	 *
	 * @param configuration the per-DataSet GeoJSON configuration
	 * @param type          the type the endpoint serves (the DataSet's inputType)
	 * @return the mapper
	 * @throws IllegalArgumentException naming every problem — the caller logs it
	 *                                  and skips the endpoint
	 */
	static GeoJsonFeatureMapper create(GeoJsonDataServiceConfiguration configuration, EClass type) {
		List<String> problems = new ArrayList<>();
		boolean pointMapping = isSet(configuration.getLongitudeFeature())
				|| isSet(configuration.getLatitudeFeature());
		boolean passthrough = isSet(configuration.getGeometryFeature());
		if (pointMapping == passthrough) {
			problems.add("the geometry source must be EITHER geometryFeature OR the "
					+ "longitudeFeature/latitudeFeature pair");
		}
		EAttribute longitude = null;
		EAttribute latitude = null;
		EAttribute elevation = null;
		EReference geometry = null;
		if (pointMapping && !passthrough) {
			longitude = numericAttribute(type, configuration.getLongitudeFeature(), "longitudeFeature", problems);
			latitude = numericAttribute(type, configuration.getLatitudeFeature(), "latitudeFeature", problems);
			if (isSet(configuration.getElevationFeature())) {
				elevation = numericAttribute(type, configuration.getElevationFeature(), "elevationFeature", problems);
			}
		}
		if (passthrough && !pointMapping) {
			EStructuralFeature feature = type.getEStructuralFeature(configuration.getGeometryFeature());
			if (feature instanceof EReference reference
					&& reference.getEReferenceType() != null
					&& (reference.getEReferenceType() == org.geojson.GeoJsonPackage.eINSTANCE.getGeometry()
							|| org.geojson.GeoJsonPackage.eINSTANCE.getGeometry()
									.isSuperTypeOf(reference.getEReferenceType()))) {
				geometry = reference;
			} else {
				problems.add("geometryFeature '" + configuration.getGeometryFeature()
						+ "' is no reference to a org.geojson.model Geometry on '" + type.getName() + "'");
			}
		}
		EAttribute id;
		if (isSet(configuration.getIdFeature())) {
			EStructuralFeature feature = type.getEStructuralFeature(configuration.getIdFeature());
			if (feature instanceof EAttribute attribute) {
				id = attribute;
			} else {
				id = null;
				problems.add("idFeature '" + configuration.getIdFeature() + "' is no attribute of '"
						+ type.getName() + "'");
			}
		} else {
			id = type.getEIDAttribute(); // may be null: a Feature id is optional
		}
		if (!problems.isEmpty()) {
			throw new IllegalArgumentException(String.join("; ", problems));
		}
		return new GeoJsonFeatureMapper(longitude, latitude, elevation, geometry, id);
	}

	private static EAttribute numericAttribute(EClass type, String name, String role, List<String> problems) {
		EStructuralFeature feature = type.getEStructuralFeature(name);
		if (!(feature instanceof EAttribute attribute)) {
			problems.add(role + " '" + name + "' is no attribute of '" + type.getName() + "'");
			return null;
		}
		Class<?> instanceClass = attribute.getEAttributeType() == null ? null
				: attribute.getEAttributeType().getInstanceClass();
		boolean numeric = instanceClass != null && (Number.class.isAssignableFrom(instanceClass)
				|| instanceClass == double.class || instanceClass == float.class || instanceClass == int.class
				|| instanceClass == long.class || instanceClass == short.class || instanceClass == byte.class);
		if (!numeric) {
			problems.add(role + " '" + name + "' of '" + type.getName() + "' is not numeric");
			return null;
		}
		return attribute;
	}

	private static boolean isSet(String value) {
		return value != null && !value.isBlank();
	}

	/** Maps one source object onto a detached {@code Feature}. */
	Feature map(EObject source) {
		Feature feature = GeoJsonFactory.eINSTANCE.createFeature();
		if (id != null && source.eIsSet(id)) {
			feature.setId(EcoreUtil.convertToString(id.getEAttributeType(), source.eGet(id)));
		}
		feature.setGeometry(mapGeometry(source));
		// the source object itself becomes the properties, minus the features
		// the geometry consumed (the id stays - the payload remains complete)
		EObject properties = EcoreUtil.copy(source);
		for (EStructuralFeature consumed : new EStructuralFeature[] { longitude, latitude, elevation, geometry }) {
			if (consumed != null) {
				properties.eUnset(consumed);
			}
		}
		feature.setProperties(properties);
		return feature;
	}

	/** The geometry, or {@code null} when the source carries no values (RFC 7946 allows it). */
	private Geometry mapGeometry(EObject source) {
		if (geometry != null) {
			Object value = source.eGet(geometry);
			return value instanceof Geometry g ? EcoreUtil.copy(g) : null;
		}
		Object lon = longitude == null ? null : source.eGet(longitude);
		Object lat = latitude == null ? null : source.eGet(latitude);
		if (!(lon instanceof Number lonNumber) || !(lat instanceof Number latNumber)) {
			return null;
		}
		Coordinates coordinates = GeoJsonFactory.eINSTANCE.createCoordinates();
		coordinates.setLongitude(lonNumber.doubleValue());
		coordinates.setLatitude(latNumber.doubleValue());
		if (elevation != null && source.eGet(elevation) instanceof Number elevationNumber) {
			coordinates.setElevation(elevationNumber.doubleValue());
		}
		Point point = GeoJsonFactory.eINSTANCE.createPoint();
		point.setCoordinates(coordinates);
		return point;
	}
}
