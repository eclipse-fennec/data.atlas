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


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.fennec.emf.osgi.annotation.provide.EPackage;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Configuration model for the Fennec Data-Atlas.
 * 
 * It describes - independent of any concrete deployment - which data sources (DataInput) provide data, which DataSets are published, which DataServices (REST, OData, OGC, QGis, XMLA, GraphQL) expose them, which reusable DistributionExport templates are used for serialization, and which Transformations map between models.
 * An instance of this model is meant to fully configure a Data-Atlas runtime, while the model types it refers to are resolved against the Model-Atlas EPackage registry.
 * <!-- end-model-doc -->
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigFactory
 * @model kind="package"
 *        annotation="http://www.eclipse.org/OCL/Import ecore='http://www.eclipse.org/emf/2002/Ecore' eorm='platform:/resource/org.eclipse.fennec.persistence.orm/model/eorm.ecore#/'"
 *        annotation="http://www.eclipse.org/emf/2002/GenModel oSGiCompatible='true' basePackage='org.eclipse.fennec.data.atlas' copyrightText='******************************************************************\nCopyright (c) 2026 Contributors to the Eclipse Foundation.\n\nThis program and the accompanying materials are made\navailable under the terms of the Eclipse Public License 2.0\nwhich is available at https://www.eclipse.org/legal/epl-2.0/\n\nSPDX-License-Identifier: EPL-2.0\n\nContributors:\n  Data In Motion Consulting - initial implementation\n******************************************************************' complianceLevel='21.0' resource='XMI'"
 * @generated
 */
@ProviderType
@EPackage(uri = DAConfigPackage.eNS_URI, fingerprint = "fp1:7039d23e1779fb50f570e6f6c50c67e7c48657bebdb260c7ab9951b83e77464c", genModel = "/model/configuration.genmodel", genModelSourceLocations = {"model/configuration.genmodel","org.eclipse.fennec.data.atlas.configuration.model/model/configuration.genmodel"}, ecore = "/model/configuration.ecore", ecoreSourceLocations = "/model/configuration.ecore")
public interface DAConfigPackage extends org.eclipse.emf.ecore.EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "configuration";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "https://eclipse.org/fennec/data/atlas/configuration/1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "configuration";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DAConfigPackage eINSTANCE = org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl <em>Data Atlas Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataAtlasConfiguration()
	 * @generated
	 */
	int DATA_ATLAS_CONFIGURATION = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ATLAS_CONFIGURATION__NAME = 0;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ATLAS_CONFIGURATION__DESCRIPTION = 1;

	/**
	 * The feature id for the '<em><b>Data Sources</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ATLAS_CONFIGURATION__DATA_SOURCES = 2;

	/**
	 * The feature id for the '<em><b>Data Inputs</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ATLAS_CONFIGURATION__DATA_INPUTS = 3;

	/**
	 * The feature id for the '<em><b>Data Sets</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ATLAS_CONFIGURATION__DATA_SETS = 4;

	/**
	 * The feature id for the '<em><b>Services</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ATLAS_CONFIGURATION__SERVICES = 5;

	/**
	 * The feature id for the '<em><b>Exports</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ATLAS_CONFIGURATION__EXPORTS = 6;

	/**
	 * The feature id for the '<em><b>Transformations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ATLAS_CONFIGURATION__TRANSFORMATIONS = 7;

	/**
	 * The number of structural features of the '<em>Data Atlas Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ATLAS_CONFIGURATION_FEATURE_COUNT = 8;

	/**
	 * The number of operations of the '<em>Data Atlas Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_ATLAS_CONFIGURATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataProviderImpl <em>Data Provider</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataProviderImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataProvider()
	 * @generated
	 */
	int DATA_PROVIDER = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_PROVIDER__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_PROVIDER__NAME = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_PROVIDER__DESCRIPTION = 2;

	/**
	 * The feature id for the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_PROVIDER__DATA_INPUT = 3;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_PROVIDER__TRANSFORMATION = 4;

	/**
	 * The feature id for the '<em><b>Distribution Export</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_PROVIDER__DISTRIBUTION_EXPORT = 5;

	/**
	 * The number of structural features of the '<em>Data Provider</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_PROVIDER_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Data Provider</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_PROVIDER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataInputImpl <em>Data Input</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataInputImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataInput()
	 * @generated
	 */
	int DATA_INPUT = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INPUT__ID = 0;

	/**
	 * The feature id for the '<em><b>Supported EClasses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INPUT__SUPPORTED_ECLASSES = 1;

	/**
	 * The number of structural features of the '<em>Data Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INPUT_FEATURE_COUNT = 2;

	/**
	 * The operation id for the '<em>Stream Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INPUT___STREAM_DATA = 0;

	/**
	 * The number of operations of the '<em>Data Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_INPUT_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.BridgeRepositoryImpl <em>Bridge Repository</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.BridgeRepositoryImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getBridgeRepository()
	 * @generated
	 */
	int BRIDGE_REPOSITORY = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRIDGE_REPOSITORY__ID = DATA_INPUT__ID;

	/**
	 * The feature id for the '<em><b>Supported EClasses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRIDGE_REPOSITORY__SUPPORTED_ECLASSES = DATA_INPUT__SUPPORTED_ECLASSES;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRIDGE_REPOSITORY__SOURCE = DATA_INPUT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Data Trafo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRIDGE_REPOSITORY__DATA_TRAFO = DATA_INPUT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Query Trafo</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRIDGE_REPOSITORY__QUERY_TRAFO = DATA_INPUT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Filter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRIDGE_REPOSITORY__FILTER = DATA_INPUT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Bridge Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRIDGE_REPOSITORY_FEATURE_COUNT = DATA_INPUT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Stream Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRIDGE_REPOSITORY___STREAM_DATA = DATA_INPUT___STREAM_DATA;

	/**
	 * The number of operations of the '<em>Bridge Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BRIDGE_REPOSITORY_OPERATION_COUNT = DATA_INPUT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataSetImpl <em>Data Set</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataSetImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataSet()
	 * @generated
	 */
	int DATA_SET = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__ID = DATA_PROVIDER__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__NAME = DATA_PROVIDER__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__DESCRIPTION = DATA_PROVIDER__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__DATA_INPUT = DATA_PROVIDER__DATA_INPUT;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__TRANSFORMATION = DATA_PROVIDER__TRANSFORMATION;

	/**
	 * The feature id for the '<em><b>Distribution Export</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__DISTRIBUTION_EXPORT = DATA_PROVIDER__DISTRIBUTION_EXPORT;

	/**
	 * The feature id for the '<em><b>Input Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__INPUT_TYPE = DATA_PROVIDER_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Output Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__OUTPUT_TYPE = DATA_PROVIDER_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Query</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__QUERY = DATA_PROVIDER_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Child Data Set</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__CHILD_DATA_SET = DATA_PROVIDER_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Parent Data Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET__PARENT_DATA_SET = DATA_PROVIDER_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Data Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET_FEATURE_COUNT = DATA_PROVIDER_FEATURE_COUNT + 5;

	/**
	 * The number of operations of the '<em>Data Set</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SET_OPERATION_COUNT = DATA_PROVIDER_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataServiceImpl <em>Data Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataServiceImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataService()
	 * @generated
	 */
	int DATA_SERVICE = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE__ID = DATA_PROVIDER__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE__NAME = DATA_PROVIDER__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE__DESCRIPTION = DATA_PROVIDER__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE__DATA_INPUT = DATA_PROVIDER__DATA_INPUT;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE__TRANSFORMATION = DATA_PROVIDER__TRANSFORMATION;

	/**
	 * The feature id for the '<em><b>Distribution Export</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE__DISTRIBUTION_EXPORT = DATA_PROVIDER__DISTRIBUTION_EXPORT;

	/**
	 * The feature id for the '<em><b>Url Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE__URL_CONTEXT = DATA_PROVIDER_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE_FEATURE_COUNT = DATA_PROVIDER_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Distributions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE___GET_DISTRIBUTIONS = DATA_PROVIDER_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE_OPERATION_COUNT = DATA_PROVIDER_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceImpl <em>Rest Data Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getRestDataService()
	 * @generated
	 */
	int REST_DATA_SERVICE = 6;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__ID = DATA_SERVICE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__NAME = DATA_SERVICE__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__DESCRIPTION = DATA_SERVICE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__DATA_INPUT = DATA_SERVICE__DATA_INPUT;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__TRANSFORMATION = DATA_SERVICE__TRANSFORMATION;

	/**
	 * The feature id for the '<em><b>Distribution Export</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__DISTRIBUTION_EXPORT = DATA_SERVICE__DISTRIBUTION_EXPORT;

	/**
	 * The feature id for the '<em><b>Url Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__URL_CONTEXT = DATA_SERVICE__URL_CONTEXT;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__CONFIGURATION = DATA_SERVICE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Open API</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__OPEN_API = DATA_SERVICE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Pagination Offset Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME = DATA_SERVICE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Pagination Size Parameter Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME = DATA_SERVICE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Rest Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE_FEATURE_COUNT = DATA_SERVICE_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get Distributions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE___GET_DISTRIBUTIONS = DATA_SERVICE___GET_DISTRIBUTIONS;

	/**
	 * The number of operations of the '<em>Rest Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE_OPERATION_COUNT = DATA_SERVICE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataServiceConfigurationImpl <em>Data Service Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataServiceConfigurationImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataServiceConfiguration()
	 * @generated
	 */
	int DATA_SERVICE_CONFIGURATION = 7;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE_CONFIGURATION__ID = 0;

	/**
	 * The feature id for the '<em><b>Data Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE_CONFIGURATION__DATA_SET = 1;

	/**
	 * The number of structural features of the '<em>Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE_CONFIGURATION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_SERVICE_CONFIGURATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceConfigurationImpl <em>Rest Data Service Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceConfigurationImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getRestDataServiceConfiguration()
	 * @generated
	 */
	int REST_DATA_SERVICE_CONFIGURATION = 8;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE_CONFIGURATION__ID = DATA_SERVICE_CONFIGURATION__ID;

	/**
	 * The feature id for the '<em><b>Data Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE_CONFIGURATION__DATA_SET = DATA_SERVICE_CONFIGURATION__DATA_SET;

	/**
	 * The feature id for the '<em><b>Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE_CONFIGURATION__PATH = DATA_SERVICE_CONFIGURATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Batch Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE = DATA_SERVICE_CONFIGURATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Batch Size Limit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT = DATA_SERVICE_CONFIGURATION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Rest Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE_CONFIGURATION_FEATURE_COUNT = DATA_SERVICE_CONFIGURATION_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Rest Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REST_DATA_SERVICE_CONFIGURATION_OPERATION_COUNT = DATA_SERVICE_CONFIGURATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.XMLADataServiceImpl <em>XMLA Data Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.XMLADataServiceImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getXMLADataService()
	 * @generated
	 */
	int XMLA_DATA_SERVICE = 9;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE__ID = DATA_SERVICE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE__NAME = DATA_SERVICE__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE__DESCRIPTION = DATA_SERVICE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE__DATA_INPUT = DATA_SERVICE__DATA_INPUT;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE__TRANSFORMATION = DATA_SERVICE__TRANSFORMATION;

	/**
	 * The feature id for the '<em><b>Distribution Export</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE__DISTRIBUTION_EXPORT = DATA_SERVICE__DISTRIBUTION_EXPORT;

	/**
	 * The feature id for the '<em><b>Url Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE__URL_CONTEXT = DATA_SERVICE__URL_CONTEXT;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE__CONFIGURATION = DATA_SERVICE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>XMLA Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE_FEATURE_COUNT = DATA_SERVICE_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Distributions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE___GET_DISTRIBUTIONS = DATA_SERVICE___GET_DISTRIBUTIONS;

	/**
	 * The number of operations of the '<em>XMLA Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE_OPERATION_COUNT = DATA_SERVICE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.XMLADataServiceConfigurationImpl <em>XMLA Data Service Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.XMLADataServiceConfigurationImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getXMLADataServiceConfiguration()
	 * @generated
	 */
	int XMLA_DATA_SERVICE_CONFIGURATION = 10;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE_CONFIGURATION__ID = DATA_SERVICE_CONFIGURATION__ID;

	/**
	 * The feature id for the '<em><b>Data Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE_CONFIGURATION__DATA_SET = DATA_SERVICE_CONFIGURATION__DATA_SET;

	/**
	 * The feature id for the '<em><b>Mapping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE_CONFIGURATION__MAPPING = DATA_SERVICE_CONFIGURATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>XMLA Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE_CONFIGURATION_FEATURE_COUNT = DATA_SERVICE_CONFIGURATION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>XMLA Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int XMLA_DATA_SERVICE_CONFIGURATION_OPERATION_COUNT = DATA_SERVICE_CONFIGURATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.GraphQLDataServiceImpl <em>Graph QL Data Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.GraphQLDataServiceImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getGraphQLDataService()
	 * @generated
	 */
	int GRAPH_QL_DATA_SERVICE = 11;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE__ID = DATA_SERVICE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE__NAME = DATA_SERVICE__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE__DESCRIPTION = DATA_SERVICE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE__DATA_INPUT = DATA_SERVICE__DATA_INPUT;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE__TRANSFORMATION = DATA_SERVICE__TRANSFORMATION;

	/**
	 * The feature id for the '<em><b>Distribution Export</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE__DISTRIBUTION_EXPORT = DATA_SERVICE__DISTRIBUTION_EXPORT;

	/**
	 * The feature id for the '<em><b>Url Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE__URL_CONTEXT = DATA_SERVICE__URL_CONTEXT;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE__CONFIGURATION = DATA_SERVICE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Graph QL Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE_FEATURE_COUNT = DATA_SERVICE_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Distributions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE___GET_DISTRIBUTIONS = DATA_SERVICE___GET_DISTRIBUTIONS;

	/**
	 * The number of operations of the '<em>Graph QL Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE_OPERATION_COUNT = DATA_SERVICE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.QGisDataServiceImpl <em>QGis Data Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.QGisDataServiceImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getQGisDataService()
	 * @generated
	 */
	int QGIS_DATA_SERVICE = 12;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE__ID = DATA_SERVICE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE__NAME = DATA_SERVICE__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE__DESCRIPTION = DATA_SERVICE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE__DATA_INPUT = DATA_SERVICE__DATA_INPUT;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE__TRANSFORMATION = DATA_SERVICE__TRANSFORMATION;

	/**
	 * The feature id for the '<em><b>Distribution Export</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE__DISTRIBUTION_EXPORT = DATA_SERVICE__DISTRIBUTION_EXPORT;

	/**
	 * The feature id for the '<em><b>Url Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE__URL_CONTEXT = DATA_SERVICE__URL_CONTEXT;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE__CONFIGURATION = DATA_SERVICE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>QGis Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE_FEATURE_COUNT = DATA_SERVICE_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Distributions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE___GET_DISTRIBUTIONS = DATA_SERVICE___GET_DISTRIBUTIONS;

	/**
	 * The number of operations of the '<em>QGis Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE_OPERATION_COUNT = DATA_SERVICE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.QGisDataServiceConfigurationImpl <em>QGis Data Service Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.QGisDataServiceConfigurationImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getQGisDataServiceConfiguration()
	 * @generated
	 */
	int QGIS_DATA_SERVICE_CONFIGURATION = 13;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE_CONFIGURATION__ID = DATA_SERVICE_CONFIGURATION__ID;

	/**
	 * The feature id for the '<em><b>Data Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE_CONFIGURATION__DATA_SET = DATA_SERVICE_CONFIGURATION__DATA_SET;

	/**
	 * The feature id for the '<em><b>Layer</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE_CONFIGURATION__LAYER = DATA_SERVICE_CONFIGURATION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>QGis Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE_CONFIGURATION_FEATURE_COUNT = DATA_SERVICE_CONFIGURATION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>QGis Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QGIS_DATA_SERVICE_CONFIGURATION_OPERATION_COUNT = DATA_SERVICE_CONFIGURATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.GraphQLDataServiceConfigurationImpl <em>Graph QL Data Service Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.GraphQLDataServiceConfigurationImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getGraphQLDataServiceConfiguration()
	 * @generated
	 */
	int GRAPH_QL_DATA_SERVICE_CONFIGURATION = 14;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE_CONFIGURATION__ID = DATA_SERVICE_CONFIGURATION__ID;

	/**
	 * The feature id for the '<em><b>Data Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE_CONFIGURATION__DATA_SET = DATA_SERVICE_CONFIGURATION__DATA_SET;

	/**
	 * The number of structural features of the '<em>Graph QL Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE_CONFIGURATION_FEATURE_COUNT = DATA_SERVICE_CONFIGURATION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Graph QL Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_QL_DATA_SERVICE_CONFIGURATION_OPERATION_COUNT = DATA_SERVICE_CONFIGURATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceImpl <em>Ogc Features Data Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getOgcFeaturesDataService()
	 * @generated
	 */
	int OGC_FEATURES_DATA_SERVICE = 15;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_FEATURES_DATA_SERVICE__ID = DATA_SERVICE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_FEATURES_DATA_SERVICE__NAME = DATA_SERVICE__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_FEATURES_DATA_SERVICE__DESCRIPTION = DATA_SERVICE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_FEATURES_DATA_SERVICE__DATA_INPUT = DATA_SERVICE__DATA_INPUT;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_FEATURES_DATA_SERVICE__TRANSFORMATION = DATA_SERVICE__TRANSFORMATION;

	/**
	 * The feature id for the '<em><b>Distribution Export</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_FEATURES_DATA_SERVICE__DISTRIBUTION_EXPORT = DATA_SERVICE__DISTRIBUTION_EXPORT;

	/**
	 * The feature id for the '<em><b>Url Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_FEATURES_DATA_SERVICE__URL_CONTEXT = DATA_SERVICE__URL_CONTEXT;

	/**
	 * The number of structural features of the '<em>Ogc Features Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_FEATURES_DATA_SERVICE_FEATURE_COUNT = DATA_SERVICE_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Distributions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_FEATURES_DATA_SERVICE___GET_DISTRIBUTIONS = DATA_SERVICE___GET_DISTRIBUTIONS;

	/**
	 * The number of operations of the '<em>Ogc Features Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_FEATURES_DATA_SERVICE_OPERATION_COUNT = DATA_SERVICE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcSensorThingsDataServiceImpl <em>Ogc Sensor Things Data Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.OgcSensorThingsDataServiceImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getOgcSensorThingsDataService()
	 * @generated
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE = 16;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE__ID = DATA_SERVICE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE__NAME = DATA_SERVICE__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE__DESCRIPTION = DATA_SERVICE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE__DATA_INPUT = DATA_SERVICE__DATA_INPUT;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE__TRANSFORMATION = DATA_SERVICE__TRANSFORMATION;

	/**
	 * The feature id for the '<em><b>Distribution Export</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE__DISTRIBUTION_EXPORT = DATA_SERVICE__DISTRIBUTION_EXPORT;

	/**
	 * The feature id for the '<em><b>Url Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE__URL_CONTEXT = DATA_SERVICE__URL_CONTEXT;

	/**
	 * The number of structural features of the '<em>Ogc Sensor Things Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE_FEATURE_COUNT = DATA_SERVICE_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Distributions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE___GET_DISTRIBUTIONS = DATA_SERVICE___GET_DISTRIBUTIONS;

	/**
	 * The number of operations of the '<em>Ogc Sensor Things Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OGC_SENSOR_THINGS_DATA_SERVICE_OPERATION_COUNT = DATA_SERVICE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.MongoRepositoryImpl <em>Mongo Repository</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.MongoRepositoryImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getMongoRepository()
	 * @generated
	 */
	int MONGO_REPOSITORY = 17;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONGO_REPOSITORY__ID = DATA_INPUT__ID;

	/**
	 * The feature id for the '<em><b>Supported EClasses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONGO_REPOSITORY__SUPPORTED_ECLASSES = DATA_INPUT__SUPPORTED_ECLASSES;

	/**
	 * The number of structural features of the '<em>Mongo Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONGO_REPOSITORY_FEATURE_COUNT = DATA_INPUT_FEATURE_COUNT + 0;

	/**
	 * The operation id for the '<em>Stream Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONGO_REPOSITORY___STREAM_DATA = DATA_INPUT___STREAM_DATA;

	/**
	 * The number of operations of the '<em>Mongo Repository</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MONGO_REPOSITORY_OPERATION_COUNT = DATA_INPUT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.FileDataInputImpl <em>File Data Input</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.FileDataInputImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getFileDataInput()
	 * @generated
	 */
	int FILE_DATA_INPUT = 18;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_DATA_INPUT__ID = DATA_INPUT__ID;

	/**
	 * The feature id for the '<em><b>Supported EClasses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_DATA_INPUT__SUPPORTED_ECLASSES = DATA_INPUT__SUPPORTED_ECLASSES;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_DATA_INPUT__URI = DATA_INPUT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>File Data Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_DATA_INPUT_FEATURE_COUNT = DATA_INPUT_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Stream Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_DATA_INPUT___STREAM_DATA = DATA_INPUT___STREAM_DATA;

	/**
	 * The number of operations of the '<em>File Data Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_DATA_INPUT_OPERATION_COUNT = DATA_INPUT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.JPADataInputImpl <em>JPA Data Input</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.JPADataInputImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getJPADataInput()
	 * @generated
	 */
	int JPA_DATA_INPUT = 19;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JPA_DATA_INPUT__ID = DATA_INPUT__ID;

	/**
	 * The feature id for the '<em><b>Supported EClasses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JPA_DATA_INPUT__SUPPORTED_ECLASSES = DATA_INPUT__SUPPORTED_ECLASSES;

	/**
	 * The feature id for the '<em><b>Persistence Config</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JPA_DATA_INPUT__PERSISTENCE_CONFIG = DATA_INPUT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Data Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JPA_DATA_INPUT__DATA_SOURCE = DATA_INPUT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>JPA Data Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JPA_DATA_INPUT_FEATURE_COUNT = DATA_INPUT_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Stream Data</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JPA_DATA_INPUT___STREAM_DATA = DATA_INPUT___STREAM_DATA;

	/**
	 * The number of operations of the '<em>JPA Data Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JPA_DATA_INPUT_OPERATION_COUNT = DATA_INPUT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.TransformationImpl <em>Transformation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.TransformationImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getTransformation()
	 * @generated
	 */
	int TRANSFORMATION = 26;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION__NAME = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION__DESCRIPTION = 2;

	/**
	 * The number of structural features of the '<em>Transformation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Transformation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataTransformationImpl <em>Data Transformation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataTransformationImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataTransformation()
	 * @generated
	 */
	int DATA_TRANSFORMATION = 20;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TRANSFORMATION__ID = TRANSFORMATION__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TRANSFORMATION__NAME = TRANSFORMATION__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TRANSFORMATION__DESCRIPTION = TRANSFORMATION__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Supported EClasses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TRANSFORMATION__SUPPORTED_ECLASSES = TRANSFORMATION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Result EClasses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TRANSFORMATION__RESULT_ECLASSES = TRANSFORMATION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>QVT Placeholder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TRANSFORMATION__QVT_PLACEHOLDER = TRANSFORMATION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Data Transformation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TRANSFORMATION_FEATURE_COUNT = TRANSFORMATION_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Data Transformation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DATA_TRANSFORMATION_OPERATION_COUNT = TRANSFORMATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.QueryTransformationImpl <em>Query Transformation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.QueryTransformationImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getQueryTransformation()
	 * @generated
	 */
	int QUERY_TRANSFORMATION = 21;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TRANSFORMATION__ID = TRANSFORMATION__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TRANSFORMATION__NAME = TRANSFORMATION__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TRANSFORMATION__DESCRIPTION = TRANSFORMATION__DESCRIPTION;

	/**
	 * The number of structural features of the '<em>Query Transformation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TRANSFORMATION_FEATURE_COUNT = TRANSFORMATION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Query Transformation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_TRANSFORMATION_OPERATION_COUNT = TRANSFORMATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.ODataDataServiceImpl <em>OData Data Service</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.ODataDataServiceImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getODataDataService()
	 * @generated
	 */
	int ODATA_DATA_SERVICE = 22;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE__ID = DATA_SERVICE__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE__NAME = DATA_SERVICE__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE__DESCRIPTION = DATA_SERVICE__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Data Input</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE__DATA_INPUT = DATA_SERVICE__DATA_INPUT;

	/**
	 * The feature id for the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE__TRANSFORMATION = DATA_SERVICE__TRANSFORMATION;

	/**
	 * The feature id for the '<em><b>Distribution Export</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE__DISTRIBUTION_EXPORT = DATA_SERVICE__DISTRIBUTION_EXPORT;

	/**
	 * The feature id for the '<em><b>Url Context</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE__URL_CONTEXT = DATA_SERVICE__URL_CONTEXT;

	/**
	 * The feature id for the '<em><b>Configuration</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE__CONFIGURATION = DATA_SERVICE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>OData Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE_FEATURE_COUNT = DATA_SERVICE_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Distributions</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE___GET_DISTRIBUTIONS = DATA_SERVICE___GET_DISTRIBUTIONS;

	/**
	 * The number of operations of the '<em>OData Data Service</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE_OPERATION_COUNT = DATA_SERVICE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.ODataDataServiceConfigurationImpl <em>OData Data Service Configuration</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.ODataDataServiceConfigurationImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getODataDataServiceConfiguration()
	 * @generated
	 */
	int ODATA_DATA_SERVICE_CONFIGURATION = 23;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE_CONFIGURATION__ID = DATA_SERVICE_CONFIGURATION__ID;

	/**
	 * The feature id for the '<em><b>Data Set</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE_CONFIGURATION__DATA_SET = DATA_SERVICE_CONFIGURATION__DATA_SET;

	/**
	 * The number of structural features of the '<em>OData Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE_CONFIGURATION_FEATURE_COUNT = DATA_SERVICE_CONFIGURATION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>OData Data Service Configuration</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ODATA_DATA_SERVICE_CONFIGURATION_OPERATION_COUNT = DATA_SERVICE_CONFIGURATION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DistributionExportImpl <em>Distribution Export</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DistributionExportImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDistributionExport()
	 * @generated
	 */
	int DISTRIBUTION_EXPORT = 24;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_EXPORT__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_EXPORT__NAME = 1;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_EXPORT__DESCRIPTION = 2;

	/**
	 * The number of structural features of the '<em>Distribution Export</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_EXPORT_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Distribution Export</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISTRIBUTION_EXPORT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.CSVDistributionExportImpl <em>CSV Distribution Export</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.CSVDistributionExportImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getCSVDistributionExport()
	 * @generated
	 */
	int CSV_DISTRIBUTION_EXPORT = 25;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CSV_DISTRIBUTION_EXPORT__ID = DISTRIBUTION_EXPORT__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CSV_DISTRIBUTION_EXPORT__NAME = DISTRIBUTION_EXPORT__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CSV_DISTRIBUTION_EXPORT__DESCRIPTION = DISTRIBUTION_EXPORT__DESCRIPTION;

	/**
	 * The feature id for the '<em><b>Separator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CSV_DISTRIBUTION_EXPORT__SEPARATOR = DISTRIBUTION_EXPORT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Compressed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CSV_DISTRIBUTION_EXPORT__COMPRESSED = DISTRIBUTION_EXPORT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Include Type Header</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CSV_DISTRIBUTION_EXPORT__INCLUDE_TYPE_HEADER = DISTRIBUTION_EXPORT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>CSV Distribution Export</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CSV_DISTRIBUTION_EXPORT_FEATURE_COUNT = DISTRIBUTION_EXPORT_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>CSV Distribution Export</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CSV_DISTRIBUTION_EXPORT_OPERATION_COUNT = DISTRIBUTION_EXPORT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.JdbcDataSourceImpl <em>Jdbc Data Source</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.JdbcDataSourceImpl
	 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getJdbcDataSource()
	 * @generated
	 */
	int JDBC_DATA_SOURCE = 27;

	/**
	 * The feature id for the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JDBC_DATA_SOURCE__FILTER = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JDBC_DATA_SOURCE__ID = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JDBC_DATA_SOURCE__NAME = 2;

	/**
	 * The number of structural features of the '<em>Jdbc Data Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JDBC_DATA_SOURCE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Jdbc Data Source</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int JDBC_DATA_SOURCE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration <em>Data Atlas Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Atlas Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration
	 * @generated
	 */
	EClass getDataAtlasConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getName()
	 * @see #getDataAtlasConfiguration()
	 * @generated
	 */
	EAttribute getDataAtlasConfiguration_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDescription()
	 * @see #getDataAtlasConfiguration()
	 * @generated
	 */
	EAttribute getDataAtlasConfiguration_Description();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDataSources <em>Data Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Data Sources</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDataSources()
	 * @see #getDataAtlasConfiguration()
	 * @generated
	 */
	EReference getDataAtlasConfiguration_DataSources();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDataInputs <em>Data Inputs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Data Inputs</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDataInputs()
	 * @see #getDataAtlasConfiguration()
	 * @generated
	 */
	EReference getDataAtlasConfiguration_DataInputs();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDataSets <em>Data Sets</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Data Sets</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getDataSets()
	 * @see #getDataAtlasConfiguration()
	 * @generated
	 */
	EReference getDataAtlasConfiguration_DataSets();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getServices <em>Services</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Services</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getServices()
	 * @see #getDataAtlasConfiguration()
	 * @generated
	 */
	EReference getDataAtlasConfiguration_Services();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getExports <em>Exports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Exports</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getExports()
	 * @see #getDataAtlasConfiguration()
	 * @generated
	 */
	EReference getDataAtlasConfiguration_Exports();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getTransformations <em>Transformations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Transformations</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration#getTransformations()
	 * @see #getDataAtlasConfiguration()
	 * @generated
	 */
	EReference getDataAtlasConfiguration_Transformations();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider <em>Data Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Provider</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataProvider
	 * @generated
	 */
	EClass getDataProvider();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataProvider#getId()
	 * @see #getDataProvider()
	 * @generated
	 */
	EAttribute getDataProvider_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataProvider#getName()
	 * @see #getDataProvider()
	 * @generated
	 */
	EAttribute getDataProvider_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataProvider#getDescription()
	 * @see #getDataProvider()
	 * @generated
	 */
	EAttribute getDataProvider_Description();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getDataInput <em>Data Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Data Input</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataProvider#getDataInput()
	 * @see #getDataProvider()
	 * @generated
	 */
	EReference getDataProvider_DataInput();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getTransformation <em>Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Transformation</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataProvider#getTransformation()
	 * @see #getDataProvider()
	 * @generated
	 */
	EReference getDataProvider_Transformation();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataProvider#getDistributionExport <em>Distribution Export</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Distribution Export</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataProvider#getDistributionExport()
	 * @see #getDataProvider()
	 * @generated
	 */
	EReference getDataProvider_DistributionExport();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.DataInput <em>Data Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Input</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataInput
	 * @generated
	 */
	EClass getDataInput();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DataInput#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataInput#getId()
	 * @see #getDataInput()
	 * @generated
	 */
	EAttribute getDataInput_Id();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataInput#getSupportedEClasses <em>Supported EClasses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Supported EClasses</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataInput#getSupportedEClasses()
	 * @see #getDataInput()
	 * @generated
	 */
	EReference getDataInput_SupportedEClasses();

	/**
	 * Returns the meta object for the '{@link org.eclipse.fennec.data.atlas.configuration.DataInput#streamData() <em>Stream Data</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Stream Data</em>' operation.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataInput#streamData()
	 * @generated
	 */
	EOperation getDataInput__StreamData();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository <em>Bridge Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bridge Repository</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.BridgeRepository
	 * @generated
	 */
	EClass getBridgeRepository();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getSource()
	 * @see #getBridgeRepository()
	 * @generated
	 */
	EReference getBridgeRepository_Source();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getDataTrafo <em>Data Trafo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Data Trafo</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getDataTrafo()
	 * @see #getBridgeRepository()
	 * @generated
	 */
	EReference getBridgeRepository_DataTrafo();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getQueryTrafo <em>Query Trafo</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Query Trafo</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getQueryTrafo()
	 * @see #getBridgeRepository()
	 * @generated
	 */
	EReference getBridgeRepository_QueryTrafo();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Filter</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.BridgeRepository#getFilter()
	 * @see #getBridgeRepository()
	 * @generated
	 */
	EReference getBridgeRepository_Filter();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.DataSet <em>Data Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Set</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataSet
	 * @generated
	 */
	EClass getDataSet();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getInputType <em>Input Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Input Type</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataSet#getInputType()
	 * @see #getDataSet()
	 * @generated
	 */
	EReference getDataSet_InputType();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getOutputType <em>Output Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Output Type</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataSet#getOutputType()
	 * @see #getDataSet()
	 * @generated
	 */
	EReference getDataSet_OutputType();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getQuery <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Query</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataSet#getQuery()
	 * @see #getDataSet()
	 * @generated
	 */
	EReference getDataSet_Query();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getChildDataSet <em>Child Data Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Child Data Set</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataSet#getChildDataSet()
	 * @see #getDataSet()
	 * @generated
	 */
	EReference getDataSet_ChildDataSet();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.DataSet#getParentDataSet <em>Parent Data Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parent Data Set</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataSet#getParentDataSet()
	 * @see #getDataSet()
	 * @generated
	 */
	EReference getDataSet_ParentDataSet();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.DataService <em>Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Service</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataService
	 * @generated
	 */
	EClass getDataService();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DataService#getUrlContext <em>Url Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Url Context</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataService#getUrlContext()
	 * @see #getDataService()
	 * @generated
	 */
	EAttribute getDataService_UrlContext();

	/**
	 * Returns the meta object for the '{@link org.eclipse.fennec.data.atlas.configuration.DataService#getDistributions() <em>Get Distributions</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Distributions</em>' operation.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataService#getDistributions()
	 * @generated
	 */
	EOperation getDataService__GetDistributions();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.RestDataService <em>Rest Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rest Data Service</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataService
	 * @generated
	 */
	EClass getRestDataService();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#getConfiguration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataService#getConfiguration()
	 * @see #getRestDataService()
	 * @generated
	 */
	EReference getRestDataService_Configuration();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#isOpenAPI <em>Open API</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Open API</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataService#isOpenAPI()
	 * @see #getRestDataService()
	 * @generated
	 */
	EAttribute getRestDataService_OpenAPI();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#getPaginationOffsetParameterName <em>Pagination Offset Parameter Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pagination Offset Parameter Name</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataService#getPaginationOffsetParameterName()
	 * @see #getRestDataService()
	 * @generated
	 */
	EAttribute getRestDataService_PaginationOffsetParameterName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.RestDataService#getPaginationSizeParameterName <em>Pagination Size Parameter Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Pagination Size Parameter Name</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataService#getPaginationSizeParameterName()
	 * @see #getRestDataService()
	 * @generated
	 */
	EAttribute getRestDataService_PaginationSizeParameterName();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.DataServiceConfiguration <em>Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Service Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataServiceConfiguration
	 * @generated
	 */
	EClass getDataServiceConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DataServiceConfiguration#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataServiceConfiguration#getId()
	 * @see #getDataServiceConfiguration()
	 * @generated
	 */
	EAttribute getDataServiceConfiguration_Id();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.DataServiceConfiguration#getDataSet <em>Data Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Data Set</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataServiceConfiguration#getDataSet()
	 * @see #getDataServiceConfiguration()
	 * @generated
	 */
	EReference getDataServiceConfiguration_DataSet();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration <em>Rest Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Rest Data Service Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration
	 * @generated
	 */
	EClass getRestDataServiceConfiguration();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getPath <em>Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Path</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getPath()
	 * @see #getRestDataServiceConfiguration()
	 * @generated
	 */
	EAttribute getRestDataServiceConfiguration_Path();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getBatchSize <em>Batch Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Batch Size</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getBatchSize()
	 * @see #getRestDataServiceConfiguration()
	 * @generated
	 */
	EAttribute getRestDataServiceConfiguration_BatchSize();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getBatchSizeLimit <em>Batch Size Limit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Batch Size Limit</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration#getBatchSizeLimit()
	 * @see #getRestDataServiceConfiguration()
	 * @generated
	 */
	EAttribute getRestDataServiceConfiguration_BatchSizeLimit();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.XMLADataService <em>XMLA Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>XMLA Data Service</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.XMLADataService
	 * @generated
	 */
	EClass getXMLADataService();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.XMLADataService#getConfiguration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.XMLADataService#getConfiguration()
	 * @see #getXMLADataService()
	 * @generated
	 */
	EReference getXMLADataService_Configuration();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration <em>XMLA Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>XMLA Data Service Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration
	 * @generated
	 */
	EClass getXMLADataServiceConfiguration();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration#getMapping <em>Mapping</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Mapping</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.XMLADataServiceConfiguration#getMapping()
	 * @see #getXMLADataServiceConfiguration()
	 * @generated
	 */
	EReference getXMLADataServiceConfiguration_Mapping();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.GraphQLDataService <em>Graph QL Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Graph QL Data Service</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.GraphQLDataService
	 * @generated
	 */
	EClass getGraphQLDataService();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.GraphQLDataService#getConfiguration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.GraphQLDataService#getConfiguration()
	 * @see #getGraphQLDataService()
	 * @generated
	 */
	EReference getGraphQLDataService_Configuration();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.QGisDataService <em>QGis Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>QGis Data Service</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.QGisDataService
	 * @generated
	 */
	EClass getQGisDataService();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.QGisDataService#getConfiguration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.QGisDataService#getConfiguration()
	 * @see #getQGisDataService()
	 * @generated
	 */
	EReference getQGisDataService_Configuration();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.QGisDataServiceConfiguration <em>QGis Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>QGis Data Service Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.QGisDataServiceConfiguration
	 * @generated
	 */
	EClass getQGisDataServiceConfiguration();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.QGisDataServiceConfiguration#getLayer <em>Layer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Layer</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.QGisDataServiceConfiguration#getLayer()
	 * @see #getQGisDataServiceConfiguration()
	 * @generated
	 */
	EReference getQGisDataServiceConfiguration_Layer();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.GraphQLDataServiceConfiguration <em>Graph QL Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Graph QL Data Service Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.GraphQLDataServiceConfiguration
	 * @generated
	 */
	EClass getGraphQLDataServiceConfiguration();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService <em>Ogc Features Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ogc Features Data Service</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService
	 * @generated
	 */
	EClass getOgcFeaturesDataService();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.OgcSensorThingsDataService <em>Ogc Sensor Things Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ogc Sensor Things Data Service</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.OgcSensorThingsDataService
	 * @generated
	 */
	EClass getOgcSensorThingsDataService();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.MongoRepository <em>Mongo Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Mongo Repository</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.MongoRepository
	 * @generated
	 */
	EClass getMongoRepository();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.FileDataInput <em>File Data Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>File Data Input</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.FileDataInput
	 * @generated
	 */
	EClass getFileDataInput();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.FileDataInput#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.FileDataInput#getUri()
	 * @see #getFileDataInput()
	 * @generated
	 */
	EAttribute getFileDataInput_Uri();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.JPADataInput <em>JPA Data Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>JPA Data Input</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.JPADataInput
	 * @generated
	 */
	EClass getJPADataInput();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.JPADataInput#getPersistenceConfig <em>Persistence Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Persistence Config</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.JPADataInput#getPersistenceConfig()
	 * @see #getJPADataInput()
	 * @generated
	 */
	EReference getJPADataInput_PersistenceConfig();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.data.atlas.configuration.JPADataInput#getDataSource <em>Data Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Data Source</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.JPADataInput#getDataSource()
	 * @see #getJPADataInput()
	 * @generated
	 */
	EReference getJPADataInput_DataSource();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.DataTransformation <em>Data Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Data Transformation</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataTransformation
	 * @generated
	 */
	EClass getDataTransformation();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataTransformation#getSupportedEClasses <em>Supported EClasses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Supported EClasses</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataTransformation#getSupportedEClasses()
	 * @see #getDataTransformation()
	 * @generated
	 */
	EReference getDataTransformation_SupportedEClasses();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.fennec.data.atlas.configuration.DataTransformation#getResultEClasses <em>Result EClasses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Result EClasses</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataTransformation#getResultEClasses()
	 * @see #getDataTransformation()
	 * @generated
	 */
	EReference getDataTransformation_ResultEClasses();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DataTransformation#getQVTPlaceholder <em>QVT Placeholder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>QVT Placeholder</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DataTransformation#getQVTPlaceholder()
	 * @see #getDataTransformation()
	 * @generated
	 */
	EAttribute getDataTransformation_QVTPlaceholder();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.QueryTransformation <em>Query Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Query Transformation</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.QueryTransformation
	 * @generated
	 */
	EClass getQueryTransformation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.ODataDataService <em>OData Data Service</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>OData Data Service</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.ODataDataService
	 * @generated
	 */
	EClass getODataDataService();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.data.atlas.configuration.ODataDataService#getConfiguration <em>Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.ODataDataService#getConfiguration()
	 * @see #getODataDataService()
	 * @generated
	 */
	EReference getODataDataService_Configuration();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration <em>OData Data Service Configuration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>OData Data Service Configuration</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration
	 * @generated
	 */
	EClass getODataDataServiceConfiguration();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport <em>Distribution Export</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Distribution Export</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DistributionExport
	 * @generated
	 */
	EClass getDistributionExport();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DistributionExport#getId()
	 * @see #getDistributionExport()
	 * @generated
	 */
	EAttribute getDistributionExport_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DistributionExport#getName()
	 * @see #getDistributionExport()
	 * @generated
	 */
	EAttribute getDistributionExport_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.DistributionExport#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.DistributionExport#getDescription()
	 * @see #getDistributionExport()
	 * @generated
	 */
	EAttribute getDistributionExport_Description();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport <em>CSV Distribution Export</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>CSV Distribution Export</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport
	 * @generated
	 */
	EClass getCSVDistributionExport();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#getSeparator <em>Separator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Separator</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#getSeparator()
	 * @see #getCSVDistributionExport()
	 * @generated
	 */
	EAttribute getCSVDistributionExport_Separator();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#isCompressed <em>Compressed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Compressed</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#isCompressed()
	 * @see #getCSVDistributionExport()
	 * @generated
	 */
	EAttribute getCSVDistributionExport_Compressed();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#isIncludeTypeHeader <em>Include Type Header</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Include Type Header</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.CSVDistributionExport#isIncludeTypeHeader()
	 * @see #getCSVDistributionExport()
	 * @generated
	 */
	EAttribute getCSVDistributionExport_IncludeTypeHeader();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.Transformation <em>Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transformation</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.Transformation
	 * @generated
	 */
	EClass getTransformation();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.Transformation#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.Transformation#getId()
	 * @see #getTransformation()
	 * @generated
	 */
	EAttribute getTransformation_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.Transformation#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.Transformation#getName()
	 * @see #getTransformation()
	 * @generated
	 */
	EAttribute getTransformation_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.Transformation#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.Transformation#getDescription()
	 * @see #getTransformation()
	 * @generated
	 */
	EAttribute getTransformation_Description();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.configuration.JdbcDataSource <em>Jdbc Data Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Jdbc Data Source</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.JdbcDataSource
	 * @generated
	 */
	EClass getJdbcDataSource();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.JdbcDataSource#getFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Filter</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.JdbcDataSource#getFilter()
	 * @see #getJdbcDataSource()
	 * @generated
	 */
	EAttribute getJdbcDataSource_Filter();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.JdbcDataSource#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.JdbcDataSource#getId()
	 * @see #getJdbcDataSource()
	 * @generated
	 */
	EAttribute getJdbcDataSource_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.configuration.JdbcDataSource#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.data.atlas.configuration.JdbcDataSource#getName()
	 * @see #getJdbcDataSource()
	 * @generated
	 */
	EAttribute getJdbcDataSource_Name();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DAConfigFactory getDAConfigFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl <em>Data Atlas Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataAtlasConfiguration()
		 * @generated
		 */
		EClass DATA_ATLAS_CONFIGURATION = eINSTANCE.getDataAtlasConfiguration();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_ATLAS_CONFIGURATION__NAME = eINSTANCE.getDataAtlasConfiguration_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_ATLAS_CONFIGURATION__DESCRIPTION = eINSTANCE.getDataAtlasConfiguration_Description();

		/**
		 * The meta object literal for the '<em><b>Data Sources</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_ATLAS_CONFIGURATION__DATA_SOURCES = eINSTANCE.getDataAtlasConfiguration_DataSources();

		/**
		 * The meta object literal for the '<em><b>Data Inputs</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_ATLAS_CONFIGURATION__DATA_INPUTS = eINSTANCE.getDataAtlasConfiguration_DataInputs();

		/**
		 * The meta object literal for the '<em><b>Data Sets</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_ATLAS_CONFIGURATION__DATA_SETS = eINSTANCE.getDataAtlasConfiguration_DataSets();

		/**
		 * The meta object literal for the '<em><b>Services</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_ATLAS_CONFIGURATION__SERVICES = eINSTANCE.getDataAtlasConfiguration_Services();

		/**
		 * The meta object literal for the '<em><b>Exports</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_ATLAS_CONFIGURATION__EXPORTS = eINSTANCE.getDataAtlasConfiguration_Exports();

		/**
		 * The meta object literal for the '<em><b>Transformations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_ATLAS_CONFIGURATION__TRANSFORMATIONS = eINSTANCE.getDataAtlasConfiguration_Transformations();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataProviderImpl <em>Data Provider</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataProviderImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataProvider()
		 * @generated
		 */
		EClass DATA_PROVIDER = eINSTANCE.getDataProvider();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_PROVIDER__ID = eINSTANCE.getDataProvider_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_PROVIDER__NAME = eINSTANCE.getDataProvider_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_PROVIDER__DESCRIPTION = eINSTANCE.getDataProvider_Description();

		/**
		 * The meta object literal for the '<em><b>Data Input</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_PROVIDER__DATA_INPUT = eINSTANCE.getDataProvider_DataInput();

		/**
		 * The meta object literal for the '<em><b>Transformation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_PROVIDER__TRANSFORMATION = eINSTANCE.getDataProvider_Transformation();

		/**
		 * The meta object literal for the '<em><b>Distribution Export</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_PROVIDER__DISTRIBUTION_EXPORT = eINSTANCE.getDataProvider_DistributionExport();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataInputImpl <em>Data Input</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataInputImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataInput()
		 * @generated
		 */
		EClass DATA_INPUT = eINSTANCE.getDataInput();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_INPUT__ID = eINSTANCE.getDataInput_Id();

		/**
		 * The meta object literal for the '<em><b>Supported EClasses</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_INPUT__SUPPORTED_ECLASSES = eINSTANCE.getDataInput_SupportedEClasses();

		/**
		 * The meta object literal for the '<em><b>Stream Data</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DATA_INPUT___STREAM_DATA = eINSTANCE.getDataInput__StreamData();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.BridgeRepositoryImpl <em>Bridge Repository</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.BridgeRepositoryImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getBridgeRepository()
		 * @generated
		 */
		EClass BRIDGE_REPOSITORY = eINSTANCE.getBridgeRepository();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BRIDGE_REPOSITORY__SOURCE = eINSTANCE.getBridgeRepository_Source();

		/**
		 * The meta object literal for the '<em><b>Data Trafo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BRIDGE_REPOSITORY__DATA_TRAFO = eINSTANCE.getBridgeRepository_DataTrafo();

		/**
		 * The meta object literal for the '<em><b>Query Trafo</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BRIDGE_REPOSITORY__QUERY_TRAFO = eINSTANCE.getBridgeRepository_QueryTrafo();

		/**
		 * The meta object literal for the '<em><b>Filter</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BRIDGE_REPOSITORY__FILTER = eINSTANCE.getBridgeRepository_Filter();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataSetImpl <em>Data Set</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataSetImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataSet()
		 * @generated
		 */
		EClass DATA_SET = eINSTANCE.getDataSet();

		/**
		 * The meta object literal for the '<em><b>Input Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SET__INPUT_TYPE = eINSTANCE.getDataSet_InputType();

		/**
		 * The meta object literal for the '<em><b>Output Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SET__OUTPUT_TYPE = eINSTANCE.getDataSet_OutputType();

		/**
		 * The meta object literal for the '<em><b>Query</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SET__QUERY = eINSTANCE.getDataSet_Query();

		/**
		 * The meta object literal for the '<em><b>Child Data Set</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SET__CHILD_DATA_SET = eINSTANCE.getDataSet_ChildDataSet();

		/**
		 * The meta object literal for the '<em><b>Parent Data Set</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SET__PARENT_DATA_SET = eINSTANCE.getDataSet_ParentDataSet();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataServiceImpl <em>Data Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataServiceImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataService()
		 * @generated
		 */
		EClass DATA_SERVICE = eINSTANCE.getDataService();

		/**
		 * The meta object literal for the '<em><b>Url Context</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_SERVICE__URL_CONTEXT = eINSTANCE.getDataService_UrlContext();

		/**
		 * The meta object literal for the '<em><b>Get Distributions</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation DATA_SERVICE___GET_DISTRIBUTIONS = eINSTANCE.getDataService__GetDistributions();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceImpl <em>Rest Data Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getRestDataService()
		 * @generated
		 */
		EClass REST_DATA_SERVICE = eINSTANCE.getRestDataService();

		/**
		 * The meta object literal for the '<em><b>Configuration</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REST_DATA_SERVICE__CONFIGURATION = eINSTANCE.getRestDataService_Configuration();

		/**
		 * The meta object literal for the '<em><b>Open API</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_DATA_SERVICE__OPEN_API = eINSTANCE.getRestDataService_OpenAPI();

		/**
		 * The meta object literal for the '<em><b>Pagination Offset Parameter Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_DATA_SERVICE__PAGINATION_OFFSET_PARAMETER_NAME = eINSTANCE.getRestDataService_PaginationOffsetParameterName();

		/**
		 * The meta object literal for the '<em><b>Pagination Size Parameter Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_DATA_SERVICE__PAGINATION_SIZE_PARAMETER_NAME = eINSTANCE.getRestDataService_PaginationSizeParameterName();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataServiceConfigurationImpl <em>Data Service Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataServiceConfigurationImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataServiceConfiguration()
		 * @generated
		 */
		EClass DATA_SERVICE_CONFIGURATION = eINSTANCE.getDataServiceConfiguration();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_SERVICE_CONFIGURATION__ID = eINSTANCE.getDataServiceConfiguration_Id();

		/**
		 * The meta object literal for the '<em><b>Data Set</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_SERVICE_CONFIGURATION__DATA_SET = eINSTANCE.getDataServiceConfiguration_DataSet();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceConfigurationImpl <em>Rest Data Service Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.RestDataServiceConfigurationImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getRestDataServiceConfiguration()
		 * @generated
		 */
		EClass REST_DATA_SERVICE_CONFIGURATION = eINSTANCE.getRestDataServiceConfiguration();

		/**
		 * The meta object literal for the '<em><b>Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_DATA_SERVICE_CONFIGURATION__PATH = eINSTANCE.getRestDataServiceConfiguration_Path();

		/**
		 * The meta object literal for the '<em><b>Batch Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE = eINSTANCE.getRestDataServiceConfiguration_BatchSize();

		/**
		 * The meta object literal for the '<em><b>Batch Size Limit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REST_DATA_SERVICE_CONFIGURATION__BATCH_SIZE_LIMIT = eINSTANCE.getRestDataServiceConfiguration_BatchSizeLimit();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.XMLADataServiceImpl <em>XMLA Data Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.XMLADataServiceImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getXMLADataService()
		 * @generated
		 */
		EClass XMLA_DATA_SERVICE = eINSTANCE.getXMLADataService();

		/**
		 * The meta object literal for the '<em><b>Configuration</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference XMLA_DATA_SERVICE__CONFIGURATION = eINSTANCE.getXMLADataService_Configuration();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.XMLADataServiceConfigurationImpl <em>XMLA Data Service Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.XMLADataServiceConfigurationImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getXMLADataServiceConfiguration()
		 * @generated
		 */
		EClass XMLA_DATA_SERVICE_CONFIGURATION = eINSTANCE.getXMLADataServiceConfiguration();

		/**
		 * The meta object literal for the '<em><b>Mapping</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference XMLA_DATA_SERVICE_CONFIGURATION__MAPPING = eINSTANCE.getXMLADataServiceConfiguration_Mapping();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.GraphQLDataServiceImpl <em>Graph QL Data Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.GraphQLDataServiceImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getGraphQLDataService()
		 * @generated
		 */
		EClass GRAPH_QL_DATA_SERVICE = eINSTANCE.getGraphQLDataService();

		/**
		 * The meta object literal for the '<em><b>Configuration</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference GRAPH_QL_DATA_SERVICE__CONFIGURATION = eINSTANCE.getGraphQLDataService_Configuration();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.QGisDataServiceImpl <em>QGis Data Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.QGisDataServiceImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getQGisDataService()
		 * @generated
		 */
		EClass QGIS_DATA_SERVICE = eINSTANCE.getQGisDataService();

		/**
		 * The meta object literal for the '<em><b>Configuration</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QGIS_DATA_SERVICE__CONFIGURATION = eINSTANCE.getQGisDataService_Configuration();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.QGisDataServiceConfigurationImpl <em>QGis Data Service Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.QGisDataServiceConfigurationImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getQGisDataServiceConfiguration()
		 * @generated
		 */
		EClass QGIS_DATA_SERVICE_CONFIGURATION = eINSTANCE.getQGisDataServiceConfiguration();

		/**
		 * The meta object literal for the '<em><b>Layer</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QGIS_DATA_SERVICE_CONFIGURATION__LAYER = eINSTANCE.getQGisDataServiceConfiguration_Layer();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.GraphQLDataServiceConfigurationImpl <em>Graph QL Data Service Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.GraphQLDataServiceConfigurationImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getGraphQLDataServiceConfiguration()
		 * @generated
		 */
		EClass GRAPH_QL_DATA_SERVICE_CONFIGURATION = eINSTANCE.getGraphQLDataServiceConfiguration();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceImpl <em>Ogc Features Data Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.OgcFeaturesDataServiceImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getOgcFeaturesDataService()
		 * @generated
		 */
		EClass OGC_FEATURES_DATA_SERVICE = eINSTANCE.getOgcFeaturesDataService();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.OgcSensorThingsDataServiceImpl <em>Ogc Sensor Things Data Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.OgcSensorThingsDataServiceImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getOgcSensorThingsDataService()
		 * @generated
		 */
		EClass OGC_SENSOR_THINGS_DATA_SERVICE = eINSTANCE.getOgcSensorThingsDataService();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.MongoRepositoryImpl <em>Mongo Repository</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.MongoRepositoryImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getMongoRepository()
		 * @generated
		 */
		EClass MONGO_REPOSITORY = eINSTANCE.getMongoRepository();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.FileDataInputImpl <em>File Data Input</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.FileDataInputImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getFileDataInput()
		 * @generated
		 */
		EClass FILE_DATA_INPUT = eINSTANCE.getFileDataInput();

		/**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FILE_DATA_INPUT__URI = eINSTANCE.getFileDataInput_Uri();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.JPADataInputImpl <em>JPA Data Input</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.JPADataInputImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getJPADataInput()
		 * @generated
		 */
		EClass JPA_DATA_INPUT = eINSTANCE.getJPADataInput();

		/**
		 * The meta object literal for the '<em><b>Persistence Config</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JPA_DATA_INPUT__PERSISTENCE_CONFIG = eINSTANCE.getJPADataInput_PersistenceConfig();

		/**
		 * The meta object literal for the '<em><b>Data Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference JPA_DATA_INPUT__DATA_SOURCE = eINSTANCE.getJPADataInput_DataSource();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DataTransformationImpl <em>Data Transformation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DataTransformationImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDataTransformation()
		 * @generated
		 */
		EClass DATA_TRANSFORMATION = eINSTANCE.getDataTransformation();

		/**
		 * The meta object literal for the '<em><b>Supported EClasses</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_TRANSFORMATION__SUPPORTED_ECLASSES = eINSTANCE.getDataTransformation_SupportedEClasses();

		/**
		 * The meta object literal for the '<em><b>Result EClasses</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DATA_TRANSFORMATION__RESULT_ECLASSES = eINSTANCE.getDataTransformation_ResultEClasses();

		/**
		 * The meta object literal for the '<em><b>QVT Placeholder</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DATA_TRANSFORMATION__QVT_PLACEHOLDER = eINSTANCE.getDataTransformation_QVTPlaceholder();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.QueryTransformationImpl <em>Query Transformation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.QueryTransformationImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getQueryTransformation()
		 * @generated
		 */
		EClass QUERY_TRANSFORMATION = eINSTANCE.getQueryTransformation();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.ODataDataServiceImpl <em>OData Data Service</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.ODataDataServiceImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getODataDataService()
		 * @generated
		 */
		EClass ODATA_DATA_SERVICE = eINSTANCE.getODataDataService();

		/**
		 * The meta object literal for the '<em><b>Configuration</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ODATA_DATA_SERVICE__CONFIGURATION = eINSTANCE.getODataDataService_Configuration();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.ODataDataServiceConfigurationImpl <em>OData Data Service Configuration</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.ODataDataServiceConfigurationImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getODataDataServiceConfiguration()
		 * @generated
		 */
		EClass ODATA_DATA_SERVICE_CONFIGURATION = eINSTANCE.getODataDataServiceConfiguration();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.DistributionExportImpl <em>Distribution Export</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DistributionExportImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getDistributionExport()
		 * @generated
		 */
		EClass DISTRIBUTION_EXPORT = eINSTANCE.getDistributionExport();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISTRIBUTION_EXPORT__ID = eINSTANCE.getDistributionExport_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISTRIBUTION_EXPORT__NAME = eINSTANCE.getDistributionExport_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DISTRIBUTION_EXPORT__DESCRIPTION = eINSTANCE.getDistributionExport_Description();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.CSVDistributionExportImpl <em>CSV Distribution Export</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.CSVDistributionExportImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getCSVDistributionExport()
		 * @generated
		 */
		EClass CSV_DISTRIBUTION_EXPORT = eINSTANCE.getCSVDistributionExport();

		/**
		 * The meta object literal for the '<em><b>Separator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CSV_DISTRIBUTION_EXPORT__SEPARATOR = eINSTANCE.getCSVDistributionExport_Separator();

		/**
		 * The meta object literal for the '<em><b>Compressed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CSV_DISTRIBUTION_EXPORT__COMPRESSED = eINSTANCE.getCSVDistributionExport_Compressed();

		/**
		 * The meta object literal for the '<em><b>Include Type Header</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CSV_DISTRIBUTION_EXPORT__INCLUDE_TYPE_HEADER = eINSTANCE.getCSVDistributionExport_IncludeTypeHeader();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.TransformationImpl <em>Transformation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.TransformationImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getTransformation()
		 * @generated
		 */
		EClass TRANSFORMATION = eINSTANCE.getTransformation();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFORMATION__ID = eINSTANCE.getTransformation_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFORMATION__NAME = eINSTANCE.getTransformation_Name();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRANSFORMATION__DESCRIPTION = eINSTANCE.getTransformation_Description();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.configuration.impl.JdbcDataSourceImpl <em>Jdbc Data Source</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.JdbcDataSourceImpl
		 * @see org.eclipse.fennec.data.atlas.configuration.impl.DAConfigPackageImpl#getJdbcDataSource()
		 * @generated
		 */
		EClass JDBC_DATA_SOURCE = eINSTANCE.getJdbcDataSource();

		/**
		 * The meta object literal for the '<em><b>Filter</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JDBC_DATA_SOURCE__FILTER = eINSTANCE.getJdbcDataSource_Filter();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JDBC_DATA_SOURCE__ID = eINSTANCE.getJdbcDataSource_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute JDBC_DATA_SOURCE__NAME = eINSTANCE.getJdbcDataSource_Name();

	}

} //DAConfigPackage
