-- Copyright (c) 2026 Contributors to the Eclipse Foundation.
--
-- This program and the accompanying materials are made
-- available under the terms of the Eclipse Public License 2.0
-- which is available at https://www.eclipse.org/legal/epl-2.0/
--
-- SPDX-License-Identifier: EPL-2.0

-- What the seeded schema PRESUPPOSES, rather than creates.
-- timescaledb: SensiNact's history provider calls create_hypertable() but never
-- creates the extension itself — in a real deployment the database it is
-- pointed at already has it. A freshly initialised database created by
-- POSTGRES_DB does not, not even on a timescaledb image, so seeding the
-- provider's DDL verbatim fails with:
--   ERROR: function create_hypertable(unknown, unknown) does not exist
-- postgis is NOT needed: since the unified schema (sensiNact 12ee7d94f,
-- 2026-09-07) a location is a GeoJSON document in a JSONB column, no PostGIS
-- type, and the Data Atlas serves that document as it is.
-- initdb.d runs files in alphabetical order, so this one goes first.

CREATE EXTENSION IF NOT EXISTS timescaledb;
