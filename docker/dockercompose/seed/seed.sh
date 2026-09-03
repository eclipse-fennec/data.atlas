#!/bin/sh
# Copyright (c) 2026 Contributors to the Eclipse Foundation.
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0

# One-shot seeder: waits for the dataatlas scope, uploads the schemas the
# example configuration references (the server must know an EPackage before it
# can deserialize instances of it), then uploads the DataAtlasConfiguration
# instance into the final 'release' stage.
#
# SEED_INSTANCE selects which configuration instance is uploaded, so the same
# seeder serves every compose setup (file/atlas example, Postgres example).
# SEED_EXTRA_SCHEMA/_NSURI/_NAME optionally upload one further schema, for
# examples that reference a domain model beyond the built-in person one.
set -eu

BASE="http://modelatlas:8080/atlas/rest"
INSTANCE="${SEED_INSTANCE:-/seed/dataatlas-atlas.xmi}"

echo "seed: waiting for the dataatlas scope..."
i=0
until curl -sf "$BASE/scopes/dataatlas" >/dev/null 2>&1; do
  i=$((i + 1))
  if [ "$i" -gt 90 ]; then
    echo "seed: dataatlas scope did not come up" >&2
    exit 1
  fi
  sleep 2
done

upload_schema() {
  FILE="$1"; NSURI="$2"; NAME="$3"
  # rawurlencode the nsUri (sufficient for the characters used here)
  ENC=$(printf '%s' "$NSURI" | sed 's|:|%3A|g; s|/|%2F|g')
  # the instance goes to the final 'release' stage, whose stage-scoped
  # ResourceSet only sees release-stage packages - so the schemas go there too
  CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST \
    -H "Content-Type: application/xmi" \
    --data-binary @"$FILE" \
    "$BASE/dataatlas/schema/stages/release?nsUri=$ENC&name=$NAME&version=1.0.0")
  case "$CODE" in
    200|201) echo "seed: schema $NAME uploaded ($CODE)" ;;
    409) echo "seed: schema $NAME already present ($CODE)" ;;
    *) echo "seed: schema $NAME upload failed ($CODE)" >&2; exit 1 ;;
  esac
}

# eorm first: configuration.ecore references it
upload_schema /seed/models/eorm.ecore "https://eclipse.org/fennec/persistence/eorm/1.0.0" eorm
upload_schema /seed/models/configuration.ecore "https://eclipse.org/fennec/data/atlas/configuration/1.0.0" configuration
upload_schema /seed/models/person.ecore "https://eclipse.org/fennec/data/atlas/example/person/1.0.0" person

# optional extra domain schemas (e.g. the SensiNact history model, or the
# projection/POI models of the full example): SEED_EXTRA_SCHEMA/_NSURI/_NAME,
# further ones with a numeric suffix (SEED_EXTRA_SCHEMA_2, ...)
for suffix in "" _2 _3 _4; do
  eval "FILE=\${SEED_EXTRA_SCHEMA$suffix:-}"
  eval "NSURI=\${SEED_EXTRA_NSURI$suffix:-}"
  eval "NAME=\${SEED_EXTRA_NAME$suffix:-}"
  if [ -n "$FILE" ] && [ -f "$FILE" ]; then
    upload_schema "$FILE" "$NSURI" "$NAME"
  fi
done

echo "seed: uploading the DataAtlasConfiguration instance $INSTANCE..."
# A freshly uploaded schema is not necessarily resolvable for an instance upload
# in the same breath - the stage's package view catches up asynchronously, and a
# too-early POST comes back 500 "Error de-serializing incoming data". Retry a
# bounded number of times instead of failing the whole setup on a race.
i=0
while : ; do
  CODE=$(curl -s -o /dev/stderr -w "%{http_code}" -X POST     -H "Content-Type: application/xmi"     --data-binary @"$INSTANCE"     "$BASE/dataatlas/registries/configurations/stages/release/dataatlas?name=dataatlas")
  case "$CODE" in
    200|201) echo "seed: done ($CODE)"; break ;;
    409) echo "seed: already present ($CODE)"; break ;;
    *)
      i=$((i + 1))
      if [ "$i" -gt 10 ]; then
        echo "seed: upload failed ($CODE) after $i attempts" >&2
        exit 1
      fi
      echo "seed: upload attempt $i got $CODE, retrying in 3s..." >&2
      sleep 3
      ;;
  esac
done
