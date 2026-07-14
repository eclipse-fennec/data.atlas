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

import org.eclipse.emf.ecore.EClass;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Ogc Features Data Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class OgcFeaturesDataServiceImpl extends DataServiceImpl implements OgcFeaturesDataService {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OgcFeaturesDataServiceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.OGC_FEATURES_DATA_SERVICE;
	}

} //OgcFeaturesDataServiceImpl
