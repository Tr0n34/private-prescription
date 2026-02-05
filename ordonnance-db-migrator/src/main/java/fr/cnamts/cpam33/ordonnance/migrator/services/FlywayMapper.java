package fr.cnamts.cpam33.ordonnance.migrator.services;

import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayMigrationFileStatus;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class FlywayMapper {

    public boolean isPending(MigrationState state) {
        return state.isResolved() && !state.isApplied();
    }

    public boolean isImportant(MigrationState state) {
        return isPending(state) || state.isFailed();
    }

    public List<FlywayMigrationFileStatus> toFileStatusList(MigrationInfo[] infos, boolean onlyImportant) {
        var out = new ArrayList<FlywayMigrationFileStatus>(infos.length);
        for ( var migration : infos ) {
            MigrationState state = migration.getState();
            if ( onlyImportant && !isImportant(state) ) {
                continue;
            }
            out.add(new FlywayMigrationFileStatus(
                    migration.getVersion() == null ? null : migration.getVersion().getVersion(),
                    migration.getDescription(),
                    migration.getScript(),
                    state.name(),
                    migration.getType().name(),
                    migration.getChecksum(),
                    migration.getInstalledOn() == null ? null : migration.getInstalledOn().toInstant().toEpochMilli(),
                    migration.getExecutionTime()
            ));
        }
        return out;
    }

}
