# The Data Atlas Configuration Model

`model/configuration.ecore` (nsURI
`https://eclipse.org/fennec/data/atlas/configuration/1.0.0`, generated into
`org.eclipse.fennec.data.atlas.configuration`, prefix `DAConfig`) describes —
independent of any concrete deployment — everything a Data Atlas instance does:
which data sources provide data, which DataSets are published, which
DataServices expose them, which reusable export templates are used for
serialization, and which transformations map between models.

## Root: `DataAtlasConfiguration`

The root describes **exactly one Data Atlas instance**. Multiple instances can
run side by side (e.g. to spread load); each is fed its own configuration,
obtained either from the file system or by retrieving it from the Model Atlas.

The root acts as a set of registries — definitions live here exactly once and
are only *referenced* from the rest of the model:

| Containment | Type | Purpose |
|---|---|---|
| `dataSources` | `DataSource` (`JdbcDataSource`, `MongoDataSource`) | Reusable data source definitions: either *bound* to a backend service the deployment configured (LDAP `filter`) or *materialized* from their connection coordinates by the runtime |
| `dataInputs` | `DataInput` | The inputs that stream EObjects into the instance |
| `dataSets` | `DataSet` | The published datasets (DCAT Dataset equivalents) |
| `services` | `DataService` | The endpoints this instance publishes |
| `exports` | `DistributionExport` | Reusable serialization templates (e.g. CSV settings) |
| `transformations` | `Transformation` | Data and query transformations |
| `publications` | `DcatPublication` | Opt-in open-data publication declarations (target catalog + metadata overrides) |

This registry design is deliberate: the same service definition can be
re-applied to another data source (tenant, test system) by swapping the
referenced `DataSource`, and export settings are templates instead of
per-provider copies.

## Implementation status markers

The model is ahead of the runtime in places. Every classifier, feature or
operation that no runtime component consumes yet carries the annotation

```xml
<eAnnotations source="https://eclipse.org/fennec/data/atlas/configuration/status">
  <details key="implementation" value="not implemented yet"/>
  <details key="note" value="…what happens if it is configured…"/>
</eAnnotations>
```

so that editors and tooling can flag it, and so that the list below has a
single source of truth (grep the ecore for the annotation source). Marked
today (2026-09-23):

| Element | Effect when configured |
|---|---|
| `XMLADataService`, `XMLADataServiceConfiguration` | ignored — no XMLA endpoint configurator |
| `GraphQLDataService`, `GraphQLDataServiceConfiguration` | ignored — no GraphQL endpoint configurator |
| `QGisDataService`, `QGisDataServiceConfiguration` | ignored — no QGis endpoint configurator |
| `OgcFeaturesDataService`, `OgcSensorThingsDataService` | ignored — no OGC endpoint configurators |
| `QueryTransformation`, `BridgeRepository.queryTrafo` | recognized but not executed: a bridge with a `queryTrafo` is refused and stays down |
| `BridgeRepository.filter` | placeholder, no runtime effect |
| `DataSet.childDataSet`, `DataSet.parentDataSet` | no runtime effect |
| `RestDataService.openAPI` | no OpenAPI document is generated; the value is ignored |
| `DataInput.streamData()`, `DataService.getDistributions()` | legacy operations of the first draft; the generated implementations throw `UnsupportedOperationException` |

Remove the annotation together with the implementation.

## Core concepts

### `DataProvider` — the common supertype and the override-else-default trias

`DataService` and `DataSet` share the abstract supertype `DataProvider`:
identity (`id`, `name`, `description`) plus the **data configuration trias**
`dataInput`, `transformation`, `distributionExport[]`.

A `DataSet` *refines* the `DataService` it is published by: if the DataSet sets
a trias feature itself, that value overrides the service's; if it leaves it
unset, the service's value applies (**override-else-default**). For
`distributionExport` the DataSet's list, if non-empty, fully replaces the
service's.

### `DataInput` — where data comes from

Abstract base for anything that streams EObjects into the instance;
`supportedEClasses` names the model types (resolved against the Model Atlas)
an input can provide. Concrete types:

- `FileDataInput` — EMF resources from a `uri` (file or directory). The
  database-free input used by the Milestone 1 slice.
- `JPADataInput` — relational source: references a `JdbcDataSource` from the
  registry and optionally a JPA `EntityMappings` (the `eorm` model of
  `org.eclipse.fennec.persistence.orm`) describing how model types map to the
  relational schema.
- `MongoDataInput` — MongoDB source: references a `MongoDataSource` from the
  registry. No mapping — the fennec Mongo backend reads the collections through
  the BSON codec from the registered EPackages, so documents have to follow its
  layout (EMF id as `_id`, `_type` discriminator, references as URIs).
- `BridgeRepository` — wraps another `DataInput` and applies a
  `DataTransformation` to loaded objects and a `QueryTransformation` to
  incoming queries; cascadable.

### `DataSource` — where a database input connects to

Abstract base of the `dataSources` registry: `id`, `name`, `description` and an
optional `filter`. `DatabaseDataSource` adds the connection coordinates
(`host`, `port`, `database`, `user`, `password`, `properties` — a list of
`ConnectionProperty` key/value pairs passed through to the driver); the
concrete types add what their backend needs:

- `JdbcDataSource` (`driver`: `postgresql` | `h2`, `schema`) — realized as a
  `javax.sql.DataSource` service, referenced by `JPADataInput.dataSource`.
- `MongoDataSource` (`authSource`, `flavor`) — realized as a `MongoDatabase`
  service of the fennec Mongo backend, referenced by `MongoDataInput.dataSource`.

A definition is realized in exactly one of two modes, validated at
registration (both or neither = error, the definition is skipped and its
inputs stay down):

| Mode | Declared by | Realization |
|---|---|---|
| **BIND** | `filter` only | The deployment configured the backend service (e.g. in a mounted Configurator file); the filter selects it. The pre-existing behaviour. |
| **MATERIALIZE** | coordinates, no `filter` | The `datasource` bundle creates the backend's factory configurations from the definition and removes them with it. |

**Credential rule.** The model never carries a credential value. `user` and
`password` must be exactly one placeholder — `$[env:NAME]` (environment
variable) or `$[secret:NAME]` (file in the runtime's secrets directory) — that
the Felix Config Admin interpolation plugin resolves in the consuming runtime.
A literal or a placeholder with a default is refused. The rule follows from
the Model Atlas config mode: what a registry serves has to be treated as
readable by anyone who reaches it. Details and the host allow-list for
materialization: the [datasource readme](../org.eclipse.fennec.data.atlas.datasource/readme.md).

### `DataService` — how data is exposed

Abstract endpoint definition (`urlContext` = base path). A service can act
standalone — serving its own `dataInput` generically — or contain per-DataSet
`DataServiceConfiguration`s (the binding service → dataset, containment).
Every configuration of a DataService must result in a DCAT Distribution.

Concrete services: `RestDataService` (identity, `urlContext` and the `openAPI`
marker; everything needed to serve a DataSet is on its per-dataset
`RestDataServiceConfiguration`: `path`, `batchSize`, `batchSizeLimit`,
`offsetParameterName`, `limitParameterName`), `GeoJsonDataService` and
`ODataDataService` (both see below, same split),
plus placeholders for `GraphQLDataService`,
`XMLADataService` (OLAP/Daanse), `QGisDataService` (generated QGis layer
configuration), `OgcFeaturesDataService` and `OgcSensorThingsDataService`.

**`GeoJsonDataService`** publishes DataSets as RFC 7946 GeoJSON
(`application/geo+json`): `GET {path}` returns a `FeatureCollection`,
`GET {path}/{id}` a single `Feature`. It is a dedicated kind because GeoJSON
needs mapping configuration the generic REST service has no place for: the
per-dataset `GeoJsonDataServiceConfiguration` names the geometry source —
**either** `geometryFeature` (a feature already holding a `org.geojson.model`
`Geometry`, passed through) **or** the `longitudeFeature`/`latitudeFeature`
attribute pair (mapped to a `Point`, `elevationFeature` optionally third) —
plus an optional `idFeature` (default: the type's EMF id attribute). All
attributes not consumed by the geometry become the Feature's `properties`.
Coordinates are WGS 84 (RFC 7946 mandates it); transforming them is a
`Transformation` concern. A mapping that names missing or non-numeric
features is a diagnosed configuration error — the endpoint stays down.

**`ODataDataService`** publishes DataSets as one OData v4.01 **service root**
(OData Part 2 §3: any URL the provider chooses — here the `urlContext`):
`GET {urlContext}/` is the service document, `{urlContext}/$metadata` the
CSDL, `{urlContext}/{entitySetName}` an entity set with the full read query
surface (`$filter`, `$orderby`, `$top`/`$skip`, `$count`, `$select`,
`$expand`, `$apply`, `$search`), read-only. One root publishes one entity
data model: exactly the sets its configurations declare, nothing else that
happens to be registered in the runtime. The per-dataset
`ODataDataServiceConfiguration` carries `entitySetName` (default: the
`outputType`'s EClass name — OData addresses sets by type, so one
configuration per type) and `batchSizeLimit` (the root's server-side `$top`
ceiling; the smallest declared limit wins). The entity type is the DataSet's
`outputType`; it needs an entity key — an `iD` attribute or the
`idFeatures` annotation of the fennec persistence stack (composite keys) —
and a DataSet with a `query` cannot be served (its base predicate would have
to be composed with `$filter`): both are diagnosed configuration errors that
keep the entity set down. Because the OData backend scopes by package, all
types of one package served by one root must come from the same `DataInput`.

### `DistributionExport` — reusable serialization templates

Defined once in the `exports` registry and referenced via
`DataProvider.distributionExport` (override-else-default like the rest of the
trias: a `DataSet`'s own exports fully replace the enclosing service's).

`mediaType` names the HTTP media type an export is served as. Unset means the
kind-specific default of the concrete export, so a `CSVDistributionExport`
needs no `mediaType`; a format without a dedicated subclass — JSON, XMI — is
expressed as a plain `DistributionExport` with `mediaType` set.

`CSVDistributionExport` carries `separator`, `compressed` and
`includeTypeHeader`. All three map onto fennec codec option keys rather than
being reimplemented, which pins their semantics:

| Attribute | Codec option | Meaning |
|---|---|---|
| `separator` | `codec.csv.delimiter` | Field delimiter; only the first character is used |
| `includeTypeHeader` | `codec.csv.dataTypeInSecondRow` | Emits an extra **SQL-type row** between header and data. The column header row is always written and cannot be switched off |
| `compressed` | *(selects the media type)* | `application/x-csv-zip` — a ZIP with one CSV per serialized EClass, **not** a gzipped single CSV |

**How the effective formats are resolved at runtime**: a `DataProvider` that
resolves to no export at all is served in the runtime's default formats
(`application/json`, `application/xml`). As soon as it resolves to at least one
export, exactly those media types are served and any other `Accept` is answered
with `406 Not Acceptable` — so a configuration that wants CSV *and* JSON must
declare both.

### `Transformation`

`DataTransformation` (model A → model B) and `QueryTransformation` (maps
incoming queries onto the underlying source; not executed yet — a
`BridgeRepository` with a configured `queryTrafo` stays down).

A `DataTransformation` references its executable as an **EObject**: the
`transformation` reference points at the QVT-O AST — an
`OperationalTransformation` of the fennec m2x `qvtoperational` metamodel —
inside a **CompiledUnit document** (`#//@unit`). A bare parsed AST is not
storable (the parser's satellites live outside any resource), so authoring
compiles the `.qvto` text once into that self-contained document; see
[`example/trafo/`](example/trafo/) for the source, the generated documents and
the generator. `supportedEClasses`/`resultEClasses` must name exactly one
EClass each — Data Atlas transformations are **1:1 by contract** (one source
object maps to one result object with the same id), which is what makes
pagination push-down and by-id lookups through a bridge correct.

In file mode the document is referenced relative to the configuration. A
configuration served by a Model Atlas names it either by an **absolute URI**
the runtime resolves locally (like every `FileDataInput` in that mode) or by
an **EObject registry reference**
`eobject-registry://<registry>/<key>#<fragment>`: the document lives in a
Model Atlas registry (the m2x metamodels seeded as schemas — their
cross-references are nsURI-based since emf.m2x#246), reaches the runtime
through the model.atlas `AtlasEObjectProvider` feeding a local emf.osgi
`EObjectRegistry`, and the atlas bootstrap resolves the reference against
that registry. A registry or key that does not exist *yet* is transient —
the bootstrap keeps the last good configuration and re-applies when the
entry appears.

### `DcatPublication` — opt-in open-data publication

Declares that a `DataProvider` referencing it (via `DataProvider.publication`)
is published to a DCAT portal (DCAT.Atlas). **Absent declaration means not
published** — nothing is published implicitly, and a configuration without any
publication is valid and complete. The reference follows override-else-default:
a `DataService`'s declaration applies to its DataSets unless a `DataSet`
references a `DcatPublication` of its own.

The element is deliberately **plain data** — `catalog` (the target catalog id,
expected to exist in the portal), an optional `portal` name (matching the
`dcat.portal` service property of a configured dcat.atlas client), an optional
`identifier` override, and metadata that is *derived by default and overridden
explicitly*: `title` (else the provider's name), `description` (else the
provider's description, else the GenModel documentation of its model type),
`language`, `keywords`, `themes`, `publisherName`/`publisherUri` and
`licenseUri`. `publisherName` and — as soon as distributions are served —
`licenseUri` are required by the portal's shapes and not derivable: leaving
them unset is a diagnosed configuration error, not a silent omission.

The mapping to `dcat:DataService`/`dcat:Dataset`/`dcat:Distribution` lives in
the omittable `org.eclipse.fennec.data.atlas.publication.dcat` bundle; this
model never depends on a DCAT model (data.atlas#4, DA-DCAT-1). The portal
endpoint itself (base URL, credentials) is deployment configuration — the
dcat.atlas client's Config-Admin factory configuration — never part of this
model.

## Runtime constraints (not yet formally validated)

- A `DataService` that does not contain any `DataSetConfiguration` must define
  its own `dataInput`.
- `DataSet.inputType` must be provided by the resolved input
  (`supportedEClasses`), and `outputType` must equal `inputType` unless a
  `transformation` maps between them.
- `id` values are used as XMI IDs and as identifying OSGi service properties
  at runtime — they must be unique per configuration.

## Example

[`example/dataatlas.xmi`](example/dataatlas.xmi) describes the Milestone 1
vertical slice: one `FileDataInput`
([`example/data/persons.xmi`](example/data/persons.xmi), model
[`example/model/person.ecore`](example/model/person.ecore)) published by one
`RestDataService` at `/example/persons`.
[`example/dataatlas-transformation.xmi`](example/dataatlas-transformation.xmi)
adds the Milestone 6 slice on the same data: a `BridgeRepository` applies the
QVT-O transformation
[`example/trafo/person-to-public.qvto`](example/trafo/person-to-public.qvto)
and serves `PublicPerson` projections at `/example-public/public-persons`.
[`example/dataatlas-geo.xmi`](example/dataatlas-geo.xmi) is the Milestone 5
slice: points of interest ([`example/model/poi.ecore`](example/model/poi.ecore),
[`example/data/pois.xmi`](example/data/pois.xmi)) served as a GeoJSON
`FeatureCollection` at `/geo/pois`.
[`example/dataatlas-odata.xmi`](example/dataatlas-odata.xmi) is the Milestone
10 slice: the persons served as the OData entity set `Persons` of the service
root `/odata/persons`.

## Open modeling questions

- `DataSet.inputType`/`outputType` as plain `EClass` references may be too
  simple — resolving non-containment references to other DataSets /
  Distributions may need a genmodel-like approach.
- `BridgeRepository.filter` is a placeholder (future security
  filters/anonymization rules).
- DCAT: all DCAT texts should be generated from the models; texts that do not
  fit into a model should be referencable from other sources. OData is a
  candidate backend for DCAT + SPARQL.
- QGis: one EClass is one layer; the QGis file (SELECT + display-name mapping
  per layer) is generated, consumed by a QGis server providing OGC
  Features/Maps.

## Appendix — original notes (German, kept verbatim)

<details>
<summary>Raw brainstorming notes that motivated the registry design</summary>

* Exports und DataSources extra
* Exports CSV will man Separator konfigurieren
* DataSources wiederverwenden

### Stefan:

Ich würde nochmal an dem Konfigurationsmodell so ein bisschen erzählen oder was ich mir vorstelle, wie so eine Konfiguration laufen kann oder welche Rahmenbedingungen von außen auf das Konfigurationsmodell wirken. Das eine ist halt, es wäre schön, wenn wir die Data Sources von dem Rest trennen könnten. So, der Rest ist halt, ich mache, ich sage hier die Klasse, dieses Modell und das Mapping und mache die ganzen Konfigurationen, an welchem Endpunkt welcher Adapter mit welchem Basisendpunkt und welchen Einstellungen hinkommt. Und dann sage ich, naja, und das bitte mit der Datenquelle. Und ich will aber gegebenenfalls dasselbe mit einer anderen Datenquelle machen. Dass ich entweder das mit dem Testsystem mache oder für zwei Kommunen oder für, dass man einfach vorsieht, dass es die ganze Einstellung gelten kann, dann vielleicht unter einem anderen Pfad, unter einer anderen Basispfad, aber dieselbe Einstellung mit einer anderen Datenquelle gelten kann. Das ist nach unten so das eine. Und dann gibt es nach oben bei den Exportern was Ähnliches nochmal, weil das das Ding, die CSVs per Komma separiert und wie er dann welche Quotes macht. Und das bei allen Schnittstellen. Das sollte man einmal templateartig definieren und dann geht man hin und sagt, naja, jetzt gibt es den Baum-Exporter, jetzt gibt es den Bank-Exporter, die sich an die Modelle kümmern, aber die wenden sozusagen diese Einstellungen nur an, dass man einen, das ist unsicherheit erreicht, dass man ein Template hat, wo man sozusagen sagt, wie das Ganze aussieht und dann nur immer wieder sagt, ja, jetzt ist wieder der derselbe CSV-Exporter, jetzt ist festgelegt wieder die Quotes, wenn wir die etwas sind und so weiter. Und vielleicht noch welcher Subpfad oder welche Application und den wendet man dann sozusagen nur noch an auf Baum und auf Bank und auf Auto und auf Feuerwehrauto und auf Wasser und so weiter, damit man das nicht zu sehr doppelt.machen wir es, wie ich das nicht gemacht hätte, wie ich auch vorgeschlagen habe, dass man für die Datenquellen, also die Data Sources, eine Registry und Container baut, genauso für die Exports, wo die dann alle beieinander drin liegen und wo wir dann sozusagen in der Konfiguration darauf referenzieren können, sodass du, ich sage mal, pro Mandant deine Datensourcen anlegen kannst. Gegebenenfalls kann man das halt dann auch mit irgendwelchen Mappings machen, die man dann noch hat, für irgendwelche Kubernetes-Mappings, die dazwischen kommen, dass man das alles nur referenziert. Das wäre so das Ende oben und unten.

* Alle Texte im DCAT generieren sich vollständig aus den Modellen
* Gfs. sollten texte auch aus anderen quellen ggfs refernziert werden können, falls sie nicht in die jeweiligen modelle passen
* DCAT Endpunkt
* OData als Backend für DCAT + SPARQL
* DCAT UI Markus

### QGis

* Eine Eclass ist ein Layer
* QGis Client hat eine Konfigufile für diesen Layer
* QGis Server Konsumiert das und erstellt OGC Features / Maps
* Layer hat folgende Tabelle mit SELECT + Umbennenung der Tabellen in anzeigbarer Name
* Wir generieren das QGis File

</details>
