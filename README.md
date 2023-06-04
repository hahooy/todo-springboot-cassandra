### Local development

You need to have Cassandra running locally before you can start the service:

1. Run cassandra locally in a container `docker run --name cass_cluster -d -p 127.0.0.1:9042:9042 --rm cassandra:4.1.2` 
2. Wait until the container is up and running, then use cqlsh to create a Cassandra keyspace for the application:
    ```shell
   query="CREATE KEYSPACE todo
        WITH REPLICATION = { 
         'class' : 'SimpleStrategy', 
         'replication_factor' : 1 
        };"

   docker exec -it cass_cluster cqlsh -e $query
    ```
3. start the service `./gradlew bootRun`

### Run service using docker compose

Use this command to run the service together with its dependencies using docker compose `docker compose -f service/docker-compose.yml up`.

### Other commands 

- start the service in debug mode `./gradlew bootRun --debug-jvm`
- list tasks `./gradlew tasks`
- generate server stub `./gradlew openApiGenerate`
- [swagger UI](http://localhost:8944/swagger-ui/index.html)
- to build docker image on Apple Silicon, set default platform to linux/x86_64 `export DOCKER_DEFAULT_PLATFORM=linux/amd64` then run `docker build -t todo-service .`
- `docker run --name todo-service -d -p 127.0.0.1:8944:8944 todo-service:latest`
