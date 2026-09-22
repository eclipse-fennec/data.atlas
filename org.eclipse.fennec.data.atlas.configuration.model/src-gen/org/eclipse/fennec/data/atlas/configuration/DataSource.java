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

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A reusable data source definition, living in the root's dataSources registry so it can be shared across DataInputs (and tenants).
 * 
 * A data source is realized in exactly one of two modes:
 * - BIND: filter is set. The deployment configured the backend service itself (e.g. a daanse DataSource factory configuration); the filter selects it. Nothing else may be set.
 * - MATERIALIZE: the connection definition of the concrete subtype is set (at least host and database). The Data Atlas creates the backend service from it.
 * Setting both, or neither, is a configuration error - the source is reported and skipped.
 * 
 * Credential rule: the configuration model never carries a credential value. user and password are placeholders resolved in the consuming runtime only: $[env:NAME] for an environment variable or $[secret:NAME] for a file in the runtime's secrets directory. A literal value is rejected.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataSource#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataSource#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataSource#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataSource#getFilter <em>Filter</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataSource()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface DataSource extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unique identifier of this data source definition. A materialized source is selectable by the service property data.atlas.datasource.id=<id>.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataSource_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataSource#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Human-readable name of the data source.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataSource_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataSource#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional description: what the database holds, who owns it.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataSource_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataSource#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * BIND mode: OSGi target filter (LDAP-style) selecting the backend service the deployment configured - a javax.sql.DataSource for JDBC (e.g. (dataSourceName=personsDs)), a MongoDatabase for MongoDB (e.g. (mongo.database.alias=assets)). Mutually exclusive with the connection definition.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Filter</em>' attribute.
	 * @see #setFilter(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataSource_Filter()
	 * @model
	 * @generated
	 */
	String getFilter();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataSource#getFilter <em>Filter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Filter</em>' attribute.
	 * @see #getFilter()
	 * @generated
	 */
	void setFilter(String value);

} // DataSource
