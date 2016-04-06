#!/bin/sh

exec java ${JAVA_OPTS} -jar sendgrid-event-receiver.jar ${APP_OPTS}
