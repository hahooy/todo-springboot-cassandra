package com.hahooy.todo;

import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cognitor.cassandra.migration.Database;
import org.cognitor.cassandra.migration.MigrationConfiguration;
import org.cognitor.cassandra.migration.MigrationRepository;
import org.cognitor.cassandra.migration.MigrationTask;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Inject))
public class TodoAppStartupRunner implements CommandLineRunner {

    private final CqlSessionBuilder cqlSessionBuilder;

    @Override
    public void run(String...args) {
        log.info("Starting DB Migration");

        var session = cqlSessionBuilder.build();
        var migrationConfig = new MigrationConfiguration().withKeyspaceName("todo");
        var database = new Database(session, migrationConfig);
        var migration = new MigrationTask(database, new MigrationRepository());

        migration.migrate();

        log.info("DB Migration Complete");
    }
}
