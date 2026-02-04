package fr.cnamts.cpam33.ordonnance.migrator.dto;

public record FlywayMigrationFileStatus(
        String version,
        String description,
        String script,
        String state,
        String type,
        Integer checksum,
        Long installedOnEpochMs,
        Integer executionTimeMs
) {
}
