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

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mongo Data Input</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * DataInput backed by a MongoDB database: the fennec Mongo persistence backend reads the collections through the BSON codec, so documents have to follow its layout (EMF id as _id, type discriminator, references as URIs). No mapping is needed - a registered EPackage is all the backend requires.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.MongoDataInput#getDataSource <em>Data Source</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getMongoDataInput()
 * @model
 * @generated
 */
@ProviderType
public interface MongoDataInput extends DataInput {
	/**
	 * Returns the value of the '<em><b>Data Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The MongoDB data source definition providing the database for this input.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data Source</em>' reference.
	 * @see #setDataSource(MongoDataSource)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getMongoDataInput_DataSource()
	 * @model required="true"
	 * @generated
	 */
	MongoDataSource getDataSource();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.MongoDataInput#getDataSource <em>Data Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Source</em>' reference.
	 * @see #getDataSource()
	 * @generated
	 */
	void setDataSource(MongoDataSource value);

} // MongoDataInput
