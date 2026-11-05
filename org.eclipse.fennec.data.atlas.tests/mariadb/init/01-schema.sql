-- Copyright (c) 2026 Contributors to the Eclipse Foundation.
--
-- This program and the accompanying materials are made
-- available under the terms of the Eclipse Public License 2.0
-- which is available at https://www.eclipse.org/legal/epl-2.0/
--
-- SPDX-License-Identifier: EPL-2.0

-- The schema DataAtlasMariaDbIntegrationTest serves. Like the PostgreSQL one
-- (docker/dockercompose/postgres/init/01-schema.sql) it is created here and NOT
-- by the persistence unit (eclipselink.ddl-generation stays at `none`), and the
-- names must match what the *derived* eorm mapping expects for
-- person.ecore#//Person (eclipse-fennec/emf.persistence-jpa#252):
--
--   * the table name is the EClass name UPPER-CASED       -> PERSON
--   * column names are the feature names VERBATIM         -> id, firstName, lastName
--
-- Unlike PostgreSQL, MariaDB on Linux does NOT fold unquoted identifiers:
-- table names are case-sensitive there (lower_case_table_names=0), column names
-- are not. So the table has to be created as PERSON exactly.

CREATE TABLE PERSON (
    id        VARCHAR(255) NOT NULL,
    firstName VARCHAR(255),
    lastName  VARCHAR(255) NOT NULL,
    CONSTRAINT pk_person PRIMARY KEY (id)
);
