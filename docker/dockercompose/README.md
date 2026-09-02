# Data Atlas — Combined Compose Setups

> **The images carry no models, configuration or data.** Nothing in a Data
> Atlas image relies on files being present out of the box — whatever an example
> needs is **mounted in** from outside, or (for the schemas in atlas mode)
> deposited in the Model Atlas. That is why both setups below have `volumes:`
> entries pointing into `configuration.model/example/`.

The setups that live here:

| File | What it shows |
|---|---|
| [`docker-compose-atlas.yml`](#docker-compose-atlasyml--file-mode-vs-model-atlas-mode) | the same file-based example served in both config modes, side by side |
| [`docker-compose-postgres.yml`](#docker-compose-postgresyml--postgresql-served-as-csv) | a PostgreSQL table served as CSV, configuration delivered by a Model Atlas |
| [`docker-compose-dcat.yml`](#docker-compose-dcatyml--publishing-to-a-dcatatlas-portal) | the file-based example published to a DCAT.Atlas portal |
| [`docker-compose-history.yml`](#docker-compose-historyyml--the-sensinact-history-database-as-csv) | the SensiNact history database - a schema we do **not** own - served as CSV, configuration (with its explicit JPA mapping) delivered by a Model Atlas |

## docker-compose-atlas.yml — file mode vs. Model Atlas mode

Runs one Model Atlas plus two Data Atlas instances against the same example:

| Service | URL | Config source |
|---|---|---|
| `modelatlas` | http://localhost:8080/atlas/rest | — |
| `dataatlas-file` | http://localhost:8081/rest/example/persons | XMI file baked into the image |
| `dataatlas-atlas` | http://localhost:8082/rest/example/persons | retrieved from the Model Atlas |

Both Data Atlas instances must answer identically.

What is mounted where, and why:

- **file mode** — the whole `example/` folder becomes
  `/opt/dataatlas/runtime/data`, the directory `DATA_ATLAS_CONFIG_URI` defaults
  into (`<data>/dataatlas.xmi`). The example's relative hrefs (`model/…`,
  `data/…`) resolve against it.
- **atlas mode** — the *configuration* comes from the Model Atlas and the
  *schemas* from its registry, so neither is mounted. The example's
  `FileDataInput` still reads its **data** from a file, so `example/data` is
  mounted at the absolute path `dataatlas-atlas.xmi` names. A configuration
  whose inputs are all databases (like the Postgres setup) needs no mount at
  all.

How the Model Atlas side is assembled:

- **Custom scope/registry**: `modelatlas/load/dataatlas.json` is injected via
  `JAVA_TOOL_OPTIONS=-Dconfigurator.initial=…`. It defines the `dataatlas`
  scope and a `configurations` registry rooted at `Ecore#//EObject` — always
  resolvable at boot, and accepting arbitrary instances since the
  model.atlas#188 fix.
- **Schemas + instance**: the one-shot `seed` service uploads the schemas the
  example references (`eorm.ecore` — a committed copy from
  `org.eclipse.fennec.persistence.orm` 0.1.0-SNAPSHOT, `configuration.ecore`
  and `person.ecore` mounted straight from the repo; eorm first, since
  `configuration.ecore` references it) into **both stages** of
  `dataatlas/schema` (each stage resolves against its own package view; the
  draft stage needs the schemas for staged updates) and then the
  example `DataAtlasConfiguration` (`example/dataatlas-atlas.xmi`, nsURI-based
  hrefs) into the final `release` stage of `configurations`.

> Once model.atlas#175 (deploy the `InitialModelLoader` bundle) is resolved,
> the schema uploads can move from the seeder to an `initial-models` mount.

### Changing the configuration at runtime

Configuration changes reach a **running** atlas-mode instance through the
Model Atlas stage workflow — no restart:

```bash
# 1. upload the new version into the writable draft stage
curl -X POST -H "Content-Type: application/xmi" --data-binary @dataatlas-v2.xmi \
  "http://localhost:8080/atlas/rest/dataatlas/registries/configurations/stages/draft/dataatlas?name=dataatlas&override=true"

# 2. transition it to the final release stage
curl -X POST -H "Content-Type: application/json" \
  -d '{"objectId": "dataatlas", "targetStage": "release"}' \
  "http://localhost:8080/atlas/rest/dataatlas/registries/configurations/stages/draft/actions/transition"
```

The instance picks the change up within `DATA_ATLAS_REFRESH_INTERVAL`
(default 300000 ms) and applies it as a diff — unchanged DataSets keep
serving. A broken version fails hard (endpoints down, error in the log); a
corrected version recovers the instance on the next refresh. The file-mode
instance reacts to changes of its configuration file directly (io.fs.watcher).

## docker-compose-postgres.yml — PostgreSQL served as CSV

```bash
docker compose -f docker-compose-postgres.yml up
```

| Service | URL |
|---|---|
| `postgres` | `localhost:15432` (db/user/password: `dataatlas`) |
| `modelatlas` | http://localhost:8080/atlas/rest |
| `dataatlas` | http://localhost:8081/rest/pg/persons |
| `dcatatlas` | http://localhost:8084/rest |

```bash
# CSV, semicolon separated, no SQL-type row — as configured
curl -H "Accept: text/csv" http://localhost:8081/rest/pg/persons
# id;firstName;lastName
# p1;Ada;Lovelace
# p2;Grace;Hopper
# p3;Margaret;Hamilton

# the same DataSet declares a JSON export too
curl -H "Accept: application/json" http://localhost:8081/rest/pg/persons

# XMI is not declared, so it is refused
curl -i -H "Accept: application/xml" http://localhost:8081/rest/pg/persons   # 406
```

The configuration is
`org.eclipse.fennec.data.atlas.configuration.model/example/dataatlas-postgres-atlas.xmi`:
a `JdbcDataSource`, a `JPADataInput` over it, one DataSet, and two exports — a
`CSVDistributionExport` (`separator=";"`, `includeTypeHeader="false"`) and a
plain `DistributionExport` with `mediaType="application/json"`. Because the
DataSet references exports, **exactly** those two media types are served;
everything else is a `406`. Remove the JSON export and the same instance serves
CSV only.

The service is also declared for **DCAT publication** (a `DcatPublication` in
the same configuration), so the setup brings up a DCAT.Atlas the Data Atlas
registers with — notably, the declaration travels *inside the configuration
the Model Atlas delivers*, unlike `docker-compose-dcat.yml` where it comes
from a file. The portal entry mirrors exactly the declared exports: one
distribution for CSV, one for JSON, none for XMI, each with the IANA media
type IRI and the endpoint above as `accessURL`:

```bash
curl -H "Accept: application/rdf+xml" http://localhost:8084/rest/datasets/persons
curl -H "Accept: application/rdf+xml" http://localhost:8084/rest/datasets/persons/distributions/csv
```

The deployment pieces (portal client configuration, catalog seeder, public
base URL, the missing AGPL shapes) are the same as in
[docker-compose-dcat.yml](#docker-compose-dcatyml--publishing-to-a-dcatatlas-portal).

### The two pieces that are not in the configuration model

**The DataSource service.** A `JdbcDataSource` only carries an OSGi filter — the
deployment provides the actual `javax.sql.DataSource`. Here
`org.eclipse.daanse.jdbc.datasource.postgresql` does, configured through the
mounted [`dataatlas/load/datasource.json`](dataatlas/load/datasource.json) and
injected with `-Dconfigurator.initial=…`, so no credential is baked into the
image. Two details that file has to get right:

- the directive is `:configurator:symbolic-name` — **with** the hyphen; the
  Configurator rejects the whole file (`Invalid JSON`) otherwise;
- the port needs the typed key `"port:Integer"`. A bare JSON number becomes a
  `Long`, and the daanse component fails with
  `IllegalArgumentException: Invalid port number type: java.lang.Long`.

`dataSourceName=personsDs` is not a key of that component at all: every non-dot
key of a daanse DataSource configuration also becomes a **service property**,
which is what the configuration's filter `(dataSourceName=personsDs)` selects.
Keys starting with a dot (`.password`) deliberately do not become service
properties.

**The schema.** `postgres/init/*.sql` is mounted into
`/docker-entrypoint-initdb.d`. The Data Atlas is a serving layer and keeps
`eclipselink.ddl-generation` at the upstream default `none`, so it never creates
anything. The SQL must therefore match what the *derived* eorm mapping expects,
and that naming is asymmetric: **table names are upper-cased, column names are
verbatim**, both emitted unquoted, so PostgreSQL folds them to lower case
(undocumented upstream — see
[emf.persistence-jpa#252](https://github.com/eclipse-fennec/emf.persistence-jpa/issues/252)).
`DataAtlasPostgresIntegrationTest` runs the real mapping against this same SQL,
so the two cannot drift apart unnoticed. If your existing schema does not follow
that derivation, pin the names explicitly with a `JPADataInput.persistenceConfig`
instead of hand-tuning the SQL.

## docker-compose-dcat.yml — publishing to a DCAT.Atlas portal

Milestone 8 (data.atlas#4): the file-mode example, with its REST service
declared for open-data publication. The Data Atlas registers the service, its
dataset and the distributions with a DCAT.Atlas portal and keeps them in sync.

```bash
docker compose -f docker-compose-dcat.yml up
```

| Service | URL |
|---|---|
| `dcatatlas` | http://localhost:8084/rest |
| `dataatlas` | http://localhost:8082/rest/example/persons |

```bash
# the published dataset, as the portal serves it (RDF/XML)
curl -H "Accept: application/rdf+xml" http://localhost:8084/rest/datasets/persons

# its distribution's accessURL points back at the Data Atlas endpoint …
curl -H "Accept: application/rdf+xml" http://localhost:8084/rest/datasets/persons/distributions/json

# … and that URL resolves to the data
curl -H "Accept: application/json" http://localhost:8082/rest/example/persons

# taking the portal down does not disturb the Data Atlas
docker compose -f docker-compose-dcat.yml stop dcatatlas
curl -H "Accept: application/json" http://localhost:8082/rest/example/persons   # still 200
```

The configuration is
`org.eclipse.fennec.data.atlas.configuration.model/example/dataatlas-dcat.xmi` —
the plain example plus a `DcatPublication` (target catalog, publisher, license)
referenced from the `RestDataService`. Removing that one reference from the
mounted file withdraws the entries from the portal at runtime; adding it back
re-publishes them.

Three deployment pieces sit outside the configuration model, as they should:
the **portal client** (`dcat/load/dcatclient.json`, the dcat.atlas client's
factory configuration, injected via `configurator.initial`), the **public base
URL** (`DATA_ATLAS_PUBLIC_BASE_URL` — the address the portal's consumers reach
the Data Atlas under), and the **target catalog**, which is expected to exist:
the one-shot `catalog-seed` service creates it (`dcat/catalog.xmi`).

The portal runs **without the DCAT-AP.de SHACL shapes** here — they are
AGPL-3.0 and not distributed with either project. SHACL enforcement is off,
the portal's model validation stays on. A real deployment mounts the GovData
shapes and drops the two `SHACL_*` overrides (see the dcat.atlas compose
setup).

## docker-compose-history.yml — the SensiNact history database as CSV

```bash
docker compose -f docker-compose-history.yml up
```

| Service | URL | Config source |
|---|---|---|
| `history-db` | `localhost:15433` (db/user/password: `sensinact`) | — |
| `modelatlas` | http://localhost:8080/atlas/rest | — |
| `dataatlas` | http://localhost:8081/rest/history/numeric, `…/text`, `…/geo` | retrieved from the Model Atlas |
| `dataatlas-file` (profile `file`) | http://localhost:8082/rest/history/numeric, `…/text`, `…/geo` | `dataatlas-history.xmi`, mounted |

The difference to the Postgres example: that one serves a schema we invented and
shaped to fit the derived eorm naming. This one serves the **TimescaleDB store of
SensiNact's history provider** — lower-case, schema-qualified, hypertables, no
primary key. That is the realistic shape of "serve an existing database", and it
is what a production setup gets pointed at.

The configuration comes from the Model Atlas: the one-shot `seed` service uploads
the referenced schemas (eorm, configuration, person and `sensinact-history.ecore`)
into the `release` stage and then the instance `dataatlas-history-atlas.xmi`; the
Data Atlas runs in atlas mode and retrieves it from there. The file-mode twin
(`dataatlas-history.xmi`, the same configuration with relative `.ecore` hrefs)
can be started next to it for a side-by-side comparison — both must answer
identically:

```bash
docker compose -f docker-compose-history.yml --profile file up
```

There is deliberately no `event.atlas` here: the example is about *reading* the
history database, not about producing the data, so the schema and a few
recordings are seeded by SQL and the setup stands on its own.

```bash
curl -H "Accept: text/csv" http://localhost:8081/rest/history/numeric
# time;modelPackageUri;model;provider;service;resource;data
# 2026-08-27 14:59:39.419406;https://eclipse.org/sensinact/example/weather/1.0.0;weather;station-1;sensor;temperature;21.4
# 2026-08-27 14:59:39.419406;https://eclipse.org/sensinact/example/weather/1.0.0;weather;station-1;sensor;humidity;63.0
# 2026-08-27 14:39:39.419406;https://eclipse.org/sensinact/example/weather/1.0.0;weather;station-1;sensor;temperature;20.8

curl -H "Accept: text/csv" http://localhost:8081/rest/history/text
# time;modelPackageUri;model;provider;service;resource;data
# 2026-08-27 14:59:39.474253;…;weather;station-1;admin;status;ok
# 2026-08-27 14:54:39.474253;…;weather;station-2;admin;status;maintenance

# JSON is declared too
curl -H "Accept: application/json" http://localhost:8081/rest/history/numeric

# XMI is not declared, so it is refused
curl -i -H "Accept: application/xml" http://localhost:8081/rest/history/numeric   # 406
```

Note the CSV header: `time;modelPackageUri;model;…` — the **model** attribute
names, while the database columns are `time, modelpackageuri, model, …`. That gap
is exactly what the explicit mapping bridges.

### What the Model Atlas has to round-trip here

A configuration for a foreign schema must carry an explicit eorm `EntityMappings`,
and the eorm model mirrors JPA's `orm.xml`, so it uses `ExtendedMetaData` XML
names (`column-definition` on disk, `columnDefinition` in the model). Until
[model.atlas#213](https://github.com/eclipse-fennec/model.atlas/issues/213) was
fixed (2026-08-28), the Model Atlas accepted such an upload (`201`) but failed to
read its own file back with `FeatureNotFoundException: Feature 'column-definition'
not found` — which is why this example started out in file mode. The setup needs a
`model.atlas:file-snapshot` image that carries the fix (any image published from
the `snapshot` branch after that date).

The delivered configuration, inline mapping included, can be inspected in the
Model Atlas:

```bash
curl -H "Accept: application/xmi" \
  "http://localhost:8080/atlas/rest/dataatlas/registries/configurations/stages/release/content?objectId=dataatlas"
```

### The three things this example had to solve

**1. The names.** The derived eorm mapping expects an upper-cased, unqualified
table (`NUMERICDATA`); the real relations are `sensinact.numeric_data_recent`. So
the `JPADataInput` carries a `persistenceConfig` — an explicit `EntityMappings`
pinning table, schema and every column name. It is **inline** in the
configuration, not an href to a file: `persistenceConfig` is a containment
reference precisely so the mapping travels with the configuration.

**2. No primary key.** The hypertables have none — upstream defines them that way,
and it is not our schema to change. JPA needs an identity, so the mapping declares
a **composite id** over `(time, provider, service, resource)`, the natural reading
of a time series per resource. Four `<id>` elements; the model allows it
(`Attributes.id` is `[0..*]`).

**3. Unbounded tables.** A hypertable that has been recording for a month must not
become one CSV response. The endpoint stays a plain dump (no query filters), so
the bound lives in SQL: the entities are mapped onto the views
`sensinact.numeric_data_recent` / `text_data_recent` (7-day window, `LIMIT 1000`).
Pointing `<table>` at `numeric_data` / `text_data` serves the raw tables instead.

### Keeping the seeded schema honest

`history/init/01-schema.sql` is a **verbatim** transcription of the statements
`TimescaleHistoricalStore#setupTables` executes (verified against `75f1f45`,
including the nine indexes). It is not ours to change — if it drifts from
upstream, upstream wins.

What the store *presupposes* rather than creates lives separately in
`00-extensions.sql`: it calls `create_hypertable()` but never creates the
`timescaledb` extension, because a real deployment's database already has it. A
database freshly created by `POSTGRES_DB` does not, so seeding the store's DDL
alone fails with `function create_hypertable(...) does not exist`.

`DataAtlasHistoryIntegrationTest` runs the real eorm mapping against these very
files in a docker-gated test, so a drift that breaks the mapping fails the build
instead of production. That matters more here than in the Postgres example,
because the reference DDL is upstream and can change.

### Geodata: it works, and PostGIS stays in the database

`geo_data` is served too — `http://localhost:8081/rest/history/geo`:

```bash
curl -H "Accept: text/csv" http://localhost:8081/rest/history/geo
# time;modelPackageUri;model;provider;service;resource;location;longitude;latitude
# 2026-08-27 …;…;weather;station-1;admin;location;POINT(11.582 50.927);11.582;50.927
```

Its column is `geography(POINT,4326)`, a PostGIS type with no JDBC
representation the persistence stack knows. Rather than teaching the Data Atlas
about PostGIS, the view projects it into ordinary SQL types:

```sql
ST_AsText(data)            AS location   -- text
ST_X(data::geometry)       AS longitude  -- double precision
ST_Y(data::geometry)       AS latitude   -- double precision
```

Those map to plain `EString`/`EDoubleObject` attributes with **no type converter
at all**. The geometry work happens in the database, where the geometry already
lives — and the lon/lat pair is exactly the shape a future GeoJSON service wants.

Serving the raw `geography` column instead would need three things the runtime
does not have today: a modelled `EDataType` for the geometry, a `TypeConverter`
(`org.eclipse.fennec.persistence.api`, matched by the attribute's instance type
name) turning the driver's `PGobject`/EWKB into it, and either PostGIS JDBC types
or WKB parsing — no PostGIS JDBC artefact is in any of our repository indexes.
Worth doing when a service needs real geometry objects; not worth doing to
produce CSV.

---

## Adding a history CSV endpoint to an existing deployment

The compose file above is a demo. This is the recipe for a real setup — an
existing Data Atlas that should additionally serve an existing SensiNact history
database. Nothing here depends on the compose file.

### 1. What has to exist

- a reachable PostgreSQL/TimescaleDB carrying the `sensinact` schema (the one
  SensiNact's history provider writes into) and a user that may `SELECT` on it;
- a Data Atlas instance, `file-snapshot` or `atlas-snapshot`. The image already
  contains the PostgreSQL driver, the daanse DataSource provider and the CSV
  codec, so **no custom runtime assembly is needed**.

### 2. Bound what you serve

The Data Atlas never creates or changes anything in the database — it stays
read-only, and `eclipselink.ddl-generation` stays `none`. Create views next to the
hypertables and point the mapping at them, so a response can never be unbounded:

```sql
CREATE OR REPLACE VIEW sensinact.numeric_data_recent AS
    SELECT time, modelpackageuri, model, provider, service, resource, data
    FROM sensinact.numeric_data
    WHERE time > now() - INTERVAL '7 days'
    ORDER BY time DESC
    LIMIT 1000;
```

Adjust the window and the limit to the deployment. If you prefer no views, point
the `<table>` elements at the hypertables and accept the consequence.

### 3. Provide the DataSource

One Config-Admin factory configuration, injected without rebuilding the image:

```json
{
	":configurator:resource-version": 1,
	":configurator:symbolic-name": "my.deployment.history.datasource",
	":configurator:version": "1.0.0",
	"daanse.jdbc.datasource.postgresql.DataSource~history": {
		"host": "history-db.internal",
		"port:Integer": 5432,
		"dbname": "sensinact",
		"user": "dataatlas_ro",
		".password": "$[secret:history-db-password]",
		"dataSourceName": "historyDs"
	}
}
```

Mount it and point the JVM at it:

```
-v /path/to/load:/opt/dataatlas/runtime/load:ro
-e JAVA_TOOL_OPTIONS=-Dconfigurator.initial=file:///opt/dataatlas/runtime/load/datasource.json
```

Four details that each cost a debug cycle:

| Detail | Why |
|---|---|
| `:configurator:symbolic-name` **with** the hyphen | otherwise the Configurator rejects the whole file as `Invalid JSON` |
| `"port:Integer"`, not `"port"` | a bare JSON number arrives as `Long` and the component refuses it |
| `dataSourceName` is **not** a key of the component | every non-dot key also becomes a *service property* — that is what the configuration's filter selects |
| `.password` starts with a dot | dot keys deliberately stay out of the service properties |

`$[secret:history-db-password]` resolves from
`/opt/dataatlas/runtime/secrets/history-db-password` — mount a docker/Kubernetes
secret there rather than passing the password as an environment variable.

### 4. Publish the domain schema

The Data Atlas needs the EPackage describing the rows.
`configuration.model/example/model/sensinact-history.ecore` (nsURI
`https://eclipse.org/fennec/data/atlas/example/sensinact/history/1.0.0`) can be
used as-is or copied under your own nsURI.

- **file mode**: put the `.ecore` next to the configuration file and reference it
  by a relative href, exactly as `dataatlas-history.xmi` does.
- **atlas mode**: upload it into the scope's schema registry and reference it by
  **nsURI**. It must be present in *every* stage that serves the configuration,
  because each stage resolves against its own package view.

### 5. The configuration

Take `configuration.model/example/dataatlas-history.xmi` (file mode) or
`dataatlas-history-atlas.xmi` (atlas mode) as the template and change:

- `dataSources/@filter` — must match the `dataSourceName` from step 3;
- the `<table name=… schema=…/>` elements in the inline `persistenceConfig` — the
  relations from step 2;
- `urlContext` and the `path` of each configuration — where the endpoints appear;
- the `exports` — which formats the DataSets offer. **Declaring exports defines
  the list**: a DataSet that references an export serves exactly those media types
  and answers `406` for anything else. Declare JSON explicitly if you want it next
  to CSV.

Which names have to agree, and this is the whole list:

| This | must equal | that |
|---|---|---|
| `dataSources/@filter` | ↔ | a service property of the DataSource (step 3) |
| `dataSets/@dataInput` | ↔ | `dataInputs/@id` |
| `configuration/@dataSet` | ↔ | `dataSets/@id` |
| `dataSets/@distributionExport` | ↔ | `exports/@id` |
| the mapping's `<table>` | ↔ | the relation in the database |
| every `<column name=…>` | ↔ | the column in the database |
| the mapping's `eclass`/`feature` hrefs | ↔ | the published `.ecore` (step 4) |

### 6. Verifying, and what failure looks like

```bash
curl -H "Accept: text/csv" http://<host>:8080/rest/history/numeric
```

The Data Atlas refuses to publish an endpoint it cannot serve, so a `404` — not an
error page — is the normal symptom of a misconfiguration. Read the log rather than
guessing:

| Symptom | Look for |
|---|---|
| `404` on the path | `Registering REST application for service …` — absent means the input never materialized. A wrong `dataSources/@filter` is the usual cause: no DataSource matches, so no repository appears |
| `404`, input realized | `Realized JPADataInput …` present but no REST line — a DataSet was skipped; the log names it and why |
| `406` | the media type is not among the DataSet's declared exports |
| `500` | logged with its cause; a wrong table or column name in the mapping surfaces here |
| endpoint serves nothing | the view's time window excludes all rows — check `select count(*)` on the *view*, not the table |
