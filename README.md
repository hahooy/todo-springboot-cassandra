# Todo Service with Spring Boot

This is a simple todo app that demonstrates how to implement a RESTful web service using Spring Boot and Cassandra.

It leverages the following technologies:

- Spring boot
- Cassandra for data storage
- Cassandra schema migration
- OpenAPI for defining APIs, auto generating server stub with bean validation and generating Swagger documentation
- Docker for containerization
- Docker compose
- Gradle
- Lombok

## Local development

### Prerequisites

This application depends on:

- java 17
- Cassandra (or having docker to run a Cassandra container)

### Run service locally

You need to have Cassandra running locally before you can start the service:

1. Run cassandra locally in a container `docker run --name cass_cluster -d -p 127.0.0.1:9042:9042 --rm cassandra:4.1.2` 
2. Wait until the container is up and running, then use cqlsh to create a Cassandra keyspace for the service:
    ```shell
   docker exec -it cass_cluster cqlsh -e "CREATE KEYSPACE todo
        WITH REPLICATION = { 
         'class' : 'SimpleStrategy', 
         'replication_factor' : 1 
        };"
    ```
3. Start the service `./gradlew bootRun`

### Run service using docker compose

Use this command to run the service together with its dependencies using docker compose `docker compose -f service/docker-compose.yml up`.

### To build docker image

- Run `docker build -t todo-service service` to build the docker image. 
- Run the image `docker run --name todo-service -d -p 127.0.0.1:8944:8944 todo-service:latest`

### Other useful commands 

- Start the service in debug mode `./gradlew bootRun --debug-jvm`
- List tasks `./gradlew tasks`
- Generate server stub `./gradlew openApiGenerate`
- Visit [Swagger UI](http://localhost:8944/swagger-ui/index.html) for API documentation
