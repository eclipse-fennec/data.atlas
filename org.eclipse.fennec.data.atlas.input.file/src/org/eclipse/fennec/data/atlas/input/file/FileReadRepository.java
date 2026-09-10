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
package org.eclipse.fennec.data.atlas.input.file;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.configuration.FileDataInput;
import org.eclipse.fennec.emf.osgi.ResourceSetFactory;
import org.eclipse.fennec.model.query.ParameterDecl;
import org.eclipse.fennec.model.query.Query;
import org.eclipse.fennec.persistence.capabilities.CommandCapabilitiesBuilder;
import org.eclipse.fennec.persistence.capabilities.PersistenceCapabilities;
import org.eclipse.fennec.persistence.capabilities.QueryCapabilities;
import org.eclipse.fennec.persistence.capabilities.StoreCapabilitiesBuilder;
import org.eclipse.fennec.persistence.query.QueryException;
import org.eclipse.fennec.persistence.query.api.QueryResult;
import org.eclipse.fennec.persistence.query.memory.MemoryQueries;
import org.eclipse.fennec.persistence.query.memory.MemoryQueryProcessor;
import org.eclipse.fennec.persistence.repository.api.PreparedQuery;
import org.eclipse.fennec.persistence.repository.api.ReadRepository;

/**
 * Read-only {@link ReadRepository} over EMF resource files: every operation
 * loads the file (or every regular file of a directory) configured on the
 * {@code FileDataInput} into a fresh {@link ResourceSet}, so each caller owns
 * the objects it receives and no shared mutable state exists.
 *
 * <p>
 * Canonical queries ({@code find}/{@code count}/{@code prepare}) are evaluated
 * by the fennec persistence stack's reference in-memory engine
 * ({@link MemoryQueries}) over the loaded objects: predicates, ordering,
 * projection, aggregation pipelines and bound parameters all work, and the
 * repository declares exactly the engine's {@link QueryCapabilities}, so a
 * consumer that gates on capabilities (the OData repository backend, for one)
 * gets honest answers instead of a refused query. Nothing is pushed anywhere —
 * every query materializes the file first — which is the nature of a file
 * input, not a limitation of the query surface.
 * </p>
 */
public class FileReadRepository implements ReadRepository {

	/**
	 * The query capabilities are those of the in-memory reference engine that
	 * evaluates every query; no commands, no store features (read-only).
	 */
	static final PersistenceCapabilities CAPABILITIES = PersistenceCapabilities.of(
			new MemoryQueryProcessor().capabilities(), CommandCapabilitiesBuilder.create().build(),
			StoreCapabilitiesBuilder.create().build());

	private final FileDataInput input;
	private final ResourceSetFactory resourceSetFactory;

	private volatile boolean disposed;
	private ResourceSet resourceSet;

	public FileReadRepository(FileDataInput input, ResourceSetFactory resourceSetFactory) {
		this.input = input;
		this.resourceSetFactory = resourceSetFactory;
	}

	// --- RepositoryService ---

	@Override
	public String id() {
		return input.getId();
	}

	@Override
	public URI baseUri() {
		return URI.createURI(input.getUri());
	}

	@Override
	public PersistenceCapabilities capabilities() {
		return CAPABILITIES;
	}

	@Override
	public boolean isDisposed() {
		return disposed;
	}

	@Override
	public void dispose() {
		disposed = true;
	}

	@Override
	public void close() {
		dispose();
	}

	@Override
	public synchronized ResourceSet getResourceSet() {
		if (resourceSet == null) {
			resourceSet = resourceSetFactory.createResourceSet();
		}
		return resourceSet;
	}

	@Override
	public ResourceSet createResourceSet() {
		return resourceSetFactory.createResourceSet();
	}

	@Override
	public URI createUri(EObject object) {
		return createUri(object.eClass(), EcoreUtil.getID(object));
	}

	@Override
	public URI createUri(EObject object, Map<?, ?> options) {
		return createUri(object);
	}

	@Override
	public URI createUri(EClass eClass, Object id) {
		return baseUri().appendFragment(String.valueOf(id));
	}

	@Override
	public EObject createProxy(EClass eClass, Object id) {
		InternalEObject proxy = (InternalEObject) EcoreUtil.create(eClass);
		proxy.eSetProxyURI(createUri(eClass, id));
		return proxy;
	}

	@Override
	public void proxify(EObject object) {
		throw readOnly("proxify");
	}

	@Override
	public Resource attach(EObject object) {
		throw readOnly("attach");
	}

	@Override
	public Resource attach(EObject object, Map<?, ?> options) {
		throw readOnly("attach");
	}

	@Override
	public EObject detach(EObject object) {
		throw readOnly("detach");
	}

	private UnsupportedOperationException readOnly(String operation) {
		return new UnsupportedOperationException(
				"file-backed repository '" + id() + "' is a read-only snapshot; " + operation + " is not supported");
	}

	// --- ReadRepository ---

	@Override
	public Resource getResource(URI uri, boolean loadOnDemand) throws IOException {
		if (!Objects.equals(uri, baseUri())) {
			throw new IOException("file-backed repository '" + id() + "' only serves its own base URI " + baseUri());
		}
		return load();
	}

	@Override
	public EObject getEObject(URI uri) throws IOException {
		return uri.fragment() == null ? null : findById(null, uri.fragment());
	}

	@Override
	public EObject getEObject(URI uri, Map<?, ?> options) throws IOException {
		return getEObject(uri);
	}

	@Override
	public EObject getEObject(EClass eClass, Object id) throws IOException {
		return findById(eClass, String.valueOf(id));
	}

	@Override
	public EObject getEObject(EClass eClass, Object id, Map<?, ?> options) throws IOException {
		return getEObject(eClass, id);
	}

	@Override
	public Stream<EObject> getAllEObjects(EClass eClass) throws IOException {
		return contents(eClass);
	}

	@Override
	public Stream<EObject> getAllEObjects(EClass eClass, Map<?, ?> options) throws IOException {
		return contents(eClass);
	}

	@Override
	public long count(EClass eClass) throws IOException {
		return contents(eClass).count();
	}

	@Override
	public long count(EClass eClass, Map<?, ?> options) throws IOException {
		return count(eClass);
	}

	@Override
	public boolean exist(URI uri) throws IOException {
		return getEObject(uri) != null;
	}

	@Override
	public boolean exist(EClass eClass, Object id) throws IOException {
		return getEObject(eClass, id) != null;
	}

	@Override
	public void reload(EObject object) {
		// every read loads fresh; there is no stale state a reload could refresh
		throw readOnly("reload");
	}

	@Override
	public QueryResult find(Query query) throws IOException {
		return find(query, null, null);
	}

	@Override
	public QueryResult find(Query query, Map<String, Object> parameters, Map<?, ?> options) throws IOException {
		checkFrom(query);
		List<EObject> candidates = contents(query.getFrom()).toList();
		try {
			return MemoryQueries.execute(query, candidates, parameters);
		} catch (QueryException e) {
			throw new IOException("file-backed repository '" + id() + "' cannot evaluate the query: " + e.getMessage(),
					e);
		}
	}

	@Override
	public QueryResult find(String name, Map<String, Object> parameters, Map<?, ?> options) throws IOException {
		throw new IOException("file-backed repository '" + id() + "' has no saved-query catalog");
	}

	@Override
	public long count(Query query) throws IOException {
		checkFrom(query);
		Query countQuery = EcoreUtil.copy(query);
		countQuery.setCountOnly(true);
		countQuery.setSkip(0);
		countQuery.setTop(-1);
		try (QueryResult result = find(countQuery, null, null)) {
			return result.count();
		}
	}

	@Override
	public PreparedQuery prepare(Query query) throws IOException {
		checkFrom(query);
		Diagnostic diagnostic = new MemoryQueryProcessor().validate(query, query.getFrom());
		if (diagnostic.getSeverity() >= Diagnostic.ERROR) {
			throw new IOException("file-backed repository '" + id() + "' cannot evaluate the query: "
					+ diagnosticText(diagnostic));
		}
		return new FilePreparedQuery(query);
	}

	@Override
	public PreparedQuery prepare(String name) throws IOException {
		throw new IOException("file-backed repository '" + id() + "' has no saved-query catalog");
	}

	private static void checkFrom(Query query) throws IOException {
		if (query.getFrom() == null) {
			throw new IOException("query has no from type");
		}
	}

	private static String diagnosticText(Diagnostic diagnostic) {
		StringBuilder text = new StringBuilder(diagnostic.getMessage() == null ? "" : diagnostic.getMessage());
		diagnostic.getChildren().forEach(child -> text.append("; ").append(child.getMessage()));
		return text.toString();
	}

	private EObject findById(EClass eClass, String id) throws IOException {
		return contents(eClass).filter(o -> Objects.equals(EcoreUtil.getID(o), id)).findFirst().orElse(null);
	}

	private Stream<EObject> contents(EClass eClass) throws IOException {
		if (disposed) {
			throw new IllegalStateException("repository '" + id() + "' is disposed");
		}
		Stream<EObject> stream;
		try {
			stream = load().getContents().stream();
		} catch (RuntimeException e) {
			throw new IOException("unable to load data input '" + id() + "' from " + input.getUri(), e);
		}
		return eClass == null ? stream : stream.filter(eClass::isInstance);
	}

	/**
	 * Loads the configured file (or every regular file of a directory) into a
	 * fresh {@link ResourceSet}, so each caller owns its data.
	 */
	private Resource load() {
		ResourceSet loadSet = resourceSetFactory.createResourceSet();
		URI uri = baseUri();
		if (uri.isFile() && uri.toFileString() != null) {
			// java.io.File tolerates URI-mangled path strings that Path.of rejects
			File file = new File(uri.toFileString());
			if (file.isDirectory()) {
				return loadDirectory(loadSet, file.toPath());
			}
		}
		return loadSet.getResource(uri, true);
	}

	private Resource loadDirectory(ResourceSet loadSet, Path directory) {
		Resource target = loadSet.createResource(URI.createFileURI(directory.resolve("contents.xmi").toString()));
		List<Path> files;
		try (Stream<Path> stream = Files.list(directory)) {
			files = stream.filter(Files::isRegularFile).sorted().toList();
		} catch (IOException e) {
			throw new UncheckedIOException("Unable to list data input directory " + directory, e);
		}
		for (Path file : files) {
			Resource resource = loadSet.getResource(URI.createFileURI(file.toString()), true);
			// moving the contents detaches them from their source resource
			target.getContents().addAll(new ArrayList<>(resource.getContents()));
		}
		return target;
	}

	/** Trivial prepared query: the subset is validated, execution re-runs find. */
	private final class FilePreparedQuery implements PreparedQuery {

		private final Query query;

		FilePreparedQuery(Query query) {
			this.query = query;
		}

		@Override
		public String name() {
			return query.getName();
		}

		@Override
		public Query query() {
			return query;
		}

		@Override
		public EList<ParameterDecl> parameterDeclarations() {
			return query.getParameters();
		}

		@Override
		public QueryResult execute(Map<String, Object> parameters) throws IOException {
			return find(query, parameters, null);
		}

		@Override
		public QueryResult execute(Map<String, Object> parameters, Map<?, ?> options) throws IOException {
			return find(query, parameters, options);
		}
	}
}
