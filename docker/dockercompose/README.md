# Data Atlas — Combined Compose Setups

Two setups live here:

| File | What it shows |
|---|---|
| [`docker-compose-atlas.yml`](#docker-compose-atlasyml--file-mode-vs-model-atlas-mode) | the same file-based example served in both config modes, side by side |
| [`docker-compose-postgres.yml`](#docker-compose-postgresyml--postgresql-served-as-csv) | a PostgreSQL table served as CSV, configuration delivered by a Model Atlas |

## docker-compose-atlas.yml — file mode vs. Model Atlas mode

Runs one Model Atlas plus two Data Atlas instances against the same example:

| Service | URL | Config source |
|---|---|---|
| `modelatlas` | http://localhost:8080/atlas/rest | — |
| `dataatlas-file` | http://localhost:8081/rest/example/persons | XMI file baked into the image |
| `dataatlas-atlas` | http://localhost:8082/rest/example/persons | retrieved from the Model Atlas |

Both Data Atlas instances must answer identically.

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

