/*
 */
package org.eclipse.fennec.data.atlas.dcat.rdf;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Description</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.dcat.rdf.Description#getAbout <em>About</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.dcat.rdf.Description#getMixed <em>Mixed</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.dcat.rdf.RdfPackage#getDescription()
 * @model extendedMetaData="name='Description' kind='empty'"
 * @generated
 */
@ProviderType
public interface Description extends EObject {
	/**
	 * Returns the value of the '<em><b>About</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>About</em>' attribute.
	 * @see #setAbout(String)
	 * @see org.eclipse.fennec.data.atlas.dcat.rdf.RdfPackage#getDescription_About()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='about' namespace='##targetNamespace'"
	 * @generated
	 */
	String getAbout();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.dcat.rdf.Description#getAbout <em>About</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>About</em>' attribute.
	 * @see #getAbout()
	 * @generated
	 */
	void setAbout(String value);

	/**
	 * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mixed</em>' attribute list.
	 * @see org.eclipse.fennec.data.atlas.dcat.rdf.RdfPackage#getDescription_Mixed()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
	 *        extendedMetaData="kind='elementWildcard' name=':mixed'"
	 * @generated
	 */
	FeatureMap getMixed();

} // Description
