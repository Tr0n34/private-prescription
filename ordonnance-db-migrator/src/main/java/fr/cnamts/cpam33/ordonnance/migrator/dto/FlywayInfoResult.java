package fr.cnamts.cpam33.ordonnance.migrator.dto;

public record FlywayInfoResult(
        String datasource,
        String currentVersion,
        String currentDescription,
        int pendingCount,
        int appliedCount,
        int failedCount
) {
}
