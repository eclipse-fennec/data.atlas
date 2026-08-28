#!/bin/sh
# Copyright (c) 2026 Contributors to the Eclipse Foundation.
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0
#
# One-shot seeder for the DCAT compose setup: waits for the portal and creates
# the target catalog. The Data Atlas expects the catalog to exist - catalog
# creation is out of scope of the publication (data.atlas#4).
set -eu

PORTAL="${PORTAL_BASE:-http://dcatatlas:8080}"

echo "waiting for the DCAT.Atlas at ${PORTAL} ..."
i=0
until curl -sf "${PORTAL}/health/ready" > /dev/null; do
  i=$((i + 1))
  if [ "$i" -gt 120 ]; then
    echo "the portal never became ready" >&2
    exit 1
  fi
  sleep 2
done

echo "creating catalog 'example' ..."
curl -sf -X PUT "${PORTAL}/rest/admin/catalogs/example" \
  -H 'Content-Type: application/xmi' \
  -H 'Accept: application/rdf+xml' \
  --data-binary @/seed/catalog.xmi > /dev/null

echo "catalog seeded."
