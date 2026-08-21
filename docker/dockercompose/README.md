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

- **Schemas at boot**: the `initial-models` mount seeds `person.ecore`,
  `configuration.ecore` (mounted straight from the repo) and a committed copy
  of `eorm.ecore` (from `org.eclipse.fennec.persistence.orm`, laid out so the
  relative href inside `configuration.ecore` resolves). Registry roots from
  custom models must be present at boot — REST-uploaded schemas are too late
  (see model.atlas#175/#188).
- **Custom scope/registry**: `modelatlas/load/dataatlas.json` is injected via
  `JAVA_TOOL_OPTIONS=-Dconfigurator.initial=…`. It defines the `dataatlas`
  scope and a `configurations` registry whose `root.eclass.uri` is the
  `DataAtlasConfiguration` EClass — the exact-match compatibility check works,
  sidestepping model.atlas#188.
- **Instance**: the one-shot `seed` service uploads
  `example/dataatlas-atlas.xmi` (nsURI-based hrefs) into the final `release`
  stage.

> **Upstream prerequisite**: the published `model.atlas:file-snapshot` image
> does not yet contain the `InitialModelLoader` bundle (model.atlas#175), so
> the schema seeding — and with it this compose file — only works with an
> image containing that fix.

### Fixture provenance

`modelatlas/initial-models/org.eclipse.fennec.persistence.orm/model/eorm.ecore`
is a copy of the model shipped in
`org.eclipse.fennec.persistence.orm` (0.1.0-SNAPSHOT). Refresh it from the
bundle jar when the upstream model changes.
