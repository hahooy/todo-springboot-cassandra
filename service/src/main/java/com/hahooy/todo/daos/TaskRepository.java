package com.hahooy.todo.daos;

import com.hahooy.todo.dataentities.TaskEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TaskRepository extends CassandraRepository<TaskEntity, UUID> {
}
