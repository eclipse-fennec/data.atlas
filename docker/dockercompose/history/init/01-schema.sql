-- Copyright (c) 2026 Contributors to the Eclipse Foundation.
--
-- This program and the accompanying materials are made
-- available under the terms of the Eclipse Public License 2.0
-- which is available at https://www.eclipse.org/legal/epl-2.0/
--
-- SPDX-License-Identifier: EPL-2.0

-- The SensiNact history schema, transcribed VERBATIM from the statements
-- TimescaleHistoricalStore#setupTables executes:
--
--   eclipse-sensinact/org.eclipse.sensinact.gateway
--   southbound/history/timescale-provider/src/main/java/org/eclipse/sensinact/
--   gateway/southbound/history/timescale/TimescaleHistoricalStore.java#L264-L290
--   verified 2026-08-27 against 75f1f45cb2c70d62a874429d27996cb343d3963e
--
-- This file exists so the example is self-contained: it seeds the same schema
-- the Event Atlas would have created, without depending on that image. It is
-- NOT ours to change — if it drifts from upstream, upstream wins. The
-- docker-gated integration test runs the real eorm mapping against this file, so
-- a drift that breaks the mapping fails the build rather than production.
--
-- Note there is deliberately NO primary key here, exactly as upstream. The JPA
-- identity is declared in sensinact-history.eorm as a composite id over
-- (time, provider, service, resource).

CREATE SCHEMA IF NOT EXISTS sensinact;

CREATE TABLE IF NOT EXISTS sensinact.numeric_data ( time TIMESTAMPTZ NOT NULL, modelpackageuri VARCHAR(128) NOT NULL, model VARCHAR(128) NOT NULL, provider VARCHAR(128) NOT NULL, service VARCHAR(128) NOT NULL, resource VARCHAR(128) NOT NULL, data NUMERIC );
SELECT create_hypertable('sensinact.numeric_data', 'time', if_not_exists => TRUE);

CREATE TABLE IF NOT EXISTS sensinact.text_data ( time TIMESTAMPTZ NOT NULL, modelpackageuri VARCHAR(128) NOT NULL, model VARCHAR(128) NOT NULL, provider VARCHAR(128) NOT NULL, service VARCHAR(128) NOT NULL, resource VARCHAR(128) NOT NULL, data text );
SELECT create_hypertable('sensinact.text_data', 'time', if_not_exists => TRUE);

CREATE EXTENSION IF NOT EXISTS Postgis;

CREATE TABLE IF NOT EXISTS sensinact.geo_data ( time TIMESTAMPTZ NOT NULL, modelpackageuri VARCHAR(128) NOT NULL, model VARCHAR(128) NOT NULL, provider VARCHAR(128) NOT NULL, service VARCHAR(128) NOT NULL, resource VARCHAR(128) NOT NULL, data geography(POINT,4326) );
SELECT create_hypertable('sensinact.geo_data', 'time', if_not_exists => TRUE);

CREATE INDEX IF NOT EXISTS idx_numeric_data_provider_service_resource_time ON sensinact.numeric_data (provider, service, resource, time DESC);
CREATE INDEX IF NOT EXISTS idx_text_data_provider_service_resource_time ON sensinact.text_data (provider, service, resource, time DESC);
CREATE INDEX IF NOT EXISTS idx_geo_data_provider_service_resource_time ON sensinact.geo_data (provider, service, resource, time DESC);
CREATE INDEX IF NOT EXISTS idx_numeric_data_time ON sensinact.numeric_data (time DESC);
CREATE INDEX IF NOT EXISTS idx_text_data_time ON sensinact.text_data (time DESC);
CREATE INDEX IF NOT EXISTS idx_geo_data_time ON sensinact.geo_data (time DESC);
CREATE INDEX IF NOT EXISTS idx_numeric_data_covering ON sensinact.numeric_data (provider, service, resource) INCLUDE (time);
CREATE INDEX IF NOT EXISTS idx_text_data_covering ON sensinact.text_data (provider, service, resource) INCLUDE (time);
CREATE INDEX IF NOT EXISTS idx_geo_data_covering ON sensinact.geo_data (provider, service, resource) INCLUDE (time);

-- ---------------------------------------------------------------------------
-- Bounding the unbounded: what the Data Atlas actually serves
-- ---------------------------------------------------------------------------
-- A hypertable that has been recording for a month has no business being
-- dumped as CSV in one response. The endpoint stays a plain dump (no query
-- filters, by decision), so the bound lives here, in SQL — the entities in
-- sensinact-history.eorm are mapped onto these views, not onto the tables.
--
-- To serve the raw tables instead, point the <table> elements in the eorm
-- mapping at numeric_data / text_data. Nothing else changes.

CREATE OR REPLACE VIEW sensinact.numeric_data_recent AS
    SELECT time, modelpackageuri, model, provider, service, resource, data
    FROM sensinact.numeric_data
    WHERE time > now() - INTERVAL '7 days'
    ORDER BY time DESC
    LIMIT 1000;

CREATE OR REPLACE VIEW sensinact.text_data_recent AS
    SELECT time, modelpackageuri, model, provider, service, resource, data
    FROM sensinact.text_data
    WHERE time > now() - INTERVAL '7 days'
    ORDER BY time DESC
    LIMIT 1000;

-- geo_data needs one thing more than a window: its `data` column is
-- geography(POINT,4326), a PostGIS type that has no JDBC representation the
-- persistence stack knows. Rather than teaching the Data Atlas about PostGIS,
-- the view projects it into ordinary SQL types - PostGIS does the work in the
-- database, where the geometry already lives:
--
--   ST_AsText(data)        -> text             (WKT, e.g. POINT(11.582 50.927))
--   ST_X/ST_Y(data::geometry) -> double precision
--
-- Both map to plain EMF attributes with no type converter at all. The lon/lat
-- pair is also exactly the shape a future GeoJSON service would want.
CREATE OR REPLACE VIEW sensinact.geo_data_recent AS
    SELECT time, modelpackageuri, model, provider, service, resource,
           ST_AsText(data)           AS location,
           ST_X(data::geometry)      AS longitude,
           ST_Y(data::geometry)      AS latitude
    FROM sensinact.geo_data
    WHERE time > now() - INTERVAL '7 days'
    ORDER BY time DESC
    LIMIT 1000;
