/*
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License 2.0 which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 */
package org.eclipse.fennec.data.atlas.tests.extension;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.fennec.atlas.configuration.AtlasConfigPackage;

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
 * @see org.eclipse.fennec.data.atlas.tests.extension.ProbeFactory
 * @model kind="package"
 * @generated
 */
@ProviderType
@EPackage(uri = ProbePackage.eNS_URI, fingerprint = "fp1:3df0e340201ad0fe88cf8645eb29b92c84769dc533c261e9f3dab2b6ed0957dc", genModel = "/model/probe.genmodel", genModelSourceLocations = {"model/probe.genmodel","org.eclipse.fennec.data.atlas.tests.extension/model/probe.genmodel"}, ecore = "/model/probe.ecore", ecoreSourceLocations = "/model/probe.ecore")
public interface ProbePackage extends org.eclipse.emf.ecore.EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "extension";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "https://eclipse.org/fennec/data/atlas/test/extension/1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "probe";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ProbePackage eINSTANCE = org.eclipse.fennec.data.atlas.tests.extension.impl.ProbePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.data.atlas.tests.extension.impl.ProbeInputImpl <em>Input</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.data.atlas.tests.extension.impl.ProbeInputImpl
	 * @see org.eclipse.fennec.data.atlas.tests.extension.impl.ProbePackageImpl#getProbeInput()
	 * @generated
	 */
	int PROBE_INPUT = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROBE_INPUT__ID = AtlasConfigPackage.DATA_INPUT__ID;

	/**
	 * The feature id for the '<em><b>Supported EClasses</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROBE_INPUT__SUPPORTED_ECLASSES = AtlasConfigPackage.DATA_INPUT__SUPPORTED_ECLASSES;

	/**
	 * The feature id for the '<em><b>Mapping File</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROBE_INPUT__MAPPING_FILE = AtlasConfigPackage.DATA_INPUT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROBE_INPUT_FEATURE_COUNT = AtlasConfigPackage.DATA_INPUT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROBE_INPUT_OPERATION_COUNT = AtlasConfigPackage.DATA_INPUT_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.data.atlas.tests.extension.ProbeInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input</em>'.
	 * @see org.eclipse.fennec.data.atlas.tests.extension.ProbeInput
	 * @generated
	 */
	EClass getProbeInput();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.data.atlas.tests.extension.ProbeInput#getMappingFile <em>Mapping File</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mapping File</em>'.
	 * @see org.eclipse.fennec.data.atlas.tests.extension.ProbeInput#getMappingFile()
	 * @see #getProbeInput()
	 * @generated
	 */
	EAttribute getProbeInput_MappingFile();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ProbeFactory getProbeFactory();

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
		 * The meta object literal for the '{@link org.eclipse.fennec.data.atlas.tests.extension.impl.ProbeInputImpl <em>Input</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.data.atlas.tests.extension.impl.ProbeInputImpl
		 * @see org.eclipse.fennec.data.atlas.tests.extension.impl.ProbePackageImpl#getProbeInput()
		 * @generated
		 */
		EClass PROBE_INPUT = eINSTANCE.getProbeInput();

		/**
		 * The meta object literal for the '<em><b>Mapping File</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROBE_INPUT__MAPPING_FILE = eINSTANCE.getProbeInput_MappingFile();

	}

} //ProbePackage
