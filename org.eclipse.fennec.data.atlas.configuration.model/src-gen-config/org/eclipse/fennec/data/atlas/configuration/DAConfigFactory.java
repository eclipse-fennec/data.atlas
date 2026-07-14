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

import org.eclipse.emf.ecore.EFactory;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage
 * @generated
 */
@ProviderType
public interface DAConfigFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DAConfigFactory eINSTANCE = org.eclipse.fennec.data.atlas.configuration.impl.DAConfigFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Bridge Repository</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Bridge Repository</em>'.
	 * @generated
	 */
	BridgeRepository createBridgeRepository();

	/**
	 * Returns a new object of class '<em>Data Set</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Set</em>'.
	 * @generated
	 */
	DataSet createDataSet();

	/**
	 * Returns a new object of class '<em>Rest Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rest Data Service</em>'.
	 * @generated
	 */
	RestDataService createRestDataService();

	/**
	 * Returns a new object of class '<em>Rest Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Rest Data Service Configuration</em>'.
	 * @generated
	 */
	RestDataServiceConfiguration createRestDataServiceConfiguration();

	/**
	 * Returns a new object of class '<em>XMLA Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>XMLA Data Service</em>'.
	 * @generated
	 */
	XMLADataService createXMLADataService();

	/**
	 * Returns a new object of class '<em>XMLA Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>XMLA Data Service Configuration</em>'.
	 * @generated
	 */
	XMLADataServiceConfiguration createXMLADataServiceConfiguration();

	/**
	 * Returns a new object of class '<em>Graph QL Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Graph QL Data Service</em>'.
	 * @generated
	 */
	GraphQLDataService createGraphQLDataService();

	/**
	 * Returns a new object of class '<em>QGis Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>QGis Data Service</em>'.
	 * @generated
	 */
	QGisDataService createQGisDataService();

	/**
	 * Returns a new object of class '<em>QGis Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>QGis Data Service Configuration</em>'.
	 * @generated
	 */
	QGisDataServiceConfiguration createQGisDataServiceConfiguration();

	/**
	 * Returns a new object of class '<em>Graph QL Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Graph QL Data Service Configuration</em>'.
	 * @generated
	 */
	GraphQLDataServiceConfiguration createGraphQLDataServiceConfiguration();

	/**
	 * Returns a new object of class '<em>Ogc Features Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ogc Features Data Service</em>'.
	 * @generated
	 */
	OgcFeaturesDataService createOgcFeaturesDataService();

	/**
	 * Returns a new object of class '<em>Ogc Sensor Things Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Ogc Sensor Things Data Service</em>'.
	 * @generated
	 */
	OgcSensorThingsDataService createOgcSensorThingsDataService();

	/**
	 * Returns a new object of class '<em>Mongo Repository</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Mongo Repository</em>'.
	 * @generated
	 */
	MongoRepository createMongoRepository();

	/**
	 * Returns a new object of class '<em>File Data Input</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>File Data Input</em>'.
	 * @generated
	 */
	FileDataInput createFileDataInput();

	/**
	 * Returns a new object of class '<em>JPA Data Input</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>JPA Data Input</em>'.
	 * @generated
	 */
	JPADataInput createJPADataInput();

	/**
	 * Returns a new object of class '<em>Data Transformation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Data Transformation</em>'.
	 * @generated
	 */
	DataTransformation createDataTransformation();

	/**
	 * Returns a new object of class '<em>Query Transformation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Query Transformation</em>'.
	 * @generated
	 */
	QueryTransformation createQueryTransformation();

	/**
	 * Returns a new object of class '<em>OData Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>OData Data Service</em>'.
	 * @generated
	 */
	ODataDataService createODataDataService();

	/**
	 * Returns a new object of class '<em>OData Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>OData Data Service Configuration</em>'.
	 * @generated
	 */
	ODataDataServiceConfiguration createODataDataServiceConfiguration();

	/**
	 * Returns a new object of class '<em>Distribution Export</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Distribution Export</em>'.
	 * @generated
	 */
	DistributionExport createDistributionExport();

	/**
	 * Returns a new object of class '<em>CSV Distribution Export</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>CSV Distribution Export</em>'.
	 * @generated
	 */
	CSVDistributionExport createCSVDistributionExport();

	/**
	 * Returns a new object of class '<em>Jdbc Data Source</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Jdbc Data Source</em>'.
	 * @generated
	 */
	JdbcDataSource createJdbcDataSource();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DAConfigPackage getDAConfigPackage();

} //DAConfigFactory
