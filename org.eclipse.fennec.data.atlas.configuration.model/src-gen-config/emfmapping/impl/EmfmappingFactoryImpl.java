/**
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

import emfmapping.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.emf.ecore.xml.type.XMLTypeFactory;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EmfmappingFactoryImpl extends EFactoryImpl implements EmfmappingFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EmfmappingFactory init() {
		try {
			EmfmappingFactory theEmfmappingFactory = (EmfmappingFactory)EPackage.Registry.INSTANCE.getEFactory(EmfmappingPackage.eNS_URI);
			if (theEmfmappingFactory != null) {
				return theEmfmappingFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EmfmappingFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmfmappingFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case EmfmappingPackage.ASSOCIATION_OVERRIDE: return createAssociationOverride();
			case EmfmappingPackage.ATTRIBUTE_OVERRIDE: return createAttributeOverride();
			case EmfmappingPackage.ATTRIBUTES: return createAttributes();
			case EmfmappingPackage.BASIC: return createBasic();
			case EmfmappingPackage.CASCADE_TYPE: return createCascadeType();
			case EmfmappingPackage.COLLECTION_TABLE: return createCollectionTable();
			case EmfmappingPackage.COLUMN: return createColumn();
			case EmfmappingPackage.COLUMN_RESULT: return createColumnResult();
			case EmfmappingPackage.CONSTRUCTOR_RESULT: return createConstructorResult();
			case EmfmappingPackage.CONVERT: return createConvert();
			case EmfmappingPackage.CONVERTER: return createConverter();
			case EmfmappingPackage.DISCRIMINATOR_COLUMN: return createDiscriminatorColumn();
			case EmfmappingPackage.DOCUMENT_ROOT: return createDocumentRoot();
			case EmfmappingPackage.ELEMENT_COLLECTION: return createElementCollection();
			case EmfmappingPackage.EMBEDDABLE: return createEmbeddable();
			case EmfmappingPackage.EMBEDDABLE_ATTRIBUTES: return createEmbeddableAttributes();
			case EmfmappingPackage.EMBEDDED: return createEmbedded();
			case EmfmappingPackage.EMBEDDED_ID: return createEmbeddedId();
			case EmfmappingPackage.EMPTY_TYPE: return createEmptyType();
			case EmfmappingPackage.ENTITY: return createEntity();
			case EmfmappingPackage.ENTITY_LISTENER: return createEntityListener();
			case EmfmappingPackage.ENTITY_LISTENERS: return createEntityListeners();
			case EmfmappingPackage.ENTITY_MAPPINGS_TYPE: return createEntityMappingsType();
			case EmfmappingPackage.ENTITY_RESULT: return createEntityResult();
			case EmfmappingPackage.FIELD_RESULT: return createFieldResult();
			case EmfmappingPackage.FOREIGN_KEY: return createForeignKey();
			case EmfmappingPackage.GENERATED_VALUE: return createGeneratedValue();
			case EmfmappingPackage.ID: return createId();
			case EmfmappingPackage.ID_CLASS: return createIdClass();
			case EmfmappingPackage.INDEX: return createIndex();
			case EmfmappingPackage.INHERITANCE: return createInheritance();
			case EmfmappingPackage.JOIN_COLUMN: return createJoinColumn();
			case EmfmappingPackage.JOIN_TABLE: return createJoinTable();
			case EmfmappingPackage.LOB: return createLob();
			case EmfmappingPackage.MANY_TO_MANY: return createManyToMany();
			case EmfmappingPackage.MANY_TO_ONE: return createManyToOne();
			case EmfmappingPackage.MAP_KEY: return createMapKey();
			case EmfmappingPackage.MAP_KEY_CLASS: return createMapKeyClass();
			case EmfmappingPackage.MAP_KEY_COLUMN: return createMapKeyColumn();
			case EmfmappingPackage.MAP_KEY_JOIN_COLUMN: return createMapKeyJoinColumn();
			case EmfmappingPackage.MAPPED_SUPERCLASS: return createMappedSuperclass();
			case EmfmappingPackage.NAMED_ATTRIBUTE_NODE: return createNamedAttributeNode();
			case EmfmappingPackage.NAMED_ENTITY_GRAPH: return createNamedEntityGraph();
			case EmfmappingPackage.NAMED_NATIVE_QUERY: return createNamedNativeQuery();
			case EmfmappingPackage.NAMED_QUERY: return createNamedQuery();
			case EmfmappingPackage.NAMED_STORED_PROCEDURE_QUERY: return createNamedStoredProcedureQuery();
			case EmfmappingPackage.NAMED_SUBGRAPH: return createNamedSubgraph();
			case EmfmappingPackage.ONE_TO_MANY: return createOneToMany();
			case EmfmappingPackage.ONE_TO_ONE: return createOneToOne();
			case EmfmappingPackage.ORDER_COLUMN: return createOrderColumn();
			case EmfmappingPackage.PERSISTENCE_UNIT_DEFAULTS: return createPersistenceUnitDefaults();
			case EmfmappingPackage.PERSISTENCE_UNIT_METADATA: return createPersistenceUnitMetadata();
			case EmfmappingPackage.POST_LOAD: return createPostLoad();
			case EmfmappingPackage.POST_PERSIST: return createPostPersist();
			case EmfmappingPackage.POST_REMOVE: return createPostRemove();
			case EmfmappingPackage.POST_UPDATE: return createPostUpdate();
			case EmfmappingPackage.PRE_PERSIST: return createPrePersist();
			case EmfmappingPackage.PRE_REMOVE: return createPreRemove();
			case EmfmappingPackage.PRE_UPDATE: return createPreUpdate();
			case EmfmappingPackage.PRIMARY_KEY_JOIN_COLUMN: return createPrimaryKeyJoinColumn();
			case EmfmappingPackage.QUERY_HINT: return createQueryHint();
			case EmfmappingPackage.SECONDARY_TABLE: return createSecondaryTable();
			case EmfmappingPackage.SEQUENCE_GENERATOR: return createSequenceGenerator();
			case EmfmappingPackage.SQL_RESULT_SET_MAPPING: return createSqlResultSetMapping();
			case EmfmappingPackage.STORED_PROCEDURE_PARAMETER: return createStoredProcedureParameter();
			case EmfmappingPackage.TABLE: return createTable();
			case EmfmappingPackage.TABLE_GENERATOR: return createTableGenerator();
			case EmfmappingPackage.TRANSIENT: return createTransient();
			case EmfmappingPackage.UNIQUE_CONSTRAINT: return createUniqueConstraint();
			case EmfmappingPackage.VERSION: return createVersion();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case EmfmappingPackage.ACCESS_TYPE:
				return createAccessTypeFromString(eDataType, initialValue);
			case EmfmappingPackage.CONSTRAINT_MODE:
				return createConstraintModeFromString(eDataType, initialValue);
			case EmfmappingPackage.DISCRIMINATOR_TYPE:
				return createDiscriminatorTypeFromString(eDataType, initialValue);
			case EmfmappingPackage.ENUM_TYPE:
				return createEnumTypeFromString(eDataType, initialValue);
			case EmfmappingPackage.FETCH_TYPE:
				return createFetchTypeFromString(eDataType, initialValue);
			case EmfmappingPackage.GENERATION_TYPE:
				return createGenerationTypeFromString(eDataType, initialValue);
			case EmfmappingPackage.INHERITANCE_TYPE:
				return createInheritanceTypeFromString(eDataType, initialValue);
			case EmfmappingPackage.LOCK_MODE_TYPE:
				return createLockModeTypeFromString(eDataType, initialValue);
			case EmfmappingPackage.PARAMETER_MODE:
				return createParameterModeFromString(eDataType, initialValue);
			case EmfmappingPackage.TEMPORAL_TYPE:
				return createTemporalTypeFromString(eDataType, initialValue);
			case EmfmappingPackage.ACCESS_TYPE_OBJECT:
				return createAccessTypeObjectFromString(eDataType, initialValue);
			case EmfmappingPackage.CONSTRAINT_MODE_OBJECT:
				return createConstraintModeObjectFromString(eDataType, initialValue);
			case EmfmappingPackage.DISCRIMINATOR_TYPE_OBJECT:
				return createDiscriminatorTypeObjectFromString(eDataType, initialValue);
			case EmfmappingPackage.DISCRIMINATOR_VALUE:
				return createDiscriminatorValueFromString(eDataType, initialValue);
			case EmfmappingPackage.ENUMERATED:
				return createEnumeratedFromString(eDataType, initialValue);
			case EmfmappingPackage.ENUM_TYPE_OBJECT:
				return createEnumTypeObjectFromString(eDataType, initialValue);
			case EmfmappingPackage.FETCH_TYPE_OBJECT:
				return createFetchTypeObjectFromString(eDataType, initialValue);
			case EmfmappingPackage.GENERATION_TYPE_OBJECT:
				return createGenerationTypeObjectFromString(eDataType, initialValue);
			case EmfmappingPackage.INHERITANCE_TYPE_OBJECT:
				return createInheritanceTypeObjectFromString(eDataType, initialValue);
			case EmfmappingPackage.LOCK_MODE_TYPE_OBJECT:
				return createLockModeTypeObjectFromString(eDataType, initialValue);
			case EmfmappingPackage.ORDER_BY:
				return createOrderByFromString(eDataType, initialValue);
			case EmfmappingPackage.PARAMETER_MODE_OBJECT:
				return createParameterModeObjectFromString(eDataType, initialValue);
			case EmfmappingPackage.TEMPORAL:
				return createTemporalFromString(eDataType, initialValue);
			case EmfmappingPackage.TEMPORAL_TYPE_OBJECT:
				return createTemporalTypeObjectFromString(eDataType, initialValue);
			case EmfmappingPackage.VERSION_TYPE:
				return createVersionTypeFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case EmfmappingPackage.ACCESS_TYPE:
				return convertAccessTypeToString(eDataType, instanceValue);
			case EmfmappingPackage.CONSTRAINT_MODE:
				return convertConstraintModeToString(eDataType, instanceValue);
			case EmfmappingPackage.DISCRIMINATOR_TYPE:
				return convertDiscriminatorTypeToString(eDataType, instanceValue);
			case EmfmappingPackage.ENUM_TYPE:
				return convertEnumTypeToString(eDataType, instanceValue);
			case EmfmappingPackage.FETCH_TYPE:
				return convertFetchTypeToString(eDataType, instanceValue);
			case EmfmappingPackage.GENERATION_TYPE:
				return convertGenerationTypeToString(eDataType, instanceValue);
			case EmfmappingPackage.INHERITANCE_TYPE:
				return convertInheritanceTypeToString(eDataType, instanceValue);
			case EmfmappingPackage.LOCK_MODE_TYPE:
				return convertLockModeTypeToString(eDataType, instanceValue);
			case EmfmappingPackage.PARAMETER_MODE:
				return convertParameterModeToString(eDataType, instanceValue);
			case EmfmappingPackage.TEMPORAL_TYPE:
				return convertTemporalTypeToString(eDataType, instanceValue);
			case EmfmappingPackage.ACCESS_TYPE_OBJECT:
				return convertAccessTypeObjectToString(eDataType, instanceValue);
			case EmfmappingPackage.CONSTRAINT_MODE_OBJECT:
				return convertConstraintModeObjectToString(eDataType, instanceValue);
			case EmfmappingPackage.DISCRIMINATOR_TYPE_OBJECT:
				return convertDiscriminatorTypeObjectToString(eDataType, instanceValue);
			case EmfmappingPackage.DISCRIMINATOR_VALUE:
				return convertDiscriminatorValueToString(eDataType, instanceValue);
			case EmfmappingPackage.ENUMERATED:
				return convertEnumeratedToString(eDataType, instanceValue);
			case EmfmappingPackage.ENUM_TYPE_OBJECT:
				return convertEnumTypeObjectToString(eDataType, instanceValue);
			case EmfmappingPackage.FETCH_TYPE_OBJECT:
				return convertFetchTypeObjectToString(eDataType, instanceValue);
			case EmfmappingPackage.GENERATION_TYPE_OBJECT:
				return convertGenerationTypeObjectToString(eDataType, instanceValue);
			case EmfmappingPackage.INHERITANCE_TYPE_OBJECT:
				return convertInheritanceTypeObjectToString(eDataType, instanceValue);
			case EmfmappingPackage.LOCK_MODE_TYPE_OBJECT:
				return convertLockModeTypeObjectToString(eDataType, instanceValue);
			case EmfmappingPackage.ORDER_BY:
				return convertOrderByToString(eDataType, instanceValue);
			case EmfmappingPackage.PARAMETER_MODE_OBJECT:
				return convertParameterModeObjectToString(eDataType, instanceValue);
			case EmfmappingPackage.TEMPORAL:
				return convertTemporalToString(eDataType, instanceValue);
			case EmfmappingPackage.TEMPORAL_TYPE_OBJECT:
				return convertTemporalTypeObjectToString(eDataType, instanceValue);
			case EmfmappingPackage.VERSION_TYPE:
				return convertVersionTypeToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssociationOverride createAssociationOverride() {
		AssociationOverrideImpl associationOverride = new AssociationOverrideImpl();
		return associationOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AttributeOverride createAttributeOverride() {
		AttributeOverrideImpl attributeOverride = new AttributeOverrideImpl();
		return attributeOverride;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Attributes createAttributes() {
		AttributesImpl attributes = new AttributesImpl();
		return attributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Basic createBasic() {
		BasicImpl basic = new BasicImpl();
		return basic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CascadeType createCascadeType() {
		CascadeTypeImpl cascadeType = new CascadeTypeImpl();
		return cascadeType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CollectionTable createCollectionTable() {
		CollectionTableImpl collectionTable = new CollectionTableImpl();
		return collectionTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Column createColumn() {
		ColumnImpl column = new ColumnImpl();
		return column;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ColumnResult createColumnResult() {
		ColumnResultImpl columnResult = new ColumnResultImpl();
		return columnResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ConstructorResult createConstructorResult() {
		ConstructorResultImpl constructorResult = new ConstructorResultImpl();
		return constructorResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Convert createConvert() {
		ConvertImpl convert = new ConvertImpl();
		return convert;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Converter createConverter() {
		ConverterImpl converter = new ConverterImpl();
		return converter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DiscriminatorColumn createDiscriminatorColumn() {
		DiscriminatorColumnImpl discriminatorColumn = new DiscriminatorColumnImpl();
		return discriminatorColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DocumentRoot createDocumentRoot() {
		DocumentRootImpl documentRoot = new DocumentRootImpl();
		return documentRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ElementCollection createElementCollection() {
		ElementCollectionImpl elementCollection = new ElementCollectionImpl();
		return elementCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Embeddable createEmbeddable() {
		EmbeddableImpl embeddable = new EmbeddableImpl();
		return embeddable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EmbeddableAttributes createEmbeddableAttributes() {
		EmbeddableAttributesImpl embeddableAttributes = new EmbeddableAttributesImpl();
		return embeddableAttributes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Embedded createEmbedded() {
		EmbeddedImpl embedded = new EmbeddedImpl();
		return embedded;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EmbeddedId createEmbeddedId() {
		EmbeddedIdImpl embeddedId = new EmbeddedIdImpl();
		return embeddedId;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EmptyType createEmptyType() {
		EmptyTypeImpl emptyType = new EmptyTypeImpl();
		return emptyType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Entity createEntity() {
		EntityImpl entity = new EntityImpl();
		return entity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EntityListener createEntityListener() {
		EntityListenerImpl entityListener = new EntityListenerImpl();
		return entityListener;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EntityListeners createEntityListeners() {
		EntityListenersImpl entityListeners = new EntityListenersImpl();
		return entityListeners;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EntityMappingsType createEntityMappingsType() {
		EntityMappingsTypeImpl entityMappingsType = new EntityMappingsTypeImpl();
		return entityMappingsType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EntityResult createEntityResult() {
		EntityResultImpl entityResult = new EntityResultImpl();
		return entityResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FieldResult createFieldResult() {
		FieldResultImpl fieldResult = new FieldResultImpl();
		return fieldResult;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ForeignKey createForeignKey() {
		ForeignKeyImpl foreignKey = new ForeignKeyImpl();
		return foreignKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public GeneratedValue createGeneratedValue() {
		GeneratedValueImpl generatedValue = new GeneratedValueImpl();
		return generatedValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Id createId() {
		IdImpl id = new IdImpl();
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public IdClass createIdClass() {
		IdClassImpl idClass = new IdClassImpl();
		return idClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Index createIndex() {
		IndexImpl index = new IndexImpl();
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Inheritance createInheritance() {
		InheritanceImpl inheritance = new InheritanceImpl();
		return inheritance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public JoinColumn createJoinColumn() {
		JoinColumnImpl joinColumn = new JoinColumnImpl();
		return joinColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public JoinTable createJoinTable() {
		JoinTableImpl joinTable = new JoinTableImpl();
		return joinTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Lob createLob() {
		LobImpl lob = new LobImpl();
		return lob;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ManyToMany createManyToMany() {
		ManyToManyImpl manyToMany = new ManyToManyImpl();
		return manyToMany;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ManyToOne createManyToOne() {
		ManyToOneImpl manyToOne = new ManyToOneImpl();
		return manyToOne;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MapKey createMapKey() {
		MapKeyImpl mapKey = new MapKeyImpl();
		return mapKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MapKeyClass createMapKeyClass() {
		MapKeyClassImpl mapKeyClass = new MapKeyClassImpl();
		return mapKeyClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MapKeyColumn createMapKeyColumn() {
		MapKeyColumnImpl mapKeyColumn = new MapKeyColumnImpl();
		return mapKeyColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MapKeyJoinColumn createMapKeyJoinColumn() {
		MapKeyJoinColumnImpl mapKeyJoinColumn = new MapKeyJoinColumnImpl();
		return mapKeyJoinColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MappedSuperclass createMappedSuperclass() {
		MappedSuperclassImpl mappedSuperclass = new MappedSuperclassImpl();
		return mappedSuperclass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NamedAttributeNode createNamedAttributeNode() {
		NamedAttributeNodeImpl namedAttributeNode = new NamedAttributeNodeImpl();
		return namedAttributeNode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NamedEntityGraph createNamedEntityGraph() {
		NamedEntityGraphImpl namedEntityGraph = new NamedEntityGraphImpl();
		return namedEntityGraph;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NamedNativeQuery createNamedNativeQuery() {
		NamedNativeQueryImpl namedNativeQuery = new NamedNativeQueryImpl();
		return namedNativeQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NamedQuery createNamedQuery() {
		NamedQueryImpl namedQuery = new NamedQueryImpl();
		return namedQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NamedStoredProcedureQuery createNamedStoredProcedureQuery() {
		NamedStoredProcedureQueryImpl namedStoredProcedureQuery = new NamedStoredProcedureQueryImpl();
		return namedStoredProcedureQuery;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NamedSubgraph createNamedSubgraph() {
		NamedSubgraphImpl namedSubgraph = new NamedSubgraphImpl();
		return namedSubgraph;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OneToMany createOneToMany() {
		OneToManyImpl oneToMany = new OneToManyImpl();
		return oneToMany;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OneToOne createOneToOne() {
		OneToOneImpl oneToOne = new OneToOneImpl();
		return oneToOne;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OrderColumn createOrderColumn() {
		OrderColumnImpl orderColumn = new OrderColumnImpl();
		return orderColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PersistenceUnitDefaults createPersistenceUnitDefaults() {
		PersistenceUnitDefaultsImpl persistenceUnitDefaults = new PersistenceUnitDefaultsImpl();
		return persistenceUnitDefaults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PersistenceUnitMetadata createPersistenceUnitMetadata() {
		PersistenceUnitMetadataImpl persistenceUnitMetadata = new PersistenceUnitMetadataImpl();
		return persistenceUnitMetadata;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PostLoad createPostLoad() {
		PostLoadImpl postLoad = new PostLoadImpl();
		return postLoad;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PostPersist createPostPersist() {
		PostPersistImpl postPersist = new PostPersistImpl();
		return postPersist;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PostRemove createPostRemove() {
		PostRemoveImpl postRemove = new PostRemoveImpl();
		return postRemove;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PostUpdate createPostUpdate() {
		PostUpdateImpl postUpdate = new PostUpdateImpl();
		return postUpdate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PrePersist createPrePersist() {
		PrePersistImpl prePersist = new PrePersistImpl();
		return prePersist;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PreRemove createPreRemove() {
		PreRemoveImpl preRemove = new PreRemoveImpl();
		return preRemove;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PreUpdate createPreUpdate() {
		PreUpdateImpl preUpdate = new PreUpdateImpl();
		return preUpdate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PrimaryKeyJoinColumn createPrimaryKeyJoinColumn() {
		PrimaryKeyJoinColumnImpl primaryKeyJoinColumn = new PrimaryKeyJoinColumnImpl();
		return primaryKeyJoinColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public QueryHint createQueryHint() {
		QueryHintImpl queryHint = new QueryHintImpl();
		return queryHint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SecondaryTable createSecondaryTable() {
		SecondaryTableImpl secondaryTable = new SecondaryTableImpl();
		return secondaryTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SequenceGenerator createSequenceGenerator() {
		SequenceGeneratorImpl sequenceGenerator = new SequenceGeneratorImpl();
		return sequenceGenerator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SqlResultSetMapping createSqlResultSetMapping() {
		SqlResultSetMappingImpl sqlResultSetMapping = new SqlResultSetMappingImpl();
		return sqlResultSetMapping;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StoredProcedureParameter createStoredProcedureParameter() {
		StoredProcedureParameterImpl storedProcedureParameter = new StoredProcedureParameterImpl();
		return storedProcedureParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Table createTable() {
		TableImpl table = new TableImpl();
		return table;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TableGenerator createTableGenerator() {
		TableGeneratorImpl tableGenerator = new TableGeneratorImpl();
		return tableGenerator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Transient createTransient() {
		TransientImpl transient_ = new TransientImpl();
		return transient_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UniqueConstraint createUniqueConstraint() {
		UniqueConstraintImpl uniqueConstraint = new UniqueConstraintImpl();
		return uniqueConstraint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Version createVersion() {
		VersionImpl version = new VersionImpl();
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AccessType createAccessTypeFromString(EDataType eDataType, String initialValue) {
		AccessType result = AccessType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAccessTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstraintMode createConstraintModeFromString(EDataType eDataType, String initialValue) {
		ConstraintMode result = ConstraintMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertConstraintModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiscriminatorType createDiscriminatorTypeFromString(EDataType eDataType, String initialValue) {
		DiscriminatorType result = DiscriminatorType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDiscriminatorTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumType createEnumTypeFromString(EDataType eDataType, String initialValue) {
		EnumType result = EnumType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEnumTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FetchType createFetchTypeFromString(EDataType eDataType, String initialValue) {
		FetchType result = FetchType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFetchTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GenerationType createGenerationTypeFromString(EDataType eDataType, String initialValue) {
		GenerationType result = GenerationType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertGenerationTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InheritanceType createInheritanceTypeFromString(EDataType eDataType, String initialValue) {
		InheritanceType result = InheritanceType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInheritanceTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LockModeType createLockModeTypeFromString(EDataType eDataType, String initialValue) {
		LockModeType result = LockModeType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLockModeTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterMode createParameterModeFromString(EDataType eDataType, String initialValue) {
		ParameterMode result = ParameterMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertParameterModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TemporalType createTemporalTypeFromString(EDataType eDataType, String initialValue) {
		TemporalType result = TemporalType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTemporalTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AccessType createAccessTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createAccessTypeFromString(EmfmappingPackage.Literals.ACCESS_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertAccessTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertAccessTypeToString(EmfmappingPackage.Literals.ACCESS_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstraintMode createConstraintModeObjectFromString(EDataType eDataType, String initialValue) {
		return createConstraintModeFromString(EmfmappingPackage.Literals.CONSTRAINT_MODE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertConstraintModeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertConstraintModeToString(EmfmappingPackage.Literals.CONSTRAINT_MODE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiscriminatorType createDiscriminatorTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createDiscriminatorTypeFromString(EmfmappingPackage.Literals.DISCRIMINATOR_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDiscriminatorTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertDiscriminatorTypeToString(EmfmappingPackage.Literals.DISCRIMINATOR_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createDiscriminatorValueFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDiscriminatorValueToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumType createEnumeratedFromString(EDataType eDataType, String initialValue) {
		return createEnumTypeFromString(EmfmappingPackage.Literals.ENUM_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEnumeratedToString(EDataType eDataType, Object instanceValue) {
		return convertEnumTypeToString(EmfmappingPackage.Literals.ENUM_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EnumType createEnumTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createEnumTypeFromString(EmfmappingPackage.Literals.ENUM_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEnumTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertEnumTypeToString(EmfmappingPackage.Literals.ENUM_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FetchType createFetchTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createFetchTypeFromString(EmfmappingPackage.Literals.FETCH_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertFetchTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertFetchTypeToString(EmfmappingPackage.Literals.FETCH_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GenerationType createGenerationTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createGenerationTypeFromString(EmfmappingPackage.Literals.GENERATION_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertGenerationTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertGenerationTypeToString(EmfmappingPackage.Literals.GENERATION_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InheritanceType createInheritanceTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createInheritanceTypeFromString(EmfmappingPackage.Literals.INHERITANCE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertInheritanceTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertInheritanceTypeToString(EmfmappingPackage.Literals.INHERITANCE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LockModeType createLockModeTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createLockModeTypeFromString(EmfmappingPackage.Literals.LOCK_MODE_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLockModeTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertLockModeTypeToString(EmfmappingPackage.Literals.LOCK_MODE_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createOrderByFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.STRING, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOrderByToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.STRING, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParameterMode createParameterModeObjectFromString(EDataType eDataType, String initialValue) {
		return createParameterModeFromString(EmfmappingPackage.Literals.PARAMETER_MODE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertParameterModeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertParameterModeToString(EmfmappingPackage.Literals.PARAMETER_MODE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TemporalType createTemporalFromString(EDataType eDataType, String initialValue) {
		return createTemporalTypeFromString(EmfmappingPackage.Literals.TEMPORAL_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTemporalToString(EDataType eDataType, Object instanceValue) {
		return convertTemporalTypeToString(EmfmappingPackage.Literals.TEMPORAL_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TemporalType createTemporalTypeObjectFromString(EDataType eDataType, String initialValue) {
		return createTemporalTypeFromString(EmfmappingPackage.Literals.TEMPORAL_TYPE, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertTemporalTypeObjectToString(EDataType eDataType, Object instanceValue) {
		return convertTemporalTypeToString(EmfmappingPackage.Literals.TEMPORAL_TYPE, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String createVersionTypeFromString(EDataType eDataType, String initialValue) {
		return (String)XMLTypeFactory.eINSTANCE.createFromString(XMLTypePackage.Literals.TOKEN, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVersionTypeToString(EDataType eDataType, Object instanceValue) {
		return XMLTypeFactory.eINSTANCE.convertToString(XMLTypePackage.Literals.TOKEN, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EmfmappingPackage getEmfmappingPackage() {
		return (EmfmappingPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EmfmappingPackage getPackage() {
		return EmfmappingPackage.eINSTANCE;
	}

} //EmfmappingFactoryImpl
