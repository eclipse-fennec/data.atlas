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

import org.eclipse.fennec.persistence.eorm.EntityMappings;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>JPA Data Input</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * DataInput backed by any relational data source supported by e.g. EclipseLink (JPA).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.JPADataInput#getPersistenceConfig <em>Persistence Config</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.JPADataInput#getDataSource <em>Data Source</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getJPADataInput()
 * @model
 * @generated
 */
@ProviderType
public interface JPADataInput extends DataInput {
	/**
	 * Returns the value of the '<em><b>Persistence Config</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * JPA entity mapping (the EclipseLink orm.xml equivalent) describing how the model types map to the relational schema. Set it when the derived naming cannot address the schema - e.g. lower-case or schema-qualified tables, or a table without a primary key that needs a declared composite id.
	 * Containment, deliberately: the mapping has to travel WITH the configuration. A non-containment href into a deployment-local file cannot be resolved by a Model Atlas that serves the configuration, which fails the upload with an unresolved EClass proxy. If a mapping ever needs to be shared between inputs, that belongs in a registry of the root, like the other reusables.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Persistence Config</em>' containment reference.
	 * @see #setPersistenceConfig(EntityMappings)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getJPADataInput_PersistenceConfig()
	 * @model containment="true"
	 * @generated
	 */
	EntityMappings getPersistenceConfig();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.JPADataInput#getPersistenceConfig <em>Persistence Config</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Persistence Config</em>' containment reference.
	 * @see #getPersistenceConfig()
	 * @generated
	 */
	void setPersistenceConfig(EntityMappings value);

	/**
	 * Returns the value of the '<em><b>Data Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The JDBC data source definition providing the database connection for this input.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Data Source</em>' reference.
	 * @see #setDataSource(JdbcDataSource)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getJPADataInput_DataSource()
	 * @model required="true"
	 * @generated
	 */
	JdbcDataSource getDataSource();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.JPADataInput#getDataSource <em>Data Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Source</em>' reference.
	 * @see #getDataSource()
	 * @generated
	 */
	void setDataSource(JdbcDataSource value);

} // JPADataInput
