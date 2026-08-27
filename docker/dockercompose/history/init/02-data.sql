-- Copyright (c) 2026 Contributors to the Eclipse Foundation.
--
-- This program and the accompanying materials are made
-- available under the terms of the Eclipse Public License 2.0
-- which is available at https://www.eclipse.org/legal/epl-2.0/
--
-- SPDX-License-Identifier: EPL-2.0

-- Sample recordings, shaped like what the Event Atlas would have written: two
-- providers of a weather model reporting a numeric and a textual resource.
--
-- Timestamps are RELATIVE to insert time (now() - INTERVAL …) on purpose: the
-- views in 01-schema.sql bound to the last 7 days, so fixed dates would silently
-- age out of the example and the endpoint would go empty for no visible reason.

INSERT INTO sensinact.numeric_data (time, modelpackageuri, model, provider, service, resource, data) VALUES
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'sensor', 'temperature', 21.4),
    (now() - INTERVAL '20 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'sensor', 'temperature', 21.1),
    (now() - INTERVAL '30 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'sensor', 'temperature', 20.8),
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'sensor', 'humidity',    63.0),
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-2', 'sensor', 'temperature', 19.9),
    (now() - INTERVAL '25 hours',   'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-2', 'sensor', 'temperature', 18.2);

INSERT INTO sensinact.text_data (time, modelpackageuri, model, provider, service, resource, data) VALUES
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'admin', 'status',    'ok'),
    (now() - INTERVAL '15 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-2', 'admin', 'status',    'maintenance'),
    (now() - INTERVAL '2 hours',    'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-2', 'admin', 'lastError', 'sensor timeout');

-- geo_data is seeded too, so the schema is exercised as upstream defines it, but
-- it is NOT mapped into the configuration: mapping geography(POINT,4326) onto an
-- EMF attribute is out of scope for this example (see eclipse-fennec/data.atlas#2).
INSERT INTO sensinact.geo_data (time, modelpackageuri, model, provider, service, resource, data) VALUES
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-1', 'admin', 'location', ST_GeogFromText('SRID=4326;POINT(11.5820 50.9270)')),
    (now() - INTERVAL '10 minutes', 'https://eclipse.org/sensinact/example/weather/1.0.0', 'weather', 'station-2', 'admin', 'location', ST_GeogFromText('SRID=4326;POINT(11.6060 50.9410)'));
