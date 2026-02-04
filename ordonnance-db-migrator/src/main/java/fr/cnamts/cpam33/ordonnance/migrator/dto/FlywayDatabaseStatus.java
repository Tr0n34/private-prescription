package fr.cnamts.cpam33.ordonnance.migrator.dto;

import java.util.List;

public record FlywayDatabaseStatus(
    String datasource,
    String currentVersion,
    String currentDescription,
    int pendingCount,
    int appliedCount,
    int failedCount,
    String[] pendingVersions,
    String[] pendingDescriptions,
    List<FlywayMigrationFileStatus> migrations
) {

}
