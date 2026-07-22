# Data Atlas Documentation

User- and architecture-facing documentation for the Eclipse Fennec Data Atlas.

## Contents

| Document | Content |
|---|---|
| [architecture.md](architecture.md) | Target architecture: configuration model, Configurator/Bootstrap, runtime components |
| [data-plane.md](data-plane.md) | The JPA data plane: watcher pipeline, involved components, test conventions |
| [../org.eclipse.fennec.data.atlas.configuration.model/configuration.md](../org.eclipse.fennec.data.atlas.configuration.model/configuration.md) | The Data Atlas configuration model (`configuration.ecore`) |
| [../org.eclipse.fennec.data.atlas.jpa.watcher/README.md](../org.eclipse.fennec.data.atlas.jpa.watcher/README.md) | JPA data-folder watcher: `.ecore`/`.eorm`/`.csv` folders → JPA persistence units |
| [../org.eclipse.fennec.data.atlas.jpa.rest/docs/jpa-rest-api.md](../org.eclipse.fennec.data.atlas.jpa.rest/docs/jpa-rest-api.md) | REST API of the JPA data plane (`/jpa/{rootFolderName}/data/{eClassName}`) |

Bundle-specific documentation stays next to its bundle; this folder holds
cross-cutting documents.
