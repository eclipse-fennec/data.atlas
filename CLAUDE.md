# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Current State

Bootstrapped bnd/Bndtools OSGi workspace (Gradle, Java 21). Contains exactly two bundles — the EMF configuration model bundle (`configuration.model`, building against the `eorm` model of `org.eclipse.fennec.persistence.orm`) and the runtime assembly bundle with bndrun configurations — plus a docker build module. The formerly contained data-plane bundles (`epackage.watcher`, `jpa.watcher`, `jpa.rest`, `jpa.config.local` + tests) and the `dcat.model` bundle were removed in August 2026 and are **out of scope for this repository**; the further roadmap (Configurator/Bootstrap, importers, endpoint generation) is being re-planned.

## Purpose & Context

**Fennec Data Atlas** is part of the Eclipse Fennec project family (EMF on pure OSGi). It is the counterpart to the **Model Atlas** (`eclipse-fennec/model.atlas`): the Model Atlas manages schemas/models (`EPackage`, `EClass`); the Data Atlas manages **instances/data** (`EObject`) — where data lives, how it is loaded, transformed, and served.

The guiding idea: a Data Atlas instance is described entirely by an **EMF configuration model** (`DataAtlasConfiguration`). A Configurator/Bootstrap component translates that model into running OSGi services (data importers, REST/GraphQL endpoints, DCAT providers) at runtime — the model is the single source of truth, not scattered Config Admin JSONs.

The spine of the repository is `org.eclipse.fennec.data.atlas.configuration.model` (`configuration.ecore`, `validation.ecore`; the JPA mapping is referenced from the `eorm` model of `org.eclipse.fennec.persistence.orm` via `usedGenPackages`). How the runtime functionality (Configurator/Bootstrap, importers, endpoint generation, DCAT publishing) gets here is subject to a new plan; the JPA data plane once migrated from `eclipse-fennec/model.atlas` was removed again and stays out of scope.

## Documentation

Cross-cutting docs live in `docs/` (index in `docs/README.md`); bundle-specific docs sit next to their bundle:

- `docs/architecture.md` — target architecture (configuration model → Configurator/Bootstrap → runtime services), implemented-vs-missing, key upstream dependencies
- `org.eclipse.fennec.data.atlas.configuration.model/configuration.md` — the configuration model

Keep these in sync when changing the corresponding code.

## Build & Development Commands

Bytecode target is Java 21; CI builds on Java 21 and 25.

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

- **bnd workspace**: every top-level dir with a `bnd.bnd` is one OSGi bundle, named after its Bundle-SymbolicName (`org.eclipse.fennec.data.atlas.*`); companion `*.tests` bundles for OSGi tests, each with its own `test.bndrun`
- **Tests must stay OS-neutral**: never embed filesystem paths in LDAP filters (backslashes are LDAP escape characters); compare `Path` objects instead of URI string suffixes
- Workspace config in `cnf/`: `cnf/ext/fennec.bnd` (fennec libraries, Java 21, `-groupid`), `cnf/ext/central.mvn` (Maven Central coordinates index). Project coordinates live once in `gradle.properties` (`github_org`, `github_repository`, `maven_group_id`)
- **EMF codegen at build time**: `-generate` in each model bundle's `bnd.bnd` runs the fennecEMF generator (genmodel → `src-gen-*`). Edit the `.ecore`/`.genmodel` and regenerate — never hand-edit generated code. **The `.genmodel` must be reconciled after every ecore refactoring** (moved/removed features leave unresolved proxies that fail the build; the genmodel is edited as plain XML here, there is no Eclipse UI in the loop)
- Generated sources (`src-gen-config`) are committed
- OSGi Declarative Services annotations for component wiring
- License: EPL-2.0, headers checked by SkyWalking Eyes (`.licenserc.yaml`, CI `license.yml`)

## Runtime Layout

`org.eclipse.fennec.data.atlas.runtime` holds the bndruns: `_base` (requirements + resolved `-runbundles`), `_local` (includes base, local paths, `-resolve: never`), `_docker` (includes base, `/opt/dataatlas` paths). The `runtime/` folder (etc/logback.xml, data/) is staged into the docker image together with the exported jar by `:docker:dataatlas:prepareDocker`; `docker/dataatlas/Dockerfile` is a distroless Java 21 two-stage build (same pattern as model.atlas).
