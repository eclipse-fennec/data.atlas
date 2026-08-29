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
  - [Trying the Example](#trying-the-example)
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
  - [Export Formats: Deciding What a DataSet Serves](#export-formats-deciding-what-a-dataset-serves)
  - [Error Handling](#error-handling)
- [Writing a Configuration](#writing-a-configuration)
  - [Minimal Example: File Input over REST](#minimal-example-file-input-over-rest)
  - [Relational Data: JPA Input](#relational-data-jpa-input)
  - [PostgreSQL End to End](#postgresql-end-to-end)
  - [Query-Defined DataSets with Parameters](#query-defined-datasets-with-parameters)
  - [Referencing Schemas](#referencing-schemas)
- [Configuration Lifecycle](#configuration-lifecycle)
  - [File Mode: Watching the Configuration File](#file-mode-watching-the-configuration-file)
  - [Model Atlas Mode: Staged Updates](#model-atlas-mode-staged-updates)
  - [Failure Semantics](#failure-semantics)
- [Publishing to a DCAT Portal](#publishing-to-a-dcat-portal)
  - [Declaring a Publication](#declaring-a-publication)
  - [Connecting the Portal](#connecting-the-portal)
  - [What Reaches the Portal](#what-reaches-the-portal)
  - [Robustness and Observability](#robustness-and-observability)
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

**The images carry no models, configuration or data.** Nothing in a Data Atlas
relies on files being present out of the box — everything it serves is mounted
in, or (in atlas mode) deposited in the Model Atlas. Started without a mount, an
instance comes up **unconfigured**: it publishes nothing, logs that no
configuration is there, and keeps watching the location, so mounting one later
brings it up without a restart.

`/opt/dataatlas/runtime/data` is the directory `DATA_ATLAS_CONFIG_URI` defaults
into (`<data>/dataatlas.xmi`), and relative hrefs in the configuration resolve
against it — so mount the folder that holds the configuration *together with*
the `.ecore` and data files it references:

```bash
docker run -d -p 8080:8080 \
  -v /path/to/config:/opt/dataatlas/runtime/data \
  eclipsefennec/data.atlas:file-snapshot
```

To keep the configuration elsewhere, point the variable at it:

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
variants side by side serving the same example (with the example mounted into
them, since the images carry nothing):

```bash
docker compose -f docker/dockercompose/docker-compose-atlas.yml up -d
```

| Service | URL |
|---------|-----|
| Model Atlas | http://localhost:8080/atlas/rest |
| Data Atlas (file mode) | http://localhost:8081/rest/example/persons |
| Data Atlas (Model Atlas mode) | http://localhost:8082/rest/example/persons |

Both Data Atlas instances answer identically — the config-source mode is
invisible to clients. Note what each mode needs from the file system: in file
mode the configuration, schemas and data are mounted; in atlas mode the
configuration and schemas come from the Model Atlas and only the example's
*data* file is mounted. A configuration whose inputs are all databases needs no
mount at all — the PostgreSQL setup below is exactly that case. See the
[compose README](../docker/dockercompose/README.md) for how the Model Atlas
side is assembled (scope, registry, seeding).

### Trying the Example

The repository ships an example under
`org.eclipse.fennec.data.atlas.configuration.model/example/` — a configuration,
a `person.ecore` and three persons as XMI. Mount that folder:

```bash
docker run -d -p 8080:8080 \
  -v "$PWD/org.eclipse.fennec.data.atlas.configuration.model/example:/opt/dataatlas/runtime/data" \
  eclipsefennec/data.atlas:file-snapshot
```

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

For the shipped example (`urlContext="/example"`, one DataSet at
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
| `application/json` | JSON |
| `application/xml` | XMI |
| `text/csv` | CSV |
| `application/x-csv-zip` | ZIP with one CSV per serialized EClass |

Which of these a given DataSet actually offers is **configuration**, not a
runtime constant — see the next section. A DataSet that declares no export
serves `application/json` and `application/xml`, which is the behaviour of every
configuration written before exports became executable.

### Export Formats: Deciding What a DataSet Serves

The formats an endpoint offers come from the `DistributionExport` templates in
the configuration's `exports` registry, referenced through
`DataProvider.distributionExport`. The resolution has two cases and no third:

- **the DataSet resolves to no export** (neither its own nor its service's) —
  `application/json` and `application/xml` are served, as before;
- **the DataSet resolves to at least one export** — *exactly* those media types
  are served. Any other `Accept` is answered with `406 Not Acceptable`.

So declaring a CSV export does not *add* CSV, it *defines* the format list. A
DataSet that should offer CSV **and** JSON declares both:

```xml
<exports xsi:type="configuration:CSVDistributionExport" id="csv" name="CSV"
    description="Semicolon separated, no SQL-type row."
    separator=";" includeTypeHeader="false"/>
<exports id="json" name="JSON" description="Plain JSON." mediaType="application/json"/>

<dataSets id="persons" name="Persons" description="..." dataInput="persons-jpa"
    distributionExport="csv json">
  ...
</dataSets>
```

`mediaType` is what makes a format without a dedicated export kind expressible.
A `CSVDistributionExport` does not need it (it defaults to `text/csv`, or
`application/x-csv-zip` when `compressed` is set); JSON and XMI are plain
`DistributionExport`s carrying only `mediaType`.

Exports are **templates**: define the CSV settings once and reference them from
every DataSet that should share them. An export set on a DataSet fully replaces
the enclosing service's — that is the same override-else-default rule as for
`dataInput`.

#### CSV settings

The three CSV attributes map onto fennec codec options rather than being
reimplemented, which fixes their semantics:

| Attribute | Effect |
|---|---|
| `separator` | Field delimiter. Only the first character is used |
| `includeTypeHeader` | Emits an extra **SQL-type row** between header and data. The column header row is always written and cannot be switched off |
| `compressed` | Serves `application/x-csv-zip` — a ZIP with one CSV per serialized EClass, **not** a gzipped single CSV |

```bash
curl -H "Accept: text/csv" http://localhost:8081/rest/pg/persons
# id;firstName;lastName
# p1;Ada;Lovelace
# p2;Grace;Hopper
# p3;Margaret;Hamilton
```

#### Letting clients override the CSV settings

By default the configured settings are authoritative: the codec's client-side
`Codec-Options` request header does **not** reach a Data Atlas endpoint, because
the filter implementing it attaches to the default Jakarta-RS application only
while every Data Atlas service is its own application.

That default is a deployment decision, not a limit. The filter is a DS
component, so its whiteboard target is a component property and can be set
through Config Admin:

```json
"org.eclipse.fennec.codec.rest.jakartas.filter.ClientCodecOptionsFilter": {
	"osgi.jakartars.application.select": "(|(emf=true)(osgi.jakartars.name=.default))"
}
```

That filter is the one the codec's own message body handlers use, so it covers
every Data Atlas application. Use `(osgi.jakartars.name=*)` for all
applications, or name individual ones
(`(osgi.jakartars.name=dataAtlas.persons-pg-rest)`) to open the knob selectively.

Once the filter reaches the endpoint, a whitelisted client option wins over the
configured one — the Data Atlas puts its configured values *underneath* whatever
the filter deposited:

```bash
curl -H "Accept: text/csv" -H "Codec-Options: codec.csv.delimiter=|"      http://localhost:8081/rest/pg/persons
```

The whitelist is per codec module and deliberately narrow (for CSV:
`codec.csv.delimiter`, `quoteMode`, `lineEnding`, `charset`,
`dataTypeInSecondRow`, `codec.tabular.referenceMode`); anything else in the
header is ignored. Leave the configuration unset if the model should stay the
only source of truth.

### Error Handling

| Status | Meaning |
|---|---|
| `200 OK` | Success (a list response may be an empty container) |
| `400 Bad Request` | Non-numeric pagination value, missing or unconvertible declared query parameter |
| `404 Not Found` | Unknown DataSet path, or no object with the given id |
| `406 Not Acceptable` | The requested media type is not one of the DataSet's configured export formats |
| `500 Internal Server Error` | Reading from the backing input failed |

---

## Writing a Configuration

### Minimal Example: File Input over REST

The complete example — one file input, one DataSet, one REST service
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

A `JdbcDataSource` binds to an OSGi `DataSource` service by LDAP filter; the
`JPADataInput` references it. The deployment provides the actual `DataSource`
service — the image ships the daanse **PostgreSQL** provider and the driver, so
for PostgreSQL it is configuration only (see the next section); other databases
need their own provider bundle.

```xml
<dataSources id="persons-db" name="Persons DB" filter="(dataSourceName=personsDs)"/>
<dataInputs xsi:type="configuration:JPADataInput" id="persons-jpa" dataSource="persons-db">
  <supportedEClasses href="model/person.ecore#//Person"/>
</dataInputs>
```

Everything downstream (DataSets, services) is identical to the file case —
DataSets do not know what kind of input feeds them.

**The Data Atlas never creates the schema.** It is a serving layer, so
`eclipselink.ddl-generation` stays at the upstream default `none`. Without an
explicit mapping the names are *derived*, and the derivation is asymmetric:
the table is the EClass name **upper-cased**, the columns are the feature names
**verbatim**, and both are emitted unquoted — so on PostgreSQL an
`id`/`firstName`/`lastName` model reads `person(id, firstname, lastname)`. This
is undocumented upstream
([emf.persistence-jpa#252](https://github.com/eclipse-fennec/emf.persistence-jpa/issues/252)).
If your existing schema does not follow it, pin the names explicitly with
`JPADataInput.persistenceConfig` (an inline `eorm` `EntityMappings`) instead of
adapting the database.

### PostgreSQL End to End

A complete, runnable setup — PostgreSQL, a Model Atlas holding the
configuration, and one atlas-mode Data Atlas serving the table as CSV:

```bash
docker compose -f docker/dockercompose/docker-compose-postgres.yml up -d

curl -H "Accept: text/csv"        http://localhost:8081/rest/pg/persons   # CSV, ';' separated
curl -H "Accept: application/json" http://localhost:8081/rest/pg/persons  # also declared
curl -i -H "Accept: application/xml" http://localhost:8081/rest/pg/persons  # 406 - not declared

# the service is also declared for DCAT publication (see below): the setup
# includes a DCAT.Atlas, and the portal entry mirrors exactly the two exports
curl -H "Accept: application/rdf+xml" http://localhost:8084/rest/datasets/persons
```

The `DataSource` is created from a mounted Configurator file, so no credential
is baked into the image. One factory configuration is all it takes:

```json
"daanse.jdbc.datasource.postgresql.DataSource~persons": {
	"host": "postgres",
	"port:Integer": 5432,
	"dbname": "dataatlas",
	"user": "dataatlas",
	".password": "dataatlas",
	"dataSourceName": "personsDs"
}
```

Three details that are easy to get wrong:

- **`port` needs the typed key** `"port:Integer"`. A bare JSON number arrives as
  a `Long` and the component rejects it with
  `Invalid port number type: java.lang.Long`.
- **`dataSourceName` is not a key of that component.** Every non-dot key of the
  configuration also becomes a **service property**, which is exactly what the
  `JdbcDataSource` filter `(dataSourceName=personsDs)` selects. Keys starting
  with a dot (`.password`) deliberately stay out of the service properties.
- In a standalone Configurator file the directive is
  `:configurator:symbolic-name` — with the hyphen; the whole file is rejected as
  invalid JSON otherwise.

There is no `url` key — the URL is assembled from host/port/dbname. The
component wraps a `PGSimpleDataSource`, i.e. **no connection pool**; pooling is
EclipseLink's (`fennec.jpa.ext.eclipselink.jdbc.connection-pool.*`).

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

## Publishing to a DCAT Portal

A Data Atlas can register its data as open data with a
[DCAT.Atlas](https://github.com/eclipse-fennec/dcat.atlas) portal and keep the
portal in sync with the running configuration. Publication is **an option a
deployment adds**: nothing is published unless the configuration declares it,
and a runtime without the publication bundles installed is a complete Data
Atlas. If a configuration *does* declare publications while no handler bundle
is installed, the bootstrap logs a warning — the data services serve
regardless.

### Declaring a Publication

Publication is declared in the configuration model: a `DcatPublication` in
the root's `publications` registry, referenced from the `DataService` (a
`DataSet` can reference its own to override catalog or metadata):

```xml
<services xsi:type="configuration:RestDataService" id="persons-rest" name="Persons REST"
    description="REST endpoint publishing the example persons." urlContext="/example"
    publication="open-data">
  <configuration id="persons-rest-config" dataSet="persons" path="persons"/>
</services>
<publications id="open-data" catalog="example"
    publisherName="Eclipse Fennec Data Atlas example"
    licenseUri="http://dcat-ap.de/def/licenses/dl-by-de/2.0">
  <keywords>persons</keywords>
</publications>
```

The target `catalog` is expected to exist in the portal — creating catalogs
is deliberately not the Data Atlas's job. Metadata is **derived by default
and overridden explicitly**: title falls back to the provider's name,
description to the provider's description (else to the documentation
annotation of its model type). `publisherName` and `licenseUri` cannot be
derived and are required by the portal — leaving them out is a diagnosed
configuration error, logged with what is missing.

### Connecting the Portal

*Which* portal to talk to is deployment configuration, not part of the
model. Two pieces:

1. **The dcat.atlas client** — a Config Admin factory configuration (PID
   `org.eclipse.fennec.dcat.atlas.client`), injected like any other extra
   configuration via a mounted Configurator JSON:

   ```json
   {
     ":configurator:resource-version": 1,
     ":configurator:symbolic-name": "org.eclipse.fennec.data.atlas.dcat.client.config",
     ":configurator:version": "1.0.0",
     "org.eclipse.fennec.dcat.atlas.client~portal": {
       "dcat.portal": "portal",
       "base.uri": "http://dcatatlas:8080/rest/"
     }
   }
   ```

   With more than one portal configured, `DcatPublication.portal` selects by
   the `dcat.portal` name.

2. **The public base URL** — `DATA_ATLAS_PUBLIC_BASE_URL`, the address the
   portal's consumers reach this Data Atlas under (behind a reverse proxy
   that is not the address the container sees). It becomes the endpoint and
   distribution URLs in the portal; declared publications without it are a
   configuration error.

The [compose setup](../docker/dockercompose/docker-compose-dcat.yml) wires
all of this end to end: a portal, a one-shot catalog seeder, and a file-mode
Data Atlas publishing the example.

### What Reaches the Portal

Only **metadata and references** — payload never leaves the Data Atlas. The
mapping is DataService-first, matching the model's own structure:

| Data Atlas | Portal | Key content |
|---|---|---|
| `DataService` | `dcat:DataService` | endpoint URL = public base + `urlContext` |
| `DataSet` | `dcat:Dataset` | title/description/keywords/themes, linked `servesDataset` and into the catalog |
| resolved `DistributionExport` | `dcat:Distribution` | access URL = the endpoint that serves it, media type, license |

The distributions mirror exactly what the endpoint serves: the resolved
exports, or the runtime defaults (JSON and XMI) when none are declared. The
registration is idempotent and re-runs in full on every configuration change;
a provider that disappears from the configuration is **withdrawn** from the
portal. Identifiers are the configuration ids (overridable via
`DcatPublication.identifier`), so they survive restarts, reloads and a
redeployment against another portal.

### Robustness and Observability

The portal is never on the critical path: an unreachable, slow or rejecting
portal does not stop the Data Atlas from serving. Transient failures are
retried on an interval (`DATA_ATLAS_DCAT_RETRY_INTERVAL`); a portal-side
validation refusal is treated as a configuration error and not retried until
the configuration changes.

Every published provider is observable without reading the portal: the
handler registers one `PublicationStatus` service per provider
(`data.atlas.config.id` = provider id) whose `data.atlas.publication.state`
property is `PENDING`, `REGISTERED`, `RETRYING` or `ERROR`, with the last
error message and timestamp on the service — and every state change is
logged.

---

## Configuration Reference

Both images are configured through environment variables.

### Common

| Variable | Default | Description |
|----------|---------|-------------|
| `DATA_ATLAS_HTTP_PORT` | `8080` | HTTP port of the REST endpoints |
| `DATA_ATLAS_PUBLIC_BASE_URL` | *(unset)* | Public base URL this instance is reachable under, used as the endpoint/distribution base of [DCAT publications](#publishing-to-a-dcat-portal). Only needed when publications are declared |
| `DATA_ATLAS_DCAT_RETRY_INTERVAL` | `30000` | Retry interval (ms) for transiently failed DCAT portal registrations |

### File variant

| Variable | Default | Description |
|----------|---------|-------------|
| `DATA_ATLAS_CONFIG_URI` | `/opt/dataatlas/runtime/data/dataatlas.xmi` | Path of the `DataAtlasConfiguration` XMI. The image ships no file there - an absent one means *unconfigured*, not broken |

### Model Atlas variant

| Variable | Default | Description |
|----------|---------|-------------|
| `MODEL_ATLAS_BASE_URI` | `http://modelatlas:8080/atlas/rest` | Base URI of the Model Atlas REST API |
| `DATA_ATLAS_SCOPE` | `dataatlas` | Model Atlas scope holding the configuration |
| `DATA_ATLAS_REGISTRY` | `configurations` | Registry within the scope |
| `DATA_ATLAS_OBJECT_ID` | `dataatlas` | Object id of the `DataAtlasConfiguration` instance |
| `DATA_ATLAS_REFRESH_INTERVAL` | `300000` | Poll interval for configuration updates, in milliseconds (also bounds the client cache revalidation) |

### Docker Image Details

Both images are distroless Java 21 images (no shell, no package manager)
running as the non-root user `65532` — anything you mount must be readable by
that uid. Fixed layout:

| Path | Purpose |
|---|---|
| `/opt/dataatlas` | Application home (runtime jar) |
| `/opt/dataatlas/runtime/data` | Data directory, **empty in the image** - mount the configuration (plus the schemas and data it references) here |
| `/opt/dataatlas/runtime/etc/logback.xml` | Logging configuration — mount your own file over it to change logging |
| `/opt/dataatlas/runtime/log` | Log files |
| `/opt/dataatlas/runtime/secrets` | Secrets directory (see below) |
| `/tmp/dataatlas` | Temp directory |

**Secrets**: all configuration values support the Felix Config Admin
interpolation plugin. Besides the `$[env:…]` environment interpolation used
throughout this guide, `$[secret:<name>]` resolves to the content of the file
`/opt/dataatlas/runtime/secrets/<name>` — mount credential files there (e.g.
a docker/k8s secret with a database password) instead of passing them as
environment variables.

**Additional OSGi configuration**: the runtime uses the standard OSGi
Configurator, so extra configurations (e.g. a `DataSource` factory
configuration for a JPA deployment) can be injected without rebuilding the
image via `JAVA_TOOL_OPTIONS=-Dconfigurator.initial=file:///path/to/config.json`
pointing at a mounted Configurator JSON — the same pattern the
[compose setup](../docker/dockercompose/README.md) uses to configure the
Model Atlas. Note that a JPA deployment additionally needs the JDBC
driver/pool *bundles*, which do require a custom runtime assembly.

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
