# Data Atlas — Roadmap

Status: **Milestones 0 and 1 implemented** (2026-08-19). Horizon is deliberately
minimal: everything beyond is parked under
[Later](#later-explicitly-not-planned) without commitment.

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

## Later (explicitly not planned)

- **Model Atlas config mode**: the second config source — a bootstrap variant
  that retrieves the `DataAtlasConfiguration` for this instance from a Model
  Atlas instead of the file system (client stack precedent:
  `model.atlas/org.eclipse.fennec.model.atlas.rest.client.*`). Only the source
  differs; the config-objects-as-services contract stays identical.
- **JPA slice**: `JPADataInput` → `org.eclipse.fennec.persistence.eclipselink`
  (`EntityMappingPersistenceUnitConfigurator`) or the new
  `persistence.repository.jpa` facade; `EORMMappingProvider` (derive
  `EntityMappings` from EClass names) makes a mapping-file-free path possible.
  Partly gated on the persistence stack being snapshot-only.
- **DCAT**: model was removed with `262bdfc`; git history is the only source
  in the ecosystem (`common.models` has RDF but no DCAT).
- Other `DataService` kinds (OData, OGC Features/SensorThings, QGis, XMLA,
  GraphQL — no GraphQL implementation exists anywhere in fennec today).
- Importers, QVT transformations (`org.eclipse.fennec.m2x`), `BridgeRepository`,
  multi-tenancy mappings, DistributionExport execution (CSV etc. beyond what the
  codec gives for free).

## Known risks / open decisions

- **emf.osgi version split across snapshot upstreams**: the fennec codec is
  built against the emf.osgi 1.1 line, `org.eclipse.fennec.persistence` against
  0.1.x (it requires `emf.core=osgi` in `[0,1.0)`). The runtimes pin the 1.1
  line (`component.minimal`), blacklist 0.1.x and satisfy persistence's
  capability range via `-runsystemcapabilities`. The clean fix is upstream: a
  persistence rebuild against emf.osgi 1.1 (worth a GitHub issue on
  `eclipse-fennec/emf.persistence-jpa`).

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
