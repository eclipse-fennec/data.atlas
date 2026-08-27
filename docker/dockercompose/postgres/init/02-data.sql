-- Copyright (c) 2026 Contributors to the Eclipse Foundation.
--
-- This program and the accompanying materials are made
-- available under the terms of the Eclipse Public License 2.0
-- which is available at https://www.eclipse.org/legal/epl-2.0/
--
-- SPDX-License-Identifier: EPL-2.0

-- The same three example persons as the file-based example (data/persons.xmi),
-- so both setups can be compared row for row.

INSERT INTO person (id, firstname, lastname) VALUES
    ('p1', 'Ada',      'Lovelace'),
    ('p2', 'Grace',    'Hopper'),
    ('p3', 'Margaret', 'Hamilton');
