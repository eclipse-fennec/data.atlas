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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.fennec.atlas.configuration.impl.DataProviderImpl;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DataAtlasProvider;
import org.eclipse.fennec.data.atlas.configuration.DcatPublication;
import org.eclipse.fennec.data.atlas.configuration.DistributionExport;
import org.eclipse.fennec.data.atlas.configuration.Transformation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Data Atlas Provider</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasProviderImpl#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasProviderImpl#getDistributionExport <em>Distribution Export</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DataAtlasProviderImpl#getPublication <em>Publication</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class DataAtlasProviderImpl extends DataProviderImpl implements DataAtlasProvider {
	/**
	 * The cached value of the '{@link #getTransformation() <em>Transformation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransformation()
	 * @generated
	 * @ordered
	 */
	protected Transformation transformation;

	/**
	 * The cached value of the '{@link #getDistributionExport() <em>Distribution Export</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistributionExport()
	 * @generated
	 * @ordered
	 */
	protected EList<DistributionExport> distributionExport;

	/**
	 * The cached value of the '{@link #getPublication() <em>Publication</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublication()
	 * @generated
	 * @ordered
	 */
	protected DcatPublication publication;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataAtlasProviderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.DATA_ATLAS_PROVIDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Transformation getTransformation() {
		if (transformation != null && transformation.eIsProxy()) {
			InternalEObject oldTransformation = (InternalEObject)transformation;
			transformation = (Transformation)eResolveProxy(oldTransformation);
			if (transformation != oldTransformation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.DATA_ATLAS_PROVIDER__TRANSFORMATION, oldTransformation, transformation));
			}
		}
		return transformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Transformation basicGetTransformation() {
		return transformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransformation(Transformation newTransformation) {
		Transformation oldTransformation = transformation;
		transformation = newTransformation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_ATLAS_PROVIDER__TRANSFORMATION, oldTransformation, transformation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DistributionExport> getDistributionExport() {
		if (distributionExport == null) {
			distributionExport = new EObjectResolvingEList<DistributionExport>(DistributionExport.class, this, DAConfigPackage.DATA_ATLAS_PROVIDER__DISTRIBUTION_EXPORT);
		}
		return distributionExport;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DcatPublication getPublication() {
		if (publication != null && publication.eIsProxy()) {
			InternalEObject oldPublication = (InternalEObject)publication;
			publication = (DcatPublication)eResolveProxy(oldPublication);
			if (publication != oldPublication) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DAConfigPackage.DATA_ATLAS_PROVIDER__PUBLICATION, oldPublication, publication));
			}
		}
		return publication;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DcatPublication basicGetPublication() {
		return publication;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPublication(DcatPublication newPublication) {
		DcatPublication oldPublication = publication;
		publication = newPublication;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DATA_ATLAS_PROVIDER__PUBLICATION, oldPublication, publication));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.DATA_ATLAS_PROVIDER__TRANSFORMATION:
				if (resolve) return getTransformation();
				return basicGetTransformation();
			case DAConfigPackage.DATA_ATLAS_PROVIDER__DISTRIBUTION_EXPORT:
				return getDistributionExport();
			case DAConfigPackage.DATA_ATLAS_PROVIDER__PUBLICATION:
				if (resolve) return getPublication();
				return basicGetPublication();
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
			case DAConfigPackage.DATA_ATLAS_PROVIDER__TRANSFORMATION:
				setTransformation((Transformation)newValue);
				return;
			case DAConfigPackage.DATA_ATLAS_PROVIDER__DISTRIBUTION_EXPORT:
				getDistributionExport().clear();
				getDistributionExport().addAll((Collection<? extends DistributionExport>)newValue);
				return;
			case DAConfigPackage.DATA_ATLAS_PROVIDER__PUBLICATION:
				setPublication((DcatPublication)newValue);
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
			case DAConfigPackage.DATA_ATLAS_PROVIDER__TRANSFORMATION:
				setTransformation((Transformation)null);
				return;
			case DAConfigPackage.DATA_ATLAS_PROVIDER__DISTRIBUTION_EXPORT:
				getDistributionExport().clear();
				return;
			case DAConfigPackage.DATA_ATLAS_PROVIDER__PUBLICATION:
				setPublication((DcatPublication)null);
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
			case DAConfigPackage.DATA_ATLAS_PROVIDER__TRANSFORMATION:
				return transformation != null;
			case DAConfigPackage.DATA_ATLAS_PROVIDER__DISTRIBUTION_EXPORT:
				return distributionExport != null && !distributionExport.isEmpty();
			case DAConfigPackage.DATA_ATLAS_PROVIDER__PUBLICATION:
				return publication != null;
		}
		return super.eIsSet(featureID);
	}

} //DataAtlasProviderImpl
