# Eclipse Fennec — Data Atlas

The **Data Atlas** is the counterpart to the
[Model Atlas](https://github.com/eclipse-fennec/model.atlas): while the Model
Atlas manages schemas and models (`EPackage`, `EClass`), the Data Atlas manages
**instances/data** (`EObject`) — where data lives, how it is loaded,
transformed, and served through endpoints such as REST, OData, OGC, or DCAT.

A Data Atlas instance is described entirely by an **EMF configuration model**
(`DataAtlasConfiguration`). A Configurator/Bootstrap component translates that
model into running OSGi services at runtime — the model is the single source
of truth.

## Status

Early bootstrap. The repository currently contains:

| Bundle | Content |
|---|---|
| `org.eclipse.fennec.data.atlas.configuration.model` | The Data Atlas configuration model (`configuration.ecore`, `validation.ecore`) with generated code; references the `eorm` JPA mapping model from `org.eclipse.fennec.persistence.orm` |
| `org.eclipse.fennec.data.atlas.runtime` | OSGi runtime assembly (bndrun configurations) and docker export |

## Build

Bnd/Bndtools OSGi workspace built with Gradle, Java 21:

```bash
./gradlew build          # full build and tests
./gradlew test           # unit tests
./gradlew testOSGi       # OSGi integration tests
```

### Runtime & Docker

```bash
# Resolve the runtime
./gradlew :org.eclipse.fennec.data.atlas.runtime:resolve.dataatlas.runtime_base

# Export the docker runtime jar
./gradlew :org.eclipse.fennec.data.atlas.runtime:export.dataatlas.runtime_docker
./gradlew :org.eclipse.fennec.data.atlas.runtime:export.dataatlas.runtime_docker_atlas

# Stage and build the docker images (file mode / Model Atlas mode)
./gradlew :docker:dataatlas:prepareDocker :docker:dataatlas-atlas:prepareDocker
docker build -t eclipsefennec/data.atlas:file-snapshot docker/dataatlas/
docker build -t eclipsefennec/data.atlas:atlas-snapshot docker/dataatlas-atlas/
```

## Documentation

Architecture and usage documentation lives in [`docs/`](docs/README.md);
bundle-specific documentation sits next to the respective bundle.

## License

[Eclipse Public License 2.0](LICENSE) — SPDX-License-Identifier: `EPL-2.0`

See [NOTICE.md](NOTICE.md) and [CONTRIBUTING.md](CONTRIBUTING.md) for project
provenance, trademark, and contribution guidelines.
