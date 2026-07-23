/*
 */
package org.eclipse.fennec.data.atlas.dcat.foaf;

import org.eclipse.fennec.data.atlas.dcat.rdf.PlainLiteral;
import org.eclipse.fennec.data.atlas.dcat.rdf.Resource;

import org.eclipse.fennec.data.atlas.dcat.skos.Concept;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Agent Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.data.atlas.dcat.foaf.AgentType#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.dcat.foaf.AgentType#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.dcat.foaf.AgentType#getPhone <em>Phone</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.dcat.foaf.AgentType#getMbox <em>Mbox</em>}</li>
 *   <li>{@link org.eclipse.fennec.data.atlas.dcat.foaf.AgentType#getNodeID <em>Node ID</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.data.atlas.dcat.foaf.FoafPackage#getAgentType()
 * @model extendedMetaData="name='Agent_._type' kind='elementOnly'"
 * @generated
 */
@ProviderType
public interface AgentType extends Resource {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' containment reference.
	 * @see #setName(PlainLiteral)
	 * @see org.eclipse.fennec.data.atlas.dcat.foaf.FoafPackage#getAgentType_Name()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='name' namespace='##targetNamespace'"
	 * @generated
	 */
	PlainLiteral getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.dcat.foaf.AgentType#getName <em>Name</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' containment reference.
	 * @see #getName()
	 * @generated
	 */
	void setName(PlainLiteral value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' containment reference.
	 * @see #setType(Concept)
	 * @see org.eclipse.fennec.data.atlas.dcat.foaf.FoafPackage#getAgentType_Type()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='type' namespace='http://purl.org/dc/terms/'"
	 * @generated
	 */
	Concept getType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.dcat.foaf.AgentType#getType <em>Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' containment reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(Concept value);

	/**
	 * Returns the value of the '<em><b>Phone</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Phone</em>' containment reference.
	 * @see #setPhone(Resource)
	 * @see org.eclipse.fennec.data.atlas.dcat.foaf.FoafPackage#getAgentType_Phone()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='phone' namespace='##targetNamespace'"
	 * @generated
	 */
	Resource getPhone();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.dcat.foaf.AgentType#getPhone <em>Phone</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Phone</em>' containment reference.
	 * @see #getPhone()
	 * @generated
	 */
	void setPhone(Resource value);

	/**
	 * Returns the value of the '<em><b>Mbox</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mbox</em>' containment reference.
	 * @see #setMbox(Resource)
	 * @see org.eclipse.fennec.data.atlas.dcat.foaf.FoafPackage#getAgentType_Mbox()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='mbox' namespace='##targetNamespace'"
	 * @generated
	 */
	Resource getMbox();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.dcat.foaf.AgentType#getMbox <em>Mbox</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mbox</em>' containment reference.
	 * @see #getMbox()
	 * @generated
	 */
	void setMbox(Resource value);

	/**
	 * Returns the value of the '<em><b>Node ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Node ID</em>' attribute.
	 * @see #setNodeID(String)
	 * @see org.eclipse.fennec.data.atlas.dcat.foaf.FoafPackage#getAgentType_NodeID()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.NCName"
	 *        extendedMetaData="kind='attribute' name='nodeID' namespace='http://www.w3.org/1999/02/22-rdf-syntax-ns#'"
	 * @generated
	 */
	String getNodeID();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.data.atlas.dcat.foaf.AgentType#getNodeID <em>Node ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Node ID</em>' attribute.
	 * @see #getNodeID()
	 * @generated
	 */
	void setNodeID(String value);

} // AgentType
