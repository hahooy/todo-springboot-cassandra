package com.hahooy.todo.dataentities;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

@Builder
@Value
@Table("task")
public class TaskEntity {

    @PrimaryKey("task_id")
    UUID taskId;

    @Column
    String name;

    @Column
    String description;

    @Column
    String username;
}
