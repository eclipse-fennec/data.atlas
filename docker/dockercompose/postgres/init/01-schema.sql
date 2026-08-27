-- Copyright (c) 2026 Contributors to the Eclipse Foundation.
--
-- This program and the accompanying materials are made
-- available under the terms of the Eclipse Public License 2.0
-- which is available at https://www.eclipse.org/legal/epl-2.0/
--
-- SPDX-License-Identifier: EPL-2.0

-- The schema the Data Atlas serves. It is created here and NOT by the
-- persistence unit: the Data Atlas is a serving layer, so
-- eclipselink.ddl-generation stays at the upstream default `none`.
--
-- The names must match what the *derived* eorm mapping expects for
-- person.ecore#//Person, which is asymmetric and worth stating explicitly
-- (see eclipse-fennec/emf.persistence-jpa#252, filed because this is
-- documented nowhere upstream):
--
--   * the table name is the EClass name UPPER-CASED       -> PERSON
--   * column names are the feature names VERBATIM         -> id, firstName, lastName
--
-- Both are emitted unquoted, so PostgreSQL folds them to lower case. Creating
-- the objects unquoted in lower case therefore matches exactly, and
-- DataAtlasPostgresIntegrationTest runs the real mapping against this file to
-- prove it stays that way.

CREATE TABLE person (
    id        VARCHAR(255) NOT NULL,
    firstname VARCHAR(255),
    lastname  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_person PRIMARY KEY (id)
);
