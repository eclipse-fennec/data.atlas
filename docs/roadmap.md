# Data Atlas — Roadmap

Status: **Milestones 0 and 1 implemented** (2026-08-19), **Milestone 2
implemented** (2026-08-20). Horizon is deliberately minimal: everything beyond
is parked under [Later](#later-explicitly-not-planned) without commitment.

## Principles

- **The configuration model is the single source of truth.** The Data Atlas is
  a separate, horizontally scalable component: several instances can run side
  by side (e.g. to spread load), and each instance is described by exactly one
  `DataAtlasConfiguration`. The bootstrap obtains that configuration in one of
  two modes — **from the file system** (Milestone 1) or **by retrieving it from
  the Model Atlas** (later) — never from hand-maintained per-component JSON.
- **Configuration objects are registered as OSGi services.** The bootstrap does
  not translate the model into Config Admin configurations itself; it registers
  the contained configuration objects (e.g. each `RestDataService`, each
  `DataInput`) as services with identifying properties. Per-technology
  configurator components pick them up (whiteboard style) and create the actual
  runtime pieces — REST endpoints, inputs — and tear them down when the config
  service goes away. Config Admin factory configs remain an implementation
  detail inside a configurator component where useful. Precedents: the removed
  `EormFileWatcher` (`git show 262bdfc^`) registered loaded model objects as
  services including proxy-resolution gating and unregistration;
  `SchemaRegistryChainConfigurator` (model.atlas workflow) and the removed
  `DataFolderWatcher` show the correlation-key + full-teardown discipline;
  `InitialModelLoader` (model.atlas bootstrap) the one-shot bootstrap half.
- **Fennec stack only.** REST via the OSGi Jakarta RESTful Whiteboard
  (`org.eclipse.osgitech.rest` / Jersey / felix.http.jetty, same versions as
  model.atlas) and serialization via `org.eclipse.fennec.codec.rest`
  (`EObjectMessageBodyHandler` / `EMFResourceMessageBodyHandler`, per-request
  `ResourceSet` via the `ResourceSetProvider` SPI). No `org.gecko.emf.*`
  bundles — model.atlas blacklists them, and the removed `jpa.rest` buildpath
  that still mixed both stacks is not carried forward.
- **Each slice must end runnable**: resolved bndruns, OSGi integration test,
  docker image.

## Milestone 0 — configuration model rework

Goal: the model can express a complete minimal Data Atlas instance and is
structurally stable enough that serialized instances can be created against it.

Work items:

1. **Root + containment backbone.** `configuration.ecore` currently has no root
   EClass and no containment references at all. Add `DataAtlasConfiguration` as
   root with containment registries: `dataSources` (`JdbcDataSource`),
   `dataInputs` (`DataInput`), `dataSets` (`DataSet`), `services`
   (`DataService`), `exports` (`DistributionExport`), `transformations`
   (`Transformation`). This implements Stefan's registry requirement
   (see [configuration.md](../org.eclipse.fennec.data.atlas.configuration.model/configuration.md)):
   data sources and export templates are defined once in registries and only
   *referenced* by services/datasets, so the same service definition can be
   re-applied to another data source (tenant/test system) and export settings
   are templates, not per-provider copies. One root per Data Atlas *instance*
   also matches the deployment model: multiple instances, each fed its own
   `DataAtlasConfiguration`.
2. **Namespace rename.** Replace the legacy nsURI
   `https://model.data.jena.de/configuration` with a fennec one (proposal:
   `https://eclipse.org/fennec/data/atlas/configuration/1.0.0`) **now**, before
   any serialized instance exists.
3. **Make `FileDataInput` usable.** It currently has no features; give it at
   least a `uri` (file or folder of EMF resources) so Milestone 1 has a
   database-free input.
4. **Genmodel hygiene.** Reconcile the genmodel after the refactoring (plain-XML
   editing, unresolved proxies fail the build); fix
   `modelPluginID="org.eclipse.fennec.data-atlas.configuration.model"`
   (hyphen → dot); verify on a clean build that the cross-repo eorm reference
   (`../../org.eclipse.fennec.persistence.orm/model/…`) resolves via the
   buildpath jar as assumed.
5. **Document runtime constraints in the model** (as EAnnotations/documentation
   first, formal validation later): e.g. a `DataService` without contained
   `DataSet` configurations must define its own `dataInput`;
   override-else-default semantics of the `DataProvider` trias.
6. **Rewrite `configuration.md`** as an English model description; keep
   Stefan's notes as an appendix or move them into an issue.
7. **Align docs with reality** on generated sources: `src-gen*` is gitignored
   but CLAUDE.md claims it is committed — decide one way and fix the other.

Acceptance:

- clean `./gradlew build`;
- an example instance (`…configuration.model/example/dataatlas.xmi` or under
  `docs/`) that describes the Milestone 1 slice — one `FileDataInput`, one
  `RestDataService` with one `RestDataServiceConfiguration`/`DataSet` — loads
  without unresolved proxies.

## Milestone 1 — vertical slice: configuration → running REST endpoint

Goal: a runtime started from the existing bndruns/docker image reads **one
`DataAtlasConfiguration` XMI from the file system** (the first of the two
config source modes) and serves the configured DataSet at
`GET {urlContext}/{path}` (list + by-id, pagination per
`RestDataServiceConfiguration`) with content negotiation through the fennec
codec (JSON and XMI at minimum). **No database** — the slice uses
`FileDataInput`; JPA is the next milestone, not this one.

Design decisions baked into the slice (each has a "why" worth preserving):

- **File over JPA input** keeps the snapshot-only Daanse/JPA runtime stack out
  of the first slice and makes the integration test trivial and OS-neutral.
- **Collections are served as an EMF `Resource`** via
  `EMFResourceMessageBodyHandler` instead of reintroducing the Gecko
  `org.gecko.emf.util.model` `Response` envelope. If a richer envelope is
  needed later, it becomes an EClass in our own model.
- **Per-request `ResourceSet`** is supplied by outranking the codec's
  `DefaultResourceSetProvider` via the `ResourceSetProvider` SPI — the exact
  pattern the removed `JpaDataResourceFilter` used (`@ServiceRanking(100)`).
- **Config objects as services, prototype-copied.** The bootstrap registers
  configuration EObjects via a `PrototypeServiceFactory` handing out
  `EcoreUtil.copy` instances (the `EORMMappingServiceComponent` pattern), so
  consumers can resolve and adapt them without mutating the shared model.

New bundles:

| Bundle | Content |
|---|---|
| `org.eclipse.fennec.data.atlas.api` | Runtime SPI: `DataInput` service interface (supplier of EObjects/Resource for a `DataSet`), shared service-property constants (atlas instance id, config object id/type) |
| `org.eclipse.fennec.data.atlas.bootstrap` | File-mode config source: single config (`config.uri`, env-interpolated) → loads the `DataAtlasConfiguration` → registers each configuration object that must result in a runtime piece (`RestDataService`, `FileDataInput`, …) as an OSGi service with identifying properties; unregisters them all on deactivate |
| `org.eclipse.fennec.data.atlas.input.file` | Input configurator: tracks `FileDataInput` config services (DYNAMIC/MULTIPLE) and registers one `DataInput` SPI implementation per config, over EMF resource files |
| `org.eclipse.fennec.data.atlas.rest` | REST endpoint configurator: tracks `RestDataService` config services and creates the whiteboard resources per configuration (PROTOTYPE scope, `@RequireJakartarsWhiteboard`/`@RequireHttpWhiteboard`), binding the matching `DataInput` by target filter and serving list/by-id with the configured pagination parameter names; removes the endpoints when the config service goes |
| `org.eclipse.fennec.data.atlas.runtime.config` | Resource-only Configurator bundle (model.atlas pattern): felix.http instance, `JakartarsServletWhiteboardRuntimeComponent`, bootstrap config with `$[env:…]` interpolation |
| `org.eclipse.fennec.data.atlas.tests` (or per-bundle `*.tests`) | OSGi integration test: start the runtime with the example config + sample data, assert `GET` returns the expected EObjects as JSON and XMI |

Infrastructure work:

- Restore the trimmed `cnf/ext/central.mvn` blocks selectively: the Jakarta-RS
  whiteboard block (osgitech.rest 1.2.3, Jersey 3.1.3, HK2, felix.http.jetty,
  ASM, Jackson) and the fennec codec block. **Leave out** the Gecko EMF
  bundles, fastcsv, and the Daanse `sql.*`/`io.fs.watcher` bundles (the latter
  forced `-runee: JavaSE-25` in the removed `launch.bndrun`; we stay on
  JavaSE-21).
- Re-resolve `dataatlas.runtime_base.bndrun`; revisit whether the
  `org.eclipse.fennec.persistence*` + postgres bundles that are currently
  dragged in only by `configuration.model`'s eorm imports should stay in
  `-runbundles`.
- Stage the example configuration + sample data into `runtime/data/` so the
  docker image serves the slice out of the box.

Acceptance:

- `./gradlew build testOSGi` green (CI on Java 21 and 25);
- the docker container answers `GET` with JSON and XMI for the example DataSet;
- the only Data-Atlas-specific configuration entering the runtime is the model
  instance (plus the static whiteboard/http Configurator JSON).

## Milestone 2 — JPA input slice: repository-backed data over REST

Implemented 2026-08-20, based on the repository facade published the same day
(`org.eclipse.fennec.persistence:*:0.1.0-SNAPSHOT`, repo
`eclipse-fennec/emf.persistence-jpa`, branch `snapshot`; user guide:
`docs/repository-user-guide.md` there).

Goal: a `JPADataInput` in the configuration turns into a **read-only
`ReadRepository`** serving the Milestone 1 REST slice from a relational
database (H2 in the tests) instead of a file. With this milestone the
Data-Atlas-private `EObjectSource` SPI is **retired**: the contract between
inputs and services becomes the upstream repository facade
(`org.eclipse.fennec.persistence.repository.api.ReadRepository`), and no input
hands raw EMF `Resource`s to consumers anymore.

Design decisions:

- **`ReadRepository` replaces `EObjectSource`.** Every DataInput materializes
  as a `ReadRepository` service correlated by the upstream service property
  `persistence.repository.id` = the input's `id`. The `rest` bundle binds
  repositories instead of sources and reads via the facade
  (`getEObject(EClass, id)`, `find(Query)`, `count`); the EMF `Resource` stays
  a `rest`-internal serialization vehicle for the codec writer, it is no longer
  part of any contract. This removes a Data-Atlas-private abstraction in favor
  of the fennec-wide one and gives every future input (Mongo, …) a defined
  target shape.
- **A DataSet is a query over an input.** The configuration model has no query
  support yet — `DataSet` only narrows by `inputType`, although its own
  documentation already promises "a concrete query" (the `QueryTransformation`
  placeholder is about mapping *incoming* requests, not about defining dataset
  content). Upstream closes exactly this gap: the canonical query is itself an
  EMF model (`org.eclipse.fennec.query.model`, nsURI
  `https://eclipse.org/fennec/query/2.0.0` — `Query.from: EClass`, `where`
  (expression model), `orderBy`, `top`/`skip`, `parameters: ParameterDecl*`
  with `name` + `typeHint: EClassifier`; "dogfooding — queries are EMF
  objects" per the model documentation). `DataSet` therefore gains an optional
  **containment `query: Query`**, serialized directly in the configuration XMI
  via `usedGenPackages` on the query/expression genmodels — the same mechanism
  the model already uses for the eorm `EntityMappings`. Unset means "all
  objects of `inputType`" (Milestone 1 behavior); when set, `query.from` must
  equal `inputType` (runtime constraint, formal validation later). Containment
  on the DataSet rather than a root registry, because the query is the
  DataSet's identity, not a reusable template. At runtime the endpoint is
  **validated eagerly via `repository.prepare(query)`** at reconcile time —
  upstream validates prepared queries against the backend's `QueryProcessor`
  at prepare time ("fail early, not at first execution"), so a DataSet whose
  query the backend refuses never comes up as an endpoint. Requests are then
  served from a per-request copy of the query with REST pagination overlaid on
  `skip`/`top`, and declared `parameters` are exposed as HTTP query parameters
  (converted via their `typeHint`, missing required parameter → 400). The
  upstream *saved-query catalog* (`saveQuery`/`find(name, …)`) was considered
  and rejected as the configuration mechanism: it persists queries into the
  backend on first execution and has, per the repository user guide, no
  load-back API — backend state, not configuration.
- **Factory configs, not code, toward the persistence stack.** Upstream is
  driven entirely by ConfigAdmin factory configurations plus a
  `javax.sql.DataSource` service:
  `fennec.jpa.EORMMappingService` (derives an `EntityMappings` from EClasses;
  keys carry the `fennec.jpa.eorm.` prefix) →
  `fennec.jpa.EMPersistenceUnit` (binds the `EntityMappings` service +
  `DataSource` by target filters, publishes a `JPAUnit`; keys carry the
  `fennec.jpa.` prefix) →
  `fennec.repository.jpa` (`repositoryId`, `unit.target`, `readOnly` — keys
  unprefixed; publishes the repository, prototype scope). Because the last
  link already *is* the `ReadRepository` service, `input.jpa` is pure config
  translation: one `JPADataInput` → exactly these three factory configs
  (config name = input id, `repositoryId` = input id) and their deletion on
  teardown — the "Config Admin factory configs remain an implementation detail
  inside a configurator" case the principles already allow.
- **Mapping-free by default.** `supportedEClasses` of the input plus the
  EPackage the bootstrap already registers (targeted by
  `emf.nsURI=<package nsURI>` — never by `emf.name`, package names are not
  unique) feed
  `fennec.jpa.EORMMappingService`. The EClass names must be listed explicitly —
  upstream's "omit to map all" documentation claim is wrong. When
  `persistenceConfig` *is* set, `input.jpa` registers that `EntityMappings`
  object directly as a service with a filterable property and targets it — no
  `.eorm` file handling in the Data Atlas either way.
- **Read-only repositories.** `readOnly=true` → upstream withholds the write
  interfaces; the file-backed repository implements only the read side. The
  Data Atlas is a serving layer; import/write paths are separate roadmap items.
- **Pagination pushes down.** `rest` expresses list requests as
  `QueryBuilder.from(type).skip(offset).top(limit).build()` against
  `find(Query)` — the JPA backend translates that to `setFirstResult`/
  `setMaxResults`, so a DB-backed DataSet never materializes the whole table
  per request; the file repository slices in memory, which it did before.
- **Prototype discipline.** Repositories are prototype-scoped services (each
  instance owns a non-thread-safe `ResourceSet` and is disposed on unget).
  `rest` binds them as `ServiceObjects` and gets/ungets an instance per
  request; `Stream`/`QueryResult` results hold backend cursors and are closed
  via try-with-resources.

Work items:

1. **Upstream docs issue** — filed as
   [emf.persistence-jpa#193](https://github.com/eclipse-fennec/emf.persistence-jpa/issues/193):
   the published Getting Started/Configuration Reference give non-working
   EORM/persistence-unit configurations (missing key prefixes, false "omit
   `eClasses` to map all", customizer wrongly called optional), the repository
   facade is unreachable from the entry pages, and the end-to-end mapping-free
   recipe plus query paging/parameters exist only in source or unpublished
   drafts.
2. **Configuration model**: add the optional containment `DataSet.query:
   Query` (upstream query model via `usedGenPackages` on the query and
   expression genmodels, same pattern as eorm); document the
   `query.from = inputType` runtime constraint and the unset-means-all
   semantics; reconcile the genmodel (M0 lesson: plain-XML editing, unresolved
   proxies fail the build).
3. **`api`**: delete `EObjectSource` (and its `INPUT_ID` correlation if nothing
   else uses it); what remains is the config-object correlation constants.
4. **`input.file` rework**: the configurator registers a **file-backed
   `ReadRepository`** per `FileDataInput` (property
   `persistence.repository.id=<input id>`) instead of an `EObjectSource`.
   Implementation evaluates extending the upstream SPI
   (`AbstractRepository`/`AbstractRepositoryComponent`) versus implementing the
   read interface directly over the loaded XMI contents; the first cut needs
   `getEObject(EClass, id)`, `getAllEObjects`, `count`, `exist` and the
   `find(Query)` subset `from` + `skip` + `top` (unsupported query features
   fail with a clear diagnostic). If a generic in-memory/resource-backed
   repository flavour turns out to be broadly useful, propose it upstream
   (`repository.file`?) instead of growing ours.
5. **`rest` rework**: bind `ReadRepository` `ServiceObjects` by
   `persistence.repository.id`; list = `find` on the DataSet's query (or
   `QueryBuilder.from(inputType)` when none) with REST pagination overlaid on a
   per-request copy's `skip`/`top` and declared query `parameters` bound from
   HTTP query parameters; by-id = `getEObject`; endpoints with a query are
   gated on a successful `prepare` at reconcile time; results are wrapped into
   a detached `Resource`/`EObject` only for the codec response writers. The
   file-backed repository's `find` subset grows `where`/`orderBy`
   interpretation only if the fixtures need it — otherwise queries stay a
   JPA-input feature for now, refused with a clear diagnostic on file inputs.
6. **New bundle `org.eclipse.fennec.data.atlas.input.jpa`**: tracks
   `JPADataInput` config services (DYNAMIC/MULTIPLE); per input creates the
   three factory configs (the `JdbcDataSource.filter` of the model goes
   verbatim into `fennec.jpa.dataSource.target`) and deletes them on teardown.
   No service registration of its own — the upstream repository service is the
   input's runtime representation.
7. **Fixtures + integration test** (`DataAtlasJpaIntegrationTest`): a
   `dataatlas-jpa.xmi` with `JPADataInput` + `JdbcDataSource`
   (finally exercising the `dataSources` registry), an H2 `DataSource` via the
   daanse factory config, seeding through a test-private *writable* repository
   config (`fennec.jpa.ext.eclipselink.ddl-generation=create-or-extend-tables`),
   then the M1 assertions against the JPA-backed DataSet: list, by-id,
   pagination (now proven to push down), plus lifecycle teardown of the three
   factory configs. One additional DataSet in the fixture carries a `query`
   with a `where` predicate and a declared parameter, proving the
   query-as-DataSet mechanic end to end (embedded query survives XMI loading,
   `prepare` gates the endpoint, HTTP parameter binds). The existing
   file-based tests pin the `input.file`/`rest` rework — they must stay green
   unchanged at the HTTP level.
8. **Runtime**: `input.jpa` into the base bndrun requirements (drags the
   repository/EclipseLink/query stack from the `fennecPersistence` library),
   blacklist `org.apache.aries.spifly.dynamic.framework.extension` (its
   embedded ASM cannot read Java-21 class files; the runtime already uses
   `spifly.dynamic.bundle`), re-resolve. The docker example stays file-based —
   the image *can* serve JPA once a configuration and a database are provided.
9. **Docs**: architecture.md (inputs are repositories now) + an `input.jpa`
   bundle doc with the model→factory-config mapping table; update the M1
   design-decision note — collections-as-`Resource` is demoted from contract to
   `rest`-internal serialization detail.

Known upstream gotchas baked into the items above: OCD key prefixes
(`fennec.jpa.eorm.*`, `fennec.jpa.*`, but `fennec.repository.jpa` keys and
`batchWriting`/`batchSize` unprefixed); never set a
`fennec.jpa.eorm.customizer.target` that matches nothing (the reference is
mandatory-static, satisfied by a default no-op component); DDL generation
defaults to `none` (right for serving an existing schema, tests must opt in).

Acceptance:

- `./gradlew build testOSGi` green including the new H2-backed test;
- `runtime_base` re-resolves with the JPA stack; docker image behavior
  unchanged (file example remains the default);
- the only Data-Atlas-specific configuration entering the runtime is still the
  model instance.

## Later (explicitly not planned)

- **Model Atlas config mode**: the second config source — a bootstrap variant
  that retrieves the `DataAtlasConfiguration` for this instance from a Model
  Atlas instead of the file system (client stack precedent:
  `model.atlas/org.eclipse.fennec.model.atlas.rest.client.*`). Only the source
  differs; the config-objects-as-services contract stays identical.
- **DCAT**: model was removed with `262bdfc`; git history is the only source
  in the ecosystem (`common.models` has RDF but no DCAT).
- Other `DataService` kinds (OData, OGC Features/SensorThings, QGis, XMLA,
  GraphQL — no GraphQL implementation exists anywhere in fennec today).
- Importers, QVT transformations (`org.eclipse.fennec.m2x`), `BridgeRepository`,
  multi-tenancy mappings, DistributionExport execution (CSV etc. beyond what the
  codec gives for free).

## Known risks / open decisions

- **emf.osgi version split across snapshot upstreams** — *resolved 2026-08-20*:
  the persistence stack was rebuilt against the emf.osgi 1.1 line and republished
  under new coordinates (`org.eclipse.fennec.persistence:…workspace.library`,
  bnd library `fennecPersistence`, incl. the new repository facade). The
  `-runsystemcapabilities` workaround was removed from the bndruns; the
  `emf.core=osgi` requirement is now satisfied by the
  `org.eclipse.fennec.persistence.capabilities` bundle. The 0.1.x blacklist
  stays as a guard against mixed stacks.

- **Snapshot-only upstreams**: `org.eclipse.fennec.codec` (0.1.0-SNAPSHOT) and
  `org.eclipse.fennec.persistence.jpa` (0.1.0-SNAPSHOT) come from the Central
  snapshot repository; only `emf.osgi` (0.1.2/1.0.x) and the bnd libraries
  release. Milestone 1 depends on the codec — a codec release should be
  encouraged upstream before a Data Atlas release.
- **eorm cross-repo genmodel reference** resolves via the jar on the buildpath
  (unverified assumption) — verify first thing in Milestone 0.
- Two distinct `model.metadata` coordinates exist
  (`org.eclipse.fennec.codec:org.eclipse.fennec.model.metadata` vs
  `org.eclipse.fennec.metadata:model.metadata`) — confirm which is current
  before adding either.
