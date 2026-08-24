# Fennec Data Atlas - User Guide

Fennec Data Atlas serves EMF model instances (`EObject`s) over REST. One
instance is described entirely by an **EMF configuration model**
(`DataAtlasConfiguration`): which data inputs provide objects, which DataSets
are published, and which services expose them. The configuration is the single
source of truth — there are no per-endpoint configuration files to maintain,
and changes to the configuration reach a running instance without a restart.

It is the counterpart to the
[Fennec Model Atlas](https://github.com/eclipse-fennec/model.atlas): the Model
Atlas manages schemas (`EPackage`, `EClass`), the Data Atlas manages the
instances of those schemas.

## Table of Contents

- [Getting Started](#getting-started)
  - [Running with Docker](#running-with-docker)
  - [Trying the Built-in Example](#trying-the-built-in-example)
  - [Building from Source](#building-from-source)
- [Core Concepts](#core-concepts)
  - [The Configuration Model](#the-configuration-model)
  - [Data Inputs](#data-inputs)
  - [DataSets and Queries](#datasets-and-queries)
  - [Data Services](#data-services)
  - [Configuration Sources: File vs. Model Atlas](#configuration-sources-file-vs-model-atlas)
- [The REST Endpoints](#the-rest-endpoints)
  - [URL Layout](#url-layout)
  - [Pagination](#pagination)
  - [Bound Query Parameters](#bound-query-parameters)
  - [Content Negotiation](#content-negotiation)
  - [Error Handling](#error-handling)
- [Writing a Configuration](#writing-a-configuration)
  - [Minimal Example: File Input over REST](#minimal-example-file-input-over-rest)
  - [Relational Data: JPA Input](#relational-data-jpa-input)
  - [Query-Defined DataSets with Parameters](#query-defined-datasets-with-parameters)
  - [Referencing Schemas](#referencing-schemas)
- [Configuration Lifecycle](#configuration-lifecycle)
  - [File Mode: Watching the Configuration File](#file-mode-watching-the-configuration-file)
  - [Model Atlas Mode: Staged Updates](#model-atlas-mode-staged-updates)
  - [Failure Semantics](#failure-semantics)
- [Configuration Reference](#configuration-reference)
- [Further Reading](#further-reading)

---

## Getting Started

### Running with Docker

Data Atlas is available as Docker images in two variants that differ only in
**where the configuration comes from**:

| Variant | Image Tag | Configuration source |
|---------|-----------|----------------------|
| **File** | `eclipsefennec/data.atlas:file-snapshot` | A `DataAtlasConfiguration` XMI file on the file system |
| **Model Atlas** | `eclipsefennec/data.atlas:atlas-snapshot` | Retrieved from a [Model Atlas](https://github.com/eclipse-fennec/model.atlas) registry |

Both are also available on GHCR as `ghcr.io/eclipse-fennec/data.atlas`.

#### File variant (standalone)

```bash
docker run -d -p 8080:8080 eclipsefennec/data.atlas:file-snapshot
```

The image ships a built-in example configuration (persons from an XMI file,
served over REST). To serve your own configuration, mount it and point
`DATA_ATLAS_CONFIG_URI` at it:

```bash
docker run -d -p 8080:8080 \
  -v /path/to/config:/dataatlas-config \
  -e DATA_ATLAS_CONFIG_URI=/dataatlas-config/myatlas.xmi \
  eclipsefennec/data.atlas:file-snapshot
```

#### Model Atlas variant (with Docker Compose)

The Model Atlas variant needs a running Model Atlas holding the configuration.
The provided compose file starts the full stack — one Model Atlas, a one-shot
seeder that uploads the example schemas and configuration, and both Data Atlas
variants side by side serving the same example:

```bash
docker compose -f docker/dockercompose/docker-compose-atlas.yml up -d
```

| Service | URL |
|---------|-----|
| Model Atlas | http://localhost:8080/atlas/rest |
| Data Atlas (file mode) | http://localhost:8081/rest/example/persons |
| Data Atlas (Model Atlas mode) | http://localhost:8082/rest/example/persons |

Both Data Atlas instances answer identically — the config-source mode is
invisible to clients. See the
[compose README](../docker/dockercompose/README.md) for how the Model Atlas
side is assembled (scope, registry, seeding).

### Trying the Built-in Example

With the file variant running on port 8080:

```bash
# List the example persons (JSON)
curl http://localhost:8080/rest/example/persons

# A single person by id
curl http://localhost:8080/rest/example/persons/p1

# The same as XMI
curl -H "Accept: application/xml" http://localhost:8080/rest/example/persons

# Paginated
curl "http://localhost:8080/rest/example/persons?offset=1&limit=2"
```

### Building from Source

Bnd/Bndtools OSGi workspace built with Gradle, Java 21:

```bash
# Build the project
./gradlew build

# Export the runtime JARs for a variant
./gradlew :org.eclipse.fennec.data.atlas.runtime:export.dataatlas.runtime_docker
./gradlew :org.eclipse.fennec.data.atlas.runtime:export.dataatlas.runtime_docker_atlas

# Prepare and build the Docker images
./gradlew :docker:dataatlas:prepareDocker :docker:dataatlas-atlas:prepareDocker
docker build -t eclipsefennec/data.atlas:file-snapshot docker/dataatlas/
docker build -t eclipsefennec/data.atlas:atlas-snapshot docker/dataatlas-atlas/
```

---

## Core Concepts

### The Configuration Model

A `DataAtlasConfiguration` describes **exactly one Data Atlas instance**.
Multiple instances can run side by side (e.g. to spread load); each is fed its
own configuration. The root acts as a set of registries — definitions live
there exactly once and are referenced from the rest of the model:

| Registry | Type | Purpose |
|---|---|---|
| `dataSources` | `JdbcDataSource` | Reusable data source definitions, bound at runtime to OSGi `DataSource` services via an LDAP target filter |
| `dataInputs` | `DataInput` | The inputs that provide EObjects to the instance |
| `dataSets` | `DataSet` | The published datasets |
| `services` | `DataService` | The endpoints this instance publishes |
| `exports` | `DistributionExport` | Reusable serialization templates |
| `transformations` | `Transformation` | Data and query transformations |

At runtime, a bootstrap component loads the configuration, registers the
referenced schemas (`EPackage`s) and the configuration objects as OSGi
services, and per-technology configurator components translate them into the
actual runtime pieces — repositories and REST endpoints. `id` values identify
objects across configuration updates, so they must be unique per
configuration.

> The full model reference lives in the
> [configuration model documentation](../org.eclipse.fennec.data.atlas.configuration.model/configuration.md);
> the runtime translation is described in the
> [architecture document](architecture.md).

### Data Inputs

A `DataInput` is anything that provides EObjects; `supportedEClasses` names
the model types it can deliver. Implemented today:

- **`FileDataInput`** — reads EMF resources from a `uri` (a single file or a
  directory). Relative URIs resolve against the configuration file's
  location. The simplest way to serve data — no database required.
- **`JPADataInput`** — reads from a relational database: references a
  `JdbcDataSource` from the registry and optionally a JPA `EntityMappings`
  model (the `eorm` model of the fennec persistence stack) describing how
  model types map to the relational schema. Without an explicit mapping, a
  default mapping is derived from `supportedEClasses`.

Every input materializes as a read-only repository service inside the
instance; all inputs are **read-only** — the Data Atlas serves data, it does
not modify the underlying sources.

### DataSets and Queries

A `DataSet` is a published dataset: it names its `dataInput`, its `inputType`
(the EClass served), and optionally a **canonical query** that defines its
content. Without a query, a DataSet is simply *all objects of `inputType`*
from its input. With a query, the DataSet is the query's result — including
filters and **declared parameters** that the serving endpoint exposes as HTTP
query parameters (see
[Bound Query Parameters](#bound-query-parameters)).

An endpoint for a DataSet only appears after the backing repository has
successfully validated the query — a DataSet with a broken query never goes
live (and is logged loudly).

### Data Services

A `DataService` exposes DataSets under a `urlContext` (the base path).
Implemented today: **`RestDataService`** — one REST application per service,
with one `configuration` entry per served DataSet (`dataSet` reference +
`path` segment, optional `batchSize`/`batchSizeLimit` pagination defaults).
The pagination parameter names are configurable per service
(`paginationOffsetParameterName`, default `offset`;
`paginationSizeParameterName`, default `limit`).

Other service kinds in the model (OData, GraphQL, OGC Features, OGC
SensorThings, XMLA, QGis) are placeholders for future milestones.

### Configuration Sources: File vs. Model Atlas

The same configuration model can reach an instance two ways:

- **File mode**: the instance loads a `DataAtlasConfiguration` XMI from the
  file system (`DATA_ATLAS_CONFIG_URI`) and watches it for changes.
- **Model Atlas mode**: the instance retrieves the configuration from a Model
  Atlas registry (scope + registry + object id) and polls it for new
  versions. Schema references in the configuration resolve against the Model
  Atlas schema registry, so the instance needs no local `.ecore` files.

Clients cannot tell the difference — the served endpoints are identical.

---

## The REST Endpoints

### URL Layout

The Jakarta-RS whiteboard serves under the `/rest` context. Each
`RestDataService` becomes one REST application under its `urlContext`, each
configured DataSet is served under its `path`:

```
GET /rest{urlContext}/{path}         # list (paginated)
GET /rest{urlContext}/{path}/{id}    # single object by id
```

For the built-in example (`urlContext="/example"`, one DataSet at
`path="persons"`):

```bash
curl http://localhost:8080/rest/example/persons
curl http://localhost:8080/rest/example/persons/p1
```

### Pagination

The list endpoint overlays REST pagination onto the DataSet's query
(`skip`/`top` push-down — the paging happens in the backing repository, not in
memory):

| Parameter | Default | Description |
|---|---|---|
| `offset` | `0` | Number of objects to skip |
| `limit` | `batchSize` of the DataSet configuration, else unlimited | Page size; capped by `batchSizeLimit` if set |

```bash
curl "http://localhost:8080/rest/example/persons?offset=10&limit=5"
```

The parameter *names* are per-service configurable
(`paginationOffsetParameterName` / `paginationSizeParameterName`). A
`batchSizeLimit` on the DataSet configuration caps every request — asking for
more (or for everything) silently returns at most the limit.

### Bound Query Parameters

If a DataSet's canonical query declares parameters, they bind from HTTP query
parameters of the same name and are converted via the declared type hint. A
missing or unconvertible parameter is a `400 Bad Request`.

```bash
# DataSet "persons-by-lastname" declares a 'lastName' parameter
curl "http://localhost:8080/rest/jpa/by-lastname?lastName=Doe"
```

### Content Negotiation

Responses are serialized by the fennec codec via standard HTTP content
negotiation:

| `Accept` | Result |
|---|---|
| `application/json` (default) | JSON |
| `application/xml` | XMI |

### Error Handling

| Status | Meaning |
|---|---|
| `200 OK` | Success (a list response may be an empty container) |
| `400 Bad Request` | Non-numeric pagination value, missing or unconvertible declared query parameter |
| `404 Not Found` | Unknown DataSet path, or no object with the given id |
| `500 Internal Server Error` | Reading from the backing input failed |

---

## Writing a Configuration

### Minimal Example: File Input over REST

The complete built-in example — one file input, one DataSet, one REST service
(this is
[`example/dataatlas.xmi`](../org.eclipse.fennec.data.atlas.configuration.model/example/dataatlas.xmi)):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration:DataAtlasConfiguration xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:configuration="https://eclipse.org/fennec/data/atlas/configuration/1.0.0"
    name="example" description="One file-based input served over REST.">
  <dataInputs xsi:type="configuration:FileDataInput" id="persons-file" uri="data/persons.xmi">
    <supportedEClasses href="model/person.ecore#//Person"/>
  </dataInputs>
  <dataSets id="persons" name="Persons" dataInput="persons-file">
    <inputType href="model/person.ecore#//Person"/>
    <outputType href="model/person.ecore#//Person"/>
  </dataSets>
  <services xsi:type="configuration:RestDataService" id="persons-rest" name="Persons REST"
      urlContext="/example" openAPI="false">
    <configuration id="persons-rest-config" dataSet="persons" path="persons"/>
  </services>
</configuration:DataAtlasConfiguration>
```

Relative URIs (`data/persons.xmi`, `model/person.ecore`) resolve against the
configuration file's own location — a configuration folder is self-contained
and can be mounted anywhere.

### Relational Data: JPA Input

A `JdbcDataSource` binds to a pooled OSGi `DataSource` service by LDAP filter;
the `JPADataInput` references it. The deployment must provide the actual
`DataSource` service (a JDBC driver/pool bundle plus its configuration — the
Data Atlas image does not bundle one):

```xml
<dataSources id="persons-db" name="Persons DB" filter="(osgi.jndi.service.name=personsdb)"/>
<dataInputs xsi:type="configuration:JPADataInput" id="persons-jpa" dataSource="persons-db">
  <supportedEClasses href="model/person.ecore#//Person"/>
</dataInputs>
```

Everything downstream (DataSets, services) is identical to the file case —
DataSets do not know what kind of input feeds them.

### Query-Defined DataSets with Parameters

A DataSet can embed a canonical query (fennec query model). This one filters
persons by a `lastName` parameter, which the REST endpoint exposes as an HTTP
query parameter:

```xml
<dataSets id="persons-by-lastname" name="Persons by last name" dataInput="persons-jpa">
  <inputType href="model/person.ecore#//Person"/>
  <outputType href="model/person.ecore#//Person"/>
  <query>
    <from href="model/person.ecore#//Person"/>
    <predicate xsi:type="expression:Comparison" operator="EQ">
      <left xsi:type="expression:PropertyPath">
        <segments xsi:type="ecore:EAttribute" href="model/person.ecore#//Person/lastName"/>
      </left>
      <right xsi:type="expression:ParameterRef" name="lastName"/>
    </predicate>
    <parameters name="lastName">
      <typeHint xsi:type="ecore:EDataType" href="http://www.eclipse.org/emf/2002/Ecore#//EString"/>
    </parameters>
  </query>
</dataSets>
```

(The query needs the additional namespaces
`xmlns:expression="https://eclipse.org/fennec/expression/1.0.0"` and
`xmlns:ecore="http://www.eclipse.org/emf/2002/Ecore"` on the root element;
references to abstract types like `PropertyPath.segments` need an explicit
`xsi:type`.)

### Referencing Schemas

How the `href`s to EClasses are written depends on the config-source mode:

- **File mode**: reference local `.ecore` files relative to the
  configuration (`model/person.ecore#//Person`). Ship the `.ecore` files next
  to the configuration.
- **Model Atlas mode**: reference schemas by their **nsURI**
  (`https://example.org/person/1.0.0#//Person`). The references resolve
  against the Model Atlas schema registry of the instance's scope — upload
  the schemas there first. Compare the two example variants:
  [`dataatlas.xmi`](../org.eclipse.fennec.data.atlas.configuration.model/example/dataatlas.xmi)
  (file) vs.
  [`dataatlas-atlas.xmi`](../org.eclipse.fennec.data.atlas.configuration.model/example/dataatlas-atlas.xmi)
  (Model Atlas).

---

## Configuration Lifecycle

Configuration changes reach a **running** instance without a restart. Every
update is applied as a diff keyed by the objects' `id`s: unchanged objects
keep their registrations, so untouched DataSets keep serving through an
update; only what actually changed is torn down and re-created.

### File Mode: Watching the Configuration File

The instance watches its configuration file. Editing (or replacing) the file
triggers a debounced reload — save the file, and within a second or two the
new state is live:

```bash
# add a DataSet + service configuration to the mounted file, then:
curl http://localhost:8080/rest/example/persons2   # the new endpoint appears
curl http://localhost:8080/rest/example/persons    # untouched ones keep serving
```

Deleting the configuration file tears the published configuration down
(loudly); restoring it brings the instance back.

### Model Atlas Mode: Staged Updates

The instance polls its configuration object every
`DATA_ATLAS_REFRESH_INTERVAL` milliseconds (default 300000 = 5 minutes).
Because the configuration lives in a final (read-only) stage, updates flow
through the Model Atlas **stage workflow**: upload the new version into the
writable `draft` stage, then transition it to `release`:

```bash
# 1. upload the new version into the draft stage
curl -X POST -H "Content-Type: application/xmi" --data-binary @dataatlas-v2.xmi \
  "http://modelatlas:8080/atlas/rest/dataatlas/registries/configurations/stages/draft/dataatlas?name=dataatlas&override=true"

# 2. transition it to the final release stage
curl -X POST -H "Content-Type: application/json" \
  -d '{"objectId": "dataatlas", "targetStage": "release"}' \
  "http://modelatlas:8080/atlas/rest/dataatlas/registries/configurations/stages/draft/actions/transition"
```

The instance picks the new version up on its next refresh. Note that **each
Model Atlas stage resolves against its own schema view** — the schemas the
configuration references must be present in both the `draft` and the
`release` stage, or the draft upload fails to deserialize.

### Failure Semantics

A broken new configuration — unresolvable schema references, a wrong root
element, a deleted file — **fails hard**: the published configuration is torn
down (endpoints answer 404) and the error is logged loudly. The watcher/poll
keeps running, so publishing a corrected version recovers the instance
automatically. A transient Model Atlas fetch error (network hiccup) does
*not* tear anything down — the instance keeps serving its current state and
retries on the next refresh.

---

## Configuration Reference

Both images are configured through environment variables.

### Common

| Variable | Default | Description |
|----------|---------|-------------|
| `DATA_ATLAS_HTTP_PORT` | `8080` | HTTP port of the REST endpoints |

### File variant

| Variable | Default | Description |
|----------|---------|-------------|
| `DATA_ATLAS_CONFIG_URI` | `/opt/dataatlas/runtime/data/dataatlas.xmi` | Path of the `DataAtlasConfiguration` XMI (the built-in example) |

### Model Atlas variant

| Variable | Default | Description |
|----------|---------|-------------|
| `MODEL_ATLAS_BASE_URI` | `http://modelatlas:8080/atlas/rest` | Base URI of the Model Atlas REST API |
| `DATA_ATLAS_SCOPE` | `dataatlas` | Model Atlas scope holding the configuration |
| `DATA_ATLAS_REGISTRY` | `configurations` | Registry within the scope |
| `DATA_ATLAS_OBJECT_ID` | `dataatlas` | Object id of the `DataAtlasConfiguration` instance |
| `DATA_ATLAS_REFRESH_INTERVAL` | `300000` | Poll interval for configuration updates, in milliseconds (also bounds the client cache revalidation) |

---

## Further Reading

- [Architecture](architecture.md) — how the configuration model becomes
  running services, and the configuration lifecycle in detail
- [The configuration model](../org.eclipse.fennec.data.atlas.configuration.model/configuration.md) —
  full model reference (all types, the override-else-default trias, runtime
  constraints)
- [Combined compose setups](../docker/dockercompose/README.md) — the
  file-vs-atlas example stack and how the Model Atlas side is assembled
- [JPA input details](../org.eclipse.fennec.data.atlas.input.jpa/readme.md) —
  how a `JPADataInput` maps to the fennec persistence stack
- [Roadmap](roadmap.md) — implemented milestones and what comes next
- [Model Atlas User Guide](https://github.com/eclipse-fennec/model.atlas/blob/snapshot/docs/user-guide.md) —
  scopes, registries, stages, and the schema APIs referenced throughout this
  guide
