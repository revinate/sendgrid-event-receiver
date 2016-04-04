# SendGrid Event Receiver

Spring Boot-based application that receives SendGrid event webhooks and forwards
them to RabbitMQ.

## Requirements

* Java 1.8
* Docker

## Building

This application can be built using the included Gradle wrapper.

To build the runnable Jar:

```
$ ./gradlew build
```

Once the Jar is built, a Docker image for the application can also be built:

```
$ docker build -t sendgrid-event-receiver .
```

## Running and testing

The Gradle wrapper can also be used to run the application locally.

First, start the RabbitMQ Docker container:

```
$ ./gradlew startDependencies
```

Next, start the application:

```
$ ./gradlew bootRun
```

If the hostname of the Docker host is not localhost (e.g. if using Docker Machine),
the configured RabbitMQ address in the application needs to be changed:

```
$ ./gradlew bootRun -Pargs="--spring.rabbitmq.addresses=<hostname>:5672"
```

Where `<hostname>` is e.g. the Docker Machine IP.

## Receiving events

Events can be POSTed to the `/interface/sendgrid/<id>` endpoint, where `<id>`
can be any value, but is designed to be the SendGrid subuser account name. This
endpoint is configured with HTTP Basic Auth, with default username "sendgrid"
and password "sendgrid".
