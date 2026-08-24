# Data Atlas — Combined Compose Setups

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
