#!/bin/sh
# Copyright (c) 2026 Contributors to the Eclipse Foundation.
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0

# One-shot seeder: waits until the Model Atlas serves the dataatlas scope
# (i.e. the configurations registry activated against the preloaded
# configuration EPackage), then uploads the example DataAtlasConfiguration
# instance into the final 'release' stage.
set -eu

BASE="http://modelatlas:8080/atlas/rest"

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

echo "seed: uploading the DataAtlasConfiguration instance..."
CODE=$(curl -s -o /dev/stderr -w "%{http_code}" -X POST \
  -H "Content-Type: application/xmi" \
  --data-binary @/seed/dataatlas-atlas.xmi \
  "$BASE/dataatlas/registries/configurations/stages/release/dataatlas?name=dataatlas&override=true")
case "$CODE" in
  200|201) echo "seed: done ($CODE)" ;;
  409) echo "seed: already present ($CODE)" ;;
  *) echo "seed: upload failed ($CODE)" >&2; exit 1 ;;
esac
