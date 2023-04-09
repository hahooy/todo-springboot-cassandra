start the service `./gradlew bootRun`
start the service in debug mode `./gradlew bootRun --debug-jvm`
list tasks `./gradlew tasks`
generate server stub `./gradlew :openApiGenerate`

Create a Cassandra keyspace for the application:
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

INSERT INTO todo.task (task_id, name, description, username) VALUES (now(), 'foo task', 'foo task description', 'foo user');
INSERT INTO todo.tasks_by_username (username, task_id) VALUES ('foo user', 7d87b620-d6a4-11ed-9640-b14676841fa6);
```