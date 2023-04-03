package com.hahooy.todo.controllers;

import com.hahooy.todo.api.TasksApi;
import com.hahooy.todo.api.models.Task;
import com.hahooy.todo.api.models.TasksGetRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class TasksController implements TasksApi {

    @Override
    public ResponseEntity<List<Task>> tasksGet(Integer limit, Integer offset) {
        var tasks = List.of(
                Task.builder()
                        .taskId(UUID.randomUUID().toString())
                        .name("foo task")
                        .description("foo task description")
                        .build()
        );
        return ResponseEntity.ok(tasks);
    }

    @Override
    public ResponseEntity<Task> tasksPost(TasksGetRequest request) {
        var task = Task.builder()
                .taskId(UUID.randomUUID().toString())
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return ResponseEntity.ok(task);
    }

    @Override
    public ResponseEntity<Task> tasksTaskIdGet(String taskId) {
        var task = Task.builder()
                        .taskId(UUID.randomUUID().toString())
                        .name("foo task")
                        .description("foo task description")
                        .build();
        return ResponseEntity.ok(task);
    }
}

