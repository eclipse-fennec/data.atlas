-- Copyright (c) 2026 Contributors to the Eclipse Foundation.
--
-- This program and the accompanying materials are made
-- available under the terms of the Eclipse Public License 2.0
-- which is available at https://www.eclipse.org/legal/epl-2.0/
--
-- SPDX-License-Identifier: EPL-2.0

-- Sample recordings, shaped like what the Event Atlas would have written into
-- the unified sensinact.history table: two providers of a weather model
-- reporting numeric, textual, boolean and location resources. value_kind is the
-- ValueKind ordinal (0 NUMBER, 1 BOOLEAN, 2 STRING, 3 GEOJSON, 4 OBJECT);
-- java_type is what the provider records (informational, not mapped).
--
-- Timestamps are RELATIVE to insert time (now() - INTERVAL …) so the example
-- always looks freshly recorded; the endpoints order by time, newest first, and
-- the 25-hour-old row is the oldest one (the integration test expects it last).

-- NUMBER -> value_num
INSERT INTO sensinact.history (time, modelpackageuri, model, provider, service, resource, value_kind, java_type, value_num) VALUES
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'sensor', 'temperature', 0, 'java.lang.Double', 21.4),
    (now() - INTERVAL '20 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'sensor', 'temperature', 0, 'java.lang.Double', 21.1),
    (now() - INTERVAL '30 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'sensor', 'temperature', 0, 'java.lang.Double', 20.8),
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'sensor', 'humidity',    0, 'java.lang.Double', 63.0),
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-2', 'sensor', 'temperature', 0, 'java.lang.Double', 19.9),
    (now() - INTERVAL '25 hours',   'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-2', 'sensor', 'temperature', 0, 'java.lang.Double', 18.2);

-- STRING -> value_json holds a JSON string ("ok"); BOOLEAN -> true/false. The
-- text endpoint serves value_json as it is, i.e. as JSON text.
INSERT INTO sensinact.history (time, modelpackageuri, model, provider, service, resource, value_kind, java_type, value_json) VALUES
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'admin', 'status',    2, 'java.lang.String',  to_jsonb('ok'::text)),
    (now() - INTERVAL '15 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-2', 'admin', 'status',    2, 'java.lang.String',  to_jsonb('maintenance'::text)),
    (now() - INTERVAL '2 hours',    'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-2', 'admin', 'lastError', 2, 'java.lang.String',  to_jsonb('sensor timeout'::text)),
    (now() - INTERVAL '5 minutes',  'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'admin', 'online',    1, 'java.lang.Boolean', 'true'::jsonb);

-- GEOJSON -> value_json holds a GeoJSON document. Both shapes the provider may
-- write are seeded: a bare Point geometry and a Feature wrapping one.
INSERT INTO sensinact.history (time, modelpackageuri, model, provider, service, resource, value_kind, java_type, value_json) VALUES
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'admin', 'location', 3, 'org.eclipse.sensinact.gateway.geojson.Point',   '{"type":"Point","coordinates":[11.5820,50.9270]}'::jsonb),
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-2', 'admin', 'location', 3, 'org.eclipse.sensinact.gateway.geojson.Feature', '{"type":"Feature","geometry":{"type":"Point","coordinates":[11.6060,50.9410]},"properties":{}}'::jsonb);
