# Data Atlas JPA Input

Translates every `JPADataInput` configuration object (registered as an OSGi
service by the bootstrap) into the fennec persistence factory configurations.
The bundle registers no runtime service of its own — the read-only repository
published by `fennec.repository.jpa` **is** the input's runtime representation,
picked up by the endpoint configurators via
`persistence.repository.id` = the input's `id`.

## Model → configuration mapping

One `JPADataInput` with id `<id>` becomes (factory configuration name = `<id>`):

| Factory PID | Key | Value |
|---|---|---|
| `fennec.jpa.EORMMappingService` *(only without `persistenceConfig`)* | `fennec.jpa.eorm.model.target` | `(emf.nsURI=<nsURI of the supportedEClasses' EPackage>)` |
| | `fennec.jpa.eorm.eClasses` | the names of `supportedEClasses` (explicit — there is no "map all") |
| | `fennec.jpa.eorm.mappingName` | `<id>` |
| `fennec.jpa.EMPersistenceUnit` | `fennec.jpa.persistenceUnitName` | `<id>` |
| | `fennec.jpa.dataSource.target` | the `JdbcDataSource.filter` of the input, verbatim |
| | `fennec.jpa.mapping.target` | `(fennec.jpa.eorm.mapping=<id>)`, or the registered mapping service (below) |
| `fennec.repository.jpa` | `repositoryId` | `<id>` *(these keys are unprefixed)* |
| | `unit.target` | `(osgi.unit.name=<id>)` |
| | `readOnly` | `true` — the Data Atlas is a serving layer |

When the input carries a `persistenceConfig` (an eorm `EntityMappings`), no
mapping is derived: the object is registered directly as an `EntityMappings`
service (property `data.atlas.config.id=<id>`) and the persistence unit targets
it.

All configurations (and the mapping service) are deleted when the input's
configuration service goes away — full teardown, verified by the OSGi
integration tests.

## Constraints

- The mapping-free path requires all `supportedEClasses` in **one** EPackage;
  the EPackage must be available as a service (the bootstrap registers the
  packages referenced by the configuration).
- `eclipselink.ddl-generation` stays at the upstream default `none`: the input
  serves an existing schema. Tests seed through their own writable persistence
  unit with DDL generation enabled.
- A deployment needs a `javax.sql.DataSource` service matching the
  `JdbcDataSource.filter`. The runtime ships the daanse PostgreSQL provider and
  the driver (see below); other databases need their own provider bundle.
- The derived mapping names the table after the EClass **upper-cased** and the
  columns after the features **verbatim**, both unquoted — so on a case-folding
  database like PostgreSQL an `id/firstName/lastName` model reads
  `person(id, firstname, lastname)`. That asymmetry is undocumented upstream
  ([emf.persistence-jpa#252](https://github.com/eclipse-fennec/emf.persistence-jpa/issues/252)).
  For an existing schema that does not follow it, pin the names explicitly with
  `persistenceConfig` rather than guessing.

The upstream configuration chain is documented in
[eclipse-fennec/emf.persistence-jpa](https://github.com/eclipse-fennec/emf.persistence-jpa)
(see [issue #193](https://github.com/eclipse-fennec/emf.persistence-jpa/issues/193)
for the end-to-end recipe).

## Providing the DataSource: PostgreSQL

The runtime contains `org.eclipse.daanse.jdbc.datasource.postgresql` and the
`org.postgresql.jdbc` driver, so a PostgreSQL deployment needs configuration
only — one factory configuration of

```
daanse.jdbc.datasource.postgresql.DataSource~<name>
```

| Key | Value |
|---|---|
| `host` | database host (default `localhost`) |
| `port:Integer` | **typed**: a bare number arrives as `Long` and is rejected |
| `dbname` | database name |
| `user` | user name |
| `.password` | password — the leading dot keeps it out of the service properties |
| *any other key* | becomes a service property, so it can be selected by filter |

There is no `url` key; the URL is assembled from host/port/dbname. The component
wraps a `PGSimpleDataSource` — **no connection pool**; pooling is EclipseLink's
(`fennec.jpa.ext.eclipselink.jdbc.connection-pool.*`).

The last row is how a `JdbcDataSource` finds it: add an arbitrary key such as
`dataSourceName=personsDs` to the configuration and filter on it.

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

```xml
<dataSources id="persons-db" name="Persons DB" filter="(dataSourceName=personsDs)"/>
```

A complete, running example is `docker/dockercompose/docker-compose-postgres.yml`
(see its [README](../docker/dockercompose/README.md)); the sibling factory PIDs
`…ConnectionPoolDataSource` and `…XADataSource` exist for the other
`DataSource` flavours.

