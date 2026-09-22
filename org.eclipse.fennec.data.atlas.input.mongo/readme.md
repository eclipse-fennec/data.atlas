# Data Atlas MongoDB Input

Translates every `MongoDataInput` configuration object (registered as an OSGi
service by the bootstrap) into the fennec Mongo repository factory
configuration. Like `input.jpa`, the bundle registers no runtime service of its
own — the read-only repository published by `fennec.repository.mongo` **is** the
input's runtime representation, picked up by the endpoint configurators via
`persistence.repository.id` = the input's `id`.

## Model → configuration mapping

One `MongoDataInput` with id `<id>` becomes (factory configuration name = `<id>`):

| Factory PID | Key | Value |
|---|---|---|
| `fennec.repository.mongo` | `repositoryId` | `<id>` |
| | `database.target` | the `MongoDataSource.filter` of the input's data source, verbatim (BIND) — or `(mongo.database.alias=dataAtlas.<dataSourceId>)`, the alias the datasource configurator gave the database it materialized (MATERIALIZE) |
| | `readOnly` | `true` — the Data Atlas is a serving layer |

The configuration is deleted when the input's configuration service goes away.

## What the backend needs

- A `com.mongodb.client.MongoDatabase` service matching the target: either
  materialized from the `MongoDataSource` by the datasource configurator (see
  its [readme](../org.eclipse.fennec.data.atlas.datasource/readme.md)) or
  configured by the deployment through the upstream PIDs
  `persistence.mongo.client` / `persistence.mongo.database` and selected by the
  definition's `filter`.
- The EPackages of the input's `supportedEClasses` as services — the bootstrap
  registers the packages the configuration references. No mapping is involved:
  the backend (de)serializes documents through the fennec BSON codec directly
  from the Ecore metadata.
- Documents that follow the codec's layout: the EMF id as `_id` (a composite id
  as a structured sub-document), the concrete type as `_type` URI, references as
  URIs. Collections written by the same backend do; arbitrary foreign
  collections may not. See the upstream
  [MongoDB user guide](https://github.com/eclipse-fennec/emf.persistence-jpa/blob/snapshot/docs/mongo-user-guide.md)
  for the layout and its knobs.

The Mongo client is liveness-gated upstream: until MongoDB answers a `ping` the
database service, the repository and therefore the input's DataSets simply do
not exist — no error is reported, the endpoints answer 404. Recovery is
automatic.
