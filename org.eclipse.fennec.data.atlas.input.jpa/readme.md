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
  `JdbcDataSource.filter` (e.g. a daanse `jdbc.datasource.*` bundle or an
  `org.osgi.service.jdbc.DataSourceFactory` setup); no provider is bundled.

The upstream configuration chain is documented in
[eclipse-fennec/emf.persistence-jpa](https://github.com/eclipse-fennec/emf.persistence-jpa)
(see [issue #193](https://github.com/eclipse-fennec/emf.persistence-jpa/issues/193)
for the end-to-end recipe).
