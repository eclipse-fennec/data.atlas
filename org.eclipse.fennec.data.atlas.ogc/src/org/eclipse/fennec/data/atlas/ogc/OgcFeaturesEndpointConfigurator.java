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
package org.eclipse.fennec.data.atlas.ogc;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataService;
import org.eclipse.fennec.data.atlas.configuration.OgcFeaturesDataServiceConfiguration;
import org.eclipse.fennec.ogc.features.api.CollectionDescriptor;
import org.eclipse.fennec.ogc.features.api.OgcFeaturesAnnotations;
import org.eclipse.fennec.persistence.repository.RepositoryConstants;
import org.eclipse.fennec.persistence.repository.api.ReadRepository;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.servlet.whiteboard.annotations.RequireHttpWhiteboard;

/**
 * OGC API Features endpoint configurator: tracks {@code OgcFeaturesDataService}
 * configuration services and the {@link ReadRepository}s realizing their
 * {@code DataInput}s (correlated by {@code persistence.repository.id} = input
 * id) and — like its OData sibling — serves nothing itself but configures the
 * Fennec OGC API Features server (emf.ogc.features) through Config Admin. One
 * {@code OgcFeaturesDataService} becomes one API root: a factory configuration
 * of the {@code OgcFeaturesServlet} mounted at {@code {urlContext}} and
 * {@code {urlContext}/*} on the Data Atlas HTTP runtime, whose
 * {@code collections} allowlist holds exactly the collections the service's
 * configurations declare (emf.ogc.features#11); per {@code DataInput} the root
 * draws from one repository feature source over the input's
 * {@code ReadRepository}, so {@code bbox}, {@code datetime} and CQL2 filters
 * push down through the same facade every other Data Atlas service kind reads
 * through (emf.ogc.features#10 made the servlet a factory); per DataSet whose
 * type carries no {@code https://eclipse.org/fennec/ogc/features} annotation
 * — or whose configuration overrides it — one configured collection
 * (emf.ogc.features#12, override-else-default); and the map viewer below the
 * root ({@code {urlContext}/viewer/}), inert without the viewer bundle.
 *
 * <p>
 * The geometry of a feature type is a containment reference to the
 * {@code Geometry} class of the GeoJSON EMF model ({@value #GEOJSON_NSURI}) —
 * named by the configuration or the annotation, else the type's single such
 * reference — and handed to the server, which serves an attribute or a
 * single-valued containment reference alike (emf.ogc.features#14). An
 * annotated type whose annotation names no geometry is therefore declared by
 * configuration, with the detected reference as its geometry.
 * </p>
 *
 * <p>
 * The feature sources and collection providers of a root are tied to its
 * servlet instance with a marker service property ({@value #SERVICE_MARKER})
 * that the servlet's {@code source.target} / {@code collectionProvider.target}
 * select, so two roots can serve the same type from different inputs under
 * different collection ids. A feature source scopes by package (nsURI), hence
 * all types of one package served by one root must come from the same input.
 * </p>
 *
 * <p>
 * Fail-early gating as everywhere: a configuration whose DataSet has no
 * resolvable input, no feature id (neither an {@code iD} attribute nor an
 * {@code idAttribute}), no geometry (neither annotated nor configured, nor a
 * single GeoJSON reference), a query (its base predicate would have to be
 * composed with the collection filters; not supported), a declaration naming
 * an attribute the type lacks, or that collides with a sibling is skipped with a loud log; a
 * root whose repositories are not (yet) available waits. Roots are torn down
 * when their service disappears (M4 lifecycle) and rebuilt when their derived
 * configuration changes.
 * </p>
 *
 * <p>
 * Deployment settings (PID {@value #PID}, optional): {@value #PROP_HTTP_TARGET}
 * — the {@code osgi.http.whiteboard.target} filter of the HTTP runtime the
 * roots mount on (the Data Atlas runtime sets {@code (id=dataAtlasHttp)});
 * {@value #PROP_HTTP_CONTEXT} — an optional servlet-context selector;
 * {@value #PROP_PUBLIC_BASE_URL} — the public base URL the roots are reachable
 * under behind a proxy (their {@code baseUrl} becomes
 * {@code <public.base.url>{urlContext}}; derived from the request when unset);
 * and any {@code ogc.*} key, passed with the prefix stripped to every servlet
 * instance this configurator creates (e.g. {@code ogc.corsOrigin},
 * {@code ogc.layerFolders}; the keys derived from the model are not
 * overridable here).
 * </p>
 */
@Component(configurationPid = OgcFeaturesEndpointConfigurator.PID,
		configurationPolicy = ConfigurationPolicy.OPTIONAL)
@RequireHttpWhiteboard
public class OgcFeaturesEndpointConfigurator {

	public static final String PID = "org.eclipse.fennec.data.atlas.ogc";
	public static final String PROP_HTTP_TARGET = "http.whiteboard.target";
	public static final String PROP_HTTP_CONTEXT = "http.whiteboard.context.select";
	public static final String PROP_PUBLIC_BASE_URL = "public.base.url";
	/** Prefix of deployment-wide servlet settings passed through to every root. */
	public static final String PASS_THROUGH_PREFIX = "ogc.";
	/** Marker property tying a root's feature sources and collection providers to its servlet instance. */
	public static final String SERVICE_MARKER = "data.atlas.ogc.service";

	// emf.ogc.features PIDs and keys (see its user guide, "Configuration")
	static final String PID_SERVLET = "org.eclipse.fennec.ogc.features.servlet";
	static final String PID_VIEWER = "org.eclipse.fennec.ogc.features.viewer";
	static final String PID_SOURCE = "org.eclipse.fennec.ogc.features.source.repository";
	static final String PID_COLLECTION = "org.eclipse.fennec.ogc.features.collection";
	private static final String KEY_SERVLET_PATTERN = "osgi.http.whiteboard.servlet.pattern";
	private static final String KEY_SERVLET_NAME = "osgi.http.whiteboard.servlet.name";
	private static final String KEY_HTTP_TARGET = "osgi.http.whiteboard.target";
	private static final String KEY_HTTP_CONTEXT = "osgi.http.whiteboard.context.select";
	private static final String KEY_TITLE = "title";
	private static final String KEY_DESCRIPTION = "description";
	private static final String KEY_DEFAULT_LIMIT = "defaultLimit";
	private static final String KEY_MAX_LIMIT = "maxLimit";
	private static final String KEY_BASE_URL = "baseUrl";
	private static final String KEY_COLLECTIONS = "collections";
	private static final String KEY_PACKAGE_TARGET = "ePackage.target";
	private static final String KEY_SOURCE_TARGET = "source.target";
	private static final String KEY_PROVIDER_TARGET = "collectionProvider.target";
	private static final String KEY_REPOSITORY_TARGET = "repository.target";
	private static final String KEY_SOURCE_PACKAGES = "nsURIs";
	private static final String KEY_COLLECTION_TYPE = "type";
	private static final String KEY_COLLECTION_ID = "id";
	private static final String KEY_COLLECTION_ID_ATTRIBUTE = "idAttribute";
	private static final String KEY_COLLECTION_GEOMETRY = "geometry";
	private static final String KEY_COLLECTION_BBOX = "bbox";
	private static final String KEY_COLLECTION_TEMPORAL = "temporal";
	private static final String KEY_COLLECTION_LAYER_GROUP = "layerGroup";
	private static final String KEY_COLLECTION_STYLE = "style";
	private static final String EMF_NSURI = "emf.nsURI";
	/** The GeoJSON EMF model whose Geometry class a feature type's geometry reference points to. */
	static final String GEOJSON_NSURI = "https://geojson.org/model/2016";
	private static final String CONFIG_NAME_PREFIX = "dataAtlas.";
	private static final Set<String> DERIVED_SERVLET_KEYS = Set.of(KEY_SERVLET_PATTERN, KEY_SERVLET_NAME,
			KEY_HTTP_TARGET, KEY_HTTP_CONTEXT, KEY_TITLE, KEY_DESCRIPTION, KEY_DEFAULT_LIMIT, KEY_MAX_LIMIT,
			KEY_BASE_URL, KEY_COLLECTIONS, KEY_PACKAGE_TARGET, KEY_SOURCE_TARGET, KEY_PROVIDER_TARGET);

	private static final Logger LOG = System.getLogger(OgcFeaturesEndpointConfigurator.class.getName());

	private final ConfigurationAdmin configAdmin;

	// all access guarded by this
	private Map<String, Object> settings;
	private final Map<String, OgcFeaturesDataService> services = new HashMap<>();
	private final Map<String, ComponentServiceObjects<ReadRepository>> repositories = new HashMap<>();
	private final Map<String, Realized> realized = new HashMap<>();

	/**
	 * One collection: a configuration resolved against its DataSet, type and
	 * input. {@code declaration} is the configured-collection factory
	 * configuration when the collection is declared by the Data Atlas (type not
	 * annotated, or overridden), {@code null} when the annotation is served as it
	 * is.
	 */
	record Collection(String configurationId, String id, EClass type, String inputId,
			Map<String, Object> declaration) {
		String nsUri() {
			return type.getEPackage().getNsURI();
		}
	}

	/** One API root: the derived shape of one OgcFeaturesDataService. */
	record Root(String id, String base, String title, String description, long defaultLimit, long maxLimit,
			List<Collection> collections) {
	}

	/** What one root is realized as: its Config Admin configurations plus their contents (for change detection). */
	private record Realized(Map<String, Dictionary<String, Object>> desired, List<Configuration> configurations) {
	}

	@Activate
	public OgcFeaturesEndpointConfigurator(@Reference ConfigurationAdmin configAdmin, Map<String, Object> settings) {
		this.configAdmin = configAdmin;
		this.settings = settings == null ? Map.of() : settings;
	}

	@Modified
	synchronized void modified(Map<String, Object> settings) {
		this.settings = settings == null ? Map.of() : settings;
		reconcile();
	}

	@Deactivate
	synchronized void deactivate() {
		realized.values().forEach(OgcFeaturesEndpointConfigurator::tearDown);
		realized.clear();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addOgcFeaturesDataService(OgcFeaturesDataService service, Map<String, Object> props) {
		String id = configObjectId(props, service.getId());
		if (id == null) {
			LOG.log(Level.WARNING, () -> "Ignoring OgcFeaturesDataService without id: " + service);
			return;
		}
		services.put(id, service);
		reconcile();
	}

	synchronized void removeOgcFeaturesDataService(OgcFeaturesDataService service, Map<String, Object> props) {
		String id = configObjectId(props, service.getId());
		if (id != null) {
			services.remove(id);
			reconcile();
		}
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addReadRepository(ComponentServiceObjects<ReadRepository> repository,
			Map<String, Object> props) {
		if (props.get(RepositoryConstants.REPOSITORY_ID) instanceof String id) {
			repositories.put(id, repository);
			reconcile();
		}
	}

	synchronized void removeReadRepository(ComponentServiceObjects<ReadRepository> repository,
			Map<String, Object> props) {
		if (props.get(RepositoryConstants.REPOSITORY_ID) instanceof String id && repositories.remove(id) != null) {
			reconcile();
		}
	}

	private String configObjectId(Map<String, Object> props, String fallback) {
		Object id = props.get(DataAtlasConstants.CONFIG_OBJECT_ID);
		return id instanceof String s ? s : fallback;
	}

	private void reconcile() {
		realized.entrySet().removeIf(entry -> {
			OgcFeaturesDataService service = services.get(entry.getKey());
			Root root = service == null ? null : resolve(service);
			if (root == null || !configurations(root).equals(entry.getValue().desired())) {
				tearDown(entry.getValue());
				LOG.log(Level.INFO, () -> "Removed OGC API Features root '" + entry.getKey() + "'");
				return true;
			}
			return false;
		});
		services.forEach((id, service) -> {
			if (realized.containsKey(id)) {
				return;
			}
			Root root = resolve(service);
			if (root == null) {
				return;
			}
			try {
				realized.put(id, realize(root));
			} catch (IOException | RuntimeException e) {
				LOG.log(Level.ERROR, () -> "Unable to configure the OGC API Features root '" + id + "': "
						+ e.getMessage(), e);
			}
		});
	}

	/**
	 * Resolves the root of a service, or {@code null} while a required
	 * {@link ReadRepository} is missing or nothing is servable. Configurations
	 * with errors are skipped with a log message (fail-early gating).
	 */
	Root resolve(OgcFeaturesDataService service) {
		List<Collection> collections = new ArrayList<>();
		Set<String> ids = new HashSet<>();
		Map<String, String> inputByPackage = new HashMap<>();
		for (OgcFeaturesDataServiceConfiguration configuration : service.getConfiguration()) {
			DataSet dataSet = configuration.getDataSet();
			if (dataSet == null) {
				LOG.log(Level.WARNING, () -> "OgcFeaturesDataServiceConfiguration '" + configuration.getId()
						+ "' has no dataSet, skipping it");
				continue;
			}
			String where = "DataSet '" + dataSet.getId() + "'";
			DataInput input = dataSet.getDataInput() != null ? dataSet.getDataInput() : service.getDataInput();
			if (input == null || input.getId() == null) {
				LOG.log(Level.WARNING, () -> where
						+ " resolves to no DataInput (neither own nor service default), skipping it");
				continue;
			}
			if (!repositories.containsKey(input.getId())) {
				return null; // required repository not (yet) available
			}
			EClass type = dataSet.getOutputType();
			if (type == null || type.getEPackage() == null || type.getEPackage().getNsURI() == null) {
				LOG.log(Level.ERROR, () -> where + ": no outputType in a namespaced package, skipping the collection");
				continue;
			}
			if (dataSet.getQuery() != null) {
				LOG.log(Level.ERROR, () -> where + ": a query-defined DataSet cannot be served as an OGC API "
						+ "Features collection (the base predicate cannot be composed with the collection filters "
						+ "yet), skipping the collection");
				continue;
			}
			Collection collection;
			try {
				collection = collection(configuration, dataSet, type, input.getId());
			} catch (IllegalArgumentException e) {
				LOG.log(Level.ERROR, () -> where + ": " + e.getMessage() + ", skipping the collection");
				continue;
			}
			if (!ids.add(collection.id())) {
				LOG.log(Level.ERROR, () -> where + ": collection id '" + collection.id() + "' is already used in "
						+ "service '" + service.getId() + "', skipping the collection");
				continue;
			}
			String previousInput = inputByPackage.putIfAbsent(collection.nsUri(), input.getId());
			if (previousInput != null && !previousInput.equals(input.getId())) {
				LOG.log(Level.ERROR, () -> where + ": package " + collection.nsUri() + " is served from input '"
						+ previousInput + "' in service '" + service.getId() + "', a second input ('" + input.getId()
						+ "') for the same package is not supported, skipping the collection");
				continue;
			}
			collections.add(collection);
		}
		if (collections.isEmpty()) {
			return null;
		}
		return new Root(service.getId(), basePath(service),
				firstNonBlank(service.getName(), service.getId()), blankToNull(service.getDescription()),
				limit(service.getDefaultLimit()), limit(service.getMaxLimit()), collections);
	}

	/**
	 * The collection of one configuration: the type's annotation served as it is
	 * when the configuration overrides nothing, else a collection declared by
	 * configuration — the annotation's values (where present) with the
	 * configuration's overrides on top, the DataSet's name and description
	 * standing in for a missing title/description. Validated through the
	 * server's own descriptor builder (feature id, attribute names, bbox shape)
	 * plus the geometry requirement. An annotated type whose annotation names no
	 * geometry is declared as well, with the geometry detected here.
	 *
	 * @throws IllegalArgumentException when the collection cannot be served
	 */
	static Collection collection(OgcFeaturesDataServiceConfiguration configuration, DataSet dataSet, EClass type,
			String inputId) {
		Optional<CollectionDescriptor> annotated = CollectionDescriptor.of(type);
		if (annotated.isPresent() && !overrides(configuration)) {
			CollectionDescriptor descriptor = annotated.get();
			if (descriptor.geometry() != null) {
				return new Collection(configuration.getId(), descriptor.id(), type, inputId, null);
			}
			// the annotation names no geometry: declared below with the detected one
		}
		List<String> bbox = configuration.getBboxFeatures().stream().map(OgcFeaturesEndpointConfigurator::blankToNull)
				.filter(Objects::nonNull).toList();
		if (!bbox.isEmpty() && bbox.size() != 4) {
			throw new IllegalArgumentException("bboxFeatures needs the four attribute names minX, minY, maxX, maxY");
		}
		Map<String, Object> declaration = new LinkedHashMap<>();
		declaration.put(KEY_COLLECTION_TYPE, type.getEPackage().getNsURI() + "#" + type.getName());
		String id = firstNonBlank(configuration.getCollectionId(), own(type, OgcFeaturesAnnotations.ID),
				type.getName());
		declaration.put(KEY_COLLECTION_ID, id);
		declaration.put(KEY_TITLE, firstNonBlank(configuration.getTitle(), own(type, OgcFeaturesAnnotations.TITLE),
				dataSet.getName(), type.getName()));
		put(declaration, KEY_DESCRIPTION,
				firstNonBlank(own(type, OgcFeaturesAnnotations.DESCRIPTION), dataSet.getDescription()));
		put(declaration, KEY_COLLECTION_ID_ATTRIBUTE,
				firstNonBlank(configuration.getIdFeature(), inherited(type, OgcFeaturesAnnotations.ID_ATTRIBUTE)));
		EStructuralFeature geometry = requireGeometry(geometryOf(type, configuration.getGeometryFeature()), type);
		declaration.put(KEY_COLLECTION_GEOMETRY, geometry.getName());
		String bboxNames = bbox.isEmpty() ? inherited(type, OgcFeaturesAnnotations.BBOX) : String.join(",", bbox);
		if (bboxNames != null) {
			declaration.put(KEY_COLLECTION_BBOX, new ArrayList<>(List.of(bboxNames.split(","))));
		}
		put(declaration, KEY_COLLECTION_TEMPORAL,
				firstNonBlank(configuration.getTemporalFeature(), inherited(type, OgcFeaturesAnnotations.TEMPORAL)));
		put(declaration, KEY_COLLECTION_LAYER_GROUP,
				firstNonBlank(configuration.getLayerGroup(), inherited(type, OgcFeaturesAnnotations.LAYER_GROUP)));
		put(declaration, KEY_COLLECTION_STYLE,
				firstNonBlank(configuration.getStyle(), own(type, OgcFeaturesAnnotations.STYLE)));

		// the server's own checks: feature id, attribute existence, bbox shape
		CollectionDescriptor descriptor = CollectionDescriptor.builder(type).id(id)
				.title((String) declaration.get(KEY_TITLE))
				.description((String) declaration.get(KEY_DESCRIPTION))
				.idAttribute((String) declaration.get(KEY_COLLECTION_ID_ATTRIBUTE))
				.geometry((String) declaration.get(KEY_COLLECTION_GEOMETRY)).bbox(bboxNames)
				.temporal((String) declaration.get(KEY_COLLECTION_TEMPORAL))
				.layerGroup((String) declaration.get(KEY_COLLECTION_LAYER_GROUP))
				.style((String) declaration.get(KEY_COLLECTION_STYLE)).build();
		return new Collection(configuration.getId(), descriptor.id(), type, inputId, declaration);
	}

	/**
	 * The geometry feature of a type: the configured name, else the annotation's
	 * (inherited) {@code geometry}, else the type's single containment reference
	 * to a GeoJSON geometry; {@code null} when there is none.
	 *
	 * @throws IllegalArgumentException when a named feature is missing or is
	 *                                  neither an attribute nor a GeoJSON reference
	 */
	static EStructuralFeature geometryOf(EClass type, String configuredName) {
		String name = firstNonBlank(configuredName, inherited(type, OgcFeaturesAnnotations.GEOMETRY));
		if (name != null) {
			EStructuralFeature feature = type.getEStructuralFeature(name);
			if (feature == null) {
				throw new IllegalArgumentException("type " + type.getName() + " has no feature '" + name + "'");
			}
			if (!(feature instanceof EAttribute) && !isGeoJsonReference(feature)) {
				throw new IllegalArgumentException("geometry feature '" + name + "' of type " + type.getName()
						+ " is neither an attribute nor a containment reference to a " + GEOJSON_NSURI + " geometry");
			}
			return feature;
		}
		List<EReference> candidates = type.getEAllReferences().stream()
				.filter(OgcFeaturesEndpointConfigurator::isGeoJsonReference).toList();
		return candidates.size() == 1 ? candidates.get(0) : null;
	}

	/** A containment reference to the Geometry class (or a subclass) of the GeoJSON EMF model. */
	static boolean isGeoJsonReference(EStructuralFeature feature) {
		return feature instanceof EReference reference && reference.isContainment()
				&& reference.getEReferenceType() != null && reference.getEReferenceType().getEPackage() != null
				&& GEOJSON_NSURI.equals(reference.getEReferenceType().getEPackage().getNsURI());
	}

	/** A collection needs a geometry. */
	private static EStructuralFeature requireGeometry(EStructuralFeature geometry, EClass type) {
		if (geometry == null) {
			throw new IllegalArgumentException("type " + type.getName() + " has no geometry (neither the "
					+ OgcFeaturesAnnotations.SOURCE + " annotation nor the configuration names one, and it has no "
					+ "containment reference to a " + GEOJSON_NSURI + " geometry)");
		}
		return geometry;
	}

	private static boolean overrides(OgcFeaturesDataServiceConfiguration configuration) {
		return blankToNull(configuration.getCollectionId()) != null || blankToNull(configuration.getTitle()) != null
				|| blankToNull(configuration.getIdFeature()) != null
				|| blankToNull(configuration.getGeometryFeature()) != null
				|| configuration.getBboxFeatures().stream().anyMatch(name -> blankToNull(name) != null)
				|| blankToNull(configuration.getTemporalFeature()) != null
				|| blankToNull(configuration.getLayerGroup()) != null || blankToNull(configuration.getStyle()) != null;
	}

	/** An annotation detail of the class itself (the non-inherited keys). */
	private static String own(EClass eClass, String key) {
		EAnnotation annotation = eClass.getEAnnotation(OgcFeaturesAnnotations.SOURCE);
		return annotation == null ? null : blankToNull(annotation.getDetails().get(key));
	}

	/** An annotation detail of the class or its nearest supertype declaring it (the inherited keys). */
	private static String inherited(EClass eClass, String key) {
		String value = own(eClass, key);
		if (value != null) {
			return value;
		}
		for (EClass superType : eClass.getEAllSuperTypes()) {
			value = own(superType, key);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	private static void put(Map<String, Object> props, String key, String value) {
		if (value != null) {
			props.put(key, value);
		}
	}

	private static String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value.trim();
	}

	private static String firstNonBlank(String... values) {
		for (String value : values) {
			if (blankToNull(value) != null) {
				return value.trim();
			}
		}
		return null;
	}

	private static long limit(BigInteger value) {
		return value == null ? -1 : value.longValue();
	}

	/** The root path: {@code urlContext} (else {@code /<id>}), leading slash, no trailing slash. */
	static String basePath(OgcFeaturesDataService service) {
		String base = service.getUrlContext() == null || service.getUrlContext().isBlank() ? "/" + service.getId()
				: service.getUrlContext().trim();
		if (!base.startsWith("/")) {
			base = "/" + base;
		}
		while (base.length() > 1 && base.endsWith("/")) {
			base = base.substring(0, base.length() - 1);
		}
		return base;
	}

	/**
	 * The Config Admin configurations a root is realized as, keyed by
	 * {@code <factoryPid>~<name>}; the same shape for the same model and
	 * settings, so it doubles as the change fingerprint. Order matters for the
	 * teardown: the servlet is created last and deleted first.
	 */
	Map<String, Dictionary<String, Object>> configurations(Root root) {
		Map<String, Dictionary<String, Object>> desired = new LinkedHashMap<>();
		String name = CONFIG_NAME_PREFIX + root.id();
		String marker = "(" + SERVICE_MARKER + "=" + root.id() + ")";

		// one repository feature source per input the root draws from, scoped
		// to the packages served from that input and tied to this root
		Map<String, List<Collection>> byInput = new LinkedHashMap<>();
		root.collections().forEach(c -> byInput.computeIfAbsent(c.inputId(), k -> new ArrayList<>()).add(c));
		byInput.forEach((inputId, collections) -> {
			Dictionary<String, Object> props = new Hashtable<>();
			props.put(KEY_REPOSITORY_TARGET, "(" + RepositoryConstants.REPOSITORY_ID + "=" + inputId + ")");
			props.put(KEY_SOURCE_PACKAGES, distinct(collections.stream().map(Collection::nsUri).toList()));
			props.put(SERVICE_MARKER, root.id());
			props.put(DataAtlasConstants.CONFIG_OBJECT_ID, root.id());
			desired.put(PID_SOURCE + "~" + name + "." + inputId, props);
		});

		// the collections declared by configuration, tied to this root
		for (Collection collection : root.collections()) {
			if (collection.declaration() == null) {
				continue;
			}
			Dictionary<String, Object> props = new Hashtable<>();
			collection.declaration().forEach(props::put);
			props.put(SERVICE_MARKER, root.id());
			props.put(DataAtlasConstants.CONFIG_OBJECT_ID, root.id());
			desired.put(PID_COLLECTION + "~" + name + "." + collection.configurationId(), props);
		}

		Dictionary<String, Object> viewer = new Hashtable<>();
		viewer.put(KEY_SERVLET_PATTERN, new ArrayList<>(List.of(root.base() + "/viewer", root.base() + "/viewer/*")));
		viewer.put(KEY_SERVLET_NAME, name + ".viewer");
		whiteboard(viewer);
		viewer.put(DataAtlasConstants.CONFIG_OBJECT_ID, root.id());
		desired.put(PID_VIEWER + "~" + name, viewer);

		Dictionary<String, Object> servlet = new Hashtable<>();
		servlet.put(KEY_SERVLET_PATTERN, new ArrayList<>(List.of(root.base(), root.base() + "/*")));
		servlet.put(KEY_SERVLET_NAME, name);
		whiteboard(servlet);
		passThrough(servlet);
		servlet.put(KEY_TITLE, root.title());
		if (root.description() != null) {
			servlet.put(KEY_DESCRIPTION, root.description());
		}
		if (root.defaultLimit() > 0) {
			servlet.put(KEY_DEFAULT_LIMIT, (int) Math.min(Integer.MAX_VALUE, root.defaultLimit()));
		}
		if (root.maxLimit() > 0) {
			servlet.put(KEY_MAX_LIMIT, (int) Math.min(Integer.MAX_VALUE, root.maxLimit()));
		}
		if (settings.get(PROP_PUBLIC_BASE_URL) instanceof String publicBase && !publicBase.isBlank()) {
			String trimmed = publicBase.trim();
			while (trimmed.endsWith("/")) {
				trimmed = trimmed.substring(0, trimmed.length() - 1);
			}
			servlet.put(KEY_BASE_URL, trimmed + root.base());
		}
		servlet.put(KEY_COLLECTIONS, new ArrayList<>(root.collections().stream().map(Collection::id).toList()));
		List<String> packages = distinct(root.collections().stream().map(Collection::nsUri).toList());
		servlet.put(KEY_PACKAGE_TARGET, packages.size() == 1 ? "(" + EMF_NSURI + "=" + packages.get(0) + ")"
				: packages.stream().map(uri -> "(" + EMF_NSURI + "=" + uri + ")").collect(Collectors.joining("", "(|", ")")));
		servlet.put(KEY_SOURCE_TARGET, marker);
		servlet.put(KEY_PROVIDER_TARGET, marker);
		servlet.put(DataAtlasConstants.CONFIG_OBJECT_ID, root.id());
		desired.put(PID_SERVLET + "~" + name, servlet);
		return desired;
	}

	private void whiteboard(Dictionary<String, Object> props) {
		if (settings.get(PROP_HTTP_TARGET) instanceof String target && !target.isBlank()) {
			props.put(KEY_HTTP_TARGET, target.trim());
		}
		if (settings.get(PROP_HTTP_CONTEXT) instanceof String context && !context.isBlank()) {
			props.put(KEY_HTTP_CONTEXT, context.trim());
		}
	}

	/** Deployment-wide {@code ogc.*} servlet settings (prefix stripped), except what the model derives. */
	private void passThrough(Dictionary<String, Object> props) {
		settings.forEach((key, value) -> {
			if (key.startsWith(PASS_THROUGH_PREFIX) && value != null) {
				String servletKey = key.substring(PASS_THROUGH_PREFIX.length());
				if (!servletKey.isBlank() && !DERIVED_SERVLET_KEYS.contains(servletKey)) {
					props.put(servletKey, value);
				}
			}
		});
	}

	private static List<String> distinct(List<String> values) {
		return new ArrayList<>(new LinkedHashSet<>(values));
	}

	private Realized realize(Root root) throws IOException {
		Map<String, Dictionary<String, Object>> desired = configurations(root);
		List<Configuration> configurations = new ArrayList<>();
		try {
			for (Map.Entry<String, Dictionary<String, Object>> entry : desired.entrySet()) {
				int separator = entry.getKey().indexOf('~');
				Configuration configuration = configAdmin.getFactoryConfiguration(
						entry.getKey().substring(0, separator), entry.getKey().substring(separator + 1), "?");
				configuration.update(entry.getValue());
				configurations.add(configuration);
			}
		} catch (IOException | RuntimeException e) {
			tearDown(new Realized(desired, configurations));
			throw e;
		}
		LOG.log(Level.INFO, () -> "Configured OGC API Features root '" + root.id() + "' at '" + root.base()
				+ "' with " + root.collections().size() + " collection(s): "
				+ root.collections().stream().map(c -> c.id() + " <- " + c.inputId()
						+ (c.declaration() == null ? " (annotated)" : " (declared)")).toList());
		return new Realized(desired, configurations);
	}

	private static void tearDown(Realized realized) {
		// servlet first, so no request reaches a root whose sources are gone
		List<Configuration> reversed = new ArrayList<>(realized.configurations());
		Collections.reverse(reversed);
		for (Configuration configuration : reversed) {
			try {
				configuration.delete();
			} catch (IOException | RuntimeException e) {
				LOG.log(Level.WARNING, () -> "Unable to delete configuration " + configuration.getPid(), e);
			}
		}
	}
}
