# Data Atlas Data Source Configurator

Realizes every `DataSource` definition of the configuration (registered as an
OSGi service by the bootstrap). A definition is realized in exactly one of two
modes:

| Mode | Declared by | What happens |
|---|---|---|
| **BIND** | `filter` set, nothing else | Nothing to create: the deployment configured the backend service itself (e.g. a `daanse.jdbc.datasource.postgresql.DataSource~…` factory configuration in a mounted Configurator file), and the input configurators target it by that filter. This is the pre-existing behaviour, unchanged. |
| **MATERIALIZE** | connection coordinates set, no `filter` | This bundle creates the backend's factory configurations from the definition and deletes them when the definition goes away or changes. |

Both or neither = configuration error: logged as `ERROR`, the definition is
skipped, and the inputs referencing it stay down until it is corrected (the
usual M4 lifecycle — a corrected version recovers without a restart).

## What is created

`JdbcDataSource` → one `daanse.jdbc.datasource.<driver>.DataSource~dataAtlas.<id>`
(the runtime ships the PostgreSQL provider; H2 is used by the tests):

| Model | `postgresql` key | `h2` key |
|---|---|---|
| `host` | `host` | *not applicable* |
| `port` | `port` (Integer) | *not applicable* |
| `database` | `dbname` | `identifier` |
| `schema` | `currentSchema` | *not applicable* |
| `user` | `user` | `username` |
| `password` | `.password` | `.password` |
| `properties` | verbatim | verbatim |
| *(always)* | `data.atlas.datasource.id=<id>` | `data.atlas.datasource.id=<id>` |

The daanse components publish every non-dot key as a service property, so the
`javax.sql.DataSource` service carries `data.atlas.datasource.id=<id>` — that is
the filter `input.jpa` derives for a definition without a `filter`.

`MongoDataSource` → two configurations of the fennec Mongo backend:

| PID | Name | Keys |
|---|---|---|
| `persistence.mongo.client` | `dataAtlas.<id>` | `ident=dataAtlas.<id>`, `connectionString=mongodb://[user[:password]@]host[:port]/[?authSource=…]`, `flavor` if set, `properties` verbatim (e.g. `liveness.*`) |
| `persistence.mongo.database` | `dataAtlas.<id>` | `alias=dataAtlas.<id>`, `database`, `client.target=(mongo.client.ident=dataAtlas.<id>)` |

The `MongoDatabase` service carries `mongo.database.alias=dataAtlas.<id>`,
which is the filter `input.mongo` derives. The client is liveness-gated
upstream: the database and every repository over it appear only after a
successful `ping` and disappear when the connection breaks.

## The credential rule

`user` and `password` are **never values**. Each, if set, must be exactly one
placeholder of the Felix Config Admin interpolation plugin:

- `$[env:NAME]` — an environment variable of the runtime process
- `$[secret:NAME]` — a file `NAME` in the runtime's secrets directory
  (`org.apache.felix.configadmin.plugin.interpolation.secretsdir`, in the
  images `/opt/dataatlas/runtime/secrets`)

A literal, a placeholder with a `;default=`, or any other placeholder type is
refused. The placeholder is written into the factory configuration verbatim;
the interpolation plugin substitutes it when Config Admin delivers the
configuration to the backend component — the value never exists in the model,
in the Model Atlas, or in Config Admin's store. The configurator additionally
checks at registration that the referenced variable is set / the secret file
exists, so a misdeployed secret fails loudly instead of as an opaque connection
error later.

Other attributes (`host`, `database`, …) may contain placeholders too but do not
have to.

## Restricting hosts

Materializing a definition that arrived over the network (Model Atlas mode)
connects this runtime — with its locally resolved credentials — to whatever
host the definition names. The configurator's own PID
`org.eclipse.fennec.data.atlas.datasource` takes

| Key | Value |
|---|---|
| `host.allowlist` | host names a materialized definition may use (String, comma-separated, or String[]); unset = unrestricted |

With a list configured, a placeholder host is refused as well (it cannot be
checked). Changing the list re-evaluates every known definition.

## Constraints

- The runtime has to carry the provider and driver bundles of the declared
  `driver`; the image ships `daanse.jdbc.datasource.postgresql` + the
  PostgreSQL driver and the fennec Mongo backend. A definition for a kind
  without provider yields a factory configuration nobody consumes — silence,
  like a BIND filter matching nothing.
- The materialized JDBC `DataSource` has **no connection pool** (the daanse
  component wraps the driver's simple DataSource); pooling is EclipseLink's.
- Mongo credentials go into the connection string, so a resolved user or
  password containing URL-reserved characters (`:`, `@`, `/`, `%`) would break
  it — keep such values out of the secret, or bind a hand-configured client.
- `properties` cannot override a key derived from the definition's attributes.
