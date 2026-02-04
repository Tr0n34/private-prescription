package fr.cnamts.cpam33.ordonnance.migrator.dto;

public record FlywayActionResult(
        String datasource,
        boolean success,
        long durationMs,
        int migrationsExecuted,
        java.util.List<FlywayMigrationFileStatus> migrations,
        String targetSchemaVersion,
        String errorType,
        String errorMessage
) {
}
