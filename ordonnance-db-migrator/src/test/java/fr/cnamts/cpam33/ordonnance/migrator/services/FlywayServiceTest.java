package fr.cnamts.cpam33.ordonnance.migrator.services;

import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayActionResult;
import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayDatabaseStatus;
import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayMigrationFileStatus;
import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayValidateResult;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.MigrationState;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.output.MigrateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.*;

import static fr.cnamts.cpam33.ordonnance.migrator.fixtures.FlywayServiceHelper.mockMigration;
import static fr.cnamts.cpam33.ordonnance.migrator.fixtures.FlywayServiceHelper.validateResult;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FlywayServiceTest {

    @Mock Flyway ordonnanceFlyway;
    @Mock Flyway traceFlyway;
    @Mock FlywayMapper mapper;

    @Mock MigrationInfoService ordonnanceInfoService;
    @Mock MigrationInfoService traceInfoService;

    FlywayService service;

    @BeforeEach
    void setUp() {
        service = new FlywayService(ordonnanceFlyway, traceFlyway, mapper);
    }

    @Test
    void regex_should_capture_full_description() throws Exception {
        var f = FlywayService.class.getDeclaredField("MIGRATION_FILE_PATTERN");
        f.setAccessible(true);
        var p = (java.util.regex.Pattern) f.get(null);

        var m = p.matcher("Checksum mismatch in V1.2__Bad_desc.sql");
        assertThat(m.find()).isTrue();
        assertThat(m.group(1)).isEqualTo("V1.2");
        assertThat(m.group(2)).isEqualTo("Bad_desc"); // ou Bad_desc.sql selon ton pattern exact
    }

    @Test
    void statusAll_shouldReturnTwoStatuses_andComputeCounts_andReturnMigrations() {
        when(ordonnanceFlyway.info()).thenReturn(ordonnanceInfoService);
        when(traceFlyway.info()).thenReturn(traceInfoService);
        MigrationInfo ordCurrent = mockMigration("2.0", "current ord", MigrationState.SUCCESS);
        MigrationInfo[] ordAll = new MigrationInfo[]{
                mockMigration("1.0", "applied 1", MigrationState.SUCCESS),
                mockMigration("1.1", "failed 1", MigrationState.FAILED),
                mockMigration("1.2", "pending 1", MigrationState.PENDING),
                mockMigration(null, "repeatable pending", MigrationState.PENDING)
        };
        MigrationInfo traceCurrent = mockMigration("5", "current trace", MigrationState.SUCCESS);
        MigrationInfo[] traceAll = new MigrationInfo[]{
                mockMigration("4", "applied", MigrationState.SUCCESS),
                mockMigration("5", "applied", MigrationState.SUCCESS)
        };
        when(ordonnanceInfoService.current()).thenReturn(ordCurrent);
        when(ordonnanceInfoService.all()).thenReturn(ordAll);
        when(traceInfoService.current()).thenReturn(traceCurrent);
        when(traceInfoService.all()).thenReturn(traceAll);
        when(mapper.isPending(any())).thenAnswer(inv -> inv.getArgument(0) == MigrationState.PENDING);
        List<FlywayMigrationFileStatus> ordFiles = List.of(mock(FlywayMigrationFileStatus.class));
        List<FlywayMigrationFileStatus> traceFiles = List.of(mock(FlywayMigrationFileStatus.class));
        when(mapper.toFileStatusList(same(ordAll), eq(true))).thenReturn(ordFiles);
        when(mapper.toFileStatusList(same(traceAll), eq(true))).thenReturn(traceFiles);
        List<FlywayDatabaseStatus> statuses = service.statusAll(true);
        assertThat(statuses).hasSize(2);
        FlywayDatabaseStatus ord = statuses.get(0);
        assertThat(ord.datasource()).isEqualTo(FlywayService.ORDONNANCE_KEY);
        assertThat(ord.currentVersion()).isEqualTo("2.0");
        assertThat(ord.currentDescription()).isEqualTo("current ord");
        assertThat(ord.appliedCount()).isEqualTo(2); // SUCCESS + FAILED
        assertThat(ord.failedCount()).isEqualTo(1);
        assertThat(ord.pendingCount()).isEqualTo(2);
        assertThat(ord.pendingVersions()).containsExactly("1.2", null);
        assertThat(ord.pendingDescriptions()).containsExactly("pending 1", "repeatable pending");
        assertThat(ord.migrations()).isSameAs(ordFiles);
        FlywayDatabaseStatus tr = statuses.get(1);
        assertThat(tr.datasource()).isEqualTo(FlywayService.TRACE_KEY);
        assertThat(tr.currentVersion()).isEqualTo("5");
        assertThat(tr.currentDescription()).isEqualTo("current trace");
        assertThat(tr.appliedCount()).isEqualTo(2);
        assertThat(tr.failedCount()).isEqualTo(0);
        assertThat(tr.pendingCount()).isEqualTo(0);
        assertThat(tr.migrations()).isSameAs(traceFiles);
    }

    @Test
    void validateAll_shouldReturnSuccess_whenValidationSuccessful() {
        when(ordonnanceFlyway.validateWithResult()).thenReturn(validateResult(true, null));
        when(traceFlyway.validateWithResult()).thenReturn(validateResult(true, null));
        List<FlywayValidateResult> results = service.validateAll();
        assertThat(results).hasSize(2);
        FlywayValidateResult r1 = results.getFirst();
        assertThat(r1.datasource()).isEqualTo(FlywayService.ORDONNANCE_KEY);
        assertThat(r1.validationSuccessful()).isTrue();
        assertThat(r1.errorMessage()).isNull();
        assertThat(r1.invalidMigrationVersion()).isNull();
        assertThat(r1.invalidMigrationDescription()).isNull();
        FlywayValidateResult r2 = results.get(1);
        assertThat(r2.datasource()).isEqualTo(FlywayService.TRACE_KEY);
        assertThat(r2.validationSuccessful()).isTrue();
    }

    @Test
    void validateAll_shouldParseInvalidMigrationVersionAndDescription_whenPatternMatches() {
        when(ordonnanceFlyway.validateWithResult())
                .thenReturn(validateResult(false, "Checksum mismatch in V1.2__Bad_desc.sql"));
        when(traceFlyway.validateWithResult()).thenReturn(validateResult(true, null));
        List<FlywayValidateResult> results = service.validateAll();
        FlywayValidateResult r = results.get(0);
        assertThat(r.datasource()).isEqualTo(FlywayService.ORDONNANCE_KEY);
        assertThat(r.validationSuccessful()).isFalse();
        assertThat(r.errorMessage()).contains("Checksum mismatch");
        assertThat(r.invalidMigrationVersion()).isEqualTo("1.2");
        assertThat(r.invalidMigrationDescription()).isEqualTo("Bad desc");
    }

    @Test
    void validateAll_shouldNotSetInvalidFields_whenNoPatternMatch() {
        when(ordonnanceFlyway.validateWithResult())
                .thenReturn(validateResult(false, "Something went wrong but no migration file present"));
        when(traceFlyway.validateWithResult()).thenReturn(validateResult(true, null));
        List<FlywayValidateResult> results = service.validateAll();
        FlywayValidateResult r = results.get(0);
        assertThat(r.validationSuccessful()).isFalse();
        assertThat(r.invalidMigrationVersion()).isNull();
        assertThat(r.invalidMigrationDescription()).isNull();
    }

    @Test
    void migrateOrdonnance_shouldReturnSuccessActionResult_withTarget_andFiles() {
        MigrateResult mr = new MigrateResult();
        mr.migrationsExecuted = 3;
        mr.targetSchemaVersion = MigrationVersion.fromVersion("10").getVersion();
        when(ordonnanceFlyway.migrate()).thenReturn(mr);
        when(ordonnanceFlyway.info()).thenReturn(ordonnanceInfoService);
        MigrationInfo[] after = new MigrationInfo[]{
                mockMigration("9", "applied", MigrationState.SUCCESS),
                mockMigration("10", "applied", MigrationState.SUCCESS)
        };
        when(ordonnanceInfoService.all()).thenReturn(after);
        List<FlywayMigrationFileStatus> files = List.of(mock(FlywayMigrationFileStatus.class));
        when(mapper.toFileStatusList(same(after), eq(true))).thenReturn(files);
        FlywayActionResult res = service.migrateOrdonnance(true);
        assertThat(res.datasource()).isEqualTo(FlywayService.ORDONNANCE_KEY);
        assertThat(res.success()).isTrue();
        assertThat(res.migrationsExecuted()).isEqualTo(3);
        assertThat(res.targetSchemaVersion()).isEqualTo("10");
        assertThat(res.migrations()).isSameAs(files);
        assertThat(res.errorType()).isNull();
        assertThat(res.errorMessage()).isNull();
        InOrder inOrder = inOrder(ordonnanceFlyway, ordonnanceInfoService, mapper);
        inOrder.verify(ordonnanceFlyway).migrate();
        inOrder.verify(ordonnanceFlyway).info();
        inOrder.verify(ordonnanceInfoService).all();
        inOrder.verify(mapper).toFileStatusList(same(after), eq(true));
    }

    @Test
    void migrateTrace_shouldReturnFailureActionResult_withRootCauseMessage_andEmptyMigrationsOnFallbackFailure() {
        RuntimeException ex = new RuntimeException("top", new IllegalStateException("boom"));
        when(traceFlyway.migrate()).thenThrow(ex);
        when(traceFlyway.info()).thenThrow(new RuntimeException("info down"));
        FlywayActionResult res = service.migrateTrace(true);
        assertThat(res.datasource()).isEqualTo(FlywayService.TRACE_KEY);
        assertThat(res.success()).isFalse();
        assertThat(res.migrationsExecuted()).isEqualTo(0);
        assertThat(res.targetSchemaVersion()).isNull();
        assertThat(res.errorType()).isEqualTo("RuntimeException");
        assertThat(res.errorMessage()).isEqualTo("boom"); // root cause message
        assertThat(res.migrations()).isEmpty();
    }

    @Test
    void migrateTrace_shouldFallbackToMapperWithOnlyImportantFalse_whenExceptionOccurs() {
        when(traceFlyway.migrate()).thenThrow(new RuntimeException("fail"));
        when(traceFlyway.info()).thenReturn(traceInfoService);
        MigrationInfo[] all = new MigrationInfo[]{mockMigration("1", "x", MigrationState.SUCCESS)};
        when(traceInfoService.all()).thenReturn(all);
        List<FlywayMigrationFileStatus> fallbackFiles = List.of(mock(FlywayMigrationFileStatus.class));
        when(mapper.toFileStatusList(same(all), eq(false))).thenReturn(fallbackFiles);
        FlywayActionResult res = service.migrateTrace(true);
        assertThat(res.success()).isFalse();
        assertThat(res.migrations()).isSameAs(fallbackFiles);
        verify(mapper).toFileStatusList(same(all), eq(false)); // false in catch
    }

    @Test
    void migrateAll_shouldThrow409_whenMigrationAlreadyRunning() throws Exception {
        CountDownLatch entered = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);

        when(ordonnanceFlyway.migrate()).thenAnswer(inv -> {
            entered.countDown();
            release.await(2, TimeUnit.SECONDS);
            MigrateResult mr = new MigrateResult();
            mr.migrationsExecuted = 0;
            mr.targetSchemaVersion = null;
            return mr;
        });
        when(ordonnanceFlyway.info()).thenReturn(ordonnanceInfoService);
        when(ordonnanceInfoService.all()).thenReturn(new MigrationInfo[0]);
        when(mapper.toFileStatusList(any(MigrationInfo[].class), anyBoolean())).thenReturn(List.of());
        MigrateResult traceMr = new MigrateResult();
        traceMr.migrationsExecuted = 0;
        traceMr.targetSchemaVersion = null;
        when(traceFlyway.migrate()).thenReturn(traceMr);
        when(traceFlyway.info()).thenReturn(traceInfoService);
        when(traceInfoService.all()).thenReturn(new MigrationInfo[0]);
        ExecutorService pool = Executors.newSingleThreadExecutor();
        Future<?> f = pool.submit(() -> service.migrateAll(true));
        assertThat(entered.await(2, TimeUnit.SECONDS)).isTrue();
        Throwable thrown = catchThrowable(() -> service.migrateAll(true));
        assertThat(thrown).isInstanceOf(ResponseStatusException.class);
        ResponseStatusException rse = (ResponseStatusException) thrown;
        assertThat(rse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(rse.getReason()).isEqualTo("Migration already running");
        release.countDown();
        f.get(2, TimeUnit.SECONDS);
        pool.shutdownNow();
    }



}
