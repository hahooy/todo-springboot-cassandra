package com.hahooy.todo.controllers;

import com.datastax.driver.core.utils.UUIDs;
import com.hahooy.todo.api.TasksApi;
import com.hahooy.todo.api.models.Task;
import com.hahooy.todo.api.models.TasksGetRequest;
import com.hahooy.todo.daos.TaskByUsernameRepository;
import com.hahooy.todo.daos.TaskRepository;
import com.hahooy.todo.dataentities.TaskByUsernameEntity;
import com.hahooy.todo.dataentities.TaskEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@AllArgsConstructor(onConstructor = @__(@Inject))
public class TasksController implements TasksApi {

    private final TaskRepository taskRepository;
    private final TaskByUsernameRepository taskByUsernameRepository;

    @Override
    public ResponseEntity<List<Task>> tasksGet(Integer limit, Integer offset) {

        var taskIds = taskByUsernameRepository.findAll()
                .stream()
                .map(t -> t.getPrimaryKey().getTaskId())
                .toList();

        var taskIdToTaskEntity = taskRepository.findAllById(taskIds)
                .stream()
                .collect(Collectors.toMap(t -> t.getTaskId(), t -> t));

        var tasks = taskIds.stream()
                .map(taskIdToTaskEntity::get)
                .map(this::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(tasks);
    }

    @Override
    public ResponseEntity<Task> tasksPost(TasksGetRequest request) {

        // insert into task table
        var taskEntity = TaskEntity.builder()
                .taskId(UUIDs.timeBased())
                .name(request.getName())
                .description(request.getDescription())
                .username(request.getUsername())
                .build();
        var createdTaskEntity = taskRepository.insert(taskEntity);

        // insert into tasks_by_username table
        var taskByUsernameEntity = TaskByUsernameEntity.builder()
                .primaryKey(TaskByUsernameEntity.TaskByUsernamePrimaryKey.builder()
                        .username(createdTaskEntity.getUsername())
                        .taskId(createdTaskEntity.getTaskId())
                        .build())
                .build();
        taskByUsernameRepository.insert(taskByUsernameEntity);

        return ResponseEntity.ok(toDto(createdTaskEntity));
    }

    @Override
    public ResponseEntity<Task> tasksTaskIdGet(String taskId) {

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
                .build();
    }
}

