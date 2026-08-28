/**
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
package org.eclipse.fennec.data.atlas.configuration.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.fennec.data.atlas.configuration.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage
 * @generated
 */
public class DAConfigSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static DAConfigPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DAConfigSwitch() {
		if (modelPackage == null) {
			modelPackage = DAConfigPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION: {
				DataAtlasConfiguration dataAtlasConfiguration = (DataAtlasConfiguration)theEObject;
				T result = caseDataAtlasConfiguration(dataAtlasConfiguration);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.DATA_PROVIDER: {
				DataProvider dataProvider = (DataProvider)theEObject;
				T result = caseDataProvider(dataProvider);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.DATA_INPUT: {
				DataInput dataInput = (DataInput)theEObject;
				T result = caseDataInput(dataInput);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.BRIDGE_REPOSITORY: {
				BridgeRepository bridgeRepository = (BridgeRepository)theEObject;
				T result = caseBridgeRepository(bridgeRepository);
				if (result == null) result = caseDataInput(bridgeRepository);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.DATA_SET: {
				DataSet dataSet = (DataSet)theEObject;
				T result = caseDataSet(dataSet);
				if (result == null) result = caseDataProvider(dataSet);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.DATA_SERVICE: {
				DataService dataService = (DataService)theEObject;
				T result = caseDataService(dataService);
				if (result == null) result = caseDataProvider(dataService);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.REST_DATA_SERVICE: {
				RestDataService restDataService = (RestDataService)theEObject;
				T result = caseRestDataService(restDataService);
				if (result == null) result = caseDataService(restDataService);
				if (result == null) result = caseDataProvider(restDataService);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.DATA_SERVICE_CONFIGURATION: {
				DataServiceConfiguration dataServiceConfiguration = (DataServiceConfiguration)theEObject;
				T result = caseDataServiceConfiguration(dataServiceConfiguration);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION: {
				RestDataServiceConfiguration restDataServiceConfiguration = (RestDataServiceConfiguration)theEObject;
				T result = caseRestDataServiceConfiguration(restDataServiceConfiguration);
				if (result == null) result = caseDataServiceConfiguration(restDataServiceConfiguration);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.XMLA_DATA_SERVICE: {
				XMLADataService xmlaDataService = (XMLADataService)theEObject;
				T result = caseXMLADataService(xmlaDataService);
				if (result == null) result = caseDataService(xmlaDataService);
				if (result == null) result = caseDataProvider(xmlaDataService);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.XMLA_DATA_SERVICE_CONFIGURATION: {
				XMLADataServiceConfiguration xmlaDataServiceConfiguration = (XMLADataServiceConfiguration)theEObject;
				T result = caseXMLADataServiceConfiguration(xmlaDataServiceConfiguration);
				if (result == null) result = caseDataServiceConfiguration(xmlaDataServiceConfiguration);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.GRAPH_QL_DATA_SERVICE: {
				GraphQLDataService graphQLDataService = (GraphQLDataService)theEObject;
				T result = caseGraphQLDataService(graphQLDataService);
				if (result == null) result = caseDataService(graphQLDataService);
				if (result == null) result = caseDataProvider(graphQLDataService);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.QGIS_DATA_SERVICE: {
				QGisDataService qGisDataService = (QGisDataService)theEObject;
				T result = caseQGisDataService(qGisDataService);
				if (result == null) result = caseDataService(qGisDataService);
				if (result == null) result = caseDataProvider(qGisDataService);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.QGIS_DATA_SERVICE_CONFIGURATION: {
				QGisDataServiceConfiguration qGisDataServiceConfiguration = (QGisDataServiceConfiguration)theEObject;
				T result = caseQGisDataServiceConfiguration(qGisDataServiceConfiguration);
				if (result == null) result = caseDataServiceConfiguration(qGisDataServiceConfiguration);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.GRAPH_QL_DATA_SERVICE_CONFIGURATION: {
				GraphQLDataServiceConfiguration graphQLDataServiceConfiguration = (GraphQLDataServiceConfiguration)theEObject;
				T result = caseGraphQLDataServiceConfiguration(graphQLDataServiceConfiguration);
				if (result == null) result = caseDataServiceConfiguration(graphQLDataServiceConfiguration);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE: {
				OgcFeaturesDataService ogcFeaturesDataService = (OgcFeaturesDataService)theEObject;
				T result = caseOgcFeaturesDataService(ogcFeaturesDataService);
				if (result == null) result = caseDataService(ogcFeaturesDataService);
				if (result == null) result = caseDataProvider(ogcFeaturesDataService);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.OGC_SENSOR_THINGS_DATA_SERVICE: {
				OgcSensorThingsDataService ogcSensorThingsDataService = (OgcSensorThingsDataService)theEObject;
				T result = caseOgcSensorThingsDataService(ogcSensorThingsDataService);
				if (result == null) result = caseDataService(ogcSensorThingsDataService);
				if (result == null) result = caseDataProvider(ogcSensorThingsDataService);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.MONGO_REPOSITORY: {
				MongoRepository mongoRepository = (MongoRepository)theEObject;
				T result = caseMongoRepository(mongoRepository);
				if (result == null) result = caseDataInput(mongoRepository);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.FILE_DATA_INPUT: {
				FileDataInput fileDataInput = (FileDataInput)theEObject;
				T result = caseFileDataInput(fileDataInput);
				if (result == null) result = caseDataInput(fileDataInput);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.JPA_DATA_INPUT: {
				JPADataInput jpaDataInput = (JPADataInput)theEObject;
				T result = caseJPADataInput(jpaDataInput);
				if (result == null) result = caseDataInput(jpaDataInput);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.DATA_TRANSFORMATION: {
				DataTransformation dataTransformation = (DataTransformation)theEObject;
				T result = caseDataTransformation(dataTransformation);
				if (result == null) result = caseTransformation(dataTransformation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.QUERY_TRANSFORMATION: {
				QueryTransformation queryTransformation = (QueryTransformation)theEObject;
				T result = caseQueryTransformation(queryTransformation);
				if (result == null) result = caseTransformation(queryTransformation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.ODATA_DATA_SERVICE: {
				ODataDataService oDataDataService = (ODataDataService)theEObject;
				T result = caseODataDataService(oDataDataService);
				if (result == null) result = caseDataService(oDataDataService);
				if (result == null) result = caseDataProvider(oDataDataService);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION: {
				ODataDataServiceConfiguration oDataDataServiceConfiguration = (ODataDataServiceConfiguration)theEObject;
				T result = caseODataDataServiceConfiguration(oDataDataServiceConfiguration);
				if (result == null) result = caseDataServiceConfiguration(oDataDataServiceConfiguration);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.DISTRIBUTION_EXPORT: {
				DistributionExport distributionExport = (DistributionExport)theEObject;
				T result = caseDistributionExport(distributionExport);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT: {
				CSVDistributionExport csvDistributionExport = (CSVDistributionExport)theEObject;
				T result = caseCSVDistributionExport(csvDistributionExport);
				if (result == null) result = caseDistributionExport(csvDistributionExport);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.TRANSFORMATION: {
				Transformation transformation = (Transformation)theEObject;
				T result = caseTransformation(transformation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.DCAT_PUBLICATION: {
				DcatPublication dcatPublication = (DcatPublication)theEObject;
				T result = caseDcatPublication(dcatPublication);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case DAConfigPackage.JDBC_DATA_SOURCE: {
				JdbcDataSource jdbcDataSource = (JdbcDataSource)theEObject;
				T result = caseJdbcDataSource(jdbcDataSource);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Atlas Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Atlas Configuration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataAtlasConfiguration(DataAtlasConfiguration object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Provider</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Provider</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataProvider(DataProvider object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Input</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Input</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataInput(DataInput object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Bridge Repository</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Bridge Repository</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBridgeRepository(BridgeRepository object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Set</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Set</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataSet(DataSet object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Service</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataService(DataService object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Rest Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rest Data Service</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRestDataService(RestDataService object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Service Configuration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataServiceConfiguration(DataServiceConfiguration object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Rest Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Rest Data Service Configuration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRestDataServiceConfiguration(RestDataServiceConfiguration object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>XMLA Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>XMLA Data Service</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseXMLADataService(XMLADataService object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>XMLA Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>XMLA Data Service Configuration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseXMLADataServiceConfiguration(XMLADataServiceConfiguration object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Graph QL Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Graph QL Data Service</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGraphQLDataService(GraphQLDataService object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>QGis Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>QGis Data Service</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseQGisDataService(QGisDataService object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>QGis Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>QGis Data Service Configuration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseQGisDataServiceConfiguration(QGisDataServiceConfiguration object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Graph QL Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Graph QL Data Service Configuration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseGraphQLDataServiceConfiguration(GraphQLDataServiceConfiguration object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ogc Features Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ogc Features Data Service</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOgcFeaturesDataService(OgcFeaturesDataService object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Ogc Sensor Things Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Ogc Sensor Things Data Service</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOgcSensorThingsDataService(OgcSensorThingsDataService object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mongo Repository</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mongo Repository</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMongoRepository(MongoRepository object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>File Data Input</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>File Data Input</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFileDataInput(FileDataInput object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>JPA Data Input</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>JPA Data Input</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseJPADataInput(JPADataInput object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Data Transformation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Data Transformation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDataTransformation(DataTransformation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Query Transformation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Query Transformation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseQueryTransformation(QueryTransformation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>OData Data Service</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>OData Data Service</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseODataDataService(ODataDataService object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>OData Data Service Configuration</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>OData Data Service Configuration</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseODataDataServiceConfiguration(ODataDataServiceConfiguration object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Distribution Export</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Distribution Export</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDistributionExport(DistributionExport object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>CSV Distribution Export</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>CSV Distribution Export</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCSVDistributionExport(CSVDistributionExport object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Transformation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Transformation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTransformation(Transformation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Dcat Publication</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Dcat Publication</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDcatPublication(DcatPublication object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Jdbc Data Source</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Jdbc Data Source</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseJdbcDataSource(JdbcDataSource object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //DAConfigSwitch
