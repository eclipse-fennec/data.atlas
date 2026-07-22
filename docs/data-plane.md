# JPA Data Plane

Turns a folder of `.ecore` models + `.eorm` JPA mappings + `.csv` data into a
JPA-backed (EclipseLink + H2) REST endpoint at
`/jpa/{rootFolderName}/data/{eClassName}`. Migrated from
[model.atlas](https://github.com/eclipse-fennec/model.atlas).

## Pipeline

1. [`WorkspaceFolderWatcher`](../org.eclipse.fennec.data.atlas.jpa.watcher/src/org/eclipse/fennec/data/atlas/jpa/watcher/WorkspaceFolderWatcher.java)
   scans a root folder and creates one `DataFolderWatcher` factory config per
   subfolder.
2. [`DataFolderWatcher`](../org.eclipse.fennec.data.atlas.jpa.watcher/src/org/eclipse/fennec/data/atlas/jpa/watcher/DataFolderWatcher.java)
   wires the whole factory-config pipeline for one data folder: EMF file
   watcher, eorm watcher, H2 datasource, CSV importer, persistence unit.
3. [`EMFFileWatcher`](../org.eclipse.fennec.data.atlas.epackage.watcher/src/org/eclipse/fennec/data/atlas/epackage/watcher/EMFFileWatcher.java)
   registers `.ecore` files as `EPackage` services (dedup by nsURI, debounced
   reload on file events).
4. [`EormFileWatcher`](../org.eclipse.fennec.data.atlas.jpa.watcher/src/org/eclipse/fennec/data/atlas/jpa/watcher/EormFileWatcher.java)
   registers `.eorm` files as `EntityMappings` services;
   [`JpaPersistenceUnitConfigurator`](../org.eclipse.fennec.data.atlas.jpa.watcher/src/org/eclipse/fennec/data/atlas/jpa/watcher/JpaPersistenceUnitConfigurator.java)
   creates the `fennec.jpa.EMPersistenceUnit` factory config once model +
   mapping are present.
5. [`JpaCsvDataImporter`](../org.eclipse.fennec.data.atlas.jpa.watcher/src/org/eclipse/fennec/data/atlas/jpa/watcher/JpaCsvDataImporter.java)
   imports `.csv` files through the Daanse CSV importer into the H2 schema.
6. [`JpaDataResource`](../org.eclipse.fennec.data.atlas.jpa.rest/src/org/eclipse/fennec/data/atlas/jpa/rest/JpaDataResource.java)
   serves the entities over Jakarta-RS (whiteboard), serialization via the
   fennec codec stack.

The PID contract between these components is defined in
[`WatcherConstants`](../org.eclipse.fennec.data.atlas.jpa.watcher/src/org/eclipse/fennec/data/atlas/jpa/watcher/api/WatcherConstants.java).

## Helper Classes

`org.eclipse.fennec.data.atlas.emf.common` (inside the epackage.watcher
bundle) holds `DynamicEPackageConfigurator` and
`EClassResolvingDynamicEFactory` — copied from model.atlas's emf.common so the
data plane has no model.atlas dependency.

## Tests

OSGi integration tests live in the `*.tests` bundles, each with its own
`test.bndrun`. They need a **Java 25 runtime** (the Eclipse Daanse `sql.*`
bundles require `osgi.ee=JavaSE-25`). Tests must stay OS-neutral: never embed
filesystem paths in LDAP filters (backslashes are LDAP escape characters) and
compare `Path` objects instead of URI string suffixes.

## Further Reading

- Folder layout and watcher configuration:
  [`jpa.watcher/README.md`](../org.eclipse.fennec.data.atlas.jpa.watcher/README.md)
- REST API reference:
  [`jpa.rest/docs/jpa-rest-api.md`](../org.eclipse.fennec.data.atlas.jpa.rest/docs/jpa-rest-api.md)
