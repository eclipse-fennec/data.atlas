-- Copyright (c) 2026 Contributors to the Eclipse Foundation.
--
-- This program and the accompanying materials are made
-- available under the terms of the Eclipse Public License 2.0
-- which is available at https://www.eclipse.org/legal/epl-2.0/
--
-- SPDX-License-Identifier: EPL-2.0

-- What SensiNact's TimescaleHistoricalStore PRESUPPOSES, rather than creates.
--
-- The store calls create_hypertable() and CREATE EXTENSION Postgis, but never
-- creates the timescaledb extension itself — in a real deployment the database
-- it is pointed at already has it. A freshly initialised database created by
-- POSTGRES_DB does not, not even on a timescaledb image, so seeding the store's
-- DDL verbatim fails with:
--
--   ERROR: function create_hypertable(unknown, unknown, if_not_exists =>
--          boolean) does not exist
--
-- This file supplies that precondition and keeps 01-schema.sql a verbatim
-- transcription of the store. initdb.d runs files in alphabetical order, so this
-- one goes first.

CREATE EXTENSION IF NOT EXISTS timescaledb;
