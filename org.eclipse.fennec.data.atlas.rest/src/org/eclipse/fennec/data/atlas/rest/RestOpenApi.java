/**
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
 */
package org.eclipse.fennec.data.atlas.rest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.RestDataService;
import org.eclipse.fennec.data.atlas.configuration.RestDataServiceConfiguration;
import org.eclipse.fennec.data.atlas.rest.DataServiceResource.DataSetEndpoint;
import org.eclipse.fennec.model.openapi.Components;
import org.eclipse.fennec.model.openapi.HttpMethod;
import org.eclipse.fennec.model.openapi.Info;
import org.eclipse.fennec.model.openapi.MediaType;
import org.eclipse.fennec.model.openapi.OpenAPI;
import org.eclipse.fennec.model.openapi.OpenApiFactory;
import org.eclipse.fennec.model.openapi.Operation;
import org.eclipse.fennec.model.openapi.Parameter;
import org.eclipse.fennec.model.openapi.ParameterLocation;
import org.eclipse.fennec.model.openapi.PathItem;
import org.eclipse.fennec.model.openapi.Response;
import org.eclipse.fennec.model.openapi.Schema;
import org.eclipse.fennec.model.query.ParameterDecl;

/**
 * The OpenAPI 3 description of one {@code RestDataService}, built on the
 * OpenAPI model of the fennec codec and serialized by its OpenAPI resource
 * factory.
 *
 * <p>
 * Every served DataSet contributes its list path (with the configured
 * offset/limit parameters and the parameters its canonical query declares) and
 * its by-id path, each answering in exactly the media types the DataSet is
 * served as. The JSON response schemas are the EClasses of the served objects:
 * the codec writes {@code components/schemas} from an {@code EPackage}, so the
 * served types — together with every type they reach through supertypes and
 * references — are copied into one schema package and referenced by name.
 * </p>
 */
final class RestOpenApi {

	/** The OpenAPI version the codec's model implements. */
	static final String OPENAPI_VERSION = "3.0.3";

	private static final String GENMODEL_SOURCE = "http://www.eclipse.org/emf/2002/GenModel";
	private static final String SCHEMA_REF_PREFIX = "#/components/schemas/";

	private final OpenAPI template;

	private RestOpenApi(OpenAPI template) {
		this.template = template;
	}

	/**
	 * Describes {@code service} as served through {@code endpoints}.
	 *
	 * @param id        the service id, the fallback title
	 * @param service   the service configuration
	 * @param endpoints the served DataSets by path, as resolved by the configurator
	 * @return the description, to be rendered per request
	 */
	static RestOpenApi describe(String id, RestDataService service, Map<String, DataSetEndpoint> endpoints) {
		OpenApiFactory factory = OpenApiFactory.eINSTANCE;
		OpenAPI openApi = factory.createOpenAPI();
		openApi.setOpenapi(OPENAPI_VERSION);
		Info info = factory.createInfo();
		info.setTitle(service.getName() != null ? service.getName() : id);
		info.setDescription(service.getDescription());
		info.setVersion("1.0.0");
		openApi.setInfo(info);

		Set<EClass> served = new LinkedHashSet<>();
		endpoints.values().forEach(endpoint -> served.add(endpoint.dataSet().getInputType()));
		SchemaPackage schemas = SchemaPackage.of(id, served);

		endpoints.forEach((path, endpoint) -> {
			String ref = SCHEMA_REF_PREFIX + schemas.nameOf(endpoint.dataSet().getInputType());
			openApi.getPaths().put("/" + path, listPath(path, endpoint, ref));
			openApi.getPaths().put("/" + path + "/{id}", byIdPath(path, endpoint, ref));
		});

		Components components = factory.createComponents();
		components.setSchemasPackage(schemas.ePackage());
		openApi.setComponents(components);
		return new RestOpenApi(openApi);
	}

	/**
	 * Renders the description as an OpenAPI JSON document.
	 *
	 * @param resourceFactory the codec's OpenAPI resource factory
	 * @param serverUrl       the URL the application is reached at, the single
	 *                        {@code servers} entry
	 * @return the JSON document
	 * @throws IOException if the codec fails to write it
	 */
	String render(Resource.Factory resourceFactory, String serverUrl) throws IOException {
		OpenAPI openApi = EcoreUtil.copy(template);
		org.eclipse.fennec.model.openapi.Server server = OpenApiFactory.eINSTANCE.createServer();
		server.setUrl(serverUrl);
		openApi.getServers().add(server);
		Resource resource = resourceFactory.createResource(URI.createURI("dataatlas:/openapi.json"));
		resource.getContents().add(openApi);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		resource.save(out, null);
		return out.toString(StandardCharsets.UTF_8);
	}

	private static PathItem listPath(String path, DataSetEndpoint endpoint, String ref) {
		DataSet dataSet = endpoint.dataSet();
		RestDataServiceConfiguration configuration = endpoint.configuration();
		Operation get = operation("list_" + path, summary(dataSet, "All objects of"), description(dataSet));
		if (configuration.getOffsetParameterName() != null) {
			get.getParameters().add(queryParameter(configuration.getOffsetParameterName(),
					"Number of objects to skip", integer(), false));
		}
		if (configuration.getLimitParameterName() != null) {
			String limit = configuration.getBatchSizeLimit() != null && configuration.getBatchSizeLimit().signum() >= 0
					? "Maximum number of objects to return, at most " + configuration.getBatchSizeLimit()
					: "Maximum number of objects to return";
			get.getParameters().add(queryParameter(configuration.getLimitParameterName(), limit, integer(), false));
		}
		if (dataSet.getQuery() != null) {
			for (ParameterDecl declaration : dataSet.getQuery().getParameters()) {
				get.getParameters().add(queryParameter(declaration.getName(),
						"Parameter of the data set's query", schemaOf(declaration), true));
			}
		}
		Schema array = OpenApiFactory.eINSTANCE.createSchema();
		array.setType("array");
		array.setItems(ref(ref));
		get.getResponses().put("200", response("The objects of the data set", endpoint, array));
		get.getResponses().put("400", plain("A query parameter is missing or malformed"));
		get.getResponses().put("406", plain("None of the accepted media types is served"));
		PathItem item = OpenApiFactory.eINSTANCE.createPathItem();
		item.setGet(get);
		return item;
	}

	private static PathItem byIdPath(String path, DataSetEndpoint endpoint, String ref) {
		DataSet dataSet = endpoint.dataSet();
		Operation get = operation("get_" + path, summary(dataSet, "One object of"), null);
		Parameter id = OpenApiFactory.eINSTANCE.createParameter();
		id.setName("id");
		id.setIn(ParameterLocation.PATH);
		id.setRequired(true);
		id.setDescription("The id of the object");
		id.setSchema(type("string"));
		get.getParameters().add(id);
		get.getResponses().put("200", response("The object", endpoint, ref(ref)));
		get.getResponses().put("404", plain("No object with this id"));
		get.getResponses().put("406", plain("None of the accepted media types is served"));
		PathItem item = OpenApiFactory.eINSTANCE.createPathItem();
		item.setGet(get);
		return item;
	}

	private static Operation operation(String operationId, String summary, String description) {
		Operation operation = OpenApiFactory.eINSTANCE.createOperation();
		operation.setMethod(HttpMethod.GET);
		operation.setOperationId(operationId);
		operation.setSummary(summary);
		operation.setDescription(description);
		return operation;
	}

	/**
	 * A response in every media type of the DataSet; only JSON carries the
	 * schema — the other formats (XML, CSV) are not shaped by it.
	 */
	private static Response response(String description, DataSetEndpoint endpoint, Schema jsonSchema) {
		Response response = OpenApiFactory.eINSTANCE.createResponse();
		response.setDescription(description);
		for (jakarta.ws.rs.core.MediaType mediaType : endpoint.formats().mediaTypes()) {
			MediaType content = OpenApiFactory.eINSTANCE.createMediaType();
			if (jakarta.ws.rs.core.MediaType.APPLICATION_JSON_TYPE.isCompatible(mediaType)) {
				content.setSchema(EcoreUtil.copy(jsonSchema));
			}
			response.getContent().put(mediaType.getType() + "/" + mediaType.getSubtype(), content);
		}
		return response;
	}

	private static Response plain(String description) {
		Response response = OpenApiFactory.eINSTANCE.createResponse();
		response.setDescription(description);
		return response;
	}

	private static Parameter queryParameter(String name, String description, Schema schema, boolean required) {
		Parameter parameter = OpenApiFactory.eINSTANCE.createParameter();
		parameter.setName(name);
		parameter.setIn(ParameterLocation.QUERY);
		parameter.setDescription(description);
		parameter.setRequired(required);
		parameter.setSchema(schema);
		return parameter;
	}

	/** The schema of a query parameter, derived from its declared type hint. */
	private static Schema schemaOf(ParameterDecl declaration) {
		if (!(declaration.getTypeHint() instanceof EDataType dataType) || dataType.getInstanceClass() == null) {
			return type("string");
		}
		Class<?> type = dataType.getInstanceClass();
		if (type == int.class || type == Integer.class || type == short.class || type == Short.class
				|| type == byte.class || type == Byte.class) {
			return integer();
		}
		if (type == long.class || type == Long.class || type == java.math.BigInteger.class) {
			Schema schema = type("integer");
			schema.setFormat("int64");
			return schema;
		}
		if (type == double.class || type == Double.class || type == float.class || type == Float.class
				|| type == java.math.BigDecimal.class) {
			return type("number");
		}
		if (type == boolean.class || type == Boolean.class) {
			return type("boolean");
		}
		if (type == java.util.Date.class) {
			Schema schema = type("string");
			schema.setFormat("date-time");
			return schema;
		}
		return type("string");
	}

	private static Schema integer() {
		Schema schema = type("integer");
		schema.setFormat("int32");
		return schema;
	}

	private static Schema type(String type) {
		Schema schema = OpenApiFactory.eINSTANCE.createSchema();
		schema.setType(type);
		return schema;
	}

	private static Schema ref(String ref) {
		Schema schema = OpenApiFactory.eINSTANCE.createSchema();
		schema.setRef(ref);
		return schema;
	}

	private static String summary(DataSet dataSet, String prefix) {
		String name = dataSet.getName() != null ? dataSet.getName() : dataSet.getId();
		return prefix + " " + name;
	}

	/**
	 * The DataSet's description, else the GenModel documentation of its type —
	 * the same override-else-default rule the DCAT publication follows.
	 */
	private static String description(DataSet dataSet) {
		if (dataSet.getDescription() != null && !dataSet.getDescription().isBlank()) {
			return dataSet.getDescription();
		}
		EClass type = dataSet.getInputType();
		EAnnotation annotation = type == null ? null : type.getEAnnotation(GENMODEL_SOURCE);
		return annotation == null ? null : annotation.getDetails().get("documentation");
	}

	/**
	 * The schema package of a service: copies of the served types and of every
	 * type they reach, gathered into one {@code EPackage} — the shape the
	 * codec's OpenAPI writer turns into {@code components/schemas}.
	 */
	private record SchemaPackage(EPackage ePackage, Map<EClass, String> names) {

		static SchemaPackage of(String serviceId, Collection<EClass> served) {
			Set<EPackage> packages = reachablePackages(served);
			EcoreUtil.Copier copier = new EcoreUtil.Copier();
			Collection<EPackage> copies = copier.copyAll(packages);
			copier.copyReferences();
			EPackage target;
			if (copies.size() == 1) {
				target = copies.iterator().next();
			} else {
				target = EcoreFactory.eINSTANCE.createEPackage();
				target.setName("schemas");
				target.setNsPrefix("schemas");
				target.setNsURI("urn:dataatlas:openapi:" + serviceId);
				Set<String> taken = new HashSet<>();
				for (EPackage copy : copies) {
					for (EClassifier classifier : List.copyOf(copy.getEClassifiers())) {
						if (!taken.add(classifier.getName())) {
							// same name in two packages: qualify the later one
							classifier.setName(copy.getName() + "_" + classifier.getName());
							taken.add(classifier.getName());
						}
						target.getEClassifiers().add(classifier);
					}
				}
			}
			Map<EClass, String> names = new java.util.HashMap<>();
			for (EClass type : served) {
				if (copier.get(type) instanceof EClass copy) {
					names.put(type, copy.getName());
				}
			}
			return new SchemaPackage(target, names);
		}

		String nameOf(EClass type) {
			return names.getOrDefault(type, type.getName());
		}

		/**
		 * The packages of the served types and of every type reachable from them
		 * through supertypes and references; Ecore itself is left out — its
		 * data types are written as JSON primitives.
		 */
		private static Set<EPackage> reachablePackages(Collection<EClass> served) {
			Set<EPackage> packages = new LinkedHashSet<>();
			Set<EClass> seen = new HashSet<>();
			Deque<EClass> todo = new ArrayDeque<>(served);
			while (!todo.isEmpty()) {
				EClass type = todo.pop();
				if (!seen.add(type) || type.getEPackage() == null || type.getEPackage() == EcorePackage.eINSTANCE) {
					continue;
				}
				if (packages.add(type.getEPackage())) {
					type.getEPackage().getEClassifiers().stream().filter(EClass.class::isInstance)
							.map(EClass.class::cast).forEach(todo::add);
				}
				todo.addAll(type.getESuperTypes());
				for (EReference reference : type.getEReferences()) {
					todo.add(reference.getEReferenceType());
				}
			}
			return packages;
		}
	}
}
