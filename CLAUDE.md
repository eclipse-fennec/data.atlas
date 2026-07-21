# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Current State

Bootstrapped bnd/Bndtools OSGi workspace (Gradle, Java 21 bytecode / Java 25 test runtime). Contains the two EMF model bundles (`configuration.model`, `dcat.model`), the data-plane bundles migrated from model.atlas (`epackage.watcher`, `jpa.watcher`, `jpa.rest`, `jpa.config.local` + test bundles — folder of `.ecore`/`.eorm`/`.csv` → JPA-backed REST at `/jpa/{rootFolderName}/data/{eClassName}`), a runtime assembly bundle with bndrun configurations, and a docker build module. The `epackage.watcher` bundle also contains the `org.eclipse.fennec.data.atlas.emf.common` helper classes (DynamicEPackageConfigurator, EClassResolvingDynamicEFactory) copied from model.atlas. The MDO runtime functionality (importer, REST/GraphQL generation, DCAT provider, Configurator/Bootstrap) is not yet migrated. The data-plane bundles are not yet part of the runtime bndruns/docker image.

## Purpose & Context

**Fennec Data Atlas** is part of the Eclipse Fennec project family (EMF on pure OSGi). It is the counterpart to the **Model Atlas** (`eclipse-fennec/model.atlas`): the Model Atlas manages schemas/models (`EPackage`, `EClass`); the Data Atlas manages **instances/data** (`EObject`) — where data lives, how it is loaded, transformed, and served.

The guiding idea: a Data Atlas instance is described entirely by an **EMF configuration model** (`DataAtlasConfiguration`). A Configurator/Bootstrap component translates that model into running OSGi services (data importers, REST/GraphQL endpoints, DCAT providers) at runtime — the model is the single source of truth, not scattered Config Admin JSONs.

The functionality is migrated from two sources:

- **`de-jena/MDO`** — the prototype that proves the end-to-end pipeline: dynamic EPackage loading, generic REST/OpenAPI/GraphQL generation per model, JDBC→PushStream→QVT→repository import, DCAT/Piveau publishing. Code is ported here under the `org.eclipse.fennec.data.atlas.*` namespace with `org.gecko.*` dependencies replaced by their Eclipse Fennec successors (`emf.osgi`, `emf.codec`, `emf.m2x`, `fennec-persistence`).
- **`eclipse-fennec/model.atlas`** — its `org.eclipse.fennec.data.atlas.*` data-plane bundles (`epackage.watcher`, `jpa.watcher`, `jpa.rest`, `jpa.config.local` + tests) move here: they turn a folder of `.ecore` models + `.eorm` JPA mappings + `.csv` data into a JPA-backed (EclipseLink + H2) REST endpoint at `/jpa/{rootFolderName}/data/{eClassName}`. See `model.atlas`'s `jpa.watcher/README.md` and `jpa.rest/docs/jpa-rest-api.md` until they land here.

The first bundles to land are the two EMF model bundles: `org.eclipse.fennec.data.atlas.configuration.model` (the configuration model spine: `configuration.ecore`, `emfmapping.ecore`, `validation.ecore`) and `org.eclipse.fennec.data.atlas.dcat.model` (DCAT-AP model stack).

## Build & Development Commands

**The OSGi tests need a Java 25 runtime** (the Eclipse Daanse `sql.*` bundles require `osgi.ee=JavaSE-25`), while the bytecode target stays Java 21. Locally run Gradle on JDK 25, e.g. `-Dorg.gradle.java.home=N:/tools/java/jdk-25.0.2`; CI uses Java 25.

```bash
./gradlew build          # full build and tests
./gradlew test           # unit tests
./gradlew testOSGi       # OSGi integration tests
./gradlew :<bundle>:test # single module

# Runtime
./gradlew :org.eclipse.fennec.data.atlas.runtime:resolve.dataatlas.runtime_base   # re-resolve -runbundles
./gradlew :org.eclipse.fennec.data.atlas.runtime:export.dataatlas.runtime_docker  # executable jar
./gradlew :docker:dataatlas:prepareDocker                                         # stage docker/dataatlas/content/
docker build -t eclipsefennec/data.atlas:snapshot docker/dataatlas/
```

Re-run the `resolve.*` task after adding bundles or changing dependencies — it rewrites `-runbundles` in `dataatlas.runtime_base.bndrun`.

## Workspace Conventions

- **bnd workspace**: every top-level dir with a `bnd.bnd` is one OSGi bundle, named after its Bundle-SymbolicName (`org.eclipse.fennec.data.atlas.*`); companion `*.tests` bundles for OSGi tests
- Workspace config in `cnf/`: `cnf/ext/fennec.bnd` (fennec libraries, Java 21, `-groupid`), `cnf/ext/central.mvn` (Maven Central coordinates index). Project coordinates live once in `gradle.properties` (`github_org`, `github_repository`, `maven_group_id`)
- **EMF codegen at build time**: `-generate` in each model bundle's `bnd.bnd` runs the fennecEMF generator (genmodel → `src-gen-*`). Edit the `.ecore`/`.genmodel` and regenerate — never hand-edit generated code. **The `.genmodel` must be reconciled after every ecore refactoring** (moved/removed features leave unresolved proxies that fail the build; the genmodel is edited as plain XML here, there is no Eclipse UI in the loop)
- Generated sources (`src-gen-config`, `src-gen-dcat`) are committed
- OSGi Declarative Services annotations for component wiring
- License: EPL-2.0, headers checked by SkyWalking Eyes (`.licenserc.yaml`, CI `license.yml`)

## Runtime Layout

`org.eclipse.fennec.data.atlas.runtime` holds the bndruns: `_base` (requirements + resolved `-runbundles`), `_local` (includes base, local paths, `-resolve: never`), `_docker` (includes base, `/opt/dataatlas` paths). The `runtime/` folder (etc/logback.xml, data/) is staged into the docker image together with the exported jar by `:docker:dataatlas:prepareDocker`; `docker/dataatlas/Dockerfile` is a distroless Java 21 two-stage build (same pattern as model.atlas).
