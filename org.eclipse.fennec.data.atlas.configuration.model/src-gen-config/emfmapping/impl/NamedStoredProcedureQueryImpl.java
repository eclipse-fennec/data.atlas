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
package emfmapping.impl;

import emfmapping.EmfmappingPackage;
import emfmapping.NamedStoredProcedureQuery;
import emfmapping.QueryHint;
import emfmapping.StoredProcedureParameter;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Named Stored Procedure Query</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link emfmapping.impl.NamedStoredProcedureQueryImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link emfmapping.impl.NamedStoredProcedureQueryImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link emfmapping.impl.NamedStoredProcedureQueryImpl#getResultClass <em>Result Class</em>}</li>
 *   <li>{@link emfmapping.impl.NamedStoredProcedureQueryImpl#getResultSetMapping <em>Result Set Mapping</em>}</li>
 *   <li>{@link emfmapping.impl.NamedStoredProcedureQueryImpl#getHint <em>Hint</em>}</li>
 *   <li>{@link emfmapping.impl.NamedStoredProcedureQueryImpl#getName <em>Name</em>}</li>
 *   <li>{@link emfmapping.impl.NamedStoredProcedureQueryImpl#getProcedureName <em>Procedure Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamedStoredProcedureQueryImpl extends MinimalEObjectImpl.Container implements NamedStoredProcedureQuery {
	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getParameter() <em>Parameter</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameter()
	 * @generated
	 * @ordered
	 */
	protected EList<StoredProcedureParameter> parameter;

	/**
	 * The cached value of the '{@link #getResultClass() <em>Result Class</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultClass()
	 * @generated
	 * @ordered
	 */
	protected EList<String> resultClass;

	/**
	 * The cached value of the '{@link #getResultSetMapping() <em>Result Set Mapping</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResultSetMapping()
	 * @generated
	 * @ordered
	 */
	protected EList<String> resultSetMapping;

	/**
	 * The cached value of the '{@link #getHint() <em>Hint</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHint()
	 * @generated
	 * @ordered
	 */
	protected EList<QueryHint> hint;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getProcedureName() <em>Procedure Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcedureName()
	 * @generated
	 * @ordered
	 */
	protected static final String PROCEDURE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProcedureName() <em>Procedure Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProcedureName()
	 * @generated
	 * @ordered
	 */
	protected String procedureName = PROCEDURE_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NamedStoredProcedureQueryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EmfmappingPackage.Literals.NAMED_STORED_PROCEDURE_QUERY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<StoredProcedureParameter> getParameter() {
		if (parameter == null) {
			parameter = new EObjectContainmentEList<StoredProcedureParameter>(StoredProcedureParameter.class, this, EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PARAMETER);
		}
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<String> getResultClass() {
		if (resultClass == null) {
			resultClass = new EDataTypeEList<String>(String.class, this, EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__RESULT_CLASS);
		}
		return resultClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<String> getResultSetMapping() {
		if (resultSetMapping == null) {
			resultSetMapping = new EDataTypeEList<String>(String.class, this, EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__RESULT_SET_MAPPING);
		}
		return resultSetMapping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<QueryHint> getHint() {
		if (hint == null) {
			hint = new EObjectContainmentEList<QueryHint>(QueryHint.class, this, EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__HINT);
		}
		return hint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getProcedureName() {
		return procedureName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProcedureName(String newProcedureName) {
		String oldProcedureName = procedureName;
		procedureName = newProcedureName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PROCEDURE_NAME, oldProcedureName, procedureName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PARAMETER:
				return ((InternalEList<?>)getParameter()).basicRemove(otherEnd, msgs);
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__HINT:
				return ((InternalEList<?>)getHint()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__DESCRIPTION:
				return getDescription();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PARAMETER:
				return getParameter();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__RESULT_CLASS:
				return getResultClass();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__RESULT_SET_MAPPING:
				return getResultSetMapping();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__HINT:
				return getHint();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__NAME:
				return getName();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PROCEDURE_NAME:
				return getProcedureName();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PARAMETER:
				getParameter().clear();
				getParameter().addAll((Collection<? extends StoredProcedureParameter>)newValue);
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__RESULT_CLASS:
				getResultClass().clear();
				getResultClass().addAll((Collection<? extends String>)newValue);
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__RESULT_SET_MAPPING:
				getResultSetMapping().clear();
				getResultSetMapping().addAll((Collection<? extends String>)newValue);
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__HINT:
				getHint().clear();
				getHint().addAll((Collection<? extends QueryHint>)newValue);
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__NAME:
				setName((String)newValue);
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PROCEDURE_NAME:
				setProcedureName((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PARAMETER:
				getParameter().clear();
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__RESULT_CLASS:
				getResultClass().clear();
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__RESULT_SET_MAPPING:
				getResultSetMapping().clear();
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__HINT:
				getHint().clear();
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PROCEDURE_NAME:
				setProcedureName(PROCEDURE_NAME_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PARAMETER:
				return parameter != null && !parameter.isEmpty();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__RESULT_CLASS:
				return resultClass != null && !resultClass.isEmpty();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__RESULT_SET_MAPPING:
				return resultSetMapping != null && !resultSetMapping.isEmpty();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__HINT:
				return hint != null && !hint.isEmpty();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY__PROCEDURE_NAME:
				return PROCEDURE_NAME_EDEFAULT == null ? procedureName != null : !PROCEDURE_NAME_EDEFAULT.equals(procedureName);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (description: ");
		result.append(description);
		result.append(", resultClass: ");
		result.append(resultClass);
		result.append(", resultSetMapping: ");
		result.append(resultSetMapping);
		result.append(", name: ");
		result.append(name);
		result.append(", procedureName: ");
		result.append(procedureName);
		result.append(')');
		return result.toString();
	}

} //NamedStoredProcedureQueryImpl
