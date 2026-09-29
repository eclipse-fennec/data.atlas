-- Copyright (c) 2026 Contributors to the Eclipse Foundation.
--
-- This program and the accompanying materials are made
-- available under the terms of the Eclipse Public License 2.0
-- which is available at https://www.eclipse.org/legal/epl-2.0/
--
-- SPDX-License-Identifier: EPL-2.0

-- The SensiNact history schema, transcribed VERBATIM from the statements
-- TimescaleSql declares (CREATE_TABLE, CREATE_INDEX, CREATE_HYPERTABLE):
--
--   eclipse-sensinact/org.eclipse.sensinact.gateway
--   southbound/history/timescale-provider/src/main/java/org/eclipse/sensinact/
--   gateway/southbound/history/timescale/TimescaleSql.java
--   verified 2026-09-29 against 12ee7d94f3f2b58f234bad0763e13553c5d449d1
--   ("Rework the history provider onto a HistoryProvider service with query
--   pushdown", 2026-09-07; unchanged since)
--
-- The provider writes ONE table, sensinact.history, instead of the former three
-- per-kind hypertables numeric_data / text_data / geo_data. The kind of a value
-- is the discriminator column value_kind (the ordinal of ValueKind: 0 NUMBER,
-- 1 BOOLEAN, 2 STRING, 3 GEOJSON, 4 OBJECT); a number lives in value_num,
-- everything else as JSON in value_json — a location is a GeoJSON document (a
-- bare geometry or a Feature), no PostGIS type. On its first start against a
-- legacy database the provider copies the old rows into sensinact.history and
-- renames the old tables to *_migrated.
--
-- This file exists so the example is self-contained: it seeds the same schema
-- the Event Atlas would have created, without depending on that image. The
-- table is NOT ours to change — if it drifts from upstream, upstream wins. The
-- docker-gated integration tests run the real eorm mapping against this file;
-- they catch a seed that no longer fits the mapping, not an upstream change
-- (that has to be re-verified by hand, see the date above).
--
-- Nothing else is installed: the Data Atlas maps the table itself (one entity,
-- HistoryEntry) and splits the kinds with per-DataSet queries that push down
-- into the database, so a deployment adds nothing to the provider's schema.
--
-- Note there is deliberately NO primary key here, exactly as upstream. The JPA
-- identity is declared in the inline eorm mapping as a composite id over
-- (time, provider, service, resource).

CREATE SCHEMA IF NOT EXISTS sensinact;

CREATE TABLE sensinact.history (time TIMESTAMPTZ NOT NULL,modelpackageuri VARCHAR(128), model VARCHAR(128),provider VARCHAR(128) NOT NULL, service VARCHAR(128) NOT NULL,resource VARCHAR(128) NOT NULL,value_kind SMALLINT NOT NULL,java_type VARCHAR(128),value_num NUMERIC,value_json JSONB);
CREATE INDEX history_psr_time ON sensinact.history (provider, service, resource, time DESC);
SELECT create_hypertable('sensinact.history', 'time');
