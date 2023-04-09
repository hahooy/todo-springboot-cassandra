package com.hahooy.todo.daos;

import com.hahooy.todo.dataentities.TaskByUsernameEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskByUsernameRepository extends CassandraRepository<TaskByUsernameEntity, TaskByUsernameEntity> {
}
