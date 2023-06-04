package com.hahooy.todo.controllers;

import com.datastax.driver.core.utils.UUIDs;
import com.hahooy.todo.api.TasksApi;
import com.hahooy.todo.api.models.CreateTaskRequest;
import com.hahooy.todo.api.models.Task;
import com.hahooy.todo.daos.TaskByUsernameRepository;
import com.hahooy.todo.daos.TaskRepository;
import com.hahooy.todo.dataentities.TaskByUsernameEntity;
import com.hahooy.todo.dataentities.TaskEntity;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@AllArgsConstructor(onConstructor = @__(@Inject))
public class TasksController implements TasksApi {

    private final CassandraTemplate cassandraTemplate;
    private final TaskRepository taskRepository;
    private final TaskByUsernameRepository taskByUsernameRepository;

    @Override
    public ResponseEntity<List<Task>> getTasks(@NonNull String username, @NonNull Integer page, @NonNull Integer size) {

        // Cassandra doesn't support the offset/limit paging pattern, instead it requires a paging state to
        // forward-only navigate through pages.
        Slice<TaskByUsernameEntity> batch = taskByUsernameRepository
                .findByUsername(username, CassandraPageRequest.first(size));
        for (int i = 1; i <= page; i++) {
            if (!batch.hasNext()) {
                return ResponseEntity.ok(List.of());
            }
            batch = taskByUsernameRepository.findByUsername(username, batch.nextPageable());
        }

        var taskIds = batch
                .stream()
                .map(TaskByUsernameEntity::getTaskId)
                .toList();

        var taskIdToTaskEntity = taskRepository.findAllById(taskIds)
                .stream()
                .collect(Collectors.toMap(TaskEntity::getTaskId, t -> t));

        var tasks = taskIds.stream()
                .map(taskIdToTaskEntity::get)
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tasks);
    }

    @Override
    public ResponseEntity<Task> createTask(@NonNull CreateTaskRequest request) {

        // insert into task table
        var taskEntity = TaskEntity.builder()
                .taskId(UUIDs.timeBased())
                .name(request.getName())
                .description(request.getDescription())
                .username(request.getUsername())
                .creationTime(Instant.now())
                .build();

        // insert into tasks_by_username table
        var taskByUsernameEntity = TaskByUsernameEntity.builder()
                .username(taskEntity.getUsername())
                .taskId(taskEntity.getTaskId())
                .build();

        var batchOps = cassandraTemplate.batchOps();
        batchOps.insert(taskEntity)
                .insert(taskByUsernameEntity)
                .execute();

        return ResponseEntity.ok(toDto(taskEntity));
    }

    @Override
    public ResponseEntity<Task> getTaskById(@NonNull String taskId) {

        var taskEntity = taskRepository.findById(UUID.fromString(taskId));

        if (taskEntity.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, String.format("task %s not found", taskId));
        }

        return ResponseEntity.ok(toDto(taskEntity.get()));
    }

    private Task toDto(TaskEntity taskEntity) {
        return Task.builder()
                .taskId(taskEntity.getTaskId().toString())
                .name(taskEntity.getName())
                .description(taskEntity.getDescription())
                .username(taskEntity.getUsername())
                .creationTime(taskEntity.getCreationTime().atOffset(ZoneOffset.UTC))
                .build();
    }
}

