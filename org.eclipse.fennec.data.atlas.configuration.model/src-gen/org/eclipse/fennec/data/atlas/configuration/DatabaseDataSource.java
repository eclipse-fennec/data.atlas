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
 * A representation of the model object '<em><b>Database Data Source</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A data source addressed by network coordinates: the connection definition shared by all database kinds (MATERIALIZE mode). host and database are the minimum; the rest is optional.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getHost <em>Host</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getPort <em>Port</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getDatabase <em>Database</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getUser <em>User</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getPassword <em>Password</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getProperties <em>Properties</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDatabaseDataSource()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface DatabaseDataSource extends DataSource {
	/**
	 * Returns the value of the '<em><b>Host</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Database host name or address. May be a $[env:NAME] placeholder.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Host</em>' attribute.
	 * @see #setHost(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDatabaseDataSource_Host()
	 * @model
	 * @generated
	 */
	String getHost();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getHost <em>Host</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Host</em>' attribute.
	 * @see #getHost()
	 * @generated
	 */
	void setHost(String value);

	/**
	 * Returns the value of the '<em><b>Port</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Database port; unset = the driver's default (5432 for PostgreSQL, 27017 for MongoDB).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Port</em>' attribute.
	 * @see #setPort(Integer)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDatabaseDataSource_Port()
	 * @model
	 * @generated
	 */
	Integer getPort();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getPort <em>Port</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port</em>' attribute.
	 * @see #getPort()
	 * @generated
	 */
	void setPort(Integer value);

	/**
	 * Returns the value of the '<em><b>Database</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the database (for H2: the database identifier - a name or path).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Database</em>' attribute.
	 * @see #setDatabase(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDatabaseDataSource_Database()
	 * @model
	 * @generated
	 */
	String getDatabase();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getDatabase <em>Database</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Database</em>' attribute.
	 * @see #getDatabase()
	 * @generated
	 */
	void setDatabase(String value);

	/**
	 * Returns the value of the '<em><b>User</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Login user - a $[env:NAME] or $[secret:NAME] placeholder ONLY, never a literal (see DataSource). Unset for databases without authentication.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>User</em>' attribute.
	 * @see #setUser(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDatabaseDataSource_User()
	 * @model
	 * @generated
	 */
	String getUser();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getUser <em>User</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User</em>' attribute.
	 * @see #getUser()
	 * @generated
	 */
	void setUser(String value);

	/**
	 * Returns the value of the '<em><b>Password</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Login password - a $[env:NAME] or $[secret:NAME] placeholder ONLY, never a literal (see DataSource).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Password</em>' attribute.
	 * @see #setPassword(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDatabaseDataSource_Password()
	 * @model
	 * @generated
	 */
	String getPassword();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DatabaseDataSource#getPassword <em>Password</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Password</em>' attribute.
	 * @see #getPassword()
	 * @generated
	 */
	void setPassword(String value);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.data.atlas.configuration.ConnectionProperty}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Additional driver-specific settings, passed through verbatim to the backend's factory configuration (e.g. sslMode=require for PostgreSQL, dbCloseDelay=-1 for H2, flavor=ferretdb for MongoDB). They cannot override the keys derived from the coordinate attributes.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDatabaseDataSource_Properties()
	 * @model containment="true"
	 * @generated
	 */
	EList<ConnectionProperty> getProperties();

} // DatabaseDataSource
