-- Copyright (c) 2026 Contributors to the Eclipse Foundation.
--
-- This program and the accompanying materials are made
-- available under the terms of the Eclipse Public License 2.0
-- which is available at https://www.eclipse.org/legal/epl-2.0/
--
-- SPDX-License-Identifier: EPL-2.0

-- The same three persons as the PostgreSQL example.

INSERT INTO PERSON (id, firstName, lastName) VALUES
    ('p1', 'Ada',      'Lovelace'),
    ('p2', 'Grace',    'Hopper'),
    ('p3', 'Margaret', 'Hamilton');
