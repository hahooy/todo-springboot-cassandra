Run cassandra locally in a container `docker run --name cass_cluster -d -p 127.0.0.1:9042:9042 --rm cassandra:latest`

Start `cqlsh` in the Cassandra container `docker exec -it cass_cluster cqlsh ` and create a Cassandra keyspace for the application:
```
CREATE KEYSPACE todo
  WITH REPLICATION = { 
   'class' : 'SimpleStrategy', 
   'replication_factor' : 1 
  };
```

Create a table for storing tasks:

```
CREATE TABLE todo.task (
   task_id timeuuid PRIMARY KEY,
   name text, 
   description text, 
   username text,
   creation_time timestamp, 
);

CREATE TABLE todo.tasks_by_username ( 
   username text, 
   task_id timeuuid,
   PRIMARY KEY (username, task_id))
WITH CLUSTERING ORDER BY (task_id DESC);
```

- start the service `./gradlew bootRun`
- start the service in debug mode `./gradlew bootRun --debug-jvm`
- list tasks `./gradlew tasks`
- generate server stub `./gradlew openApiGenerate`
