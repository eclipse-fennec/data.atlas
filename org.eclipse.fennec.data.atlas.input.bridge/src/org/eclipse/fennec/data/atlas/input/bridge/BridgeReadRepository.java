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
package org.eclipse.fennec.data.atlas.input.bridge;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.data.atlas.api.DataTransformer;
import org.eclipse.fennec.emf.osgi.ResourceSetFactory;
import org.eclipse.fennec.model.query.ParameterDecl;
import org.eclipse.fennec.model.query.Query;
import org.eclipse.fennec.model.query.builder.QueryBuilder;
import org.eclipse.fennec.persistence.capabilities.PersistenceCapabilities;
import org.eclipse.fennec.persistence.query.api.Hit;
import org.eclipse.fennec.persistence.query.api.QueryResult;
import org.eclipse.fennec.persistence.query.api.QueryResultRow;
import org.eclipse.fennec.persistence.query.api.QueryShape;
import org.eclipse.fennec.persistence.repository.api.PreparedQuery;
import org.eclipse.fennec.persistence.repository.api.ReadRepository;
import org.osgi.service.component.ComponentServiceObjects;

/**
 * Read-only {@link ReadRepository} over another repository plus a
 * {@link DataTransformer}: every read leases the source input's repository,
 * fetches the source objects (of the transformer's input type) and returns
 * their 1:1 transformation (instances of the transformer's output type).
 *
 * <p>
 * The query surface without a {@code QueryTransformation} is deliberately the
 * {@code from} + {@code skip} + {@code top} subset on the <em>output</em>
 * type: the bridge rewrites {@code from} to the source type and delegates, so
 * pagination pushes down to the source — correct because the transformation
 * contract is 1:1. Everything else (predicates, ordering, projection,
 * parameters) is refused with a clear diagnostic until the
 * {@code QueryTransformation} exists. By-id lookups rely on the 1:1 contract
 * preserving the id: the source object is fetched by the requested id and
 * transformed.
 * </p>
 */
public class BridgeReadRepository implements ReadRepository {

	private final String id;
	private final ComponentServiceObjects<ReadRepository> source;
	private final DataTransformer transformer;
	private final ResourceSetFactory resourceSetFactory;

	private volatile boolean disposed;
	private ResourceSet resourceSet;

	public BridgeReadRepository(String id, ComponentServiceObjects<ReadRepository> source,
			DataTransformer transformer, ResourceSetFactory resourceSetFactory) {
		this.id = id;
		this.source = source;
		this.transformer = transformer;
		this.resourceSetFactory = resourceSetFactory;
	}

	// --- RepositoryService ---

	@Override
	public String id() {
		return id;
	}

	@Override
	public URI baseUri() {
		return URI.createURI("bridge:/" + id);
	}

	@Override
	public PersistenceCapabilities capabilities() {
		// the bridge flavour declares no backend capabilities (yet)
		return null;
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
				"bridge repository '" + id + "' is a read-only view; " + operation + " is not supported");
	}

	// --- ReadRepository ---

	@Override
	public Resource getResource(URI uri, boolean loadOnDemand) throws IOException {
		throw new IOException("bridge repository '" + id + "' serves no resources, only objects");
	}

	@Override
	public EObject getEObject(URI uri) throws IOException {
		return uri.fragment() == null ? null : getEObject(transformer.outputType(), uri.fragment());
	}

	@Override
	public EObject getEObject(URI uri, Map<?, ?> options) throws IOException {
		return getEObject(uri);
	}

	@Override
	public EObject getEObject(EClass eClass, Object objectId) throws IOException {
		checkNotDisposed();
		checkOutputType(eClass);
		EObject sourceObject = leased(repository -> {
			EObject match = repository.getEObject(transformer.inputType(), objectId);
			return match == null ? null : EcoreUtil.copy(match);
		});
		if (sourceObject == null) {
			return null;
		}
		List<EObject> transformed = transformer.transform(List.of(sourceObject));
		return transformed.get(0);
	}

	@Override
	public EObject getEObject(EClass eClass, Object objectId, Map<?, ?> options) throws IOException {
		return getEObject(eClass, objectId);
	}

	@Override
	public Stream<EObject> getAllEObjects(EClass eClass) throws IOException {
		checkNotDisposed();
		checkOutputType(eClass);
		List<EObject> sources = leased(repository -> {
			try (Stream<EObject> stream = repository.getAllEObjects(transformer.inputType())) {
				return List.copyOf(EcoreUtil.copyAll(stream.toList()));
			}
		});
		return transformer.transform(sources).stream();
	}

	@Override
	public Stream<EObject> getAllEObjects(EClass eClass, Map<?, ?> options) throws IOException {
		return getAllEObjects(eClass);
	}

	@Override
	public long count(EClass eClass) throws IOException {
		checkNotDisposed();
		checkOutputType(eClass);
		return leased(repository -> repository.count(transformer.inputType()));
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
	public boolean exist(EClass eClass, Object objectId) throws IOException {
		return getEObject(eClass, objectId) != null;
	}

	@Override
	public void reload(EObject object) {
		// every read fetches fresh from the source; nothing to refresh
		throw readOnly("reload");
	}

	@Override
	public QueryResult find(Query query) throws IOException {
		return find(query, null, null);
	}

	@Override
	public QueryResult find(Query query, Map<String, Object> parameters, Map<?, ?> options) throws IOException {
		checkNotDisposed();
		checkSupported(query);
		Query sourceQuery = rewrite(query);
		List<EObject> sources = leased(repository -> {
			try (QueryResult result = repository.find(sourceQuery, null, null)) {
				// copy before the lease is released: the objects may live in
				// the source repository instance's ResourceSet
				return List.copyOf(EcoreUtil.copyAll(result.objects().toList()));
			}
		});
		return new BridgeQueryResult(transformer.transform(sources));
	}

	@Override
	public QueryResult find(String name, Map<String, Object> parameters, Map<?, ?> options) throws IOException {
		throw new IOException("bridge repository '" + id + "' has no saved-query catalog");
	}

	@Override
	public long count(Query query) throws IOException {
		checkNotDisposed();
		checkSupported(query);
		Query sourceQuery = rewrite(query);
		return leased(repository -> repository.count(sourceQuery));
	}

	@Override
	public PreparedQuery prepare(Query query) throws IOException {
		checkNotDisposed();
		checkSupported(query);
		// gate downstream too: the source repository must accept the rewrite
		Query sourceQuery = rewrite(query);
		leased(repository -> repository.prepare(sourceQuery));
		return new BridgePreparedQuery(query);
	}

	@Override
	public PreparedQuery prepare(String name) throws IOException {
		throw new IOException("bridge repository '" + id + "' has no saved-query catalog");
	}

	/**
	 * The supported canonical-query subset without a QueryTransformation is
	 * from + skip + top on the output type; refuse everything else with a
	 * diagnostic instead of silently ignoring it.
	 */
	private void checkSupported(Query query) throws IOException {
		if (query.getFrom() == null) {
			throw new IOException("query has no from type");
		}
		if (!isOutputType(query.getFrom())) {
			throw new IOException("bridge repository '" + id + "' serves '" + typeName(transformer.outputType())
					+ "', not '" + typeName(query.getFrom()) + "'");
		}
		String unsupported = null;
		if (query.getPredicate() != null) {
			unsupported = "predicate";
		} else if (!query.getOrderBy().isEmpty()) {
			unsupported = "orderBy";
		} else if (!query.getSelect().isEmpty()) {
			unsupported = "projection";
		} else if (query.getApply() != null) {
			unsupported = "pipeline";
		} else if (!query.getExpand().isEmpty()) {
			unsupported = "expand";
		} else if (query.isDistinct() || query.isCountOnly() || query.isWithScores()) {
			unsupported = "distinct/countOnly/withScores";
		} else if (!query.getParameters().isEmpty()) {
			unsupported = "parameters";
		}
		if (unsupported != null) {
			throw new IOException("bridge repository '" + id
					+ "' supports only from/skip/top queries until a QueryTransformation is configured; "
					+ "unsupported query feature: " + unsupported);
		}
	}

	/** Rewrites the output-type query to the source type, keeping skip/top. */
	private Query rewrite(Query query) {
		Query sourceQuery = QueryBuilder.from(transformer.inputType()).build();
		sourceQuery.setSkip(query.getSkip());
		sourceQuery.setTop(query.getTop());
		return sourceQuery;
	}

	private void checkOutputType(EClass eClass) throws IOException {
		if (eClass != null && !isOutputType(eClass)) {
			throw new IOException("bridge repository '" + id + "' serves '" + typeName(transformer.outputType())
					+ "', not '" + typeName(eClass) + "'");
		}
	}

	/** Type identity by nsURI + name, robust across EPackage instances. */
	private boolean isOutputType(EClass eClass) {
		EClass outputType = transformer.outputType();
		if (eClass == outputType) {
			return true;
		}
		return eClass.getName().equals(outputType.getName()) && eClass.getEPackage() != null
				&& outputType.getEPackage() != null
				&& Objects.equals(eClass.getEPackage().getNsURI(), outputType.getEPackage().getNsURI());
	}

	private String typeName(EClass eClass) {
		return eClass.getEPackage() != null ? eClass.getEPackage().getNsURI() + "#" + eClass.getName()
				: eClass.getName();
	}

	private void checkNotDisposed() {
		if (disposed) {
			throw new IllegalStateException("repository '" + id + "' is disposed");
		}
	}

	private interface RepositoryCall<T> {
		T call(ReadRepository repository) throws IOException;
	}

	/** Leases a source repository instance for one call and always releases it. */
	private <T> T leased(RepositoryCall<T> call) throws IOException {
		ReadRepository repository = source.getService();
		try {
			return call.call(repository);
		} finally {
			source.ungetService(repository);
		}
	}

	/** OBJECTS-shaped result over an already materialized list. */
	private static final class BridgeQueryResult implements QueryResult {

		private final List<EObject> objects;

		BridgeQueryResult(List<EObject> objects) {
			this.objects = objects;
		}

		@Override
		public QueryShape shape() {
			return QueryShape.OBJECTS;
		}

		@Override
		public Stream<EObject> objects() {
			return objects.stream();
		}

		@Override
		public Stream<QueryResultRow> rows() {
			throw new IllegalStateException("result shape is OBJECTS, not PROJECTION");
		}

		@Override
		public long count() {
			throw new IllegalStateException("result shape is OBJECTS, not COUNT");
		}

		@Override
		public Stream<Hit> hits() {
			throw new IllegalStateException("result shape is OBJECTS, no scores requested");
		}

		@Override
		public Map<String, Double> scores() {
			throw new IllegalStateException("result shape is OBJECTS, no scores requested");
		}

		@Override
		public void close() {
			// nothing to release: the list is materialized, no backend cursor
		}
	}

	/** Trivial prepared query: the subset is validated, execution re-runs find. */
	private final class BridgePreparedQuery implements PreparedQuery {

		private final Query query;

		BridgePreparedQuery(Query query) {
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
