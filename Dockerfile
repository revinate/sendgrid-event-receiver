FROM java:8

EXPOSE 8080

COPY docker-entrypoint.sh /
COPY build/libs/sendgrid-event-receiver.jar /

ENTRYPOINT ["/docker-entrypoint.sh"]
