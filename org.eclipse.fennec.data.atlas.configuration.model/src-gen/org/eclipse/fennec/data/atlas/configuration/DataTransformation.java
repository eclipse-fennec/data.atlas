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

import org.eclipse.emf.ecore.EClass;

import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Transformation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Describes a transformation Step from Model A to B.
 * 
 * The executable transformation is a QVT-O AST (an OperationalTransformation of the fennec m2x qvto model) referenced as an EObject: in file mode from an XMI next to the configuration, in Model Atlas mode from a dedicated EObject registry of the scope. Authoring happens in QVT-O text, parsed once at publish time.
 * 
 * Data-Atlas contract: the transformation is 1:1 - one source object maps to exactly one result object carrying the same id.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataTransformation#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataTransformation#getSupportedEClasses <em>Supported EClasses</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DataTransformation#getResultEClasses <em>Result EClasses</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataTransformation()
 * @model
 * @generated
 */
@ProviderType
public interface DataTransformation extends Transformation {
	/**
	 * Returns the value of the '<em><b>Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The executable QVT-O transformation (the parsed AST, referenced as an EObject).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transformation</em>' reference.
	 * @see #setTransformation(OperationalTransformation)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataTransformation_Transformation()
	 * @model required="true"
	 * @generated
	 */
	OperationalTransformation getTransformation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DataTransformation#getTransformation <em>Transformation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transformation</em>' reference.
	 * @see #getTransformation()
	 * @generated
	 */
	void setTransformation(OperationalTransformation value);

	/**
	 * Returns the value of the '<em><b>Supported EClasses</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EClass}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Input EClasses this transformation accepts.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Supported EClasses</em>' reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataTransformation_SupportedEClasses()
	 * @model
	 * @generated
	 */
	EList<EClass> getSupportedEClasses();

	/**
	 * Returns the value of the '<em><b>Result EClasses</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EClass}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Output EClasses this transformation produces.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Result EClasses</em>' reference list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDataTransformation_ResultEClasses()
	 * @model
	 * @generated
	 */
	EList<EClass> getResultEClasses();

} // DataTransformation
