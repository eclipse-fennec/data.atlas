# Data Atlas — Architecture

## Positioning

The **Data Atlas** is the counterpart to the
[Model Atlas](https://github.com/eclipse-fennec/model.atlas):

| | Model Atlas | Data Atlas |
|---|---|---|
| Subject | Schemas & models (`EPackage`, `EClass`) | **Instances / data** (`EObject`) |
| Core question | "Which models exist, how are they versioned/validated?" | "Where does data live, how is it loaded, transformed, and served?" |

## Guiding idea

A Data Atlas instance is described entirely by an **EMF configuration model**
(`DataAtlasConfiguration`, see
[`configuration.ecore`](../org.eclipse.fennec.data.atlas.configuration.model/model/configuration.ecore)).
The model is the single source of truth, not scattered Config Admin JSONs.

The Data Atlas is a separate, horizontally scalable component: multiple
instances can run side by side (e.g. to spread load), each described by its own
`DataAtlasConfiguration`. An instance obtains its configuration in one of two
modes — **from the file system** or **by retrieving it from the Model Atlas**.

A **Bootstrap** component loads the model and registers the contained
configuration objects (e.g. each `RestDataService`, each `DataInput`) as OSGi
services. Per-technology **configurator components** pick these config services
up whiteboard-style and create the actual runtime pieces — data importers,
REST/GraphQL endpoints, DCAT providers — and tear them down again when the
config service disappears.

```mermaid
flowchart TB
    subgraph CONFIG["Configuration model (EMF/XMI) — the single source of truth"]
        ROOT["DataAtlasConfiguration (root)"]
        DS["DataSources<br/>(JdbcDataSource, ...)"]
        IN["DataInputs<br/>(JPA, Mongo, File, Bridge)"]
        SET["DataSets"]
        SVC["DataServices<br/>(REST/OData/OGC/QGis/XMLA/GraphQL)"]
        EXP["DistributionExports<br/>(CSV, ... templates)"]
        TRAFO["Transformations (QVT)"]
        ROOT --> DS & IN & SET & SVC & EXP & TRAFO
    end

    SRC["Config source:<br/>file system | Model Atlas"] -->|provides| CONFIG
    CONFIG -->|loads| BOOT["Bootstrap<br/>(registers configuration objects as OSGi services)"]

    BOOT -->|config services picked up by<br/>per-technology configurators| RT
    subgraph RT["Runtime"]
        IMP["Data importer + QVT"]
        REPO["Repositories / DataInputs"]
        REST["Endpoint generation<br/>(Jakarta-RS whiteboard, ...)"]
        DCAT["DCAT provider<br/>(distributions from DataServices)"]
    end

    subgraph MA["Model Atlas"]
        EPKG["EPackage / schema registry"]
    end

    RT -->|resolves EClasses via| MA
    DCAT --> CAT[("DCAT catalog / open-data portal")]
    REST --> CLIENTS(("Clients"))
```

## Current state vs. target

Implemented today:

- **Model bundle**: `configuration.model` (configuration/validation ecores,
  generated code; the JPA mapping type is referenced from the `eorm` model of
  `org.eclipse.fennec.persistence.orm`).
- **Runtime assembly**: bndruns (`_base`/`_local`/`_docker`) and a distroless
  docker image build.

The JPA data plane (folder of `.ecore`/`.eorm`/`.csv` → JPA-backed REST) and the
DCAT-AP model stack were removed from this repository and are **out of scope
here**. The remaining runtime functionality (Configurator/Bootstrap, importers,
endpoint generation) is subject to a new plan.

## Key dependencies

| Stack | Provider |
|---|---|
| EMF on OSGi (EPackage registry, codegen) | [eclipse-fennec/emf.osgi](https://github.com/eclipse-fennec/emf.osgi) |
| JPA mapping model (`eorm`, build-time genmodel reference) | [eclipse-fennec/emf.persistence-jpa](https://github.com/eclipse-fennec/emf.persistence-jpa) via the `fennecJPA` bnd library |
