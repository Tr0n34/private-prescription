package fr.cnamts.cpam33.ordonnance.migrator.fixtures;

import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayActionResult;
import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayMigrationFileStatus;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public final class FlywayMigrationFixtures {

    public static FlywayMigrationFileStatus flywayMigrationFileStatusSuccess() {
        return new FlywayMigrationFileStatus(
                "1",
                "init",
                "V1__init.sql",
                "SUCCESS",
                "SQL",
                123,
                null,
                5
        );
    }

    public static @NonNull FlywayActionResult flywayActionResultFromOrdonnanceMigrationSuccess() {
        FlywayMigrationFileStatus file = flywayMigrationFileStatusSuccess();
        return new FlywayActionResult(
                "ordonnance",
                true,
                12,
                1,
                List.of(file),
                "1",
                null,
                null
        );
    }

    public static @NonNull FlywayActionResult flywayActionResultFromOrdonnanceMigrationSuccessWith2Executions() {
        return new FlywayActionResult(
                "ordonnance",
                true,
                10,
                2,
                List.of(),
                "10",
                null,
                null
        );
    }

    public static @NonNull FlywayActionResult flywayActionResultFromTraceMigrationFailure() {
        return new FlywayActionResult(
                "trace",
                false,
                2,
                0,
                List.of(),
                null,
                "RuntimeException",
                "Migration already running"
        );
    }

}
