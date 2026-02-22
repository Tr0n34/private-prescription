package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.thesorimed;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums.ThesorimedExceptionCode;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

/**
 * Helper de lecture typée pour éviter les casts lors du mapping d'une sortie Thesorimed vers
 * un objet métier
 */
public final class Row {

    public static final String EMPTY_VALUE_COLUMN = "";

    private Row() {}

    public static String getString(Map<String, Object> row, String key) {
        Object value = get(row, key);
        if ( value == null ) {
            throw new InfrastructureException(
                    ThesorimedExceptionCode.TECH_THESO_ROW_MAPPER_NULL_COLUMN,
                    Map.of("key", key));
        }
        return String.valueOf(value);
    }

    public static String getNullableString(Map<String, Object> row, String key) {
        Object value = get(row, key);
        return ( value == null ) ? null : String.valueOf(value);
    }

    static Long getLong(Map<String, Object> row, String key) {
        Object value = get(row, key);
        if ( value == null ) {
            throw new InfrastructureException(
                    ThesorimedExceptionCode.TECH_THESO_ROW_MAPPER_NULL_COLUMN,
                    Map.of("key", key));
        }
        return switch (value) {
            case Long l       -> l;
            case Integer i    -> i.longValue();
            case BigDecimal bd-> bd.longValue();
            case String s     -> Long.parseLong(s);
            case Number n     -> n.longValue();
            default -> throw new InfrastructureException(
                    ThesorimedExceptionCode.TECH_THESO_ROW_MAPPER_NUMERIC_TYPE_UNSUPPORTED,
                    Map.of("type", value));
        };
    }

    private static Object get(Map<String, Object> row, String key) {
        if ( row == null ) {
            throw new InfrastructureException(ThesorimedExceptionCode.TECH_THESO_ROW_MAPPER_ROW_NULL);
        }
        if ( key == null ) {
            throw new InfrastructureException(ThesorimedExceptionCode.TECH_THESO_ROW_MAPPER_KEY_NULL);
        }
        Object result = null;
        boolean found = false;
        if ( row.containsKey(key) ) {
            result = row.get(key);
            found = true;
        } else {
            var wanted = normalize(key);
            result = check(row, wanted);
            found = result != null;
        }
        if  ( !found ) {
            throw new InfrastructureException(
                    ThesorimedExceptionCode.TECH_THESO_ROW_MAPPER_COLUMN_MISSING,
                    Map.of("key", key)
            );
        }
        return result;
    }

    private static Object check(Map<String, Object> row, String wanted) {
        Object result = null;
        for ( var entry : row.entrySet()) {
            if ( normalize(entry.getKey()).equals(wanted) ) {
                result = entry.getValue();
                break;
            }
        }
        return result;
    }

    private static String normalize(String s) {
        return s == null ? EMPTY_VALUE_COLUMN : s.trim().toLowerCase(Locale.ROOT);
    }

}
