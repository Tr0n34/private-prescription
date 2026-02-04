package fr.cnamts.cpam33.ordonnance.migrator.dto;

public record FlywayValidateResult(
        String datasource,
        boolean validationSuccessful,
        String errorMessage,
        String errorCode,
        String invalidMigrationVersion,
        String invalidMigrationDescription
) {

    public record InvalidMigration(
            String version,
            String description,
            String filepath
    ) {}

}
