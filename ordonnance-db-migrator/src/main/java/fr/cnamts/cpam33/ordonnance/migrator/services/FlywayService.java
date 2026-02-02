// ---------- FlywayService.java ----------
package fr.cnamts.cpam33.ordonnance.migrator.services;

import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayActionResult;
import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayDatabaseStatus;
import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayMigrationFileStatus;
import fr.cnamts.cpam33.ordonnance.migrator.dto.FlywayValidateResult;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.flywaydb.core.api.output.ValidateResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

@Service
public class FlywayService {

    private static final Logger log = LoggerFactory.getLogger(FlywayService.class);

    private static final Pattern MIGRATION_FILE_PATTERN = Pattern.compile("(V\\d+(?:\\.\\d+)*)__([^\\s]+?)(?:\\.sql)?");
    public static final String ORDONNANCE_KEY = "ordonnance";
    public static final String TRACE_KEY = "trace";

    private final Flyway ordonnance;
    private final Flyway trace;
    private final FlywayMapper mapper;

    private final ReentrantLock migrateLock = new ReentrantLock();

    public FlywayService(@Qualifier("ordonnanceFlyway") Flyway ordonnance,
                         @Qualifier("traceFlyway") Flyway trace,
                         FlywayMapper mapper) {
        this.ordonnance = ordonnance;
        this.trace = trace;
        this.mapper = mapper;
    }

    public List<FlywayDatabaseStatus> statusAll(boolean onlyImportant) {
        return List.of(
                statusOne(ORDONNANCE_KEY, ordonnance, onlyImportant),
                statusOne(TRACE_KEY, trace, onlyImportant)
        );
    }

    public List<FlywayValidateResult> validateAll() {
        return List.of(
                validateOne(ORDONNANCE_KEY, ordonnance),
                validateOne(TRACE_KEY, trace)
        );
    }

    public List<FlywayActionResult> migrateAll(boolean onlyImportant) {
        return withMigrationLock(() -> List.of(
                migrateOne(ORDONNANCE_KEY, ordonnance, onlyImportant),
                migrateOne(TRACE_KEY, trace, onlyImportant)
        ));
    }

    public FlywayActionResult migrateOrdonnance(boolean onlyImportant) {
        return withMigrationLock(() -> migrateOne(ORDONNANCE_KEY, ordonnance, onlyImportant));
    }

    public FlywayActionResult migrateTrace(boolean onlyImportant) {
        return withMigrationLock(() -> migrateOne(TRACE_KEY, trace, onlyImportant));
    }

    private FlywayDatabaseStatus statusOne(String name, Flyway flyway, boolean onlyImportant) {
        var current = flyway.info().current();
        MigrationInfo[] infos = flyway.info().all();
        int applied = 0;
        int failed = 0;
        int pending = 0;
        List<String> pendingVersions = new ArrayList<>();
        List<String> pendingDescriptions = new ArrayList<>();
        for ( var migration : infos ) {
            MigrationState state = migration.getState();
            if (state.isApplied()) applied++;
            if (state.isFailed()) failed++;
            if ( mapper.isPending(state) ) {
                pending++;
                pendingVersions.add(migration.getVersion() == null ? null : migration.getVersion().getVersion());
                pendingDescriptions.add(migration.getDescription());
            }
        }
        String currentVersion = (current == null || current.getVersion() == null)
                ? null
                : current.getVersion().getVersion();
        String currentDescription = (current == null) ? null : current.getDescription();
        List<FlywayMigrationFileStatus> migrations = mapper.toFileStatusList(infos, onlyImportant);
        return new FlywayDatabaseStatus(
                name,
                currentVersion,
                currentDescription,
                pending,
                applied,
                failed,
                pendingVersions.toArray(String[]::new),
                pendingDescriptions.toArray(String[]::new),
                migrations
        );
    }

    private FlywayValidateResult validateOne(String name, Flyway flyway) {
        ValidateResult res = flyway.validateWithResult();
        if ( res.validationSuccessful ) {
            return new FlywayValidateResult(name, true, null, null, null, null);
        }
        String msg = res.errorDetails == null ? null : res.errorDetails.errorMessage;
        String invalidVersion = null;
        String invalidDesc = null;
        if ( msg != null ) {
            var m = MIGRATION_FILE_PATTERN.matcher(msg);
            if (m.find()) {
                invalidVersion = m.group(1).substring(1);
                invalidDesc = m.group(2).replace('_', ' ');
            }
        }
        return new FlywayValidateResult(name, false, msg, null, invalidVersion, invalidDesc);
    }

    private FlywayActionResult migrateOne(String name, Flyway flyway, boolean onlyImportant) {
        long start = System.nanoTime();
        log.info("Starting Flyway migrate for {}", name);
        try {
            var r = flyway.migrate();
            long ms = nanosToMs(start);
            String target = (r.targetSchemaVersion == null) ? null : String.valueOf(r.targetSchemaVersion);
            MigrationInfo[] allAfter = flyway.info().all();
            List<FlywayMigrationFileStatus> migrations = mapper.toFileStatusList(allAfter, onlyImportant);
            log.info("Flyway migrate for {} done: executed={}, target={}, durationMs={}, files={}",
                    name, r.migrationsExecuted, target, ms, migrations.size());
            return new FlywayActionResult(
                    name,
                    true,
                    ms,
                    r.migrationsExecuted,
                    migrations,
                    target,
                    null,
                    null
            );
        } catch (Exception e) {
            long ms = nanosToMs(start);
            Throwable root = rootCause(e);
            String message = bestMessage(root, e);
            List<FlywayMigrationFileStatus> migrations;
            try {
                migrations = mapper.toFileStatusList(flyway.info().all(), false);
            } catch (Exception ignored) {
                migrations = List.of();
            }
            log.error("Flyway migrate for {} failed after {}ms: {}", name, ms, message, e);
            return new FlywayActionResult(
                    name,
                    false,
                    ms,
                    0,
                    migrations,
                    null,
                    e.getClass().getSimpleName(),
                    message
            );
        }
    }

    private <T> T withMigrationLock(SupplierWithResult<T> action) {
        if ( !migrateLock.tryLock() ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Migration already running");
        }
        try {
            return action.get();
        } finally {
            migrateLock.unlock();
        }
    }

    private long nanosToMs(long startNanos) {
        return ( System.nanoTime() - startNanos ) / 1_000_000;
    }

    private Throwable rootCause(Throwable e) {
        Throwable root = e;
        while ( root.getCause() != null && root.getCause() != root ) {
            root = root.getCause();
        }
        return root;
    }

    private String bestMessage(Throwable root, Throwable original) {
        if ( root.getMessage() != null && !root.getMessage().isBlank() ) return root.getMessage();
        return original.getMessage();
    }

    @FunctionalInterface
    private interface SupplierWithResult<T> {
        T get();
    }
}
