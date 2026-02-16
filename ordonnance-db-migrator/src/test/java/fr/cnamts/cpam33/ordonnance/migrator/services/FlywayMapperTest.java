package fr.cnamts.cpam33.ordonnance.migrator.services;

import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayMigrationFileStatus;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FlywayMapperTest {

    private final FlywayMapper mapper = new FlywayMapper();

    @Test
    void isPending_shouldBeTrue_whenResolvedAndNotApplied() {
        MigrationState migrationState = mock(MigrationState.class);
        when(migrationState.isResolved()).thenReturn(true);
        when(migrationState.isApplied()).thenReturn(false);
        assertThat(mapper.isPending(migrationState)).isTrue();
    }

    @Test
    void isPending_shouldBeFalse_whenNotResolved() {
        MigrationState migrationState = mock(MigrationState.class);
        when(migrationState.isResolved()).thenReturn(false);
        when(migrationState.isApplied()).thenReturn(false);
        assertThat(mapper.isPending(migrationState)).isFalse();
    }

    @Test
    void isPending_shouldBeFalse_whenApplied() {
        MigrationState migrationState = mock(MigrationState.class);
        when(migrationState.isResolved()).thenReturn(true);
        when(migrationState.isApplied()).thenReturn(true);
        assertThat(mapper.isPending(migrationState)).isFalse();
    }

    @Test
    void isImportant_shouldBeTrue_whenPending() {
        MigrationState migrationState = mock(MigrationState.class);
        when(migrationState.isResolved()).thenReturn(true);
        when(migrationState.isApplied()).thenReturn(false);
        when(migrationState.isFailed()).thenReturn(false);
        assertThat(mapper.isImportant(migrationState)).isTrue();
    }

    @Test
    void isImportant_shouldBeTrue_whenFailedEvenIfNotPending() {
        MigrationState migrationState = mock(MigrationState.class);
        when(migrationState.isResolved()).thenReturn(true);
        when(migrationState.isApplied()).thenReturn(true);
        when(migrationState.isFailed()).thenReturn(true);
        assertThat(mapper.isImportant(migrationState)).isTrue();
    }

    @Test
    void isImportant_shouldBeFalse_whenNeitherPendingNorFailed() {
        MigrationState migrationState = mock(MigrationState.class);
        when(migrationState.isResolved()).thenReturn(true);
        when(migrationState.isApplied()).thenReturn(true);
        when(migrationState.isFailed()).thenReturn(false);
        assertThat(mapper.isImportant(migrationState)).isFalse();
    }

    @Test
    void toFileStatusList_shouldMapAllFields_whenOnlyImportantFalse() {
        MigrationInfo migrationInfo = mock(MigrationInfo.class, RETURNS_DEEP_STUBS);
        when(migrationInfo.getVersion()).thenReturn(MigrationVersion.fromVersion("1.2"));
        when(migrationInfo.getDescription()).thenReturn("Add table");
        when(migrationInfo.getScript()).thenReturn("V1.2__Add_table.sql");
        when(migrationInfo.getChecksum()).thenReturn(123456);
        when(migrationInfo.getExecutionTime()).thenReturn(42);
        MigrationState state = mock(MigrationState.class);
        when(state.name()).thenReturn("SUCCESS");
        when(migrationInfo.getState()).thenReturn(state);
        when(migrationInfo.getType().name()).thenReturn("SQL");
        Instant installed = Instant.parse("2026-02-03T10:00:00Z");
        when(migrationInfo.getInstalledOn()).thenReturn(Timestamp.from(installed));
        List<FlywayMigrationFileStatus> out = mapper.toFileStatusList(new MigrationInfo[]{migrationInfo}, false);
        assertThat(out).hasSize(1);
        FlywayMigrationFileStatus s = out.getFirst();
        assertThat(s.version()).isEqualTo("1.2");
        assertThat(s.description()).isEqualTo("Add table");
        assertThat(s.script()).isEqualTo("V1.2__Add_table.sql");
        assertThat(s.state()).isEqualTo("SUCCESS");
        assertThat(s.type()).isEqualTo("SQL");
        assertThat(s.checksum()).isEqualTo(123456);
        assertThat(s.installedOnEpochMs()).isEqualTo(installed.toEpochMilli());
        assertThat(s.executionTimeMs()).isEqualTo(42);
    }

    @Test
    void toFileStatusList_shouldHandleNullVersionAndNullInstalledOn() {
        MigrationInfo migrationInfo = mock(MigrationInfo.class, RETURNS_DEEP_STUBS);
        when(migrationInfo.getVersion()).thenReturn(null);
        when(migrationInfo.getDescription()).thenReturn("Repeatable");
        when(migrationInfo.getScript()).thenReturn("R__refresh_views.sql");
        when(migrationInfo.getChecksum()).thenReturn(null);
        when(migrationInfo.getExecutionTime()).thenReturn(null);
        when(migrationInfo.getInstalledOn()).thenReturn(null);
        MigrationState state = mock(MigrationState.class);
        when(state.name()).thenReturn("PENDING");
        when(migrationInfo.getState()).thenReturn(state);
        when(migrationInfo.getType().name()).thenReturn("SQL");
        List<FlywayMigrationFileStatus> out = mapper.toFileStatusList(new MigrationInfo[]{migrationInfo}, false);
        assertThat(out).hasSize(1);
        FlywayMigrationFileStatus s = out.get(0);
        assertThat(s.version()).isNull();
        assertThat(s.installedOnEpochMs()).isNull();
        assertThat(s.checksum()).isNull();
        assertThat(s.executionTimeMs()).isNull();
        assertThat(s.type()).isEqualTo("SQL");
    }

    @Test
    void toFileStatusList_shouldFilterOnlyImportant_whenOnlyImportantTrue() {
        MigrationInfo pendingMigration = mock(MigrationInfo.class, RETURNS_DEEP_STUBS);
        when(pendingMigration.getVersion()).thenReturn(MigrationVersion.fromVersion("1"));
        when(pendingMigration.getDescription()).thenReturn("Pending");
        when(pendingMigration.getScript()).thenReturn("V1__Pending.sql");
        when(pendingMigration.getType().name()).thenReturn("SQL");
        when(pendingMigration.getChecksum()).thenReturn(1);
        when(pendingMigration.getInstalledOn()).thenReturn(null);
        when(pendingMigration.getExecutionTime()).thenReturn(1);
        MigrationState pendingState = mock(MigrationState.class);
        when(pendingState.isResolved()).thenReturn(true);
        when(pendingState.isApplied()).thenReturn(false);
        when(pendingState.isFailed()).thenReturn(false);
        when(pendingState.name()).thenReturn("PENDING");
        when(pendingMigration.getState()).thenReturn(pendingState);

        MigrationInfo failedMigration = mock(MigrationInfo.class, RETURNS_DEEP_STUBS);
        when(failedMigration.getVersion()).thenReturn(MigrationVersion.fromVersion("2"));
        when(failedMigration.getDescription()).thenReturn("Failed");
        when(failedMigration.getScript()).thenReturn("V2__Failed.sql");
        when(failedMigration.getType().name()).thenReturn("SQL");
        when(failedMigration.getChecksum()).thenReturn(2);
        when(failedMigration.getInstalledOn()).thenReturn(null);
        when(failedMigration.getExecutionTime()).thenReturn(2);
        MigrationState failedState = mock(MigrationState.class);
        when(failedState.isResolved()).thenReturn(true);
        when(failedState.isApplied()).thenReturn(true);
        when(failedState.isFailed()).thenReturn(true);
        when(failedState.name()).thenReturn("FAILED");
        when(failedMigration.getState()).thenReturn(failedState);

        MigrationInfo okMigration = mock(MigrationInfo.class, RETURNS_DEEP_STUBS);
        when(okMigration.getVersion()).thenReturn(MigrationVersion.fromVersion("3"));
        when(okMigration.getDescription()).thenReturn("OK");
        when(okMigration.getScript()).thenReturn("V3__Ok.sql");
        when(okMigration.getType().name()).thenReturn("SQL");
        when(okMigration.getChecksum()).thenReturn(3);
        when(okMigration.getInstalledOn()).thenReturn(null);
        when(okMigration.getExecutionTime()).thenReturn(3);
        MigrationState okState = mock(MigrationState.class);
        when(okState.isResolved()).thenReturn(true);
        when(okState.isApplied()).thenReturn(true);
        when(okState.isFailed()).thenReturn(false);
        when(okState.name()).thenReturn("SUCCESS");
        when(okMigration.getState()).thenReturn(okState);

        List<FlywayMigrationFileStatus> out = mapper.toFileStatusList(
                new MigrationInfo[]{pendingMigration, failedMigration, okMigration},
                true
        );

        assertThat(out).hasSize(2);
        assertThat(out)
                .extracting(FlywayMigrationFileStatus::version)
                .containsExactly("1", "2");
        assertThat(out)
                .extracting(FlywayMigrationFileStatus::state)
                .containsExactly("PENDING", "FAILED");
    }

}
