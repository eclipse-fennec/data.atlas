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
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.fennec.atlas.configuration.AtlasConfigPackage;

import org.eclipse.fennec.data.atlas.configuration.BridgeRepository;
import org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport;
import org.eclipse.fennec.data.atlas.configuration.DAConfigFactory;
import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasProvider;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasService;
import org.eclipse.fennec.data.atlas.configuration.DataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.DcatPublication;
import org.eclipse.fennec.data.atlas.configuration.DistributionExport;
import org.eclipse.fennec.data.atlas.configuration.GeoJsonDataService;
import org.eclipse.fennec.data.atlas.configuration.GeoJsonDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.GraphQLDataService;
import org.eclipse.fennec.data.atlas.configuration.GraphQLDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.ODataDataService;
import org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.OgcSensorThingsDataService;
import org.eclipse.fennec.data.atlas.configuration.QGisDataService;
import org.eclipse.fennec.data.atlas.configuration.QGisDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.QueryTransformation;
import org.eclipse.fennec.data.atlas.configuration.RestDataService;
import org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.configuration.XMLADataService;
import org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration;

import org.eclipse.fennec.model.expression.ExpressionPackage;

import org.eclipse.fennec.model.query.QueryPackage;

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
	private EClass dataAtlasProviderEClass = null;

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
	private EClass dataAtlasServiceEClass = null;

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
	private EClass geoJsonDataServiceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass geoJsonDataServiceConfigurationEClass = null;

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
	private EClass ogcFeaturesDataServiceConfigurationEClass = null;

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
	private EClass dcatPublicationEClass = null;

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
		AtlasConfigPackage.eINSTANCE.eClass();
		QueryPackage.eINSTANCE.eClass();
		ExpressionPackage.eINSTANCE.eClass();

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
	public EClass getDataAtlasProvider() {
		return dataAtlasProviderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataAtlasProvider_Transformation() {
		return (EReference)dataAtlasProviderEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataAtlasProvider_DistributionExport() {
		return (EReference)dataAtlasProviderEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getDataAtlasProvider_Publication() {
		return (EReference)dataAtlasProviderEClass.getEStructuralFeatures().get(2);
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
	public EClass getDataAtlasService() {
		return dataAtlasServiceEClass;
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
	public EAttribute getRestDataServiceConfiguration_OffsetParameterName() {
		return (EAttribute)restDataServiceConfigurationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRestDataServiceConfiguration_LimitParameterName() {
		return (EAttribute)restDataServiceConfigurationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getGeoJsonDataService() {
		return geoJsonDataServiceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getGeoJsonDataService_Configuration() {
		return (EReference)geoJsonDataServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getGeoJsonDataServiceConfiguration() {
		return geoJsonDataServiceConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeoJsonDataServiceConfiguration_Path() {
		return (EAttribute)geoJsonDataServiceConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeoJsonDataServiceConfiguration_BatchSize() {
		return (EAttribute)geoJsonDataServiceConfigurationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeoJsonDataServiceConfiguration_BatchSizeLimit() {
		return (EAttribute)geoJsonDataServiceConfigurationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeoJsonDataServiceConfiguration_OffsetParameterName() {
		return (EAttribute)geoJsonDataServiceConfigurationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeoJsonDataServiceConfiguration_LimitParameterName() {
		return (EAttribute)geoJsonDataServiceConfigurationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeoJsonDataServiceConfiguration_LongitudeFeature() {
		return (EAttribute)geoJsonDataServiceConfigurationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeoJsonDataServiceConfiguration_LatitudeFeature() {
		return (EAttribute)geoJsonDataServiceConfigurationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeoJsonDataServiceConfiguration_ElevationFeature() {
		return (EAttribute)geoJsonDataServiceConfigurationEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeoJsonDataServiceConfiguration_GeometryFeature() {
		return (EAttribute)geoJsonDataServiceConfigurationEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getGeoJsonDataServiceConfiguration_IdFeature() {
		return (EAttribute)geoJsonDataServiceConfigurationEClass.getEStructuralFeatures().get(9);
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
	public EAttribute getOgcFeaturesDataService_DefaultLimit() {
		return (EAttribute)ogcFeaturesDataServiceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOgcFeaturesDataService_MaxLimit() {
		return (EAttribute)ogcFeaturesDataServiceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOgcFeaturesDataService_Configuration() {
		return (EReference)ogcFeaturesDataServiceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOgcFeaturesDataServiceConfiguration() {
		return ogcFeaturesDataServiceConfigurationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOgcFeaturesDataServiceConfiguration_CollectionId() {
		return (EAttribute)ogcFeaturesDataServiceConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOgcFeaturesDataServiceConfiguration_Title() {
		return (EAttribute)ogcFeaturesDataServiceConfigurationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOgcFeaturesDataServiceConfiguration_IdFeature() {
		return (EAttribute)ogcFeaturesDataServiceConfigurationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOgcFeaturesDataServiceConfiguration_GeometryFeature() {
		return (EAttribute)ogcFeaturesDataServiceConfigurationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOgcFeaturesDataServiceConfiguration_BboxFeatures() {
		return (EAttribute)ogcFeaturesDataServiceConfigurationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOgcFeaturesDataServiceConfiguration_TemporalFeature() {
		return (EAttribute)ogcFeaturesDataServiceConfigurationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOgcFeaturesDataServiceConfiguration_LayerGroup() {
		return (EAttribute)ogcFeaturesDataServiceConfigurationEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOgcFeaturesDataServiceConfiguration_Style() {
		return (EAttribute)ogcFeaturesDataServiceConfigurationEClass.getEStructuralFeatures().get(7);
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
	public EAttribute getODataDataServiceConfiguration_EntitySetName() {
		return (EAttribute)oDataDataServiceConfigurationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getODataDataServiceConfiguration_BatchSizeLimit() {
		return (EAttribute)oDataDataServiceConfigurationEClass.getEStructuralFeatures().get(1);
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
	public EAttribute getDcatPublication_RightsHolderName() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_RightsHolderUri() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_ContactName() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_ContactEmail() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_ContactUrl() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(15);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDcatPublication_LicenseUri() {
		return (EAttribute)dcatPublicationEClass.getEStructuralFeatures().get(16);
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

		dataAtlasProviderEClass = createEClass(DATA_ATLAS_PROVIDER);
		createEReference(dataAtlasProviderEClass, DATA_ATLAS_PROVIDER__TRANSFORMATION);
		createEReference(dataAtlasProviderEClass, DATA_ATLAS_PROVIDER__DISTRIBUTION_EXPORT);
		createEReference(dataAtlasProviderEClass, DATA_ATLAS_PROVIDER__PUBLICATION);

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

		dataAtlasServiceEClass = createEClass(DATA_ATLAS_SERVICE);

		restDataServiceEClass = createEClass(REST_DATA_SERVICE);
		createEReference(restDataServiceEClass, REST_DATA_SERVICE__CONFIGURATION);
		createEAttribute(restDataServiceEClass, REST_DATA_SERVICE__OPEN_API);

		dataServiceConfigurationEClass = createEClass(DATA_SERVICE_CONFIGURATION);
		createEAttribute(dataServiceConfigurationEClass, DATA_SERVICE_CONFIGURATION__ID);
		createEReference(dataServiceConfigurationEClass, DATA_SERVICE_CONFIGURATION__DATA_SET);

		restDataServiceConfigurationEClass = createEClass(REST_DATA_SERVICE_CONFIGURATION);
		createEAttribute(restDataServiceConfigurationEClass, REST_DATA_SERVICE_CONFIGURATION__PATH);
		createEAttribute(restDataServiceConfigurationEClass, REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE);
		createEAttribute(restDataServiceConfigurationEClass, REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT);
		createEAttribute(restDataServiceConfigurationEClass, REST_DATA_SERVICE_CONFIGURATION__OFFSET_PARAMETER_NAME);
		createEAttribute(restDataServiceConfigurationEClass, REST_DATA_SERVICE_CONFIGURATION__LIMIT_PARAMETER_NAME);

		geoJsonDataServiceEClass = createEClass(GEO_JSON_DATA_SERVICE);
		createEReference(geoJsonDataServiceEClass, GEO_JSON_DATA_SERVICE__CONFIGURATION);

		geoJsonDataServiceConfigurationEClass = createEClass(GEO_JSON_DATA_SERVICE_CONFIGURATION);
		createEAttribute(geoJsonDataServiceConfigurationEClass, GEO_JSON_DATA_SERVICE_CONFIGURATION__PATH);
		createEAttribute(geoJsonDataServiceConfigurationEClass, GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE);
		createEAttribute(geoJsonDataServiceConfigurationEClass, GEO_JSON_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT);
		createEAttribute(geoJsonDataServiceConfigurationEClass, GEO_JSON_DATA_SERVICE_CONFIGURATION__OFFSET_PARAMETER_NAME);
		createEAttribute(geoJsonDataServiceConfigurationEClass, GEO_JSON_DATA_SERVICE_CONFIGURATION__LIMIT_PARAMETER_NAME);
		createEAttribute(geoJsonDataServiceConfigurationEClass, GEO_JSON_DATA_SERVICE_CONFIGURATION__LONGITUDE_FEATURE);
		createEAttribute(geoJsonDataServiceConfigurationEClass, GEO_JSON_DATA_SERVICE_CONFIGURATION__LATITUDE_FEATURE);
		createEAttribute(geoJsonDataServiceConfigurationEClass, GEO_JSON_DATA_SERVICE_CONFIGURATION__ELEVATION_FEATURE);
		createEAttribute(geoJsonDataServiceConfigurationEClass, GEO_JSON_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE);
		createEAttribute(geoJsonDataServiceConfigurationEClass, GEO_JSON_DATA_SERVICE_CONFIGURATION__ID_FEATURE);

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
		createEAttribute(ogcFeaturesDataServiceEClass, OGC_FEATURES_DATA_SERVICE__DEFAULT_LIMIT);
		createEAttribute(ogcFeaturesDataServiceEClass, OGC_FEATURES_DATA_SERVICE__MAX_LIMIT);
		createEReference(ogcFeaturesDataServiceEClass, OGC_FEATURES_DATA_SERVICE__CONFIGURATION);

		ogcFeaturesDataServiceConfigurationEClass = createEClass(OGC_FEATURES_DATA_SERVICE_CONFIGURATION);
		createEAttribute(ogcFeaturesDataServiceConfigurationEClass, OGC_FEATURES_DATA_SERVICE_CONFIGURATION__COLLECTION_ID);
		createEAttribute(ogcFeaturesDataServiceConfigurationEClass, OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TITLE);
		createEAttribute(ogcFeaturesDataServiceConfigurationEClass, OGC_FEATURES_DATA_SERVICE_CONFIGURATION__ID_FEATURE);
		createEAttribute(ogcFeaturesDataServiceConfigurationEClass, OGC_FEATURES_DATA_SERVICE_CONFIGURATION__GEOMETRY_FEATURE);
		createEAttribute(ogcFeaturesDataServiceConfigurationEClass, OGC_FEATURES_DATA_SERVICE_CONFIGURATION__BBOX_FEATURES);
		createEAttribute(ogcFeaturesDataServiceConfigurationEClass, OGC_FEATURES_DATA_SERVICE_CONFIGURATION__TEMPORAL_FEATURE);
		createEAttribute(ogcFeaturesDataServiceConfigurationEClass, OGC_FEATURES_DATA_SERVICE_CONFIGURATION__LAYER_GROUP);
		createEAttribute(ogcFeaturesDataServiceConfigurationEClass, OGC_FEATURES_DATA_SERVICE_CONFIGURATION__STYLE);

		ogcSensorThingsDataServiceEClass = createEClass(OGC_SENSOR_THINGS_DATA_SERVICE);

		queryTransformationEClass = createEClass(QUERY_TRANSFORMATION);

		oDataDataServiceEClass = createEClass(ODATA_DATA_SERVICE);
		createEReference(oDataDataServiceEClass, ODATA_DATA_SERVICE__CONFIGURATION);

		oDataDataServiceConfigurationEClass = createEClass(ODATA_DATA_SERVICE_CONFIGURATION);
		createEAttribute(oDataDataServiceConfigurationEClass, ODATA_DATA_SERVICE_CONFIGURATION__ENTITY_SET_NAME);
		createEAttribute(oDataDataServiceConfigurationEClass, ODATA_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT);

		distributionExportEClass = createEClass(DISTRIBUTION_EXPORT);
		createEAttribute(distributionExportEClass, DISTRIBUTION_EXPORT__ID);
		createEAttribute(distributionExportEClass, DISTRIBUTION_EXPORT__NAME);
		createEAttribute(distributionExportEClass, DISTRIBUTION_EXPORT__DESCRIPTION);
		createEAttribute(distributionExportEClass, DISTRIBUTION_EXPORT__MEDIA_TYPE);

		csvDistributionExportEClass = createEClass(CSV_DISTRIBUTION_EXPORT);
		createEAttribute(csvDistributionExportEClass, CSV_DISTRIBUTION_EXPORT__SEPARATOR);
		createEAttribute(csvDistributionExportEClass, CSV_DISTRIBUTION_EXPORT__COMPRESSED);
		createEAttribute(csvDistributionExportEClass, CSV_DISTRIBUTION_EXPORT__INCLUDE_TYPE_HEADER);

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
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__RIGHTS_HOLDER_NAME);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__RIGHTS_HOLDER_URI);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__CONTACT_NAME);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__CONTACT_EMAIL);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__CONTACT_URL);
		createEAttribute(dcatPublicationEClass, DCAT_PUBLICATION__LICENSE_URI);
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
		AtlasConfigPackage theAtlasConfigPackage = (AtlasConfigPackage)EPackage.Registry.INSTANCE.getEPackage(AtlasConfigPackage.eNS_URI);
		QueryPackage theQueryPackage = (QueryPackage)EPackage.Registry.INSTANCE.getEPackage(QueryPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		dataAtlasProviderEClass.getESuperTypes().add(theAtlasConfigPackage.getDataProvider());
		bridgeRepositoryEClass.getESuperTypes().add(theAtlasConfigPackage.getDataInput());
		dataSetEClass.getESuperTypes().add(this.getDataAtlasProvider());
		dataAtlasServiceEClass.getESuperTypes().add(theAtlasConfigPackage.getDataService());
		dataAtlasServiceEClass.getESuperTypes().add(this.getDataAtlasProvider());
		restDataServiceEClass.getESuperTypes().add(this.getDataAtlasService());
		restDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		geoJsonDataServiceEClass.getESuperTypes().add(this.getDataAtlasService());
		geoJsonDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		xmlaDataServiceEClass.getESuperTypes().add(this.getDataAtlasService());
		xmlaDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		graphQLDataServiceEClass.getESuperTypes().add(this.getDataAtlasService());
		qGisDataServiceEClass.getESuperTypes().add(this.getDataAtlasService());
		qGisDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		graphQLDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		ogcFeaturesDataServiceEClass.getESuperTypes().add(this.getDataAtlasService());
		ogcFeaturesDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		ogcSensorThingsDataServiceEClass.getESuperTypes().add(this.getDataAtlasService());
		queryTransformationEClass.getESuperTypes().add(theAtlasConfigPackage.getTransformation());
		oDataDataServiceEClass.getESuperTypes().add(this.getDataAtlasService());
		oDataDataServiceConfigurationEClass.getESuperTypes().add(this.getDataServiceConfiguration());
		csvDistributionExportEClass.getESuperTypes().add(this.getDistributionExport());

		// Initialize classes, features, and operations; add parameters
		initEClass(dataAtlasConfigurationEClass, DataAtlasConfiguration.class, "DataAtlasConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDataAtlasConfiguration_Name(), ecorePackage.getEString(), "name", null, 1, 1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDataAtlasConfiguration_Description(), ecorePackage.getEString(), "description", null, 0, 1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_DataSources(), theAtlasConfigPackage.getDataSource(), null, "dataSources", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_DataInputs(), theAtlasConfigPackage.getDataInput(), null, "dataInputs", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_DataSets(), this.getDataSet(), null, "dataSets", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_Services(), this.getDataAtlasService(), null, "services", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_Exports(), this.getDistributionExport(), null, "exports", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_Transformations(), theAtlasConfigPackage.getTransformation(), null, "transformations", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasConfiguration_Publications(), this.getDcatPublication(), null, "publications", null, 0, -1, DataAtlasConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataAtlasProviderEClass, DataAtlasProvider.class, "DataAtlasProvider", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataAtlasProvider_Transformation(), theAtlasConfigPackage.getTransformation(), null, "transformation", null, 0, 1, DataAtlasProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasProvider_DistributionExport(), this.getDistributionExport(), null, "distributionExport", null, 0, -1, DataAtlasProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataAtlasProvider_Publication(), this.getDcatPublication(), null, "publication", null, 0, 1, DataAtlasProvider.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(bridgeRepositoryEClass, BridgeRepository.class, "BridgeRepository", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getBridgeRepository_Source(), theAtlasConfigPackage.getDataInput(), null, "source", null, 1, 1, BridgeRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBridgeRepository_DataTrafo(), theAtlasConfigPackage.getDataTransformation(), null, "dataTrafo", null, 1, 1, BridgeRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBridgeRepository_QueryTrafo(), this.getQueryTransformation(), null, "queryTrafo", null, 0, 1, BridgeRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getBridgeRepository_Filter(), ecorePackage.getEObject(), null, "filter", null, 0, 1, BridgeRepository.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataSetEClass, DataSet.class, "DataSet", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getDataSet_InputType(), ecorePackage.getEClass(), null, "inputType", null, 1, 1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataSet_OutputType(), ecorePackage.getEClass(), null, "outputType", null, 1, 1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataSet_Query(), theQueryPackage.getQuery(), null, "query", null, 0, 1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataSet_ChildDataSet(), this.getDataSet(), this.getDataSet_ParentDataSet(), "childDataSet", null, 0, -1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataSet_ParentDataSet(), this.getDataSet(), this.getDataSet_ChildDataSet(), "parentDataSet", null, 0, 1, DataSet.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataAtlasServiceEClass, DataAtlasService.class, "DataAtlasService", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(restDataServiceEClass, RestDataService.class, "RestDataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRestDataService_Configuration(), this.getRestDataServiceConfiguration(), null, "configuration", null, 0, -1, RestDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);
		initEAttribute(getRestDataService_OpenAPI(), ecorePackage.getEBoolean(), "openAPI", "true", 1, 1, RestDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dataServiceConfigurationEClass, DataServiceConfiguration.class, "DataServiceConfiguration", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDataServiceConfiguration_Id(), ecorePackage.getEString(), "id", null, 1, 1, DataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getDataServiceConfiguration_DataSet(), this.getDataSet(), null, "dataSet", null, 1, 1, DataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(restDataServiceConfigurationEClass, RestDataServiceConfiguration.class, "RestDataServiceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRestDataServiceConfiguration_Path(), ecorePackage.getEString(), "path", null, 1, 1, RestDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestDataServiceConfiguration_BatchSize(), ecorePackage.getEBigInteger(), "batchSize", "-1", 1, 1, RestDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestDataServiceConfiguration_BatchSizeLimit(), ecorePackage.getEBigInteger(), "batchSizeLimit", "-1", 1, 1, RestDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestDataServiceConfiguration_OffsetParameterName(), ecorePackage.getEString(), "offsetParameterName", "offset", 1, 1, RestDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRestDataServiceConfiguration_LimitParameterName(), ecorePackage.getEString(), "limitParameterName", "limit", 1, 1, RestDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(geoJsonDataServiceEClass, GeoJsonDataService.class, "GeoJsonDataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getGeoJsonDataService_Configuration(), this.getGeoJsonDataServiceConfiguration(), null, "configuration", null, 0, -1, GeoJsonDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(geoJsonDataServiceConfigurationEClass, GeoJsonDataServiceConfiguration.class, "GeoJsonDataServiceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getGeoJsonDataServiceConfiguration_Path(), ecorePackage.getEString(), "path", null, 1, 1, GeoJsonDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeoJsonDataServiceConfiguration_BatchSize(), ecorePackage.getEBigInteger(), "batchSize", "-1", 1, 1, GeoJsonDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeoJsonDataServiceConfiguration_BatchSizeLimit(), ecorePackage.getEBigInteger(), "batchSizeLimit", "-1", 1, 1, GeoJsonDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeoJsonDataServiceConfiguration_OffsetParameterName(), ecorePackage.getEString(), "offsetParameterName", "offset", 1, 1, GeoJsonDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeoJsonDataServiceConfiguration_LimitParameterName(), ecorePackage.getEString(), "limitParameterName", "limit", 1, 1, GeoJsonDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeoJsonDataServiceConfiguration_LongitudeFeature(), ecorePackage.getEString(), "longitudeFeature", null, 0, 1, GeoJsonDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeoJsonDataServiceConfiguration_LatitudeFeature(), ecorePackage.getEString(), "latitudeFeature", null, 0, 1, GeoJsonDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeoJsonDataServiceConfiguration_ElevationFeature(), ecorePackage.getEString(), "elevationFeature", null, 0, 1, GeoJsonDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeoJsonDataServiceConfiguration_GeometryFeature(), ecorePackage.getEString(), "geometryFeature", null, 0, 1, GeoJsonDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getGeoJsonDataServiceConfiguration_IdFeature(), ecorePackage.getEString(), "idFeature", null, 0, 1, GeoJsonDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
		initEAttribute(getOgcFeaturesDataService_DefaultLimit(), ecorePackage.getEBigInteger(), "defaultLimit", "-1", 1, 1, OgcFeaturesDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOgcFeaturesDataService_MaxLimit(), ecorePackage.getEBigInteger(), "maxLimit", "-1", 1, 1, OgcFeaturesDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOgcFeaturesDataService_Configuration(), this.getOgcFeaturesDataServiceConfiguration(), null, "configuration", null, 0, -1, OgcFeaturesDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(ogcFeaturesDataServiceConfigurationEClass, OgcFeaturesDataServiceConfiguration.class, "OgcFeaturesDataServiceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOgcFeaturesDataServiceConfiguration_CollectionId(), ecorePackage.getEString(), "collectionId", null, 0, 1, OgcFeaturesDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOgcFeaturesDataServiceConfiguration_Title(), ecorePackage.getEString(), "title", null, 0, 1, OgcFeaturesDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOgcFeaturesDataServiceConfiguration_IdFeature(), ecorePackage.getEString(), "idFeature", null, 0, 1, OgcFeaturesDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOgcFeaturesDataServiceConfiguration_GeometryFeature(), ecorePackage.getEString(), "geometryFeature", null, 0, 1, OgcFeaturesDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOgcFeaturesDataServiceConfiguration_BboxFeatures(), ecorePackage.getEString(), "bboxFeatures", null, 0, 4, OgcFeaturesDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOgcFeaturesDataServiceConfiguration_TemporalFeature(), ecorePackage.getEString(), "temporalFeature", null, 0, 1, OgcFeaturesDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOgcFeaturesDataServiceConfiguration_LayerGroup(), ecorePackage.getEString(), "layerGroup", null, 0, 1, OgcFeaturesDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOgcFeaturesDataServiceConfiguration_Style(), ecorePackage.getEString(), "style", null, 0, 1, OgcFeaturesDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ogcSensorThingsDataServiceEClass, OgcSensorThingsDataService.class, "OgcSensorThingsDataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(queryTransformationEClass, QueryTransformation.class, "QueryTransformation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(oDataDataServiceEClass, ODataDataService.class, "ODataDataService", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getODataDataService_Configuration(), this.getODataDataServiceConfiguration(), null, "configuration", null, 0, -1, ODataDataService.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, !IS_ORDERED);

		initEClass(oDataDataServiceConfigurationEClass, ODataDataServiceConfiguration.class, "ODataDataServiceConfiguration", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getODataDataServiceConfiguration_EntitySetName(), ecorePackage.getEString(), "entitySetName", null, 0, 1, ODataDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getODataDataServiceConfiguration_BatchSizeLimit(), ecorePackage.getEBigInteger(), "batchSizeLimit", "-1", 1, 1, ODataDataServiceConfiguration.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(distributionExportEClass, DistributionExport.class, "DistributionExport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDistributionExport_Id(), ecorePackage.getEString(), "id", null, 1, 1, DistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDistributionExport_Name(), ecorePackage.getEString(), "name", null, 1, 1, DistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDistributionExport_Description(), ecorePackage.getEString(), "description", null, 1, 1, DistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDistributionExport_MediaType(), ecorePackage.getEString(), "mediaType", null, 0, 1, DistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(csvDistributionExportEClass, CSVDistributionExport.class, "CSVDistributionExport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCSVDistributionExport_Separator(), ecorePackage.getEString(), "separator", null, 0, 1, CSVDistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCSVDistributionExport_Compressed(), ecorePackage.getEBoolean(), "compressed", null, 0, 1, CSVDistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCSVDistributionExport_IncludeTypeHeader(), ecorePackage.getEBoolean(), "includeTypeHeader", null, 0, 1, CSVDistributionExport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

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
		initEAttribute(getDcatPublication_RightsHolderName(), ecorePackage.getEString(), "rightsHolderName", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_RightsHolderUri(), ecorePackage.getEString(), "rightsHolderUri", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_ContactName(), ecorePackage.getEString(), "contactName", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_ContactEmail(), ecorePackage.getEString(), "contactEmail", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_ContactUrl(), ecorePackage.getEString(), "contactUrl", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDcatPublication_LicenseUri(), ecorePackage.getEString(), "licenseUri", null, 0, 1, DcatPublication.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.eclipse.org/OCL/Import
		createImportAnnotations();
		// http://www.eclipse.org/emf/2002/GenModel
		createGenModelAnnotations();
		// https://eclipse.org/fennec/data/atlas/configuration/status
		createStatusAnnotations();
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
			   "ecore", "http://www.eclipse.org/emf/2002/Ecore"
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
			   "documentation", "Configuration model for the Fennec Data-Atlas.\n\nIt describes - independent of any concrete deployment - which data sources (DataInput) provide data, which DataSets are published, which DataServices (REST, OData, OGC, QGis, XMLA, GraphQL) expose them, which reusable DistributionExport templates are used for serialization, and which Transformations map between models.\nAn instance of this model is meant to fully configure a Data-Atlas runtime, while the model types it refers to are resolved against the Model-Atlas EPackage registry.\n\nThe building blocks shared by all Fennec atlases - data sources (DataSource and its subtypes), data inputs (DataInput, FileDataInput, MongoDataInput, ...) and the DataProvider/DataService bases - come from the shared atlas configuration model (https://eclipse.org/fennec/atlas/configuration/1.0.0, org.eclipse.fennec.atlas.configuration.model); JPADataInput from its JPA extension (https://eclipse.org/fennec/atlas/configuration/jpa/1.0.0). The engine-neutral Transformation/DataTransformation also come from the shared model, the QVT-O binding QvtoDataTransformation from its QVT-O extension (https://eclipse.org/fennec/atlas/configuration/qvto/1.0.0). This model adds what is specific to the Data Atlas: the root registries, DataSets, the concrete DataServices, exports, query transformations, bridges and publications.",
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
			   "documentation", "Registry of reusable data source definitions (JdbcDataSource, MongoDataSource of the shared atlas configuration model), referenced from DataInputs. Kinds the Data Atlas cannot realize (e.g. MqttDataSource) are diagnosed and skipped.\nA data source either BINDS to a service the deployment configured (filter set) or is MATERIALIZED from its connection definition by the Data Atlas (host and database set) - never both."
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
			   "documentation", "Registry of reusable DcatPublication declarations, referenced from DataServices and DataSets (via DataAtlasProvider.publication)."
		   });
		addAnnotation
		  (dataAtlasProviderEClass,
		   source,
		   new String[] {
			   "documentation", "Data-Atlas refinement of the shared DataProvider (id, name, description, dataInput): anything that provides data and can be distributed - a DataService (via DataAtlasService) or a concrete DataSet. Adds the rest of the data configuration trias besides the dataInput - transformation and distribution exports - plus the opt-in publication.\n\nA DataSet refines its enclosing DataService: if it sets one of the trias features (dataInput, transformation, distributionExport) itself, that value overrides the one of the DataService; if it leaves the feature unset, the value of the DataService applies (override-else-default). Runtime constraint: a DataService that does not contain any DataSet must define a dataInput."
		   });
		addAnnotation
		  (getDataAtlasProvider_Transformation(),
		   source,
		   new String[] {
			   "documentation", "Optional transformation applied to the data of this provider. On a DataSet it overrides the transformation of the enclosing DataService; if left unset, the service\'s transformation applies."
		   });
		addAnnotation
		  (getDataAtlasProvider_DistributionExport(),
		   source,
		   new String[] {
			   "documentation", "References reusable DistributionExport templates (defined in the export registry) describing the formats this provider can be serialized to.\nOn a DataService this is the set of available/default exports. On a DataSet, if it defines its own exports they fully replace the service\'s; if left empty, the service\'s exports apply (override-else-default)."
		   });
		addAnnotation
		  (getDataAtlasProvider_Publication(),
		   source,
		   new String[] {
			   "documentation", "Opt-in open-data publication of this provider: references a DcatPublication declaration (defined in the publications registry). Absent means the provider is not published - nothing is published implicitly.\nFollows the override-else-default rule: what a DataService declares applies to its DataSets unless a DataSet references a DcatPublication of its own.\nDeliberately plain data - the mapping to a DCAT model lives in the omittable publication bundle, never in this model (see data.atlas issue #4, DA-DCAT-1)."
		   });
		addAnnotation
		  (bridgeRepositoryEClass,
		   source,
		   new String[] {
			   "documentation", "A DataInput that can load data from another source and transform it if necessary.\nBridgeRepository can be cascaded, so one original Entity can result in multiple intermediate formats if necessary.\n\nA bridge can have an attached data transformation for the source objects it loads.\nOn the other hand, incoming queries might be transformed as well.\n\nBridge contract: the data transformation is 1:1 - exactly one supported and one result EClass, one result object per source object, carrying the same id. That is what keeps paging push-down and by-id lookups through the bridge correct; a transformation violating it is refused.\n\nThe transformation types themselves (DataTransformation, and its QVT-O binding QvtoDataTransformation) come from the shared atlas configuration model and its QVT-O extension (https://eclipse.org/fennec/atlas/configuration/qvto/1.0.0)."
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
		  (dataAtlasServiceEClass,
		   source,
		   new String[] {
			   "documentation", "Abstract definition of a Data-Atlas endpoint to publish DataSets: the shared DataService (urlContext) with the Data-Atlas provider features (transformation, distribution exports, publication).\nAs a DataProvider a DataService can also act standalone - serving its own dataInput generically in the formats given by its distributionExport - or it can contain refined DataSets (via its configurations). Each configuration of a published DataService results in a DCAT Distribution."
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
			   "documentation", "Whether the service publishes its OpenAPI 3 document at {urlContext}/openapi.json (default true): the list and by-id path of every DataSet with its paging and query parameters, the media types it is served as, and the JSON schemas of the served types."
		   });
		addAnnotation
		  (dataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "documentation", "Abstract binding of a DataSet to a specific DataService (the link DataService -> configuration -> dataSet).\nHolds the service-specific, per-DataSet settings (e.g. REST path / mime types). The exports themselves are no longer held here - they are resolved via the DataService default and the DataSet override (see DataAtlasProvider.distributionExport)."
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
			   "documentation", "Everything the REST runtime needs to serve one DataSet of a RestDataService: its path under the service\'s urlContext, the paging defaults and bounds, and the names of the HTTP query parameters the paging is driven by. Nothing per-DataSet is configured on the service itself."
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
		  (getRestDataServiceConfiguration_OffsetParameterName(),
		   source,
		   new String[] {
			   "documentation", "Name of the HTTP query parameter carrying the start offset for pagination of this DataSet."
		   });
		addAnnotation
		  (getRestDataServiceConfiguration_LimitParameterName(),
		   source,
		   new String[] {
			   "documentation", "Name of the HTTP query parameter carrying the page size for pagination of this DataSet."
		   });
		addAnnotation
		  (geoJsonDataServiceEClass,
		   source,
		   new String[] {
			   "documentation", "DataService that publishes DataSets as RFC 7946 GeoJSON: GET {path} returns a FeatureCollection, GET {path}/{id} a single Feature, media type application/geo+json.\nA dedicated service kind rather than a media type on RestDataService, because serving GeoJSON needs mapping configuration (which features carry the geometry, what becomes the Feature id) that has no place on the generic REST service. Coordinates are assumed to be WGS 84 (RFC 7946 mandates it); coordinate transformation is a Transformation concern, not a serving one."
		   });
		addAnnotation
		  (getGeoJsonDataService_Configuration(),
		   source,
		   new String[] {
			   "documentation", "The per-DataSet GeoJSON configurations provided by this service."
		   });
		addAnnotation
		  (geoJsonDataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "documentation", "Per-DataSet configuration for a GeoJsonDataService: the path plus the mapping of the domain type onto a GeoJSON Feature.\nThe geometry source is EITHER geometryFeature (a feature already holding a org.geojson.model Geometry, passed through) OR the longitudeFeature/latitudeFeature pair of numeric attributes (mapped to a Point, elevationFeature optionally third) - declaring both, or neither, is a configuration error and the endpoint stays down. All attributes not consumed by the mapping become the Feature\'s properties."
		   });
		addAnnotation
		  (getGeoJsonDataServiceConfiguration_Path(),
		   source,
		   new String[] {
			   "documentation", "derives by default from the Dataset name"
		   });
		addAnnotation
		  (getGeoJsonDataServiceConfiguration_BatchSize(),
		   source,
		   new String[] {
			   "documentation", "the page size for pagination. -1 means no pagination"
		   });
		addAnnotation
		  (getGeoJsonDataServiceConfiguration_BatchSizeLimit(),
		   source,
		   new String[] {
			   "documentation", "the page size limit for pagination, so the server will not be overtaxed"
		   });
		addAnnotation
		  (getGeoJsonDataServiceConfiguration_OffsetParameterName(),
		   source,
		   new String[] {
			   "documentation", "Name of the HTTP query parameter carrying the start offset for pagination of this DataSet."
		   });
		addAnnotation
		  (getGeoJsonDataServiceConfiguration_LimitParameterName(),
		   source,
		   new String[] {
			   "documentation", "Name of the HTTP query parameter carrying the page size for pagination of this DataSet."
		   });
		addAnnotation
		  (getGeoJsonDataServiceConfiguration_LongitudeFeature(),
		   source,
		   new String[] {
			   "documentation", "Name of the numeric attribute of the DataSet\'s type carrying the WGS 84 longitude. Used together with latitudeFeature to build a Point geometry; mutually exclusive with geometryFeature."
		   });
		addAnnotation
		  (getGeoJsonDataServiceConfiguration_LatitudeFeature(),
		   source,
		   new String[] {
			   "documentation", "Name of the numeric attribute carrying the WGS 84 latitude; see longitudeFeature."
		   });
		addAnnotation
		  (getGeoJsonDataServiceConfiguration_ElevationFeature(),
		   source,
		   new String[] {
			   "documentation", "Optional name of the numeric attribute carrying the elevation, emitted as the Point\'s third coordinate."
		   });
		addAnnotation
		  (getGeoJsonDataServiceConfiguration_GeometryFeature(),
		   source,
		   new String[] {
			   "documentation", "Name of the feature already holding a org.geojson.model Geometry, passed through as the Feature\'s geometry; mutually exclusive with longitudeFeature/latitudeFeature."
		   });
		addAnnotation
		  (getGeoJsonDataServiceConfiguration_IdFeature(),
		   source,
		   new String[] {
			   "documentation", "Optional name of the attribute used as the Feature id. Unset falls back to the type\'s EMF id attribute; without either the Feature carries no id."
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
			   "documentation", "DataService that exposes DataSets via the OGC API - Features standard (Part 1 Core, Part 3 Filtering with CQL2), served by the Fennec OGC API Features server (emf.ogc.features). One service is one API root with its own landing page at urlContext: /conformance, /collections, /collections/{collectionId}/items (GeoJSON, HTML), the OpenAPI document at /api, a QGIS project at /collections?f=qgs and, when the viewer bundle is present, a map viewer at {urlContext}/viewer/. The root publishes exactly the collections its configurations declare; every collection is served through the ReadRepository of its DataSet\'s DataInput, so bbox, datetime and CQL2 filters push down into the input. Read only."
		   });
		addAnnotation
		  (getOgcFeaturesDataService_DefaultLimit(),
		   source,
		   new String[] {
			   "documentation", "Page size of /items when a request carries no limit parameter. -1 means the server default (10)."
		   });
		addAnnotation
		  (getOgcFeaturesDataService_MaxLimit(),
		   source,
		   new String[] {
			   "documentation", "Largest page size served; a larger limit is reduced to it. -1 means the server default (10000)."
		   });
		addAnnotation
		  (getOgcFeaturesDataService_Configuration(),
		   source,
		   new String[] {
			   "documentation", "The per-DataSet collection configurations provided by this service."
		   });
		addAnnotation
		  (ogcFeaturesDataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "documentation", "Everything the OGC API Features runtime needs to serve one DataSet of an OgcFeaturesDataService as a feature collection. The feature type is the DataSet\'s outputType. Override-else-default: an EClass carrying the https://eclipse.org/fennec/ogc/features annotation of emf.ogc.features (collection=true with id, geometry, bbox, temporal, idAttribute, layerGroup, style) is served as annotated; every feature set here replaces the annotation\'s value, and a schema without the annotation (e.g. one resolved from a Model Atlas) is served through these features alone. The geometry is a containment reference to the Geometry class of the GeoJSON EMF model (https://geojson.org/model/2016) - named by geometryFeature or the annotation, else the type\'s single such reference; a DataSet whose type has no geometry is a diagnosed configuration error and the collection stays down, as is a DataSet with a query (the base predicate cannot be composed with the collection filters). The feature id is idFeature, else the type\'s EMF id attribute - a collection needs one."
		   });
		addAnnotation
		  (getOgcFeaturesDataServiceConfiguration_CollectionId(),
		   source,
		   new String[] {
			   "documentation", "The collection id under the root (GET {urlContext}/collections/{collectionId}). Defaults to the annotation\'s id, else the outputType\'s EClass name; must be unique within the service."
		   });
		addAnnotation
		  (getOgcFeaturesDataServiceConfiguration_Title(),
		   source,
		   new String[] {
			   "documentation", "Title of the collection. Defaults to the annotation\'s title, else the DataSet\'s name."
		   });
		addAnnotation
		  (getOgcFeaturesDataServiceConfiguration_IdFeature(),
		   source,
		   new String[] {
			   "documentation", "Name of the attribute used as the feature id. Defaults to the annotation\'s idAttribute, else the EMF id attribute of the type."
		   });
		addAnnotation
		  (getOgcFeaturesDataServiceConfiguration_GeometryFeature(),
		   source,
		   new String[] {
			   "documentation", "Name of the feature holding the feature geometry: a containment reference to the Geometry class of the GeoJSON EMF model (https://geojson.org/model/2016). Defaults to the annotation\'s geometry, else the type\'s single such reference."
		   });
		addAnnotation
		  (getOgcFeaturesDataServiceConfiguration_BboxFeatures(),
		   source,
		   new String[] {
			   "documentation", "The four attributes minX, minY, maxX, maxY holding the persisted bounding box of a feature, in this order; lets bbox requests push down into the input instead of testing every geometry. Defaults to the annotation\'s bbox."
		   });
		addAnnotation
		  (getOgcFeaturesDataServiceConfiguration_TemporalFeature(),
		   source,
		   new String[] {
			   "documentation", "Name of the date/time attribute the datetime parameter filters on. Defaults to the annotation\'s temporal."
		   });
		addAnnotation
		  (getOgcFeaturesDataServiceConfiguration_LayerGroup(),
		   source,
		   new String[] {
			   "documentation", "Group the map viewer and the QGIS project put the collection\'s layer in. Defaults to the annotation\'s layerGroup."
		   });
		addAnnotation
		  (getOgcFeaturesDataServiceConfiguration_Style(),
		   source,
		   new String[] {
			   "documentation", "Display style of the layer for the viewer and the QGIS project: a CSS color or a JSON object, as in the annotation. Defaults to the annotation\'s style."
		   });
		addAnnotation
		  (ogcSensorThingsDataServiceEClass,
		   source,
		   new String[] {
			   "documentation", "DataService that exposes DataSets via the OGC SensorThings API."
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
			   "documentation", "Everything the OData runtime needs to serve one DataSet of an ODataDataService as an entity set: the DataSet reference, the entity set\'s name under the service root and the server-side $top ceiling. The entity type is the DataSet\'s outputType; it needs an identity (an iD attribute or the idFeatures annotation of the fennec persistence stack), and within one service every outputType is served by exactly one configuration - OData addresses entity sets by type. A DataSet with a query cannot be served as OData (the base predicate would have to be composed with $filter) and keeps the entity set down."
		   });
		addAnnotation
		  (getODataDataServiceConfiguration_EntitySetName(),
		   source,
		   new String[] {
			   "documentation", "The entity set name under the service root (GET {urlContext}/{entitySetName}). Defaults to the name of the DataSet\'s outputType EClass; must be unique within the service."
		   });
		addAnnotation
		  (getODataDataServiceConfiguration_BatchSizeLimit(),
		   source,
		   new String[] {
			   "documentation", "Server-side $top ceiling (applies even without a client $top). -1 means the runtime default (1000). The OData server enforces the ceiling per service root, so the smallest positive limit declared by the service\'s configurations applies to all of its entity sets."
		   });
		addAnnotation
		  (distributionExportEClass,
		   source,
		   new String[] {
			   "documentation", "A reusable export/serialization template for a distribution (e.g. CSV settings). Defined once in the export registry and referenced from DataServices and DataSets (via DataAtlasProvider.distributionExport), so the same export settings are not duplicated per provider."
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
		  (dcatPublicationEClass,
		   source,
		   new String[] {
			   "documentation", "Declares that a DataProvider referencing this element is published as open data to a DCAT portal (DCAT.Atlas). Defined once in the publications registry and referenced from DataServices and DataSets (via DataAtlasProvider.publication), so the same target catalog and metadata defaults are not duplicated per provider.\n\nDeliberately plain data: an id, a catalog name and a handful of metadata overrides. The mapping to dcat:Dataset / dcat:DataService lives in the omittable publication bundle - this model must never depend on a DCAT model (data.atlas issue #4, DA-DCAT-1). Metadata left unset here is derived from the provider (name, description, model annotations); an explicit value wins over a derived one (DA-DCAT-8).\n\nThe portal endpoint itself (base URL, credentials, timeouts) is a deployment concern and is NOT configured here: it is the Config-Admin configuration of the dcat.atlas client (DA-DCAT-6)."
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
		  (getDcatPublication_RightsHolderName(),
		   source,
		   new String[] {
			   "documentation", "Optional name of the dct:rightsHolder (a foaf:Agent): who owns or manages the rights over the published entries, where that is somebody other than the publisher - e.g. a utility publishes data whose rights the city holds. Unset means no rights holder is stated."
		   });
		addAnnotation
		  (getDcatPublication_RightsHolderUri(),
		   source,
		   new String[] {
			   "documentation", "Optional IRI identifying the rights holder agent. Only used together with rightsHolderName."
		   });
		addAnnotation
		  (getDcatPublication_ContactName(),
		   source,
		   new String[] {
			   "documentation", "Optional formatted name (vcard:fn) of the dcat:contactPoint (a vcard:Organization) of the published entries. A contact point is stated when at least one of contactName, contactEmail and contactUrl is set."
		   });
		addAnnotation
		  (getDcatPublication_ContactEmail(),
		   source,
		   new String[] {
			   "documentation", "Optional e-mail address of the contact point (vcard:hasEmail). A plain address is published as a mailto: IRI."
		   });
		addAnnotation
		  (getDcatPublication_ContactUrl(),
		   source,
		   new String[] {
			   "documentation", "Optional web page of the contact point (vcard:hasURL), e.g. a contact form."
		   });
		addAnnotation
		  (getDcatPublication_LicenseUri(),
		   source,
		   new String[] {
			   "documentation", "License IRI of the published distributions (e.g. a dcat-ap.de license vocabulary entry). Mandatory for a distribution by the portal\'s shapes - a publication whose provider serves distributions needs one (DA-DCAT-9)."
		   });
	}

	/**
	 * Initializes the annotations for <b>https://eclipse.org/fennec/data/atlas/configuration/status</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createStatusAnnotations() {
		String source = "https://eclipse.org/fennec/data/atlas/configuration/status";
		addAnnotation
		  (getBridgeRepository_QueryTrafo(),
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "recognized but not executed: a bridge with a queryTrafo is refused and stays down"
		   });
		addAnnotation
		  (getBridgeRepository_Filter(),
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "placeholder without runtime effect"
		   });
		addAnnotation
		  (getDataSet_ChildDataSet(),
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "no runtime effect"
		   });
		addAnnotation
		  (getDataSet_ParentDataSet(),
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "no runtime effect"
		   });
		addAnnotation
		  (xmlaDataServiceEClass,
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "no XMLA endpoint configurator exists"
		   });
		addAnnotation
		  (xmlaDataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "no XMLA endpoint configurator exists"
		   });
		addAnnotation
		  (graphQLDataServiceEClass,
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "no GraphQL endpoint configurator exists"
		   });
		addAnnotation
		  (qGisDataServiceEClass,
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "no QGis endpoint configurator exists"
		   });
		addAnnotation
		  (qGisDataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "no QGis endpoint configurator exists"
		   });
		addAnnotation
		  (graphQLDataServiceConfigurationEClass,
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "no GraphQL endpoint configurator exists"
		   });
		addAnnotation
		  (ogcSensorThingsDataServiceEClass,
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "no OGC SensorThings endpoint configurator exists"
		   });
		addAnnotation
		  (queryTransformationEClass,
		   source,
		   new String[] {
			   "implementation", "not implemented yet",
			   "note", "no query transformation engine is wired; a bridge that references one stays down"
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
		  (getGeoJsonDataService_Configuration(),
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
		  (getOgcFeaturesDataService_Configuration(),
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
