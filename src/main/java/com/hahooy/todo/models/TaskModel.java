package com.hahooy.todo.models;

import java.util.UUID;

public record TaskModel(UUID id, String name, String description) {

}
