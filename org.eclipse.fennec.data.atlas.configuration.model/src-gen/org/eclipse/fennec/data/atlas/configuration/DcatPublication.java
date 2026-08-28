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

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dcat Publication</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Declares that a DataProvider referencing this element is published as open data to a DCAT portal (DCAT.Atlas). Defined once in the publications registry and referenced from DataServices and DataSets (via DataProvider.publication), so the same target catalog and metadata defaults are not duplicated per provider.
 * 
 * Deliberately plain data: an id, a catalog name and a handful of metadata overrides. The mapping to dcat:Dataset / dcat:DataService lives in the omittable publication bundle - this model must never depend on a DCAT model (data.atlas issue #4, DA-DCAT-1). Metadata left unset here is derived from the provider (name, description, model annotations); an explicit value wins over a derived one (DA-DCAT-8).
 * 
 * The portal endpoint itself (base URL, credentials, timeouts) is a deployment concern and is NOT configured here: it is the Config-Admin configuration of the dcat.atlas client (DA-DCAT-6).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getCatalog <em>Catalog</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getPortal <em>Portal</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getTitle <em>Title</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getThemes <em>Themes</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getPublisherName <em>Publisher Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getPublisherUri <em>Publisher Uri</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getLicenseUri <em>License Uri</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication()
 * @model
 * @generated
 */
@ProviderType
public interface DcatPublication extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unique identifier of this publication declaration.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Catalog</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Identifier of the target catalog in the portal the published entries are linked into. The catalog is expected to exist - catalog creation is out of scope (DA-DCAT-5).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Catalog</em>' attribute.
	 * @see #setCatalog(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_Catalog()
	 * @model required="true"
	 * @generated
	 */
	String getCatalog();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getCatalog <em>Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Catalog</em>' attribute.
	 * @see #getCatalog()
	 * @generated
	 */
	void setCatalog(String value);

	/**
	 * Returns the value of the '<em><b>Portal</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Logical name of the portal to publish to, matching the 'dcat.portal' service property of a configured dcat.atlas client. Unset means the single configured client; with more than one client configured, an unset portal is a configuration error. This is the link between the deployment-owned client configuration and this model (DA-DCAT-5/6).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Portal</em>' attribute.
	 * @see #setPortal(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_Portal()
	 * @model
	 * @generated
	 */
	String getPortal();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getPortal <em>Portal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Portal</em>' attribute.
	 * @see #getPortal()
	 * @generated
	 */
	void setPortal(String value);

	/**
	 * Returns the value of the '<em><b>Identifier</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional override of the DCAT identifier (the last PUT path segment). Unset means the id of the published provider. Must be stable and deterministic - it survives restarts, configuration reloads and redeployments (DA-DCAT-10/11).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Identifier</em>' attribute.
	 * @see #setIdentifier(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_Identifier()
	 * @model
	 * @generated
	 */
	String getIdentifier();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getIdentifier <em>Identifier</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Identifier</em>' attribute.
	 * @see #getIdentifier()
	 * @generated
	 */
	void setIdentifier(String value);

	/**
	 * Returns the value of the '<em><b>Title</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Explicit dct:title override. Unset derives from the provider's name.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Title</em>' attribute.
	 * @see #setTitle(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_Title()
	 * @model
	 * @generated
	 */
	String getTitle();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getTitle <em>Title</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Title</em>' attribute.
	 * @see #getTitle()
	 * @generated
	 */
	void setTitle(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Explicit dct:description override. Unset derives from the provider's description, else from the GenModel documentation annotation of the provider's model type.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Language</b></em>' attribute.
	 * The default value is <code>"en"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Language tag of the published plain literals (title, description, keywords).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Language</em>' attribute.
	 * @see #setLanguage(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_Language()
	 * @model default="en"
	 * @generated
	 */
	String getLanguage();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getLanguage <em>Language</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Language</em>' attribute.
	 * @see #getLanguage()
	 * @generated
	 */
	void setLanguage(String value);

	/**
	 * Returns the value of the '<em><b>Keywords</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * dcat:keyword entries of the published dataset(s).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Keywords</em>' attribute list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_Keywords()
	 * @model
	 * @generated
	 */
	EList<String> getKeywords();

	/**
	 * Returns the value of the '<em><b>Themes</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * dcat:theme IRIs of the published dataset(s), e.g. an EU data theme vocabulary entry.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Themes</em>' attribute list.
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_Themes()
	 * @model
	 * @generated
	 */
	EList<String> getThemes();

	/**
	 * Returns the value of the '<em><b>Publisher Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Name of the dct:publisher (a foaf:Agent). Mandatory for publication by the portal's shapes and not derivable from the data model - a publication without one is a diagnosed configuration error (DA-DCAT-9).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Publisher Name</em>' attribute.
	 * @see #setPublisherName(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_PublisherName()
	 * @model
	 * @generated
	 */
	String getPublisherName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getPublisherName <em>Publisher Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Publisher Name</em>' attribute.
	 * @see #getPublisherName()
	 * @generated
	 */
	void setPublisherName(String value);

	/**
	 * Returns the value of the '<em><b>Publisher Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional IRI identifying the publisher agent.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Publisher Uri</em>' attribute.
	 * @see #setPublisherUri(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_PublisherUri()
	 * @model
	 * @generated
	 */
	String getPublisherUri();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getPublisherUri <em>Publisher Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Publisher Uri</em>' attribute.
	 * @see #getPublisherUri()
	 * @generated
	 */
	void setPublisherUri(String value);

	/**
	 * Returns the value of the '<em><b>License Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * License IRI of the published distributions (e.g. a dcat-ap.de license vocabulary entry). Mandatory for a distribution by the portal's shapes - a publication whose provider serves distributions needs one (DA-DCAT-9).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>License Uri</em>' attribute.
	 * @see #setLicenseUri(String)
	 * @see org.eclipse.fennec.data.atlas.configuration.DAConfigPackage#getDcatPublication_LicenseUri()
	 * @model
	 * @generated
	 */
	String getLicenseUri();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.configuration.DcatPublication#getLicenseUri <em>License Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>License Uri</em>' attribute.
	 * @see #getLicenseUri()
	 * @generated
	 */
	void setLicenseUri(String value);

} // DcatPublication
