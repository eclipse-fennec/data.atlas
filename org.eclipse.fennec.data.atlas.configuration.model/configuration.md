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
| `dataSources` | `JdbcDataSource` | Reusable data source definitions (bound at runtime to pooled OSGi `DataSource` services via an LDAP target filter) |
| `dataInputs` | `DataInput` | The inputs that stream EObjects into the instance |
| `dataSets` | `DataSet` | The published datasets (DCAT Dataset equivalents) |
| `services` | `DataService` | The endpoints this instance publishes |
| `exports` | `DistributionExport` | Reusable serialization templates (e.g. CSV settings) |
| `transformations` | `Transformation` | Data and query transformations |

This registry design is deliberate: the same service definition can be
re-applied to another data source (tenant, test system) by swapping the
referenced `JdbcDataSource`, and export settings are templates instead of
per-provider copies.

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
  registry and a JPA `EntityMappings` (the `eorm` model of
  `org.eclipse.fennec.persistence.orm`) describing how model types map to the
  relational schema.
- `MongoRepository` — MongoDB-backed source (placeholder, no features yet).
- `BridgeRepository` — wraps another `DataInput` and applies a
  `DataTransformation` to loaded objects and a `QueryTransformation` to
  incoming queries; cascadable.

### `DataService` — how data is exposed

Abstract endpoint definition (`urlContext` = base path). A service can act
standalone — serving its own `dataInput` generically — or contain per-DataSet
`DataServiceConfiguration`s (the binding service → dataset, containment).
Every configuration of a DataService must result in a DCAT Distribution.

Concrete services: `RestDataService` (with `openAPI` marker and configurable
pagination parameter names; per-dataset `RestDataServiceConfiguration` with
`path`, `batchSize`, `batchSizeLimit`), plus placeholders for `ODataDataService`,
`GraphQLDataService`, `XMLADataService` (OLAP/Daanse), `QGisDataService`
(generated QGis layer configuration), `OgcFeaturesDataService` and
`OgcSensorThingsDataService`.

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

`DataTransformation` (model A → model B, QVT script placeholder for now, with
`supportedEClasses`/`resultEClasses`) and `QueryTransformation` (maps incoming
queries onto the underlying source).

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
