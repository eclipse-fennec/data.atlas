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
package emfmapping;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Foreign Key</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({}) @Retention(RUNTIME)
 *         public @interface ForeignKey {
 *           String name() default "";
 * 	  ConstraintMode value() default CONSTRAINT;
 *           String foreign-key-definition() default "";
 * 
 *         Note that the elements that embed the use of the annotation
 *         default this use as @ForeignKey(PROVIDER_DEFAULT).
 * 
 *         }
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link emfmapping.ForeignKey#getDescription <em>Description</em>}</li>
 *   <li>{@link emfmapping.ForeignKey#getConstraintMode <em>Constraint Mode</em>}</li>
 *   <li>{@link emfmapping.ForeignKey#getForeignKeyDefinition <em>Foreign Key Definition</em>}</li>
 *   <li>{@link emfmapping.ForeignKey#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see emfmapping.EmfmappingPackage#getForeignKey()
 * @model extendedMetaData="name='foreign-key' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface ForeignKey extends EObject {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see emfmapping.EmfmappingPackage#getForeignKey_Description()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='description' namespace='##targetNamespace'"
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link emfmapping.ForeignKey#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Constraint Mode</b></em>' attribute.
	 * The literals are from the enumeration {@link emfmapping.ConstraintMode}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraint Mode</em>' attribute.
	 * @see emfmapping.ConstraintMode
	 * @see #isSetConstraintMode()
	 * @see #unsetConstraintMode()
	 * @see #setConstraintMode(ConstraintMode)
	 * @see emfmapping.EmfmappingPackage#getForeignKey_ConstraintMode()
	 * @model unsettable="true"
	 *        extendedMetaData="kind='attribute' name='constraint-mode'"
	 * @generated
	 */
	ConstraintMode getConstraintMode();

	/**
	 * Sets the value of the '{@link emfmapping.ForeignKey#getConstraintMode <em>Constraint Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Constraint Mode</em>' attribute.
	 * @see emfmapping.ConstraintMode
	 * @see #isSetConstraintMode()
	 * @see #unsetConstraintMode()
	 * @see #getConstraintMode()
	 * @generated
	 */
	void setConstraintMode(ConstraintMode value);

	/**
	 * Unsets the value of the '{@link emfmapping.ForeignKey#getConstraintMode <em>Constraint Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetConstraintMode()
	 * @see #getConstraintMode()
	 * @see #setConstraintMode(ConstraintMode)
	 * @generated
	 */
	void unsetConstraintMode();

	/**
	 * Returns whether the value of the '{@link emfmapping.ForeignKey#getConstraintMode <em>Constraint Mode</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Constraint Mode</em>' attribute is set.
	 * @see #unsetConstraintMode()
	 * @see #getConstraintMode()
	 * @see #setConstraintMode(ConstraintMode)
	 * @generated
	 */
	boolean isSetConstraintMode();

	/**
	 * Returns the value of the '<em><b>Foreign Key Definition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Foreign Key Definition</em>' attribute.
	 * @see #setForeignKeyDefinition(String)
	 * @see emfmapping.EmfmappingPackage#getForeignKey_ForeignKeyDefinition()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='foreign-key-definition'"
	 * @generated
	 */
	String getForeignKeyDefinition();

	/**
	 * Sets the value of the '{@link emfmapping.ForeignKey#getForeignKeyDefinition <em>Foreign Key Definition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Foreign Key Definition</em>' attribute.
	 * @see #getForeignKeyDefinition()
	 * @generated
	 */
	void setForeignKeyDefinition(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see emfmapping.EmfmappingPackage#getForeignKey_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link emfmapping.ForeignKey#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ForeignKey
