/*
 */
package org.eclipse.fennec.data.atlas.dcat.dcat;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dataset Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.dcat.dcat.DatasetContainer#getDataset <em>Dataset</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.dcat.dcat.DcatPackage#getDatasetContainer()
 * @model extendedMetaData="name='dataset' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface DatasetContainer extends EObject {
	/**
	 * Returns the value of the '<em><b>Dataset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dataset</em>' containment reference.
	 * @see #setDataset(Dataset)
	 * @see org.eclipse.fennec.data.atlas.dcat.dcat.DcatPackage#getDatasetContainer_Dataset()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' namespace='##targetNamespace' name='Dataset'"
	 * @generated
	 */
	Dataset getDataset();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.dcat.dcat.DatasetContainer#getDataset <em>Dataset</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dataset</em>' containment reference.
	 * @see #getDataset()
	 * @generated
	 */
	void setDataset(Dataset value);

} // DatasetContainer
