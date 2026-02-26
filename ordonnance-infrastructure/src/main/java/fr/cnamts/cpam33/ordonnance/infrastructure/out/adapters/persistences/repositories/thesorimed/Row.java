package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.thesorimed;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.enums.ThesorimedExceptionCode;

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
        if (row.containsKey(key)) {
            return row.get(key);
        }
        var wanted = normalize(key);
        var entry = row.entrySet().stream()
                .filter(e -> normalize(e.getKey()).equals(wanted))
                .findFirst()
                .orElseThrow(() -> new InfrastructureException(
                        ThesorimedExceptionCode.TECH_THESO_ROW_MAPPER_COLUMN_MISSING,
                        Map.of("key", key)
                ));
        return entry.getValue();
    }

    private static String normalize(String s) {
        return s == null
                ? EMPTY_VALUE_COLUMN
                : s.trim()
                .toLowerCase(Locale.ROOT)
                .replace("_", "")
                .replace("-", "")
                .replace(" ", "");
    }

}
