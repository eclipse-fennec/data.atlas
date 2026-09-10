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
package org.eclipse.fennec.data.atlas.odata;

import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.fennec.data.atlas.api.DataAtlasConstants;
import org.eclipse.fennec.data.atlas.configuration.DataInput;
import org.eclipse.fennec.data.atlas.configuration.DataSet;
import org.eclipse.fennec.data.atlas.configuration.ODataDataService;
import org.eclipse.fennec.data.atlas.configuration.ODataDataServiceConfiguration;
import org.eclipse.fennec.persistence.helper.CompositeIds;
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
 * OData endpoint configurator: tracks {@code ODataDataService} configuration
 * services and the {@link ReadRepository}s realizing their {@code DataInput}s
 * (correlated by {@code persistence.repository.id} = input id), like its REST
 * and GeoJSON siblings — but instead of serving itself it configures the
 * Fennec OData server (emf.odata) through Config Admin. One
 * {@code ODataDataService} becomes one OData <em>service root</em> (OData 4.01
 * Part 2 §3): a factory configuration of the {@code ODataServlet} mounted at
 * {@code {urlContext}/*} on the Data Atlas HTTP runtime, publishing exactly
 * the entity sets the service's configurations declare (package and
 * entity-set allowlist, emf.odata#77/#78), guarded by its own request-limits
 * filter instance, and — per {@code DataInput} the service draws from — one
 * repository-backed {@code QueryService} (emf.odata#79) over the input's
 * {@code ReadRepository}, so {@code $filter}/{@code $orderby}/paging/
 * {@code $count}/{@code $apply} are pushed down through the same facade every
 * other Data Atlas service kind reads through (JPA inputs push into the
 * database; file and bridge inputs evaluate in memory).
 *
 * <p>
 * The backends of a root are tied to it with a marker service property
 * ({@value #SERVICE_MARKER}) that the servlet instance's
 * {@code QueryService.target} selects, so two roots can serve the same entity
 * type from different inputs. Within one root OData addresses an entity set by
 * type, hence one configuration per {@code outputType}; and because the
 * repository backend scopes by package (nsURI), all types of one package served
 * by one root must come from the same input.
 * </p>
 *
 * <p>
 * Fail-early gating as everywhere: a configuration whose DataSet has no
 * resolvable input, no identity (no {@code iD} attribute and no
 * {@code idFeatures} annotation — OData needs a key), a query (its base
 * predicate would have to be composed with {@code $filter}; not supported), or
 * that collides with a sibling is skipped with a loud log; a root whose
 * repositories are not (yet) available waits. Roots are torn down when their
 * service disappears (M4 lifecycle) and rebuilt when their derived
 * configuration changes.
 * </p>
 *
 * <p>
 * Deployment settings (PID {@value #PID}, optional): {@value #PROP_HTTP_TARGET}
 * — the {@code osgi.http.whiteboard.target} filter of the HTTP runtime the
 * roots mount on (the Data Atlas runtime sets {@code (id=dataAtlasHttp)});
 * {@value #PROP_HTTP_CONTEXT} — an optional servlet-context selector; and any
 * {@code odata.*} limit or CORS key of the servlet PID, passed through to every
 * root this configurator creates (a root's own {@code odata.model.*} keys and
 * {@code odata.max.top}, derived from the model, are not overridable here).
 * </p>
 */
@Component(immediate = true, configurationPid = ODataEndpointConfigurator.PID,
		configurationPolicy = ConfigurationPolicy.OPTIONAL)
@RequireHttpWhiteboard
public class ODataEndpointConfigurator {

	public static final String PID = "org.eclipse.fennec.data.atlas.odata";
	public static final String PROP_HTTP_TARGET = "http.whiteboard.target";
	public static final String PROP_HTTP_CONTEXT = "http.whiteboard.context.select";
	/** Marker property tying a root's backends to its servlet instance. */
	public static final String SERVICE_MARKER = "data.atlas.odata.service";

	// emf.odata PIDs and keys (see its manual, "Configuration & Security")
	static final String PID_SERVLET = "org.eclipse.fennec.odata.servlet";
	static final String PID_FILTER = "org.eclipse.fennec.odata.request.filter";
	static final String PID_BACKEND = "org.eclipse.fennec.odata.persistence.repository";
	private static final String KEY_SERVLET_PATTERN = "osgi.http.whiteboard.servlet.pattern";
	private static final String KEY_FILTER_PATTERN = "osgi.http.whiteboard.filter.pattern";
	private static final String KEY_HTTP_TARGET = "osgi.http.whiteboard.target";
	private static final String KEY_HTTP_CONTEXT = "osgi.http.whiteboard.context.select";
	private static final String KEY_MODEL_PACKAGES = "odata.model.packages";
	private static final String KEY_MODEL_ENTITY_SETS = "odata.model.entitysets";
	private static final String KEY_MAX_TOP = "odata.max.top";
	private static final String KEY_QUERY_SERVICE_TARGET = "QueryService.target";
	/**
	 * The backend's DS reference is declared by {@code setRepository(...)} without an
	 * explicit name, so its target property is {@code Repository.target} — not the
	 * lower-case key the emf.odata manual documents, which DS ignores (the backend then
	 * binds an arbitrary repository; emf.odata#82). Config Admin keys are
	 * case-insensitive, so exactly one spelling can be set: the one that works. Config
	 * Admin's case-insensitive lookup also keeps it working should upstream rename the
	 * reference to {@code repository}.
	 */
	private static final String KEY_REPOSITORY_TARGET = "Repository.target";
	private static final String KEY_BACKEND_PACKAGES = "emf.nsURIs";
	private static final String KEY_BACKEND_PAGE_SIZE = "max.page.size";
	private static final String CONFIG_NAME_PREFIX = "dataAtlas.";

	private static final Logger LOG = System.getLogger(ODataEndpointConfigurator.class.getName());

	private final ConfigurationAdmin configAdmin;

	// all access guarded by this
	private Map<String, Object> settings;
	private final Map<String, ODataDataService> services = new HashMap<>();
	private final Map<String, ComponentServiceObjects<ReadRepository>> repositories = new HashMap<>();
	private final Map<String, Realized> realized = new HashMap<>();

	/** One entity set: a configuration resolved against its DataSet, type and input. */
	record EntitySet(String configurationId, String name, EClass type, String inputId, long limit) {
		String nsUri() {
			return type.getEPackage().getNsURI();
		}
	}

	/** One OData service root: the derived shape of one ODataDataService. */
	record ServiceRoot(String id, String base, List<EntitySet> sets) {
	}

	/** What one root is realized as: its Config Admin configurations plus their contents (for change detection). */
	private record Realized(Map<String, Dictionary<String, Object>> desired, List<Configuration> configurations) {
	}

	@Activate
	public ODataEndpointConfigurator(@Reference ConfigurationAdmin configAdmin, Map<String, Object> settings) {
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
		realized.values().forEach(ODataEndpointConfigurator::tearDown);
		realized.clear();
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	synchronized void addODataDataService(ODataDataService service, Map<String, Object> props) {
		String id = configObjectId(props, service.getId());
		if (id == null) {
			LOG.log(Level.WARNING, () -> "Ignoring ODataDataService without id: " + service);
			return;
		}
		services.put(id, service);
		reconcile();
	}

	synchronized void removeODataDataService(ODataDataService service, Map<String, Object> props) {
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
			ODataDataService service = services.get(entry.getKey());
			ServiceRoot root = service == null ? null : resolve(service);
			if (root == null || !configurations(root).equals(entry.getValue().desired())) {
				tearDown(entry.getValue());
				LOG.log(Level.INFO, () -> "Removed OData service root '" + entry.getKey() + "'");
				return true;
			}
			return false;
		});
		services.forEach((id, service) -> {
			if (realized.containsKey(id)) {
				return;
			}
			ServiceRoot root = resolve(service);
			if (root == null) {
				return;
			}
			try {
				realized.put(id, realize(root));
			} catch (IOException | RuntimeException e) {
				LOG.log(Level.ERROR, () -> "Unable to configure the OData service root '" + id + "': " + e.getMessage(),
						e);
			}
		});
	}

	/**
	 * Resolves the root of a service, or {@code null} while a required
	 * {@link ReadRepository} is missing or nothing is servable. Configurations
	 * with errors are skipped with a log message (fail-early gating).
	 */
	ServiceRoot resolve(ODataDataService service) {
		List<EntitySet> sets = new ArrayList<>();
		Set<String> names = new HashSet<>();
		Set<String> types = new HashSet<>();
		Map<String, String> inputByPackage = new HashMap<>();
		for (ODataDataServiceConfiguration configuration : service.getConfiguration()) {
			DataSet dataSet = configuration.getDataSet();
			if (dataSet == null) {
				LOG.log(Level.WARNING, () -> "ODataDataServiceConfiguration '" + configuration.getId()
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
				LOG.log(Level.ERROR, () -> where + ": no outputType in a namespaced package, skipping the entity set");
				continue;
			}
			if (dataSet.getQuery() != null) {
				LOG.log(Level.ERROR, () -> where + ": a query-defined DataSet cannot be served as OData (the base "
						+ "predicate cannot be composed with $filter yet), skipping the entity set");
				continue;
			}
			if (CompositeIds.idAttributes(type).isEmpty()) {
				LOG.log(Level.ERROR, () -> where + ": type " + typeName(type) + " has no identity (neither an iD "
						+ "attribute nor an idFeatures annotation) - OData needs an entity key, skipping the entity set");
				continue;
			}
			String name = configuration.getEntitySetName() == null || configuration.getEntitySetName().isBlank()
					? type.getName()
					: configuration.getEntitySetName().trim();
			if (!names.add(name)) {
				LOG.log(Level.ERROR, () -> where + ": entity set name '" + name + "' is already used in service '"
						+ service.getId() + "', skipping the entity set");
				continue;
			}
			if (!types.add(typeName(type))) {
				LOG.log(Level.ERROR, () -> where + ": type " + typeName(type) + " is already served by service '"
						+ service.getId() + "' (OData addresses an entity set by type), skipping the entity set");
				continue;
			}
			String previousInput = inputByPackage.putIfAbsent(type.getEPackage().getNsURI(), input.getId());
			if (previousInput != null && !previousInput.equals(input.getId())) {
				LOG.log(Level.ERROR, () -> where + ": package " + type.getEPackage().getNsURI() + " is served from "
						+ "input '" + previousInput + "' in service '" + service.getId() + "', a second input ('"
						+ input.getId() + "') for the same package is not supported, skipping the entity set");
				continue;
			}
			BigInteger limit = configuration.getBatchSizeLimit();
			sets.add(new EntitySet(configuration.getId(), name, type, input.getId(),
					limit == null ? -1 : limit.longValue()));
		}
		if (sets.isEmpty()) {
			return null;
		}
		return new ServiceRoot(service.getId(), basePath(service), sets);
	}

	/** The service root path: {@code urlContext} (else {@code /<id>}), leading slash, no trailing slash. */
	static String basePath(ODataDataService service) {
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

	private static String typeName(EClass type) {
		return type.getEPackage().getNsURI() + "#" + type.getName();
	}

	/**
	 * The Config Admin configurations a root is realized as, keyed by
	 * {@code <factoryPid>~<name>}; the same shape for the same model and
	 * settings, so it doubles as the change fingerprint.
	 */
	Map<String, Dictionary<String, Object>> configurations(ServiceRoot root) {
		Map<String, Dictionary<String, Object>> desired = new LinkedHashMap<>();
		String name = CONFIG_NAME_PREFIX + root.id();

		// one repository backend per input the root draws from, scoped to the
		// packages served from that input and tied to this root
		Map<String, List<EntitySet>> byInput = new LinkedHashMap<>();
		root.sets().forEach(set -> byInput.computeIfAbsent(set.inputId(), k -> new ArrayList<>()).add(set));
		byInput.forEach((inputId, sets) -> {
			Dictionary<String, Object> props = new Hashtable<>();
			String repositoryFilter = "(" + RepositoryConstants.REPOSITORY_ID + "=" + inputId + ")";
			props.put(KEY_REPOSITORY_TARGET, repositoryFilter);
			props.put(KEY_BACKEND_PACKAGES, distinct(sets.stream().map(EntitySet::nsUri).toList()));
			long limit = smallestLimit(sets);
			if (limit > 0) {
				props.put(KEY_BACKEND_PAGE_SIZE, limit);
			}
			props.put(SERVICE_MARKER, root.id());
			props.put(DataAtlasConstants.CONFIG_OBJECT_ID, root.id());
			desired.put(PID_BACKEND + "~" + name + "." + inputId, props);
		});

		Dictionary<String, Object> filter = new Hashtable<>();
		filter.put(KEY_FILTER_PATTERN, root.base() + "/*");
		whiteboard(filter);
		passThrough(filter);
		filter.put(DataAtlasConstants.CONFIG_OBJECT_ID, root.id());
		desired.put(PID_FILTER + "~" + name, filter);

		Dictionary<String, Object> servlet = new Hashtable<>();
		servlet.put(KEY_SERVLET_PATTERN, root.base() + "/*");
		whiteboard(servlet);
		passThrough(servlet);
		servlet.put(KEY_MODEL_PACKAGES, distinct(root.sets().stream().map(EntitySet::nsUri).toList()));
		servlet.put(KEY_MODEL_ENTITY_SETS, new ArrayList<>(root.sets().stream()
				.map(set -> set.name() + "=" + set.nsUri() + "#" + set.type().getName()).toList()));
		servlet.put(KEY_QUERY_SERVICE_TARGET, "(" + SERVICE_MARKER + "=" + root.id() + ")");
		long limit = smallestLimit(root.sets());
		if (limit > 0) {
			servlet.put(KEY_MAX_TOP, limit);
		}
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

	/** Deployment-wide {@code odata.*} limits/CORS, except what the model derives. */
	private void passThrough(Dictionary<String, Object> props) {
		settings.forEach((key, value) -> {
			if (key.startsWith("odata.") && !key.startsWith("odata.model.") && !KEY_MAX_TOP.equals(key)
					&& value != null) {
				props.put(key, value);
			}
		});
	}

	private static long smallestLimit(List<EntitySet> sets) {
		return sets.stream().mapToLong(EntitySet::limit).filter(limit -> limit > 0).min().orElse(-1);
	}

	private static List<String> distinct(List<String> values) {
		return new ArrayList<>(new LinkedHashSet<>(values));
	}

	private Realized realize(ServiceRoot root) throws IOException {
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
		LOG.log(Level.INFO, () -> "Configured OData service root '" + root.id() + "' at '" + root.base()
				+ "' with " + root.sets().size() + " entity set(s): "
				+ root.sets().stream().map(set -> set.name() + " <- " + set.inputId()).toList());
		return new Realized(desired, configurations);
	}

	private static void tearDown(Realized realized) {
		// servlet first, so no request reaches a root whose backend is gone
		List<Configuration> reversed = new ArrayList<>(realized.configurations());
		java.util.Collections.reverse(reversed);
		for (Configuration configuration : reversed) {
			try {
				configuration.delete();
			} catch (IOException | RuntimeException e) {
				LOG.log(Level.WARNING, () -> "Unable to delete configuration " + configuration.getPid(), e);
			}
		}
	}
}
