package com.hahooy.todo.controllers;

import com.hahooy.todo.api.TasksApi;
import com.hahooy.todo.api.models.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TasksController implements TasksApi {

    @Override
    public ResponseEntity<List<Task>> tasksGet(Integer limit, Integer offset) {
        var tasks = List.of(
                Task.builder()
                        .taskName("foo task")
                        .description("foo task description")
                        .build()
        );
        return ResponseEntity.ok(tasks);
    }

    @Override
    public ResponseEntity<Task> tasksPost(Task task) {
        return ResponseEntity.ok(task);
    }
}

