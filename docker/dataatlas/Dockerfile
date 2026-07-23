# Copyright (c) 2026 Contributors to the Eclipse Foundation.
#
# This program and the accompanying materials are made
# available under the terms of the Eclipse Public License 2.0
# which is available at https://www.eclipse.org/legal/epl-2.0/
#
# SPDX-License-Identifier: EPL-2.0

ARG APP_HOME=/opt/dataatlas
ARG RUNTIME_DIR=${APP_HOME}/runtime
ARG DATA_DIR=${RUNTIME_DIR}/data
ARG LOG_DIR=${RUNTIME_DIR}/log
ARG TMPDIR=/tmp/dataatlas
ARG RUNTIME_JAR=${APP_HOME}/dataatlas.runtime_docker.jar

FROM eclipse-temurin:21-jre-alpine AS builder

ARG APP_HOME
ARG RUNTIME_DIR
ARG DATA_DIR
ARG LOG_DIR
ARG TMPDIR
ARG RUNTIME_JAR

RUN set -eux; \
    mkdir -p "${APP_HOME}" "${DATA_DIR}" "${LOG_DIR}" "${TMPDIR}"; \
    chown -R 65532:65532 "${APP_HOME}" "${TMPDIR}"; \
    chmod -R u+rwX,go-rwx "${DATA_DIR}" "${LOG_DIR}" "${TMPDIR}"

COPY content/dataatlas.runtime_docker.jar "${RUNTIME_JAR}"
COPY content/runtime/ "${RUNTIME_DIR}/"

RUN chown -R 65532:65532 "${APP_HOME}"

FROM gcr.io/distroless/java21-debian12:nonroot

ARG APP_HOME
ARG RUNTIME_DIR
ARG DATA_DIR
ARG LOG_DIR
ARG TMPDIR
ARG RUNTIME_JAR

ENV APP_HOME=${APP_HOME} \
    RUNTIME_DIR=${RUNTIME_DIR} \
    DATA_DIR=${DATA_DIR} \
    LOG_DIR=${LOG_DIR} \
    TMPDIR=${TMPDIR} \
    RUNTIME_JAR=${RUNTIME_JAR}

COPY --from=builder ${APP_HOME} ${APP_HOME}
COPY --from=builder --chown=65532:65532 ${TMPDIR} ${TMPDIR}

EXPOSE 8080
WORKDIR ${APP_HOME}

ENTRYPOINT ["java", \
    "-Dgosh.args=--nointeractive", \
    "-Ddata=/opt/dataatlas/runtime/data", \
    "-Datlas.log.dir=/opt/dataatlas/runtime/log", \
    "-Dlogback.configurationFile=file:/opt/dataatlas/runtime/etc/logback.xml", \
    "-Djava.io.tmpdir=/tmp/dataatlas", \
    "-jar", "/opt/dataatlas/dataatlas.runtime_docker.jar"]
