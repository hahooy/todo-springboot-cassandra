CREATE TABLE IF NOT EXISTS todo.task (
   task_id timeuuid PRIMARY KEY,
   name text,
   description text,
   username text,
   creation_time timestamp,
);

CREATE TABLE IF NOT EXISTS todo.tasks_by_username (
   username text,
   task_id timeuuid,
   PRIMARY KEY (username, task_id))
WITH CLUSTERING ORDER BY (task_id DESC);