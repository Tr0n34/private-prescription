package fr.cnamts.cpam33.ordonnance.migrator.fixtures;

import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationState;
import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.output.ValidateResult;
import org.objenesis.ObjenesisStd;

import java.lang.reflect.Field;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

public final class FlywayServiceHelper {

    public static MigrationInfo mockMigration(String version, String desc, MigrationState state) {
        MigrationInfo mi = mock(MigrationInfo.class);
        when(mi.getState()).thenReturn(state);
        lenient().when(mi.getDescription()).thenReturn(desc);
        if (version == null) {
            lenient().when(mi.getVersion()).thenReturn(null);
        } else {
            lenient().when(mi.getVersion()).thenReturn(MigrationVersion.fromVersion(version));
        }
        return mi;
    }

    public static ValidateResult validateResult(boolean success, String errorMessage) {
        try {
            ObjenesisStd objenesis = new ObjenesisStd();
            ValidateResult vr = objenesis.newInstance(ValidateResult.class);
            setField(vr, "validationSuccessful", success);
            if ( !success ) {
                Field f = ValidateResult.class.getDeclaredField("errorDetails");
                f.setAccessible(true);
                Object details = null;
                Class<?> detailsType = f.getType();
                if ( detailsType != Void.TYPE ) {
                    details = objenesis.newInstance(detailsType);
                    trySetField(details, "errorMessage", errorMessage);
                }
                f.set(vr, details);
            }
            return vr;
        } catch (Exception e) {
            throw new RuntimeException("Cannot build ValidateResult for tests (Flyway API differs).", e);
        }
    }

    static void setField(Object target, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    static boolean trySetField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

}
