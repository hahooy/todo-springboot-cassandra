package com.hahooy.todo.daos;

import com.hahooy.todo.dataentities.TaskByUsernameEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskByUsernameRepository extends CassandraRepository<TaskByUsernameEntity, TaskByUsernameEntity> {

    Slice<TaskByUsernameEntity> findByUsername(String username, Pageable pageRequest);
}
