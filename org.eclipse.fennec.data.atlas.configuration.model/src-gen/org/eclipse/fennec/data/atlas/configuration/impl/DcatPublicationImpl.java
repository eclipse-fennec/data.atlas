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

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import org.eclipse.fennec.data.atlas.configuration.DAConfigPackage;
import org.eclipse.fennec.data.atlas.configuration.DcatPublication;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dcat Publication</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getCatalog <em>Catalog</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getPortal <em>Portal</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getThemes <em>Themes</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getPublisherName <em>Publisher Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getPublisherUri <em>Publisher Uri</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.impl.DcatPublicationImpl#getLicenseUri <em>License Uri</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DcatPublicationImpl extends MinimalEObjectImpl.Container implements DcatPublication {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getCatalog() <em>Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCatalog()
	 * @generated
	 * @ordered
	 */
	protected static final String CATALOG_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCatalog() <em>Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCatalog()
	 * @generated
	 * @ordered
	 */
	protected String catalog = CATALOG_EDEFAULT;

	/**
	 * The default value of the '{@link #getPortal() <em>Portal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortal()
	 * @generated
	 * @ordered
	 */
	protected static final String PORTAL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPortal() <em>Portal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortal()
	 * @generated
	 * @ordered
	 */
	protected String portal = PORTAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdentifier()
	 * @generated
	 * @ordered
	 */
	protected static final String IDENTIFIER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIdentifier() <em>Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdentifier()
	 * @generated
	 * @ordered
	 */
	protected String identifier = IDENTIFIER_EDEFAULT;

	/**
	 * The default value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected static final String TITLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTitle() <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTitle()
	 * @generated
	 * @ordered
	 */
	protected String title = TITLE_EDEFAULT;

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
	 * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanguage()
	 * @generated
	 * @ordered
	 */
	protected static final String LANGUAGE_EDEFAULT = "en";

	/**
	 * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanguage()
	 * @generated
	 * @ordered
	 */
	protected String language = LANGUAGE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getKeywords() <em>Keywords</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeywords()
	 * @generated
	 * @ordered
	 */
	protected EList<String> keywords;

	/**
	 * The cached value of the '{@link #getThemes() <em>Themes</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getThemes()
	 * @generated
	 * @ordered
	 */
	protected EList<String> themes;

	/**
	 * The default value of the '{@link #getPublisherName() <em>Publisher Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublisherName()
	 * @generated
	 * @ordered
	 */
	protected static final String PUBLISHER_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPublisherName() <em>Publisher Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublisherName()
	 * @generated
	 * @ordered
	 */
	protected String publisherName = PUBLISHER_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPublisherUri() <em>Publisher Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublisherUri()
	 * @generated
	 * @ordered
	 */
	protected static final String PUBLISHER_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPublisherUri() <em>Publisher Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPublisherUri()
	 * @generated
	 * @ordered
	 */
	protected String publisherUri = PUBLISHER_URI_EDEFAULT;

	/**
	 * The default value of the '{@link #getLicenseUri() <em>License Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLicenseUri()
	 * @generated
	 * @ordered
	 */
	protected static final String LICENSE_URI_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLicenseUri() <em>License Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLicenseUri()
	 * @generated
	 * @ordered
	 */
	protected String licenseUri = LICENSE_URI_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DcatPublicationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DAConfigPackage.Literals.DCAT_PUBLICATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DCAT_PUBLICATION__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCatalog() {
		return catalog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCatalog(String newCatalog) {
		String oldCatalog = catalog;
		catalog = newCatalog;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DCAT_PUBLICATION__CATALOG, oldCatalog, catalog));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPortal() {
		return portal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortal(String newPortal) {
		String oldPortal = portal;
		portal = newPortal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DCAT_PUBLICATION__PORTAL, oldPortal, portal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIdentifier(String newIdentifier) {
		String oldIdentifier = identifier;
		identifier = newIdentifier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DCAT_PUBLICATION__IDENTIFIER, oldIdentifier, identifier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTitle(String newTitle) {
		String oldTitle = title;
		title = newTitle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DCAT_PUBLICATION__TITLE, oldTitle, title));
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
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DCAT_PUBLICATION__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLanguage() {
		return language;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLanguage(String newLanguage) {
		String oldLanguage = language;
		language = newLanguage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DCAT_PUBLICATION__LANGUAGE, oldLanguage, language));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<String> getKeywords() {
		if (keywords == null) {
			keywords = new EDataTypeUniqueEList<String>(String.class, this, DAConfigPackage.DCAT_PUBLICATION__KEYWORDS);
		}
		return keywords;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<String> getThemes() {
		if (themes == null) {
			themes = new EDataTypeUniqueEList<String>(String.class, this, DAConfigPackage.DCAT_PUBLICATION__THEMES);
		}
		return themes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPublisherName() {
		return publisherName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPublisherName(String newPublisherName) {
		String oldPublisherName = publisherName;
		publisherName = newPublisherName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DCAT_PUBLICATION__PUBLISHER_NAME, oldPublisherName, publisherName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPublisherUri() {
		return publisherUri;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPublisherUri(String newPublisherUri) {
		String oldPublisherUri = publisherUri;
		publisherUri = newPublisherUri;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DCAT_PUBLICATION__PUBLISHER_URI, oldPublisherUri, publisherUri));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLicenseUri() {
		return licenseUri;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLicenseUri(String newLicenseUri) {
		String oldLicenseUri = licenseUri;
		licenseUri = newLicenseUri;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DAConfigPackage.DCAT_PUBLICATION__LICENSE_URI, oldLicenseUri, licenseUri));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DAConfigPackage.DCAT_PUBLICATION__ID:
				return getId();
			case DAConfigPackage.DCAT_PUBLICATION__CATALOG:
				return getCatalog();
			case DAConfigPackage.DCAT_PUBLICATION__PORTAL:
				return getPortal();
			case DAConfigPackage.DCAT_PUBLICATION__IDENTIFIER:
				return getIdentifier();
			case DAConfigPackage.DCAT_PUBLICATION__TITLE:
				return getTitle();
			case DAConfigPackage.DCAT_PUBLICATION__DESCRIPTION:
				return getDescription();
			case DAConfigPackage.DCAT_PUBLICATION__LANGUAGE:
				return getLanguage();
			case DAConfigPackage.DCAT_PUBLICATION__KEYWORDS:
				return getKeywords();
			case DAConfigPackage.DCAT_PUBLICATION__THEMES:
				return getThemes();
			case DAConfigPackage.DCAT_PUBLICATION__PUBLISHER_NAME:
				return getPublisherName();
			case DAConfigPackage.DCAT_PUBLICATION__PUBLISHER_URI:
				return getPublisherUri();
			case DAConfigPackage.DCAT_PUBLICATION__LICENSE_URI:
				return getLicenseUri();
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
			case DAConfigPackage.DCAT_PUBLICATION__ID:
				setId((String)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__CATALOG:
				setCatalog((String)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__PORTAL:
				setPortal((String)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__IDENTIFIER:
				setIdentifier((String)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__TITLE:
				setTitle((String)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__LANGUAGE:
				setLanguage((String)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__KEYWORDS:
				getKeywords().clear();
				getKeywords().addAll((Collection<? extends String>)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__THEMES:
				getThemes().clear();
				getThemes().addAll((Collection<? extends String>)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__PUBLISHER_NAME:
				setPublisherName((String)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__PUBLISHER_URI:
				setPublisherUri((String)newValue);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__LICENSE_URI:
				setLicenseUri((String)newValue);
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
			case DAConfigPackage.DCAT_PUBLICATION__ID:
				setId(ID_EDEFAULT);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__CATALOG:
				setCatalog(CATALOG_EDEFAULT);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__PORTAL:
				setPortal(PORTAL_EDEFAULT);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__IDENTIFIER:
				setIdentifier(IDENTIFIER_EDEFAULT);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__TITLE:
				setTitle(TITLE_EDEFAULT);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__LANGUAGE:
				setLanguage(LANGUAGE_EDEFAULT);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__KEYWORDS:
				getKeywords().clear();
				return;
			case DAConfigPackage.DCAT_PUBLICATION__THEMES:
				getThemes().clear();
				return;
			case DAConfigPackage.DCAT_PUBLICATION__PUBLISHER_NAME:
				setPublisherName(PUBLISHER_NAME_EDEFAULT);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__PUBLISHER_URI:
				setPublisherUri(PUBLISHER_URI_EDEFAULT);
				return;
			case DAConfigPackage.DCAT_PUBLICATION__LICENSE_URI:
				setLicenseUri(LICENSE_URI_EDEFAULT);
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
			case DAConfigPackage.DCAT_PUBLICATION__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case DAConfigPackage.DCAT_PUBLICATION__CATALOG:
				return CATALOG_EDEFAULT == null ? catalog != null : !CATALOG_EDEFAULT.equals(catalog);
			case DAConfigPackage.DCAT_PUBLICATION__PORTAL:
				return PORTAL_EDEFAULT == null ? portal != null : !PORTAL_EDEFAULT.equals(portal);
			case DAConfigPackage.DCAT_PUBLICATION__IDENTIFIER:
				return IDENTIFIER_EDEFAULT == null ? identifier != null : !IDENTIFIER_EDEFAULT.equals(identifier);
			case DAConfigPackage.DCAT_PUBLICATION__TITLE:
				return TITLE_EDEFAULT == null ? title != null : !TITLE_EDEFAULT.equals(title);
			case DAConfigPackage.DCAT_PUBLICATION__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case DAConfigPackage.DCAT_PUBLICATION__LANGUAGE:
				return LANGUAGE_EDEFAULT == null ? language != null : !LANGUAGE_EDEFAULT.equals(language);
			case DAConfigPackage.DCAT_PUBLICATION__KEYWORDS:
				return keywords != null && !keywords.isEmpty();
			case DAConfigPackage.DCAT_PUBLICATION__THEMES:
				return themes != null && !themes.isEmpty();
			case DAConfigPackage.DCAT_PUBLICATION__PUBLISHER_NAME:
				return PUBLISHER_NAME_EDEFAULT == null ? publisherName != null : !PUBLISHER_NAME_EDEFAULT.equals(publisherName);
			case DAConfigPackage.DCAT_PUBLICATION__PUBLISHER_URI:
				return PUBLISHER_URI_EDEFAULT == null ? publisherUri != null : !PUBLISHER_URI_EDEFAULT.equals(publisherUri);
			case DAConfigPackage.DCAT_PUBLICATION__LICENSE_URI:
				return LICENSE_URI_EDEFAULT == null ? licenseUri != null : !LICENSE_URI_EDEFAULT.equals(licenseUri);
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
		result.append(" (id: ");
		result.append(id);
		result.append(", catalog: ");
		result.append(catalog);
		result.append(", portal: ");
		result.append(portal);
		result.append(", identifier: ");
		result.append(identifier);
		result.append(", title: ");
		result.append(title);
		result.append(", description: ");
		result.append(description);
		result.append(", language: ");
		result.append(language);
		result.append(", keywords: ");
		result.append(keywords);
		result.append(", themes: ");
		result.append(themes);
		result.append(", publisherName: ");
		result.append(publisherName);
		result.append(", publisherUri: ");
		result.append(publisherUri);
		result.append(", licenseUri: ");
		result.append(licenseUri);
		result.append(')');
		return result.toString();
	}

} //DcatPublicationImpl
