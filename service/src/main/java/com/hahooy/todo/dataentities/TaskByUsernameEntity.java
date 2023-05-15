package com.hahooy.todo.dataentities;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.UUID;

import static org.springframework.data.cassandra.core.cql.Ordering.DESCENDING;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.CLUSTERED;
import static org.springframework.data.cassandra.core.cql.PrimaryKeyType.PARTITIONED;

@Builder
@Value
@Table("tasks_by_username")
public class TaskByUsernameEntity {

    @PrimaryKeyColumn(ordinal = 0, type = PARTITIONED)
    String username;

    @PrimaryKeyColumn(name = "task_id", ordinal = 1, type = CLUSTERED, ordering = DESCENDING)
    UUID taskId;
}
