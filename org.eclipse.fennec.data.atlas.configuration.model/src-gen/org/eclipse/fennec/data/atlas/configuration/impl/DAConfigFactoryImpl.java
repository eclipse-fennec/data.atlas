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
package org.eclipse.fennec.data.atlas.configuration.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.fennec.data.atlas.configuration.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DAConfigFactoryImpl extends EFactoryImpl implements DAConfigFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DAConfigFactory init() {
		try {
			DAConfigFactory theDAConfigFactory = (DAConfigFactory)EPackage.Registry.INSTANCE.getEFactory(DAConfigPackage.eNS_URI);
			if (theDAConfigFactory != null) {
				return theDAConfigFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DAConfigFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DAConfigFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION: return createDataAtlasConfiguration();
			case DAConfigPackage.BRIDGE_REPOSITORY: return createBridgeRepository();
			case DAConfigPackage.DATA_SET: return createDataSet();
			case DAConfigPackage.REST_DATA_SERVICE: return createRestDataService();
			case DAConfigPackage.REST_DATA_SERVICE_CONFIGURATION: return createRestDataServiceConfiguration();
			case DAConfigPackage.XMLA_DATA_SERVICE: return createXMLADataService();
			case DAConfigPackage.XMLA_DATA_SERVICE_CONFIGURATION: return createXMLADataServiceConfiguration();
			case DAConfigPackage.GRAPH_QL_DATA_SERVICE: return createGraphQLDataService();
			case DAConfigPackage.QGIS_DATA_SERVICE: return createQGisDataService();
			case DAConfigPackage.QGIS_DATA_SERVICE_CONFIGURATION: return createQGisDataServiceConfiguration();
			case DAConfigPackage.GRAPH_QL_DATA_SERVICE_CONFIGURATION: return createGraphQLDataServiceConfiguration();
			case DAConfigPackage.OGC_FEATURES_DATA_SERVICE: return createOgcFeaturesDataService();
			case DAConfigPackage.OGC_SENSOR_THINGS_DATA_SERVICE: return createOgcSensorThingsDataService();
			case DAConfigPackage.MONGO_REPOSITORY: return createMongoRepository();
			case DAConfigPackage.FILE_DATA_INPUT: return createFileDataInput();
			case DAConfigPackage.JPA_DATA_INPUT: return createJPADataInput();
			case DAConfigPackage.DATA_TRANSFORMATION: return createDataTransformation();
			case DAConfigPackage.QUERY_TRANSFORMATION: return createQueryTransformation();
			case DAConfigPackage.ODATA_DATA_SERVICE: return createODataDataService();
			case DAConfigPackage.ODATA_DATA_SERVICE_CONFIGURATION: return createODataDataServiceConfiguration();
			case DAConfigPackage.DISTRIBUTION_EXPORT: return createDistributionExport();
			case DAConfigPackage.CSV_DISTRIBUTION_EXPORT: return createCSVDistributionExport();
			case DAConfigPackage.DCAT_PUBLICATION: return createDcatPublication();
			case DAConfigPackage.JDBC_DATA_SOURCE: return createJdbcDataSource();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DataAtlasConfiguration createDataAtlasConfiguration() {
		DataAtlasConfigurationImpl dataAtlasConfiguration = new DataAtlasConfigurationImpl();
		return dataAtlasConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BridgeRepository createBridgeRepository() {
		BridgeRepositoryImpl bridgeRepository = new BridgeRepositoryImpl();
		return bridgeRepository;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DataSet createDataSet() {
		DataSetImpl dataSet = new DataSetImpl();
		return dataSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RestDataService createRestDataService() {
		RestDataServiceImpl restDataService = new RestDataServiceImpl();
		return restDataService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RestDataServiceConfiguration createRestDataServiceConfiguration() {
		RestDataServiceConfigurationImpl restDataServiceConfiguration = new RestDataServiceConfigurationImpl();
		return restDataServiceConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public XMLADataService createXMLADataService() {
		XMLADataServiceImpl xmlaDataService = new XMLADataServiceImpl();
		return xmlaDataService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public XMLADataServiceConfiguration createXMLADataServiceConfiguration() {
		XMLADataServiceConfigurationImpl xmlaDataServiceConfiguration = new XMLADataServiceConfigurationImpl();
		return xmlaDataServiceConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GraphQLDataService createGraphQLDataService() {
		GraphQLDataServiceImpl graphQLDataService = new GraphQLDataServiceImpl();
		return graphQLDataService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public QGisDataService createQGisDataService() {
		QGisDataServiceImpl qGisDataService = new QGisDataServiceImpl();
		return qGisDataService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public QGisDataServiceConfiguration createQGisDataServiceConfiguration() {
		QGisDataServiceConfigurationImpl qGisDataServiceConfiguration = new QGisDataServiceConfigurationImpl();
		return qGisDataServiceConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GraphQLDataServiceConfiguration createGraphQLDataServiceConfiguration() {
		GraphQLDataServiceConfigurationImpl graphQLDataServiceConfiguration = new GraphQLDataServiceConfigurationImpl();
		return graphQLDataServiceConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OgcFeaturesDataService createOgcFeaturesDataService() {
		OgcFeaturesDataServiceImpl ogcFeaturesDataService = new OgcFeaturesDataServiceImpl();
		return ogcFeaturesDataService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OgcSensorThingsDataService createOgcSensorThingsDataService() {
		OgcSensorThingsDataServiceImpl ogcSensorThingsDataService = new OgcSensorThingsDataServiceImpl();
		return ogcSensorThingsDataService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MongoRepository createMongoRepository() {
		MongoRepositoryImpl mongoRepository = new MongoRepositoryImpl();
		return mongoRepository;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FileDataInput createFileDataInput() {
		FileDataInputImpl fileDataInput = new FileDataInputImpl();
		return fileDataInput;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public JPADataInput createJPADataInput() {
		JPADataInputImpl jpaDataInput = new JPADataInputImpl();
		return jpaDataInput;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DataTransformation createDataTransformation() {
		DataTransformationImpl dataTransformation = new DataTransformationImpl();
		return dataTransformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public QueryTransformation createQueryTransformation() {
		QueryTransformationImpl queryTransformation = new QueryTransformationImpl();
		return queryTransformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ODataDataService createODataDataService() {
		ODataDataServiceImpl oDataDataService = new ODataDataServiceImpl();
		return oDataDataService;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ODataDataServiceConfiguration createODataDataServiceConfiguration() {
		ODataDataServiceConfigurationImpl oDataDataServiceConfiguration = new ODataDataServiceConfigurationImpl();
		return oDataDataServiceConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DistributionExport createDistributionExport() {
		DistributionExportImpl distributionExport = new DistributionExportImpl();
		return distributionExport;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CSVDistributionExport createCSVDistributionExport() {
		CSVDistributionExportImpl csvDistributionExport = new CSVDistributionExportImpl();
		return csvDistributionExport;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DcatPublication createDcatPublication() {
		DcatPublicationImpl dcatPublication = new DcatPublicationImpl();
		return dcatPublication;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public JdbcDataSource createJdbcDataSource() {
		JdbcDataSourceImpl jdbcDataSource = new JdbcDataSourceImpl();
		return jdbcDataSource;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DAConfigPackage getDAConfigPackage() {
		return (DAConfigPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DAConfigPackage getPackage() {
		return DAConfigPackage.eINSTANCE;
	}

} //DAConfigFactoryImpl
