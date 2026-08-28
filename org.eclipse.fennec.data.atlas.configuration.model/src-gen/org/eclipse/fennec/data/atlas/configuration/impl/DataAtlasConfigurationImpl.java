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
package org.eclipse.fennec.data.atlas.configuration.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasConfiguration;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.DataService;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.DcatPublication;
import org.eclipse.fennec.data.atlas.configuration.DistributionExport;
import org.eclipse.fennec.data.atlas.configuration.JdbcDataSource;
import org.eclipse.fennec.data.atlas.configuration.Transformation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Atlas Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl#getDataSources <em>Data Sources</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl#getDataInputs <em>Data Inputs</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl#getDataSets <em>Data Sets</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl#getServices <em>Services</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl#getExports <em>Exports</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl#getTransformations <em>Transformations</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasConfigurationImpl#getPublications <em>Publications</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DataAtlasConfigurationImpl extends MinimalEObjectImpl.Container implements DataAtlasConfiguration {
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
	 * The cached value of the '{@link #getDataSources() <em>Data Sources</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataSources()
	 * @generated
	 * @ordered
	 */
	protected EList<JdbcDataSource> dataSources;

	/**
	 * The cached value of the '{@link #getDataInputs() <em>Data Inputs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataInputs()
	 * @generated
	 * @ordered
	 */
	protected EList<DataInput> dataInputs;

	/**
	 * The cached value of the '{@link #getDataSets() <em>Data Sets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDataSets()
	 * @generated
	 * @ordered
	 */
	protected EList<DataSet> dataSets;

	/**
	 * The cached value of the '{@link #getServices() <em>Services</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServices()
	 * @generated
	 * @ordered
	 */
	protected EList<DataService> services;

	/**
	 * The cached value of the '{@link #getExports() <em>Exports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExports()
	 * @generated
	 * @ordered
	 */
	protected EList<DistributionExport> exports;

	/**
	 * The cached value of the '{@link #getTransformations() <em>Transformations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransformations()
	 * @generated
	 * @ordered
	 */
	protected EList<Transformation> transformations;

	/**
	 * The cached value of the '{@link #getPublications() <em>Publications</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublications()
	 * @generated
	 * @ordered
	 */
	protected EList<DcatPublication> publications;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataAtlasConfigurationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.DATA_ATLAS_CONFIGURATION;
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
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_ATLAS_CONFIGURATION__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_ATLAS_CONFIGURATION__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<JdbcDataSource> getDataSources() {
		if (dataSources == null) {
			dataSources = new EObjectContainmentEList<JdbcDataSource>(JdbcDataSource.class, this, DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SOURCES);
		}
		return dataSources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DataInput> getDataInputs() {
		if (dataInputs == null) {
			dataInputs = new EObjectContainmentEList<DataInput>(DataInput.class, this, DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_INPUTS);
		}
		return dataInputs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DataSet> getDataSets() {
		if (dataSets == null) {
			dataSets = new EObjectContainmentEList<DataSet>(DataSet.class, this, DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SETS);
		}
		return dataSets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DataService> getServices() {
		if (services == null) {
			services = new EObjectContainmentEList<DataService>(DataService.class, this, DAConfigPackage.DATA_ATLAS_CONFIGURATION__SERVICES);
		}
		return services;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DistributionExport> getExports() {
		if (exports == null) {
			exports = new EObjectContainmentEList<DistributionExport>(DistributionExport.class, this, DAConfigPackage.DATA_ATLAS_CONFIGURATION__EXPORTS);
		}
		return exports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Transformation> getTransformations() {
		if (transformations == null) {
			transformations = new EObjectContainmentEList<Transformation>(Transformation.class, this, DAConfigPackage.DATA_ATLAS_CONFIGURATION__TRANSFORMATIONS);
		}
		return transformations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DcatPublication> getPublications() {
		if (publications == null) {
			publications = new EObjectContainmentEList<DcatPublication>(DcatPublication.class, this, DAConfigPackage.DATA_ATLAS_CONFIGURATION__PUBLICATIONS);
		}
		return publications;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SOURCES:
				return ((InternalEList<?>)getDataSources()).basicRemove(otherEnd, msgs);
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_INPUTS:
				return ((InternalEList<?>)getDataInputs()).basicRemove(otherEnd, msgs);
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SETS:
				return ((InternalEList<?>)getDataSets()).basicRemove(otherEnd, msgs);
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__SERVICES:
				return ((InternalEList<?>)getServices()).basicRemove(otherEnd, msgs);
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__EXPORTS:
				return ((InternalEList<?>)getExports()).basicRemove(otherEnd, msgs);
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__TRANSFORMATIONS:
				return ((InternalEList<?>)getTransformations()).basicRemove(otherEnd, msgs);
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__PUBLICATIONS:
				return ((InternalEList<?>)getPublications()).basicRemove(otherEnd, msgs);
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
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__NAME:
				return getName();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DESCRIPTION:
				return getDescription();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SOURCES:
				return getDataSources();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_INPUTS:
				return getDataInputs();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SETS:
				return getDataSets();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__SERVICES:
				return getServices();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__EXPORTS:
				return getExports();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__TRANSFORMATIONS:
				return getTransformations();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__PUBLICATIONS:
				return getPublications();
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
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__NAME:
				setName((String)newValue);
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SOURCES:
				getDataSources().clear();
				getDataSources().addAll((Collection<? extends JdbcDataSource>)newValue);
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_INPUTS:
				getDataInputs().clear();
				getDataInputs().addAll((Collection<? extends DataInput>)newValue);
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SETS:
				getDataSets().clear();
				getDataSets().addAll((Collection<? extends DataSet>)newValue);
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__SERVICES:
				getServices().clear();
				getServices().addAll((Collection<? extends DataService>)newValue);
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__EXPORTS:
				getExports().clear();
				getExports().addAll((Collection<? extends DistributionExport>)newValue);
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__TRANSFORMATIONS:
				getTransformations().clear();
				getTransformations().addAll((Collection<? extends Transformation>)newValue);
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__PUBLICATIONS:
				getPublications().clear();
				getPublications().addAll((Collection<? extends DcatPublication>)newValue);
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
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__NAME:
				setName(NAME_EDEFAULT);
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SOURCES:
				getDataSources().clear();
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_INPUTS:
				getDataInputs().clear();
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SETS:
				getDataSets().clear();
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__SERVICES:
				getServices().clear();
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__EXPORTS:
				getExports().clear();
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__TRANSFORMATIONS:
				getTransformations().clear();
				return;
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__PUBLICATIONS:
				getPublications().clear();
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
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SOURCES:
				return dataSources != null && !dataSources.isEmpty();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_INPUTS:
				return dataInputs != null && !dataInputs.isEmpty();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__DATA_SETS:
				return dataSets != null && !dataSets.isEmpty();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__SERVICES:
				return services != null && !services.isEmpty();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__EXPORTS:
				return exports != null && !exports.isEmpty();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__TRANSFORMATIONS:
				return transformations != null && !transformations.isEmpty();
			case DAConfigPackage.DATA_ATLAS_CONFIGURATION__PUBLICATIONS:
				return publications != null && !publications.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(", description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

} //DataAtlasConfigurationImpl
