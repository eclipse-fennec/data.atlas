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

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.fennec.data.atlas.configuration.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage
 * @generated
 */
public class DAConfigAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static DAConfigPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DAConfigAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = DAConfigPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DAConfigSwitch<Adapter> modelSwitch =
		new DAConfigSwitch<Adapter>() {
			@Override
			public Adapter caseDataAtlasConfiguration(DataAtlasConfiguration object) {
				return createDataAtlasConfigurationAdapter();
			}
			@Override
			public Adapter caseDataProvider(DataProvider object) {
				return createDataProviderAdapter();
			}
			@Override
			public Adapter caseDataInput(DataInput object) {
				return createDataInputAdapter();
			}
			@Override
			public Adapter caseBridgeRepository(BridgeRepository object) {
				return createBridgeRepositoryAdapter();
			}
			@Override
			public Adapter caseDataSet(DataSet object) {
				return createDataSetAdapter();
			}
			@Override
			public Adapter caseDataService(DataService object) {
				return createDataServiceAdapter();
			}
			@Override
			public Adapter caseRestDataService(RestDataService object) {
				return createRestDataServiceAdapter();
			}
			@Override
			public Adapter caseDataServiceConfiguration(DataServiceConfiguration object) {
				return createDataServiceConfigurationAdapter();
			}
			@Override
			public Adapter caseRestDataServiceConfiguration(RestDataServiceConfiguration object) {
				return createRestDataServiceConfigurationAdapter();
			}
			@Override
			public Adapter caseXMLADataService(XMLADataService object) {
				return createXMLADataServiceAdapter();
			}
			@Override
			public Adapter caseXMLADataServiceConfiguration(XMLADataServiceConfiguration object) {
				return createXMLADataServiceConfigurationAdapter();
			}
			@Override
			public Adapter caseGraphQLDataService(GraphQLDataService object) {
				return createGraphQLDataServiceAdapter();
			}
			@Override
			public Adapter caseQGisDataService(QGisDataService object) {
				return createQGisDataServiceAdapter();
			}
			@Override
			public Adapter caseQGisDataServiceConfiguration(QGisDataServiceConfiguration object) {
				return createQGisDataServiceConfigurationAdapter();
			}
			@Override
			public Adapter caseGraphQLDataServiceConfiguration(GraphQLDataServiceConfiguration object) {
				return createGraphQLDataServiceConfigurationAdapter();
			}
			@Override
			public Adapter caseOgcFeaturesDataService(OgcFeaturesDataService object) {
				return createOgcFeaturesDataServiceAdapter();
			}
			@Override
			public Adapter caseOgcSensorThingsDataService(OgcSensorThingsDataService object) {
				return createOgcSensorThingsDataServiceAdapter();
			}
			@Override
			public Adapter caseMongoRepository(MongoRepository object) {
				return createMongoRepositoryAdapter();
			}
			@Override
			public Adapter caseFileDataInput(FileDataInput object) {
				return createFileDataInputAdapter();
			}
			@Override
			public Adapter caseJPADataInput(JPADataInput object) {
				return createJPADataInputAdapter();
			}
			@Override
			public Adapter caseDataTransformation(DataTransformation object) {
				return createDataTransformationAdapter();
			}
			@Override
			public Adapter caseQueryTransformation(QueryTransformation object) {
				return createQueryTransformationAdapter();
			}
			@Override
			public Adapter caseODataDataService(ODataDataService object) {
				return createODataDataServiceAdapter();
			}
			@Override
			public Adapter caseODataDataServiceConfiguration(ODataDataServiceConfiguration object) {
				return createODataDataServiceConfigurationAdapter();
			}
			@Override
			public Adapter caseDistributionExport(DistributionExport object) {
				return createDistributionExportAdapter();
			}
			@Override
			public Adapter caseCSVDistributionExport(CSVDistributionExport object) {
				return createCSVDistributionExportAdapter();
			}
			@Override
			public Adapter caseTransformation(Transformation object) {
				return createTransformationAdapter();
			}
			@Override
			public Adapter caseDcatPublication(DcatPublication object) {
				return createDcatPublicationAdapter();
			}
			@Override
			public Adapter caseJdbcDataSource(JdbcDataSource object) {
				return createJdbcDataSourceAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration <em>Data Atlas Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration
	 * @generated
	 */
	public Adapter createDataAtlasConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider <em>Data Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataProvider
	 * @generated
	 */
	public Adapter createDataProviderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.DataInput <em>Data Input</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataInput
	 * @generated
	 */
	public Adapter createDataInputAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository <em>Bridge Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.BridgeRepository
	 * @generated
	 */
	public Adapter createBridgeRepositoryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.DataSet <em>Data Set</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataSet
	 * @generated
	 */
	public Adapter createDataSetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.DataService <em>Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataService
	 * @generated
	 */
	public Adapter createDataServiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.RestDataService <em>Rest Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataService
	 * @generated
	 */
	public Adapter createRestDataServiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.DataServiceConfiguration <em>Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataServiceConfiguration
	 * @generated
	 */
	public Adapter createDataServiceConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration <em>Rest Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration
	 * @generated
	 */
	public Adapter createRestDataServiceConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.XMLADataService <em>XMLA Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.XMLADataService
	 * @generated
	 */
	public Adapter createXMLADataServiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration <em>XMLA Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration
	 * @generated
	 */
	public Adapter createXMLADataServiceConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.GraphQLDataService <em>Graph QL Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.GraphQLDataService
	 * @generated
	 */
	public Adapter createGraphQLDataServiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.QGisDataService <em>QGis Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.QGisDataService
	 * @generated
	 */
	public Adapter createQGisDataServiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.QGisDataServiceConfiguration <em>QGis Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.QGisDataServiceConfiguration
	 * @generated
	 */
	public Adapter createQGisDataServiceConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.GraphQLDataServiceConfiguration <em>Graph QL Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.GraphQLDataServiceConfiguration
	 * @generated
	 */
	public Adapter createGraphQLDataServiceConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService <em>Ogc Features Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService
	 * @generated
	 */
	public Adapter createOgcFeaturesDataServiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.OgcSensorThingsDataService <em>Ogc Sensor Things Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.OgcSensorThingsDataService
	 * @generated
	 */
	public Adapter createOgcSensorThingsDataServiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.MongoRepository <em>Mongo Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.MongoRepository
	 * @generated
	 */
	public Adapter createMongoRepositoryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.FileDataInput <em>File Data Input</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.FileDataInput
	 * @generated
	 */
	public Adapter createFileDataInputAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.JPADataInput <em>JPA Data Input</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.JPADataInput
	 * @generated
	 */
	public Adapter createJPADataInputAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.DataTransformation <em>Data Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataTransformation
	 * @generated
	 */
	public Adapter createDataTransformationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.QueryTransformation <em>Query Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.QueryTransformation
	 * @generated
	 */
	public Adapter createQueryTransformationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.ODataDataService <em>OData Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.ODataDataService
	 * @generated
	 */
	public Adapter createODataDataServiceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration <em>OData Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration
	 * @generated
	 */
	public Adapter createODataDataServiceConfigurationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport <em>Distribution Export</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.DistributionExport
	 * @generated
	 */
	public Adapter createDistributionExportAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport <em>CSV Distribution Export</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport
	 * @generated
	 */
	public Adapter createCSVDistributionExportAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.Transformation <em>Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.Transformation
	 * @generated
	 */
	public Adapter createTransformationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication <em>Dcat Publication</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.DcatPublication
	 * @generated
	 */
	public Adapter createDcatPublicationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.eclipse.fennec.data.atlas.configuration.JdbcDataSource <em>Jdbc Data Source</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.eclipse.fennec.data.atlas.configuration.JdbcDataSource
	 * @generated
	 */
	public Adapter createJdbcDataSourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //DAConfigAdapterFactory
