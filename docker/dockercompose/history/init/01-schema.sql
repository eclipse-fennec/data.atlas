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
--   verified 2026-09-18 against 12ee7d94f3f2b58f234bad0763e13553c5d449d1
--   ("Rework the history provider onto a HistoryProvider service with query
--   pushdown", 2026-09-07)
--
-- Since that commit the provider writes ONE table, sensinact.history, instead
-- of the three per-kind hypertables numeric_data / text_data / geo_data. The
-- kind of a value is the discriminator column value_kind (the ordinal of
-- ValueKind: 0 NUMBER, 1 BOOLEAN, 2 STRING, 3 GEOJSON, 4 OBJECT); a number
-- lives in value_num, everything else as JSON in value_json — a location is a
-- GeoJSON document (a bare geometry or a Feature), no PostGIS type any more.
-- On its first start against a legacy database the provider copies the old
-- rows into sensinact.history and renames the old tables to *_migrated.
--
-- This file exists so the example is self-contained: it seeds the same schema
-- the Event Atlas would have created, without depending on that image. The
-- table is NOT ours to change — if it drifts from upstream, upstream wins. The
-- docker-gated integration tests run the real eorm mapping against this file;
-- they catch a seed that no longer fits the mapping, not an upstream change
-- (that has to be re-verified by hand, see the date above).
--
-- Note there is deliberately NO primary key here, exactly as upstream. The JPA
-- identity is declared in the inline eorm mapping as a composite id over
-- (time, provider, service, resource).

CREATE SCHEMA IF NOT EXISTS sensinact;

CREATE TABLE sensinact.history (time TIMESTAMPTZ NOT NULL,modelpackageuri VARCHAR(128), model VARCHAR(128),provider VARCHAR(128) NOT NULL, service VARCHAR(128) NOT NULL,resource VARCHAR(128) NOT NULL,value_kind SMALLINT NOT NULL,java_type VARCHAR(128),value_num NUMERIC,value_json JSONB);
CREATE INDEX history_psr_time ON sensinact.history (provider, service, resource, time DESC);
SELECT create_hypertable('sensinact.history', 'time');

-- ---------------------------------------------------------------------------
-- Bounding the unbounded, and splitting the kinds: what the Data Atlas serves
-- ---------------------------------------------------------------------------
-- From here on the SQL is OURS, not upstream's.
--
-- A hypertable that has been recording for a month has no business being
-- dumped as CSV in one response. The endpoint stays a plain dump (no query
-- filters, by decision), so the bound lives here, in SQL — the entities in the
-- inline eorm mapping are mapped onto these views, not onto the table.
--
-- The views also keep the three-kinds reading of the history that the domain
-- model (sensinact-history.ecore: NumericData, TextData, GeoData) and the
-- mapping were written for: each view selects one group of value_kind values
-- and projects the value into ONE typed `data` column (or, for locations, into
-- location/longitude/latitude). Names and column types are the same as before
-- the unified schema, so a deployment that migrates can CREATE OR REPLACE the
-- old views in place and keep its mapping.
--
-- The window and LIMIT are taken on the RAW rows first (inner query), so the
-- plan stays an index-backed chunk scan that stops after LIMIT rows; the
-- projection runs on at most 1000 rows.
--
-- Deployments that migrate: a PostgreSQL view references its table by OID, so
-- a view created on numeric_data FOLLOWS the provider's rename and silently
-- keeps reading the frozen numeric_data_migrated copy — no error, the endpoint
-- just goes empty once the window has passed. Re-create the views as below
-- after the first start of the new provider.

CREATE OR REPLACE VIEW sensinact.numeric_data_recent AS
    SELECT time, modelpackageuri, model, provider, service, resource, value_num AS data
    FROM (SELECT * FROM sensinact.history
          WHERE time > now() - INTERVAL '7 days' AND value_kind = 0
          ORDER BY time DESC
          LIMIT 1000) h;

-- STRING values are JSON strings ("ok"); #>> '{}' unwraps them to the bare
-- text. BOOLEAN and OBJECT values stay JSON text (true, {"a":1}).
CREATE OR REPLACE VIEW sensinact.text_data_recent AS
    SELECT time, modelpackageuri, model, provider, service, resource,
           CASE WHEN value_kind = 2 THEN value_json #>> '{}' ELSE value_json::text END AS data
    FROM (SELECT * FROM sensinact.history
          WHERE time > now() - INTERVAL '7 days' AND value_kind IN (1, 2, 4)
          ORDER BY time DESC
          LIMIT 1000) h;

-- A location is GeoJSON in value_json: a bare geometry or a Feature. Rather
-- than teaching the Data Atlas about geometries, the view projects it into
-- ordinary SQL types — PostGIS does the work in the database:
--
--   ST_AsText(g)               -> text             (WKT, e.g. POINT(11.582 50.927))
--   ST_X/ST_Y(ST_Centroid(g))  -> double precision (the centroid, so that a
--                                 non-point geometry still yields one pair)
--
-- All three map to plain EMF attributes with no type converter at all. The
-- lon/lat pair is exactly the shape the GeoJSON service wants.
CREATE OR REPLACE VIEW sensinact.geo_data_recent AS
    SELECT time, modelpackageuri, model, provider, service, resource,
           ST_AsText(g)                 AS location,
           ST_X(ST_Centroid(g))         AS longitude,
           ST_Y(ST_Centroid(g))         AS latitude
    FROM (SELECT *,
                 ST_GeomFromGeoJSON((CASE WHEN value_json->>'type' = 'Feature'
                                          THEN value_json->'geometry'
                                          ELSE value_json END)::text) AS g
          FROM sensinact.history
          WHERE time > now() - INTERVAL '7 days' AND value_kind = 3 AND value_json IS NOT NULL
          ORDER BY time DESC
          LIMIT 1000) h;
