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
 * A representation of the model object '<em><b>Order Column</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 
 *         @Target({METHOD, FIELD}) @Retention(RUNTIME)
 *         public @interface OrderColumn {
 *           String name() default "";
 *           boolean nullable() default true;
 *           boolean insertable() default true;
 *           boolean updatable() default true;
 *           String columnDefinition() default "";
 *          }
 * 
 *       
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link emfmapping.OrderColumn#getColumnDefinition <em>Column Definition</em>}</li>
 *   <li>{@link emfmapping.OrderColumn#isInsertable <em>Insertable</em>}</li>
 *   <li>{@link emfmapping.OrderColumn#getName <em>Name</em>}</li>
 *   <li>{@link emfmapping.OrderColumn#isNullable <em>Nullable</em>}</li>
 *   <li>{@link emfmapping.OrderColumn#isUpdatable <em>Updatable</em>}</li>
 * </ul>
 *
 * @see emfmapping.EmfmappingPackage#getOrderColumn()
 * @model extendedMetaData="name='order-column' kind='empty'"
 * @generated
 */
@ProviderType
public interface OrderColumn extends EObject {
	/**
	 * Returns the value of the '<em><b>Column Definition</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Column Definition</em>' attribute.
	 * @see #setColumnDefinition(String)
	 * @see emfmapping.EmfmappingPackage#getOrderColumn_ColumnDefinition()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='column-definition'"
	 * @generated
	 */
	String getColumnDefinition();

	/**
	 * Sets the value of the '{@link emfmapping.OrderColumn#getColumnDefinition <em>Column Definition</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Column Definition</em>' attribute.
	 * @see #getColumnDefinition()
	 * @generated
	 */
	void setColumnDefinition(String value);

	/**
	 * Returns the value of the '<em><b>Insertable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Insertable</em>' attribute.
	 * @see #isSetInsertable()
	 * @see #unsetInsertable()
	 * @see #setInsertable(boolean)
	 * @see emfmapping.EmfmappingPackage#getOrderColumn_Insertable()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='insertable'"
	 * @generated
	 */
	boolean isInsertable();

	/**
	 * Sets the value of the '{@link emfmapping.OrderColumn#isInsertable <em>Insertable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Insertable</em>' attribute.
	 * @see #isSetInsertable()
	 * @see #unsetInsertable()
	 * @see #isInsertable()
	 * @generated
	 */
	void setInsertable(boolean value);

	/**
	 * Unsets the value of the '{@link emfmapping.OrderColumn#isInsertable <em>Insertable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetInsertable()
	 * @see #isInsertable()
	 * @see #setInsertable(boolean)
	 * @generated
	 */
	void unsetInsertable();

	/**
	 * Returns whether the value of the '{@link emfmapping.OrderColumn#isInsertable <em>Insertable</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Insertable</em>' attribute is set.
	 * @see #unsetInsertable()
	 * @see #isInsertable()
	 * @see #setInsertable(boolean)
	 * @generated
	 */
	boolean isSetInsertable();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see emfmapping.EmfmappingPackage#getOrderColumn_Name()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='name'"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link emfmapping.OrderColumn#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Nullable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nullable</em>' attribute.
	 * @see #isSetNullable()
	 * @see #unsetNullable()
	 * @see #setNullable(boolean)
	 * @see emfmapping.EmfmappingPackage#getOrderColumn_Nullable()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='nullable'"
	 * @generated
	 */
	boolean isNullable();

	/**
	 * Sets the value of the '{@link emfmapping.OrderColumn#isNullable <em>Nullable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nullable</em>' attribute.
	 * @see #isSetNullable()
	 * @see #unsetNullable()
	 * @see #isNullable()
	 * @generated
	 */
	void setNullable(boolean value);

	/**
	 * Unsets the value of the '{@link emfmapping.OrderColumn#isNullable <em>Nullable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetNullable()
	 * @see #isNullable()
	 * @see #setNullable(boolean)
	 * @generated
	 */
	void unsetNullable();

	/**
	 * Returns whether the value of the '{@link emfmapping.OrderColumn#isNullable <em>Nullable</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Nullable</em>' attribute is set.
	 * @see #unsetNullable()
	 * @see #isNullable()
	 * @see #setNullable(boolean)
	 * @generated
	 */
	boolean isSetNullable();

	/**
	 * Returns the value of the '<em><b>Updatable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Updatable</em>' attribute.
	 * @see #isSetUpdatable()
	 * @see #unsetUpdatable()
	 * @see #setUpdatable(boolean)
	 * @see emfmapping.EmfmappingPackage#getOrderColumn_Updatable()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='attribute' name='updatable'"
	 * @generated
	 */
	boolean isUpdatable();

	/**
	 * Sets the value of the '{@link emfmapping.OrderColumn#isUpdatable <em>Updatable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Updatable</em>' attribute.
	 * @see #isSetUpdatable()
	 * @see #unsetUpdatable()
	 * @see #isUpdatable()
	 * @generated
	 */
	void setUpdatable(boolean value);

	/**
	 * Unsets the value of the '{@link emfmapping.OrderColumn#isUpdatable <em>Updatable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetUpdatable()
	 * @see #isUpdatable()
	 * @see #setUpdatable(boolean)
	 * @generated
	 */
	void unsetUpdatable();

	/**
	 * Returns whether the value of the '{@link emfmapping.OrderColumn#isUpdatable <em>Updatable</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Updatable</em>' attribute is set.
	 * @see #unsetUpdatable()
	 * @see #isUpdatable()
	 * @see #setUpdatable(boolean)
	 * @generated
	 */
	boolean isSetUpdatable();

} // OrderColumn
