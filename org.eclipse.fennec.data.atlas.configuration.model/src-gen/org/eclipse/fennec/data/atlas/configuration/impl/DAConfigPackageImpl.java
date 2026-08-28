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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.fennec.data.atlas.configuration.BridgeRepository;
import org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport;
import org.eclipse.fennec.data.atlas.configuration.DAConfigFactory;
import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.DataProvider;
import org.eclipse.fennec.data.atlas.configuration.DataService;
import org.eclipse.fennec.data.atlas.configuration.DataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.DataTransformation;
import org.eclipse.fennec.data.atlas.configuration.DcatPublication;
import org.eclipse.fennec.data.atlas.configuration.DistributionExport;
import org.eclipse.fennec.data.atlas.configuration.FileDataInput;
import org.eclipse.fennec.data.atlas.configuration.GraphQLDataService;
import org.eclipse.fennec.data.atlas.configuration.GraphQLDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.JPADataInput;
import org.eclipse.fennec.data.atlas.configuration.JdbcDataSource;
import org.eclipse.fennec.data.atlas.configuration.MongoRepository;
import org.eclipse.fennec.data.atlas.configuration.ODataDataService;
import org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService;
import org.eclipse.fennec.data.atlas.configuration.OgcSensorThingsDataService;
import org.eclipse.fennec.data.atlas.configuration.QGisDataService;
import org.eclipse.fennec.data.atlas.configuration.QGisDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.QueryTransformation;
import org.eclipse.fennec.data.atlas.configuration.RestDataService;
import org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.Transformation;
import org.eclipse.fennec.data.atlas.configuration.XMLADataService;
import org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration;

import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage;

import org.eclipse.fennec.model.expression.ExpressionPackage;

import org.eclipse.fennec.model.query.QueryPackage;

import org.eclipse.fennec.persistence.eorm.EORMPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DAConfigPackageImpl extends EPackageImpl implements DAConfigPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataAtlasConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataProviderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataInputEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass bridgeRepositoryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataSetEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass restDataServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataServiceConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass restDataServiceConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass xmlaDataServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass xmlaDataServiceConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass graphQLDataServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass qGisDataServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass qGisDataServiceConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass graphQLDataServiceConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ogcFeaturesDataServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ogcSensorThingsDataServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mongoRepositoryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fileDataInputEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass jpaDataInputEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dataTransformationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass queryTransformationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass oDataDataServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass oDataDataServiceConfigurationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass distributionExportEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass csvDistributionExportEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass transformationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dcatPublicationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass jdbcDataSourceEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DAConfigPackageImpl() {
		super(eNS_URI, DAConfigFactory.eINSTANCE);
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link DAConfigPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DAConfigPackage init() {
		if (isInited) return (DAConfigPackage)EPackage.Registry.INSTANCE.getEPackage(DAConfigPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredDAConfigPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		DAConfigPackageImpl theDAConfigPackage = registeredDAConfigPackage instanceof DAConfigPackageImpl ? (DAConfigPackageImpl)registeredDAConfigPackage : new DAConfigPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		EORMPackage.eINSTANCE.eClass();
		QueryPackage.eINSTANCE.eClass();
		ExpressionPackage.eINSTANCE.eClass();
		QvtOperationalPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theDAConfigPackage.createPackageContents();

		// Initialize created meta-data
		theDAConfigPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDAConfigPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DAConfigPackage.eNS_URI, theDAConfigPackage);
		return theDAConfigPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDataAtlasConfiguration() {
		return dataAtlasConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDataAtlasConfiguration_Name() {
		return (EAttribute)dataAtlasConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDataAtlasConfiguration_Description() {
		return (EAttribute)dataAtlasConfigurationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataAtlasConfiguration_DataSources() {
		return (EReference)dataAtlasConfigurationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataAtlasConfiguration_DataInputs() {
		return (EReference)dataAtlasConfigurationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataAtlasConfiguration_DataSets() {
		return (EReference)dataAtlasConfigurationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataAtlasConfiguration_Services() {
		return (EReference)dataAtlasConfigurationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataAtlasConfiguration_Exports() {
		return (EReference)dataAtlasConfigurationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataAtlasConfiguration_Transformations() {
		return (EReference)dataAtlasConfigurationEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataAtlasConfiguration_Publications() {
		return (EReference)dataAtlasConfigurationEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDataProvider() {
		return dataProviderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDataProvider_Id() {
		return (EAttribute)dataProviderEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDataProvider_Name() {
		return (EAttribute)dataProviderEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDataProvider_Description() {
		return (EAttribute)dataProviderEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataProvider_DataInput() {
		return (EReference)dataProviderEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataProvider_Transformation() {
		return (EReference)dataProviderEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataProvider_DistributionExport() {
		return (EReference)dataProviderEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataProvider_Publication() {
		return (EReference)dataProviderEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDataInput() {
		return dataInputEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDataInput_Id() {
		return (EAttribute)dataInputEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataInput_SupportedEClasses() {
		return (EReference)dataInputEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getDataInput__StreamData() {
		return dataInputEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBridgeRepository() {
		return bridgeRepositoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBridgeRepository_Source() {
		return (EReference)bridgeRepositoryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBridgeRepository_DataTrafo() {
		return (EReference)bridgeRepositoryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBridgeRepository_QueryTrafo() {
		return (EReference)bridgeRepositoryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getBridgeRepository_Filter() {
		return (EReference)bridgeRepositoryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDataSet() {
		return dataSetEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataSet_InputType() {
		return (EReference)dataSetEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataSet_OutputType() {
		return (EReference)dataSetEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataSet_Query() {
		return (EReference)dataSetEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataSet_ChildDataSet() {
		return (EReference)dataSetEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataSet_ParentDataSet() {
		return (EReference)dataSetEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDataService() {
		return dataServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDataService_UrlContext() {
		return (EAttribute)dataServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getDataService__GetDistributions() {
		return dataServiceEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRestDataService() {
		return restDataServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRestDataService_Configuration() {
		return (EReference)restDataServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestDataService_OpenAPI() {
		return (EAttribute)restDataServiceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestDataService_PaginationOffsetParameterName() {
		return (EAttribute)restDataServiceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestDataService_PaginationSizeParameterName() {
		return (EAttribute)restDataServiceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDataServiceConfiguration() {
		return dataServiceConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDataServiceConfiguration_Id() {
		return (EAttribute)dataServiceConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataServiceConfiguration_DataSet() {
		return (EReference)dataServiceConfigurationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRestDataServiceConfiguration() {
		return restDataServiceConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestDataServiceConfiguration_Path() {
		return (EAttribute)restDataServiceConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestDataServiceConfiguration_BatchSize() {
		return (EAttribute)restDataServiceConfigurationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestDataServiceConfiguration_BatchSizeLimit() {
		return (EAttribute)restDataServiceConfigurationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getXMLADataService() {
		return xmlaDataServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getXMLADataService_Configuration() {
		return (EReference)xmlaDataServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getXMLADataServiceConfiguration() {
		return xmlaDataServiceConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getXMLADataServiceConfiguration_Mapping() {
		return (EReference)xmlaDataServiceConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getGraphQLDataService() {
		return graphQLDataServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getGraphQLDataService_Configuration() {
		return (EReference)graphQLDataServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getQGisDataService() {
		return qGisDataServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getQGisDataService_Configuration() {
		return (EReference)qGisDataServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getQGisDataServiceConfiguration() {
		return qGisDataServiceConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getQGisDataServiceConfiguration_Layer() {
		return (EReference)qGisDataServiceConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getGraphQLDataServiceConfiguration() {
		return graphQLDataServiceConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOgcFeaturesDataService() {
		return ogcFeaturesDataServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOgcSensorThingsDataService() {
		return ogcSensorThingsDataServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMongoRepository() {
		return mongoRepositoryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFileDataInput() {
		return fileDataInputEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFileDataInput_Uri() {
		return (EAttribute)fileDataInputEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getJPADataInput() {
		return jpaDataInputEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getJPADataInput_PersistenceConfig() {
		return (EReference)jpaDataInputEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getJPADataInput_DataSource() {
		return (EReference)jpaDataInputEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDataTransformation() {
		return dataTransformationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataTransformation_Transformation() {
		return (EReference)dataTransformationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataTransformation_SupportedEClasses() {
		return (EReference)dataTransformationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataTransformation_ResultEClasses() {
		return (EReference)dataTransformationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getQueryTransformation() {
		return queryTransformationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getODataDataService() {
		return oDataDataServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getODataDataService_Configuration() {
		return (EReference)oDataDataServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getODataDataServiceConfiguration() {
		return oDataDataServiceConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDistributionExport() {
		return distributionExportEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDistributionExport_Id() {
		return (EAttribute)distributionExportEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDistributionExport_Name() {
		return (EAttribute)distributionExportEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDistributionExport_Description() {
		return (EAttribute)distributionExportEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDistributionExport_MediaType() {
		return (EAttribute)distributionExportEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCSVDistributionExport() {
		return csvDistributionExportEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCSVDistributionExport_Separator() {
		return (EAttribute)csvDistributionExportEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCSVDistributionExport_Compressed() {
		return (EAttribute)csvDistributionExportEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCSVDistributionExport_IncludeTypeHeader() {
		return (EAttribute)csvDistributionExportEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTransformation() {
		return transformationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransformation_Id() {
		return (EAttribute)transformationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransformation_Name() {
		return (EAttribute)transformationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getTransformation_Description() {
		return (EAttribute)transformationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDcatPublication() {
		return dcatPublicationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_Id() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_Catalog() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_Portal() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_Identifier() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_Title() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_Description() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_Language() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_Keywords() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_Themes() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_PublisherName() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_PublisherUri() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_LicenseUri() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getJdbcDataSource() {
		return jdbcDataSourceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJdbcDataSource_Filter() {
		return (EAttribute)jdbcDataSourceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJdbcDataSource_Id() {
		return (EAttribute)jdbcDataSourceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getJdbcDataSource_Name() {
		return (EAttribute)jdbcDataSourceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DAConfigFactory getDAConfigFactory() {
		return (DAConfigFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		dataAtlasConfigurationEClass = createEClass(DATA_ATLAS_CONFIGURATION);
		createEAttribute(dataAtlasConfigurationEClass, DATA_ATLAS_CONFIGURATION__NAME);
		createEAttribute(dataAtlasConfigurationEClass, DATA_ATLAS_CONFIGURATION__DESCRIPTION);
		createEReference(dataAtlasConfigurationEClass, DATA_ATLAS_CONFIGURATION__DATA_SOURCES);
		createEReference(dataAtlasConfigurationEClass, DATA_ATLAS_CONFIGURATION__DATA_INPUTS);
		createEReference(dataAtlasConfigurationEClass, DATA_ATLAS_CONFIGURATION__DATA_SETS);
		createEReference(dataAtlasConfigurationEClass, DATA_ATLAS_CONFIGURATION__SERVICES);
		createEReference(dataAtlasConfigurationEClass, DATA_ATLAS_CONFIGURATION__EXPORTS);
		createEReference(dataAtlasConfigurationEClass, DATA_ATLAS_CONFIGURATION__TRANSFORMATIONS);
		createEReference(dataAtlasConfigurationEClass, DATA_ATLAS_CONFIGURATION__PUBLICATIONS);

		dataProviderEClass = createEClass(DATA_PROVIDER);
		createEAttribute(dataProviderEClass, DATA_PROVIDER__ID);
		createEAttribute(dataProviderEClass, DATA_PROVIDER__NAME);
		createEAttribute(dataProviderEClass, DATA_PROVIDER__DESCRIPTION);
		createEReference(dataProviderEClass, DATA_PROVIDER__DATA_INPUT);
		createEReference(dataProviderEClass, DATA_PROVIDER__TRANSFORMATION);
		createEReference(dataProviderEClass, DATA_PROVIDER__DISTRIBUTION_EXPORT);
		createEReference(dataProviderEClass, DATA_PROVIDER__PUBLICATION);

		dataInputEClass = createEClass(DATA_INPUT);
		createEAttribute(dataInputEClass, DATA_INPUT__ID);
		createEReference(dataInputEClass, DATA_INPUT__SUPPORTED_ECLASSES);
		createEOperation(dataInputEClass, DATA_INPUT___STREAM_DATA);

		bridgeRepositoryEClass = createEClass(BRIDGE_REPOSITORY);
		createEReference(bridgeRepositoryEClass, BRIDGE_REPOSITORY__SOURCE);
		createEReference(bridgeRepositoryEClass, BRIDGE_REPOSITORY__DATA_TRAFO);
		createEReference(bridgeRepositoryEClass, BRIDGE_REPOSITORY__QUERY_TRAFO);
		createEReference(bridgeRepositoryEClass, BRIDGE_REPOSITORY__FILTER);

		dataSetEClass = createEClass(DATA_SET);
		createEReference(dataSetEClass, DATA_SET__INPUT_TYPE);
		createEReference(dataSetEClass, DATA_SET__OUTPUT_TYPE);
		createEReference(dataSetEClass, DATA_SET__QUERY);
		createEReference(dataSetEClass, DATA_SET__CHILD_DATA_SET);
		createEReference(dataSetEClass, DATA_SET__PARENT_DATA_SET);

		dataServiceEClass = createEClass(DATA_SERVICE);
		createEAttribute(dataServiceEClass, DATA_SERVICE__URL_CONTEXT);
		createEOperation(dataServiceEClass, DATA_SERVICE___GET_DISTRIBUTIONS);

		restDataServiceEClass = createEClass(REST_DATA_SERVICE);
		createEReference(restDataServiceEClass, REST_DATA_SERVICE__CONFIGURATION);
		createEAttribute(restDataServiceEClass, REST_DATA_SERVICE__OPEN_API);
		createEAttribute(restDataServiceEClass, REST_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME);
		createEAttribute(restDataServiceEClass, REST_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME);

		dataServiceConfigurationEClass = createEClass(DATA_SERVICE_CONFIGURATION);
		createEAttribute(dataServiceConfigurationEClass, DATA_SERVICE_CONFIGURATION__ID);
		createEReference(dataServiceConfigurationEClass, DATA_SERVICE_CONFIGURATION__DATA_SET);

		restDataServiceConfigurationEClass = createEClass(REST_DATA_SERVICE_CONFIGURATION);
		createEAttribute(restDataServiceConfigurationEClass, REST_DATA_SERVICE_CONFIGURATION__PATH);
		createEAttribute(restDataServiceConfigurationEClass, REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE);
		createEAttribute(restDataServiceConfigurationEClass, REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT);

		xmlaDataServiceEClass = createEClass(XMLA_DATA_SERVICE);
		createEReference(xmlaDataServiceEClass, XMLA_DATA_SERVICE__CONFIGURATION);

		xmlaDataServiceConfigurationEClass = createEClass(XMLA_DATA_SERVICE_CONFIGURATION);
		createEReference(xmlaDataServiceConfigurationEClass, XMLA_DATA_SERVICE_CONFIGURATION__MAPPING);

		graphQLDataServiceEClass = createEClass(GRAPH_QL_DATA_SERVICE);
		createEReference(graphQLDataServiceEClass, GRAPH_QL_DATA_SERVICE__CONFIGURATION);

		qGisDataServiceEClass = createEClass(QGIS_DATA_SERVICE);
		createEReference(qGisDataServiceEClass, QGIS_DATA_SERVICE__CONFIGURATION);

		qGisDataServiceConfigurationEClass = createEClass(QGIS_DATA_SERVICE_CONFIGURATION);
		createEReference(qGisDataServiceConfigurationEClass, QGIS_DATA_SERVICE_CONFIGURATION__LAYER);

		graphQLDataServiceConfigurationEClass = createEClass(GRAPH_QL_DATA_SERVICE_CONFIGURATION);

		ogcFeaturesDataServiceEClass = createEClass(OGC_FEATURES_DATA_SERVICE);

		ogcSensorThingsDataServiceEClass = createEClass(OGC_SENSOR_THINGS_DATA_SERVICE);

		mongoRepositoryEClass = createEClass(MONGO_REPOSITORY);

		fileDataInputEClass = createEClass(FILE_DATA_INPUT);
		createEAttribute(fileDataInputEClass, FILE_DATA_INPUT__URI);

		jpaDataInputEClass = createEClass(JPA_DATA_INPUT);
		createEReference(jpaDataInputEClass, JPA_DATA_INPUT__PERSISTENCE_CONFIG);
		createEReference(jpaDataInputEClass, JPA_DATA_INPUT__DATA_SOURCE);

		dataTransformationEClass = createEClass(DATA_TRANSFORMATION);
		createEReference(dataTransformationEClass, DATA_TRANSFORMATION__TRANSFORMATION);
		createEReference(dataTransformationEClass, DATA_TRANSFORMATION__SUPPORTED_ECLASSES);
		createEReference(dataTransformationEClass, DATA_TRANSFORMATION__RESULT_ECLASSES);

		queryTransformationEClass = createEClass(QUERY_TRANSFORMATION);

		oDataDataServiceEClass = createEClass(ODATA_DATA_SERVICE);
		createEReference(oDataDataServiceEClass, ODATA_DATA_SERVICE__CONFIGURATION);

		oDataDataServiceConfigurationEClass = createEClass(ODATA_DATA_SERVICE_CONFIGURATION);

		distributionExportEClass = createEClass(DISTRIBUTION_EXPORT);
		createEAttribute(distributionExportEClass, DISTRIBUTION_EXPORT__ID);
		createEAttribute(distributionExportEClass, DISTRIBUTION_EXPORT__NAME);
		createEAttribute(distributionExportEClass, DISTRIBUTION_EXPORT__DESCRIPTION);
		createEAttribute(distributionExportEClass, DISTRIBUTION_EXPORT__MEDIA_TYPE);

		csvDistributionExportEClass = createEClass(CSV_DISTRIBUTION_EXPORT);
		createEAttribute(csvDistributionExportEClass, CSV_DISTRIBUTION_EXPORT__SEPARATOR);
		createEAttribute(csvDistributionExportEClass, CSV_DISTRIBUTION_EXPORT__COMPRESSED);
		createEAttribute(csvDistributionExportEClass, CSV_DISTRIBUTION_EXPORT__INCLUDE_TYPE_HEADER);

		transformationEClass = createEClass(TRANSFORMATION);
		createEAttribute(transformationEClass, TRANSFORMATION__ID);
		createEAttribute(transformationEClass, TRANSFORMATION__NAME);
		createEAttribute(transformationEClass, TRANSFORMATION__DESCRIPTION);

		dcatPublicationEClass = createEClass(DCAT_PUBLICATION);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__ID);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__CATALOG);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__PORTAL);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__IDENTIFIER);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__TITLE);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__DESCRIPTION);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__LANGUAGE);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__KEYWORDS);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__THEMES);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__PUBLISHER_NAME);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__PUBLISHER_URI);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__LICENSE_URI);

		jdbcDataSourceEClass = createEClass(JDBC_DATA_SOURCE);
		createEAttribute(jdbcDataSourceEClass, JDBC_DATA_SOURCE__FILTER);
		createEAttribute(jdbcDataSourceEClass, JDBC_DATA_SOURCE__ID);
		createEAttribute(jdbcDataSourceEClass, JDBC_DATA_SOURCE__NAME);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		QueryPackage theQueryPackage = (QueryPackage)EPackage.Registry.INSTANCE.getEPackage(QueryPackage.eNS_URI);
		EORMPackage theEORMPackage = (EORMPackage)EPackage.Registry.INSTANCE.getEPackage(EORMPackage.eNS_URI);
		QvtOperationalPackage theQvtOperationalPackage = (QvtOperationalPackage)EPackage.Registry.INSTANCE.getEPackage(QvtOperationalPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		bridgeRepositoryEClass.getESuperTypes().add(this.getDataInput());
		dataSetEClass.getESuperTypes().add(this.getDataProvider());
		dataServiceEClass.getESuperTypes().add(this.getDataProvider());
		restDataServiceEClass.getESuperTypes().add(this.getDataService());
		restDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		xmlaDataServiceEClass.getESuperTypes().add(this.getDataService());
		xmlaDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		graphQLDataServiceEClass.getESuperTypes().add(this.getDataService());
		qGisDataServiceEClass.getESuperTypes().add(this.getDataService());
		qGisDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		graphQLDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		ogcFeaturesDataServiceEClass.getESuperTypes().add(this.getDataService());
		ogcSensorThingsDataServiceEClass.getESuperTypes().add(this.getDataService());
		mongoRepositoryEClass.getESuperTypes().add(this.getDataInput());
		fileDataInputEClass.getESuperTypes().add(this.getDataInput());
		jpaDataInputEClass.getESuperTypes().add(this.getDataInput());
		dataTransformationEClass.getESuperTypes().add(this.getTransformation());
		queryTransformationEClass.getESuperTypes().add(this.getTransformation());
		oDataDataServiceEClass.getESuperTypes().add(this.getDataService());
		oDataDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		csvDistributionExportEClass.getESuperTypes().add(this.getDistributionExport());

		// Initialize classes, features, and operations; add parameters
		initEClass(dataAtlasConfigurationEClass, DataAtlasConfiguration.class, "DataAtlasConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDataAtlasConfiguration_Name(), ecorePackage.getEString(), "name", null, 1, 1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDataAtlasConfiguration_Description(), ecorePackage.getEString(), "description", null, 0, 1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_DataSources(), this.getJdbcDataSource(), null, "dataSources", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_DataInputs(), this.getDataInput(), null, "dataInputs", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_DataSets(), this.getDataSet(), null, "dataSets", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_Services(), this.getDataService(), null, "services", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_Exports(), this.getDistributionExport(), null, "exports", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_Transformations(), this.getTransformation(), null, "transformations", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_Publications(), this.getDcatPublication(), null, "publications", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataProviderEClass, DataProvider.class, "DataProvider", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDataProvider_Id(), ecorePackage.getEString(), "id", null, 1, 1, DataProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDataProvider_Name(), ecorePackage.getEString(), "name", null, 1, 1, DataProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDataProvider_Description(), ecorePackage.getEString(), "description", null, 1, 1, DataProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataProvider_DataInput(), this.getDataInput(), null, "dataInput", null, 0, 1, DataProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataProvider_Transformation(), this.getTransformation(), null, "transformation", null, 0, 1, DataProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataProvider_DistributionExport(), this.getDistributionExport(), null, "distributionExport", null, 0, -1, DataProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataProvider_Publication(), this.getDcatPublication(), null, "publication", null, 0, 1, DataProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataInputEClass, DataInput.class, "DataInput", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDataInput_Id(), ecorePackage.getEString(), "id", null, 1, 1, DataInput.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataInput_SupportedEClasses(), ecorePackage.getEClass(), null, "supportedEClasses", null, 0, -1, DataInput.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getDataInput__StreamData(), null, "streamData", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(bridgeRepositoryEClass, BridgeRepository.class, "BridgeRepository", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBridgeRepository_Source(), this.getDataInput(), null, "source", null, 1, 1, BridgeRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBridgeRepository_DataTrafo(), this.getDataTransformation(), null, "dataTrafo", null, 1, 1, BridgeRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBridgeRepository_QueryTrafo(), this.getQueryTransformation(), null, "queryTrafo", null, 0, 1, BridgeRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBridgeRepository_Filter(), ecorePackage.getEObject(), null, "filter", null, 0, 1, BridgeRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataSetEClass, DataSet.class, "DataSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataSet_InputType(), ecorePackage.getEClass(), null, "inputType", null, 1, 1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataSet_OutputType(), ecorePackage.getEClass(), null, "outputType", null, 1, 1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataSet_Query(), theQueryPackage.getQuery(), null, "query", null, 0, 1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataSet_ChildDataSet(), this.getDataSet(), this.getDataSet_ParentDataSet(), "childDataSet", null, 0, -1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataSet_ParentDataSet(), this.getDataSet(), this.getDataSet_ChildDataSet(), "parentDataSet", null, 0, 1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataServiceEClass, DataService.class, "DataService", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDataService_UrlContext(), ecorePackage.getEString(), "urlContext", null, 1, 1, DataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getDataService__GetDistributions(), null, "getDistributions", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(restDataServiceEClass, RestDataService.class, "RestDataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRestDataService_Configuration(), this.getRestDataServiceConfiguration(), null, "configuration", null, 0, -1, RestDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getRestDataService_OpenAPI(), ecorePackage.getEBoolean(), "openAPI", null, 1, 1, RestDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestDataService_PaginationOffsetParameterName(), ecorePackage.getEString(), "paginationOffsetParameterName", "offset", 1, 1, RestDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestDataService_PaginationSizeParameterName(), ecorePackage.getEString(), "paginationSizeParameterName", "limit", 1, 1, RestDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataServiceConfigurationEClass, DataServiceConfiguration.class, "DataServiceConfiguration", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDataServiceConfiguration_Id(), ecorePackage.getEString(), "id", null, 1, 1, DataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataServiceConfiguration_DataSet(), this.getDataSet(), null, "dataSet", null, 1, 1, DataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(restDataServiceConfigurationEClass, RestDataServiceConfiguration.class, "RestDataServiceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRestDataServiceConfiguration_Path(), ecorePackage.getEString(), "path", null, 1, 1, RestDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestDataServiceConfiguration_BatchSize(), ecorePackage.getEBigInteger(), "batchSize", "-1", 1, 1, RestDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestDataServiceConfiguration_BatchSizeLimit(), ecorePackage.getEBigInteger(), "batchSizeLimit", "-1", 1, 1, RestDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(xmlaDataServiceEClass, XMLADataService.class, "XMLADataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getXMLADataService_Configuration(), this.getXMLADataServiceConfiguration(), null, "configuration", null, 0, -1, XMLADataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(xmlaDataServiceConfigurationEClass, XMLADataServiceConfiguration.class, "XMLADataServiceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getXMLADataServiceConfiguration_Mapping(), ecorePackage.getEClass(), null, "mapping", null, 1, 1, XMLADataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(graphQLDataServiceEClass, GraphQLDataService.class, "GraphQLDataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGraphQLDataService_Configuration(), this.getGraphQLDataServiceConfiguration(), null, "configuration", null, 0, -1, GraphQLDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(qGisDataServiceEClass, QGisDataService.class, "QGisDataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getQGisDataService_Configuration(), this.getQGisDataServiceConfiguration(), null, "configuration", null, 0, -1, QGisDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(qGisDataServiceConfigurationEClass, QGisDataServiceConfiguration.class, "QGisDataServiceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getQGisDataServiceConfiguration_Layer(), ecorePackage.getEClass(), null, "layer", null, 1, 1, QGisDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(graphQLDataServiceConfigurationEClass, GraphQLDataServiceConfiguration.class, "GraphQLDataServiceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ogcFeaturesDataServiceEClass, OgcFeaturesDataService.class, "OgcFeaturesDataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(ogcSensorThingsDataServiceEClass, OgcSensorThingsDataService.class, "OgcSensorThingsDataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(mongoRepositoryEClass, MongoRepository.class, "MongoRepository", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(fileDataInputEClass, FileDataInput.class, "FileDataInput", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFileDataInput_Uri(), ecorePackage.getEString(), "uri", null, 1, 1, FileDataInput.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(jpaDataInputEClass, JPADataInput.class, "JPADataInput", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getJPADataInput_PersistenceConfig(), theEORMPackage.getEntityMappings(), null, "persistenceConfig", null, 0, 1, JPADataInput.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getJPADataInput_DataSource(), this.getJdbcDataSource(), null, "dataSource", null, 1, 1, JPADataInput.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataTransformationEClass, DataTransformation.class, "DataTransformation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataTransformation_Transformation(), theQvtOperationalPackage.getOperationalTransformation(), null, "transformation", null, 1, 1, DataTransformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataTransformation_SupportedEClasses(), ecorePackage.getEClass(), null, "supportedEClasses", null, 0, -1, DataTransformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataTransformation_ResultEClasses(), ecorePackage.getEClass(), null, "resultEClasses", null, 0, -1, DataTransformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(queryTransformationEClass, QueryTransformation.class, "QueryTransformation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(oDataDataServiceEClass, ODataDataService.class, "ODataDataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getODataDataService_Configuration(), this.getODataDataServiceConfiguration(), null, "configuration", null, 0, -1, ODataDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(oDataDataServiceConfigurationEClass, ODataDataServiceConfiguration.class, "ODataDataServiceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(distributionExportEClass, DistributionExport.class, "DistributionExport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDistributionExport_Id(), ecorePackage.getEString(), "id", null, 1, 1, DistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDistributionExport_Name(), ecorePackage.getEString(), "name", null, 1, 1, DistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDistributionExport_Description(), ecorePackage.getEString(), "description", null, 1, 1, DistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDistributionExport_MediaType(), ecorePackage.getEString(), "mediaType", null, 0, 1, DistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(csvDistributionExportEClass, CSVDistributionExport.class, "CSVDistributionExport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCSVDistributionExport_Separator(), ecorePackage.getEString(), "separator", null, 0, 1, CSVDistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCSVDistributionExport_Compressed(), ecorePackage.getEBoolean(), "compressed", null, 0, 1, CSVDistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCSVDistributionExport_IncludeTypeHeader(), ecorePackage.getEBoolean(), "includeTypeHeader", null, 0, 1, CSVDistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(transformationEClass, Transformation.class, "Transformation", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTransformation_Id(), ecorePackage.getEString(), "id", null, 1, 1, Transformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransformation_Name(), ecorePackage.getEString(), "name", null, 1, 1, Transformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getTransformation_Description(), ecorePackage.getEString(), "description", null, 1, 1, Transformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dcatPublicationEClass, DcatPublication.class, "DcatPublication", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDcatPublication_Id(), ecorePackage.getEString(), "id", null, 1, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_Catalog(), ecorePackage.getEString(), "catalog", null, 1, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_Portal(), ecorePackage.getEString(), "portal", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_Identifier(), ecorePackage.getEString(), "identifier", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_Title(), ecorePackage.getEString(), "title", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_Description(), ecorePackage.getEString(), "description", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_Language(), ecorePackage.getEString(), "language", "en", 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_Keywords(), ecorePackage.getEString(), "keywords", null, 0, -1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_Themes(), ecorePackage.getEString(), "themes", null, 0, -1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_PublisherName(), ecorePackage.getEString(), "publisherName", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_PublisherUri(), ecorePackage.getEString(), "publisherUri", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_LicenseUri(), ecorePackage.getEString(), "licenseUri", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(jdbcDataSourceEClass, JdbcDataSource.class, "JdbcDataSource", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getJdbcDataSource_Filter(), ecorePackage.getEString(), "filter", null, 0, 1, JdbcDataSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJdbcDataSource_Id(), ecorePackage.getEString(), "id", null, 1, 1, JdbcDataSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getJdbcDataSource_Name(), ecorePackage.getEString(), "name", null, 1, 1, JdbcDataSource.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/OCL/Import
		createImportAnnotations();
		// http://www.eclipse.org/emf/2002/GenModel
		createGenModelAnnotations();
		// http://www.eclipse.org/OCL/Collection
		createCollectionAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/OCL/Import</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createImportAnnotations() {
		String source = "http://www.eclipse.org/OCL/Import";
		addAnnotation
		  (this,
		   source,
		   new String[] {
			   "ecore", "http://www.eclipse.org/emf/2002/Ecore",
			   "eorm", "platform:/resource/org.eclipse.fennec.persistence.orm/model/eorm.ecore#/"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/emf/2002/GenModel</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createGenModelAnnotations() {
		String source = "http://www.eclipse.org/emf/2002/GenModel";
		addAnnotation
		  (this,
		   source,
		   new String[] {
			   "documentation", "Configuration model for the Fennec Data-Atlas.\n\nIt describes - independent of any concrete deployment - which data sources (DataInput) provide data, which DataSets are published, which DataServices (REST, OData, OGC, QGis, XMLA, GraphQL) expose them, which reusable DistributionExport templates are used for serialization, and which Transformations map between models.\nAn instance of this model is meant to fully configure a Data-Atlas runtime, while the model types it refers to are resolved against the Model-Atlas EPackage registry.",
			   "oSGiCompatible", "true",
			   "basePackage", "org.eclipse.fennec.data.atlas",
			   "copyrightText", "******************************************************************\nCopyright (c) 2026 Contributors to the Eclipse Foundation.\n\nThis program and the accompanying materials are made\navailable under the terms of the Eclipse Public License 2.0\nwhich is available at https://www.eclipse.org/legal/epl-2.0/\n\nSPDX-License-Identifier: EPL-2.0\n\nContributors:\n  Data In Motion Consulting - initial implementation\n******************************************************************",
			   "complianceLevel", "21.0",
			   "resource", "XMI"
		   });
		addAnnotation
		  (dataAtlasConfigurationEClass,
		   source,
		   new String[] {
			   "documentation", "Root of the configuration model: the complete description of exactly one Data-Atlas instance.\nMultiple Data-Atlas instances can run side by side (e.g. to spread load); each instance is fed its own DataAtlasConfiguration, obtained either from the file system or by retrieving it from the Model-Atlas.\n\nThe root acts as a set of registries: data sources, data inputs, transformations and distribution-export templates are defined here exactly once and only referenced from DataServices and DataSets, so the same service definition can be re-applied to another data source (tenant/test system) and export settings are reusable templates rather than per-provider copies."
		   });
		addAnnotation
		  (getDataAtlasConfiguration_Name(),
		   source,
		   new String[] {
			   "documentation", "Name of the Data-Atlas instance this configuration describes."
		   });
		addAnnotation
		  (getDataAtlasConfiguration_Description(),
		   source,
		   new String[] {
			   "documentation", "Optional description of this Data-Atlas instance."
		   });
		addAnnotation
		  (getDataAtlasConfiguration_DataSources(),
		   source,
		   new String[] {
			   "documentation", "Registry of reusable data source definitions, referenced from DataInputs."
		   });
		addAnnotation
		  (getDataAtlasConfiguration_DataInputs(),
		   source,
		   new String[] {
			   "documentation", "Registry of the DataInputs of this instance, referenced from DataServices and DataSets."
		   });
		addAnnotation
		  (getDataAtlasConfiguration_DataSets(),
		   source,
		   new String[] {
			   "documentation", "Registry of the DataSets of this instance, referenced from the DataServiceConfigurations."
		   });
		addAnnotation
		  (getDataAtlasConfiguration_Services(),
		   source,
		   new String[] {
			   "documentation", "The DataServices (endpoints) this instance publishes."
		   });
		addAnnotation
		  (getDataAtlasConfiguration_Exports(),
		   source,
		   new String[] {
			   "documentation", "Registry of reusable DistributionExport templates, referenced from DataServices and DataSets."
		   });
		addAnnotation
		  (getDataAtlasConfiguration_Transformations(),
		   source,
		   new String[] {
			   "documentation", "Registry of the Transformations of this instance, referenced from DataProviders and BridgeRepositories."
		   });
		addAnnotation
		  (getDataAtlasConfiguration_Publications(),
		   source,
		   new String[] {
			   "documentation", "Registry of reusable DcatPublication declarations, referenced from DataServices and DataSets (via DataProvider.publication)."
		   });
		addAnnotation
		  (dataProviderEClass,
		   source,
		   new String[] {
			   "documentation", "Abstract base for anything that provides data and can be distributed: a generic DataService or a concrete DataSet.\nCarries the common identity (id/name/description) and the data configuration trias: data source (dataInput), transformation and distribution exports.\n\nA DataSet refines its enclosing DataService: if it sets one of the trias features itself, that value overrides the one of the DataService; if it leaves the feature unset, the value of the DataService applies (override-else-default)."
		   });
		addAnnotation
		  (getDataProvider_Id(),
		   source,
		   new String[] {
			   "documentation", "Unique identifier of this data provider (DataService or DataSet)."
		   });
		addAnnotation
		  (getDataProvider_Name(),
		   source,
		   new String[] {
			   "documentation", "Derives from the model annotation by default"
		   });
		addAnnotation
		  (getDataProvider_Description(),
		   source,
		   new String[] {
			   "documentation", "Derives from the model annotation by default"
		   });
		addAnnotation
		  (getDataProvider_DataInput(),
		   source,
		   new String[] {
			   "documentation", "The DataInput (data source) used by this provider.\nOn a standalone DataService this is the source served generically. On a DataSet it overrides the DataInput of the enclosing DataService; if left unset, the service\'s DataInput applies.\nRuntime constraint: a DataService that does not contain any DataSet must define a dataInput here."
		   });
		addAnnotation
		  (getDataProvider_Transformation(),
		   source,
		   new String[] {
			   "documentation", "Optional transformation applied to the data of this provider. On a DataSet it overrides the transformation of the enclosing DataService; if left unset, the service\'s transformation applies."
		   });
		addAnnotation
		  (getDataProvider_DistributionExport(),
		   source,
		   new String[] {
			   "documentation", "References reusable DistributionExport templates (defined in the export registry) describing the formats this provider can be serialized to.\nOn a DataService this is the set of available/default exports. On a DataSet, if it defines its own exports they fully replace the service\'s; if left empty, the service\'s exports apply (override-else-default)."
		   });
		addAnnotation
		  (getDataProvider_Publication(),
		   source,
		   new String[] {
			   "documentation", "Opt-in open-data publication of this provider: references a DcatPublication declaration (defined in the publications registry). Absent means the provider is not published - nothing is published implicitly.\nFollows the override-else-default rule: what a DataService declares applies to its DataSets unless a DataSet references a DcatPublication of its own.\nDeliberately plain data - the mapping to a DCAT model lives in the omittable publication bundle, never in this model (see data.atlas issue #4, DA-DCAT-1)."
		   });
		addAnnotation
		  (dataInputEClass,
		   source,
		   new String[] {
			   "documentation", "Base type for any data source that can stream EObjects into the Data-Atlas.\nA DataInput can be backed by a database (JPADataInput), MongoDB (MongoRepository), EMF files (FileDataInput) or another input wrapped and transformed by a BridgeRepository.\n\nIntended as an abstract base - it is not meant to be instantiated directly."
		   });
		addAnnotation
		  (getDataInput__StreamData(),
		   source,
		   new String[] {
			   "documentation", "Returns a PushStream of the EObjects provided by this input."
		   });
		addAnnotation
		  (getDataInput_Id(),
		   source,
		   new String[] {
			   "documentation", "Unique identifier of this data input, used to reference it from DataSets and DataServices."
		   });
		addAnnotation
		  (getDataInput_SupportedEClasses(),
		   source,
		   new String[] {
			   "documentation", "The EClasses (model types resolved against the Model-Atlas) whose instances this input can provide."
		   });
		addAnnotation
		  (bridgeRepositoryEClass,
		   source,
		   new String[] {
			   "documentation", "A DataInput that can load data from another source and transform it if necessary.\nBridgeRepository can be cascaded, so one original Entity can result in multiple intermediate formats if necessary.\n\nA bridge can have an attached data transformation for the source objects it loads.\nOn the other hand, incoming queries might be transformed as well."
		   });
		addAnnotation
		  (getBridgeRepository_Source(),
		   source,
		   new String[] {
			   "documentation", "The underlying DataInput this bridge reads from before applying its transformations."
		   });
		addAnnotation
		  (getBridgeRepository_DataTrafo(),
		   source,
		   new String[] {
			   "documentation", "Transformation applied to the source EObjects when they are loaded."
		   });
		addAnnotation
		  (getBridgeRepository_QueryTrafo(),
		   source,
		   new String[] {
			   "documentation", "Transformation applied to incoming queries so they can be executed against the source. Optional: without one, the bridge only accepts from/skip/top queries on its output type and rewrites them to the source type."
		   });
		addAnnotation
		  (getBridgeRepository_Filter(),
		   source,
		   new String[] {
			   "documentation", "A Placeholder for now. Will provide e.g. security filters or anonymization rules for the bridged dataset"
		   });
		addAnnotation
		  (dataSetEClass,
		   source,
		   new String[] {
			   "documentation", "Conforms to the DCAT Dataset. A concrete realization of a DataService: it refines the service for a specific case (e.g. a concrete query) by overriding the inherited dataInput, transformation and distributionExport where needed.\nInherits id, name, description and the data configuration trias from DataProvider."
		   });
		addAnnotation
		  (getDataSet_InputType(),
		   source,
		   new String[] {
			   "documentation", "The model type (EClass) that is read from the dataInput. Might be too simple. We may need an approach like a genmodel, to properly resolve e.g. non containment references to other Datasets or Distributions or DataServices."
		   });
		addAnnotation
		  (getDataSet_OutputType(),
		   source,
		   new String[] {
			   "documentation", "The model type (EClass) that is published by this DataSet after an optional transformation. Might be too simple. We may need an approach like a genmodel, to properly resolve e.g. non containment references to other Datasets or Distributions or DataServices."
		   });
		addAnnotation
		  (getDataSet_Query(),
		   source,
		   new String[] {
			   "documentation", "Optional canonical query (fennec query model) defining the content of this DataSet over its dataInput. Unset means all objects of inputType. Runtime constraint: query.from must equal inputType. Declared query parameters are exposed by the serving DataService (e.g. as HTTP query parameters); the service overlays its pagination on skip/top of a per-request copy. The endpoint is only published if the backing repository validates the query at prepare time."
		   });
		addAnnotation
		  (getDataSet_ChildDataSet(),
		   source,
		   new String[] {
			   "documentation", "Child DataSets within a DCAT dataset hierarchy/series; this DataSet acts as the parent of the referenced ones."
		   });
		addAnnotation
		  (getDataSet_ParentDataSet(),
		   source,
		   new String[] {
			   "documentation", "Parent DataSet within a DCAT dataset hierarchy/series; opposite of childDataSet."
		   });
		addAnnotation
		  (dataServiceEClass,
		   source,
		   new String[] {
			   "documentation", "Abstract definition of an Endpoint to publish DataSets.\nAs a DataProvider a DataService can also act standalone - serving its own dataInput generically in the formats given by its distributionExport - or it can contain refined DataSets (via its configurations)."
		   });
		addAnnotation
		  (getDataService__GetDistributions(),
		   source,
		   new String[] {
			   "documentation", "Marker operation. Each configuration of a DataService must result in a DCAT Distribution."
		   });
		addAnnotation
		  (getDataService_UrlContext(),
		   source,
		   new String[] {
			   "documentation", "Base URL context (path prefix) under which this service is published."
		   });
		addAnnotation
		  (restDataServiceEClass,
		   source,
		   new String[] {
			   "documentation", "DataService definition for a REST endpoint over HTTP(s), serialized to several mimetypes like XML, CSV, JSON etc."
		   });
		addAnnotation
		  (getRestDataService_Configuration(),
		   source,
		   new String[] {
			   "documentation", "Definition of the DataSets that should be provided by this RestDataService."
		   });
		addAnnotation
		  (getRestDataService_OpenAPI(),
		   source,
		   new String[] {
			   "documentation", "Marker, if an openAPI definition should be provided"
		   });
		addAnnotation
		  (getRestDataService_PaginationOffsetParameterName(),
		   source,
		   new String[] {
			   "documentation", "configurable parameter names for the start offset for pagination"
		   });
		addAnnotation
		  (getRestDataService_PaginationSizeParameterName(),
		   source,
		   new String[] {
			   "documentation", "configurable parameter names for the batch limit for pagination"
		   });
		addAnnotation
		  (dataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "documentation", "Abstract binding of a DataSet to a specific DataService (the link DataService -> configuration -> dataSet).\nHolds the service-specific, per-DataSet settings (e.g. REST path / mime types). The exports themselves are no longer held here - they are resolved via the DataService default and the DataSet override (see DataProvider.distributionExport)."
		   });
		addAnnotation
		  (getDataServiceConfiguration_Id(),
		   source,
		   new String[] {
			   "documentation", "Unique identifier of this per-DataSet service configuration."
		   });
		addAnnotation
		  (getDataServiceConfiguration_DataSet(),
		   source,
		   new String[] {
			   "documentation", "The dataset to be published by a DataService"
		   });
		addAnnotation
		  (restDataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "documentation", "A set of Configuration parameters for a specific DataSet"
		   });
		addAnnotation
		  (getRestDataServiceConfiguration_Path(),
		   source,
		   new String[] {
			   "documentation", "derives by default from the Dataset name"
		   });
		addAnnotation
		  (getRestDataServiceConfiguration_BatchSize(),
		   source,
		   new String[] {
			   "documentation", "the page size for pagination. -1 means no pagination"
		   });
		addAnnotation
		  (getRestDataServiceConfiguration_BatchSizeLimit(),
		   source,
		   new String[] {
			   "documentation", "the page size limit for pagination, so the server will not be overtaxed"
		   });
		addAnnotation
		  (xmlaDataServiceEClass,
		   source,
		   new String[] {
			   "documentation", "DataService that exposes DataSets via the XML for Analysis (XMLA) protocol, e.g. for OLAP / Daanse cubes."
		   });
		addAnnotation
		  (getXMLADataService_Configuration(),
		   source,
		   new String[] {
			   "documentation", "The per-DataSet XMLA configurations provided by this service."
		   });
		addAnnotation
		  (xmlaDataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "documentation", "Per-DataSet configuration for an XMLADataService."
		   });
		addAnnotation
		  (getXMLADataServiceConfiguration_Mapping(),
		   source,
		   new String[] {
			   "documentation", "TODO Mapping to the XMLA Mapping description"
		   });
		addAnnotation
		  (graphQLDataServiceEClass,
		   source,
		   new String[] {
			   "documentation", "DataService that exposes DataSets via a GraphQL endpoint."
		   });
		addAnnotation
		  (getGraphQLDataService_Configuration(),
		   source,
		   new String[] {
			   "documentation", "The per-DataSet GraphQL configurations provided by this service."
		   });
		addAnnotation
		  (qGisDataServiceEClass,
		   source,
		   new String[] {
			   "documentation", "Endpoint that provides a generated QGis Mapping Configuration file.\nTODO: Repeat for other geoServers"
		   });
		addAnnotation
		  (getQGisDataService_Configuration(),
		   source,
		   new String[] {
			   "documentation", "The per-DataSet QGis configurations provided by this service."
		   });
		addAnnotation
		  (qGisDataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "documentation", "Per-DataSet configuration for a QGisDataService; maps a DataSet to a QGis layer."
		   });
		addAnnotation
		  (getQGisDataServiceConfiguration_Layer(),
		   source,
		   new String[] {
			   "documentation", "mapping for the QGis configuration"
		   });
		addAnnotation
		  (graphQLDataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "documentation", "Per-DataSet configuration for a GraphQLDataService. Placeholder - no GraphQL-specific parameters yet."
		   });
		addAnnotation
		  (ogcFeaturesDataServiceEClass,
		   source,
		   new String[] {
			   "documentation", "DataService that exposes DataSets via the OGC API - Features standard."
		   });
		addAnnotation
		  (ogcSensorThingsDataServiceEClass,
		   source,
		   new String[] {
			   "documentation", "DataService that exposes DataSets via the OGC SensorThings API."
		   });
		addAnnotation
		  (mongoRepositoryEClass,
		   source,
		   new String[] {
			   "documentation", "DataInput backed by MongoDB as a source."
		   });
		addAnnotation
		  (fileDataInputEClass,
		   source,
		   new String[] {
			   "documentation", "DataInput backed by EMF files as a source."
		   });
		addAnnotation
		  (getFileDataInput_Uri(),
		   source,
		   new String[] {
			   "documentation", "URI of the EMF resource (or a directory of EMF resources) this input loads its EObjects from."
		   });
		addAnnotation
		  (jpaDataInputEClass,
		   source,
		   new String[] {
			   "documentation", "DataInput backed by any relational data source supported by e.g. EclipseLink (JPA)."
		   });
		addAnnotation
		  (getJPADataInput_PersistenceConfig(),
		   source,
		   new String[] {
			   "documentation", "JPA entity mapping (the EclipseLink orm.xml equivalent) describing how the model types map to the relational schema. Set it when the derived naming cannot address the schema - e.g. lower-case or schema-qualified tables, or a table without a primary key that needs a declared composite id.\nContainment, deliberately: the mapping has to travel WITH the configuration. A non-containment href into a deployment-local file cannot be resolved by a Model Atlas that serves the configuration, which fails the upload with an unresolved EClass proxy. If a mapping ever needs to be shared between inputs, that belongs in a registry of the root, like the other reusables."
		   });
		addAnnotation
		  (getJPADataInput_DataSource(),
		   source,
		   new String[] {
			   "documentation", "The JDBC data source definition providing the database connection for this input."
		   });
		addAnnotation
		  (dataTransformationEClass,
		   source,
		   new String[] {
			   "documentation", "Describes a transformation Step from Model A to B.\n\nThe executable transformation is a QVT-O AST (an OperationalTransformation of the fennec m2x qvto model) referenced as an EObject: in file mode from an XMI next to the configuration, in Model Atlas mode from a dedicated EObject registry of the scope. Authoring happens in QVT-O text, parsed once at publish time.\n\nData-Atlas contract: the transformation is 1:1 - one source object maps to exactly one result object carrying the same id."
		   });
		addAnnotation
		  (getDataTransformation_Transformation(),
		   source,
		   new String[] {
			   "documentation", "The executable QVT-O transformation (the parsed AST, referenced as an EObject)."
		   });
		addAnnotation
		  (getDataTransformation_SupportedEClasses(),
		   source,
		   new String[] {
			   "documentation", "Input EClasses this transformation accepts."
		   });
		addAnnotation
		  (getDataTransformation_ResultEClasses(),
		   source,
		   new String[] {
			   "documentation", "Output EClasses this transformation produces."
		   });
		addAnnotation
		  (queryTransformationEClass,
		   source,
		   new String[] {
			   "documentation", "Queries are incoming requests, that need to be mapped on the corresponding data source"
		   });
		addAnnotation
		  (oDataDataServiceEClass,
		   source,
		   new String[] {
			   "documentation", "DataService that exposes DataSets via the OData protocol."
		   });
		addAnnotation
		  (getODataDataService_Configuration(),
		   source,
		   new String[] {
			   "documentation", "The per-DataSet OData configurations provided by this service."
		   });
		addAnnotation
		  (oDataDataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "documentation", "Per-DataSet configuration for an ODataDataService. Placeholder - no OData-specific parameters yet."
		   });
		addAnnotation
		  (distributionExportEClass,
		   source,
		   new String[] {
			   "documentation", "A reusable export/serialization template for a distribution (e.g. CSV settings). Defined once in the export registry and referenced from DataServices and DataSets (via DataProvider.distributionExport), so the same export settings are not duplicated per provider."
		   });
		addAnnotation
		  (getDistributionExport_Id(),
		   source,
		   new String[] {
			   "documentation", "Unique identifier of this export template."
		   });
		addAnnotation
		  (getDistributionExport_Name(),
		   source,
		   new String[] {
			   "documentation", "Derives from the model annotation by default"
		   });
		addAnnotation
		  (getDistributionExport_Description(),
		   source,
		   new String[] {
			   "documentation", "Derives from the model annotation by default"
		   });
		addAnnotation
		  (getDistributionExport_MediaType(),
		   source,
		   new String[] {
			   "documentation", "The HTTP media type this export is served as, e.g. \'text/csv\' or \'application/json\'.\nUnset means the kind-specific default of the concrete export: CSVDistributionExport serves \'text/csv\', or \'application/x-csv-zip\' when compressed is set. A plain DistributionExport therefore needs this attribute to be meaningful - it is how a configuration keeps a format that has no dedicated subclass (e.g. JSON) alongside a specialized one.\nResolution at runtime: a DataProvider that resolves to no export at all is served in the runtime default formats; as soon as it resolves to at least one export, exactly the media types of those exports are served and any other Accept is answered with 406."
		   });
		addAnnotation
		  (csvDistributionExportEClass,
		   source,
		   new String[] {
			   "documentation", "DistributionExport template for CSV serialization."
		   });
		addAnnotation
		  (getCSVDistributionExport_Separator(),
		   source,
		   new String[] {
			   "documentation", "Column separator character (e.g. \',\' or \';\'). Only the first character is used - it maps to the fennec codec option \'codec.csv.delimiter\', which takes a single char."
		   });
		addAnnotation
		  (getCSVDistributionExport_Compressed(),
		   source,
		   new String[] {
			   "documentation", "Whether the CSV output is delivered compressed. This is NOT a gzip of a single CSV: it selects the fennec codec\'s zipped multi-table CSV (media type \'application/x-csv-zip\'), a ZIP containing one CSV per serialized EClass."
		   });
		addAnnotation
		  (getCSVDistributionExport_IncludeTypeHeader(),
		   source,
		   new String[] {
			   "documentation", "Whether to emit an additional row carrying each column\'s SQL type between the header row and the data rows. The column header row itself is ALWAYS written and cannot be switched off. Maps to the fennec codec option \'codec.csv.dataTypeInSecondRow\'."
		   });
		addAnnotation
		  (transformationEClass,
		   source,
		   new String[] {
			   "documentation", "Base type for a named transformation, either of loaded data (DataTransformation) or of incoming queries (QueryTransformation).\n\nIntended as an abstract base - it is not meant to be instantiated directly."
		   });
		addAnnotation
		  (getTransformation_Id(),
		   source,
		   new String[] {
			   "documentation", "Unique identifier of this transformation."
		   });
		addAnnotation
		  (getTransformation_Name(),
		   source,
		   new String[] {
			   "documentation", "Derives from the model annotation by default"
		   });
		addAnnotation
		  (getTransformation_Description(),
		   source,
		   new String[] {
			   "documentation", "Derives from the model annotation by default"
		   });
		addAnnotation
		  (dcatPublicationEClass,
		   source,
		   new String[] {
			   "documentation", "Declares that a DataProvider referencing this element is published as open data to a DCAT portal (DCAT.Atlas). Defined once in the publications registry and referenced from DataServices and DataSets (via DataProvider.publication), so the same target catalog and metadata defaults are not duplicated per provider.\n\nDeliberately plain data: an id, a catalog name and a handful of metadata overrides. The mapping to dcat:Dataset / dcat:DataService lives in the omittable publication bundle - this model must never depend on a DCAT model (data.atlas issue #4, DA-DCAT-1). Metadata left unset here is derived from the provider (name, description, model annotations); an explicit value wins over a derived one (DA-DCAT-8).\n\nThe portal endpoint itself (base URL, credentials, timeouts) is a deployment concern and is NOT configured here: it is the Config-Admin configuration of the dcat.atlas client (DA-DCAT-6)."
		   });
		addAnnotation
		  (getDcatPublication_Id(),
		   source,
		   new String[] {
			   "documentation", "Unique identifier of this publication declaration."
		   });
		addAnnotation
		  (getDcatPublication_Catalog(),
		   source,
		   new String[] {
			   "documentation", "Identifier of the target catalog in the portal the published entries are linked into. The catalog is expected to exist - catalog creation is out of scope (DA-DCAT-5)."
		   });
		addAnnotation
		  (getDcatPublication_Portal(),
		   source,
		   new String[] {
			   "documentation", "Logical name of the portal to publish to, matching the \'dcat.portal\' service property of a configured dcat.atlas client. Unset means the single configured client; with more than one client configured, an unset portal is a configuration error. This is the link between the deployment-owned client configuration and this model (DA-DCAT-5/6)."
		   });
		addAnnotation
		  (getDcatPublication_Identifier(),
		   source,
		   new String[] {
			   "documentation", "Optional override of the DCAT identifier (the last PUT path segment). Unset means the id of the published provider. Must be stable and deterministic - it survives restarts, configuration reloads and redeployments (DA-DCAT-10/11)."
		   });
		addAnnotation
		  (getDcatPublication_Title(),
		   source,
		   new String[] {
			   "documentation", "Explicit dct:title override. Unset derives from the provider\'s name."
		   });
		addAnnotation
		  (getDcatPublication_Description(),
		   source,
		   new String[] {
			   "documentation", "Explicit dct:description override. Unset derives from the provider\'s description, else from the GenModel documentation annotation of the provider\'s model type."
		   });
		addAnnotation
		  (getDcatPublication_Language(),
		   source,
		   new String[] {
			   "documentation", "Language tag of the published plain literals (title, description, keywords)."
		   });
		addAnnotation
		  (getDcatPublication_Keywords(),
		   source,
		   new String[] {
			   "documentation", "dcat:keyword entries of the published dataset(s)."
		   });
		addAnnotation
		  (getDcatPublication_Themes(),
		   source,
		   new String[] {
			   "documentation", "dcat:theme IRIs of the published dataset(s), e.g. an EU data theme vocabulary entry."
		   });
		addAnnotation
		  (getDcatPublication_PublisherName(),
		   source,
		   new String[] {
			   "documentation", "Name of the dct:publisher (a foaf:Agent). Mandatory for publication by the portal\'s shapes and not derivable from the data model - a publication without one is a diagnosed configuration error (DA-DCAT-9)."
		   });
		addAnnotation
		  (getDcatPublication_PublisherUri(),
		   source,
		   new String[] {
			   "documentation", "Optional IRI identifying the publisher agent."
		   });
		addAnnotation
		  (getDcatPublication_LicenseUri(),
		   source,
		   new String[] {
			   "documentation", "License IRI of the published distributions (e.g. a dcat-ap.de license vocabulary entry). Mandatory for a distribution by the portal\'s shapes - a publication whose provider serves distributions needs one (DA-DCAT-9)."
		   });
		addAnnotation
		  (jdbcDataSourceEClass,
		   source,
		   new String[] {
			   "documentation", "A reusable JDBC data source definition. At runtime it is bound to a pooled OSGi DataSource service that is selected via the OSGi target filter. Meant to live in a shared data source registry so it can be reused across DataInputs and tenants."
		   });
		addAnnotation
		  (getJdbcDataSource_Filter(),
		   source,
		   new String[] {
			   "documentation", "OSGi target filter (LDAP-style) used to select the actual DataSource service to bind to, e.g. (datasource.name=Derby_MDO)."
		   });
		addAnnotation
		  (getJdbcDataSource_Id(),
		   source,
		   new String[] {
			   "documentation", "Unique identifier of this data source definition."
		   });
		addAnnotation
		  (getJdbcDataSource_Name(),
		   source,
		   new String[] {
			   "documentation", "Derives from the model annotation by default"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.eclipse.org/OCL/Collection</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createCollectionAnnotations() {
		String source = "http://www.eclipse.org/OCL/Collection";
		addAnnotation
		  (getRestDataService_Configuration(),
		   source,
		   new String[] {
			   "nullFree", "false"
		   });
		addAnnotation
		  (getXMLADataService_Configuration(),
		   source,
		   new String[] {
			   "nullFree", "false"
		   });
		addAnnotation
		  (getGraphQLDataService_Configuration(),
		   source,
		   new String[] {
			   "nullFree", "false"
		   });
		addAnnotation
		  (getQGisDataService_Configuration(),
		   source,
		   new String[] {
			   "nullFree", "false"
		   });
		addAnnotation
		  (getODataDataService_Configuration(),
		   source,
		   new String[] {
			   "nullFree", "false"
		   });
	}

} //DAConfigPackageImpl
