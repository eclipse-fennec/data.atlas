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

import org.eclipse.fennec.atlas.configuration.DataService;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Atlas Service</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract definition of a Data-Atlas endpoint to publish DataSets: the shared DataService (urlContext) with the Data-Atlas provider features (transformation, distribution exports, publication).
 * As a DataProvider a DataService can also act standalone - serving its own dataInput generically in the formats given by its distributionExport - or it can contain refined DataSets (via its configurations). Each configuration of a published DataService results in a DCAT Distribution.
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataAtlasService()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface DataAtlasService extends DataService, DataAtlasProvider {
} // DataAtlasService
