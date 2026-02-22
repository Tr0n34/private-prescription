package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.thesorimed;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

/**
 * Helper de lecture typée pour éviter les casts lors du mapping d'une sortie Thesorimed vers
 * un objet métier
 */
public final class Row {

    private Row() {}


    public static String getString(Map<String, Object> row, String key) {
        Object v = get(row, key);
        if (v == null) {
            throw new IllegalStateException("Null value for column: " + key);
        }
        return String.valueOf(v);
    }

    public static String getNullableString(Map<String, Object> row, String key) {
        Object v = get(row, key);
        return (v == null) ? null : String.valueOf(v);
    }

    static Long getLong(Map<String, Object> row, String key) {
        Object v = get(row, key);
        if (v == null) throw new IllegalStateException("Null value for column: " + key);

        if (v instanceof Long l) return l;
        if (v instanceof Integer i) return i.longValue();
        if (v instanceof BigDecimal bd) return bd.longValue();
        if (v instanceof String s) return Long.parseLong(s);

        throw new IllegalStateException("Unsupported numeric type for " + key + ": " + v.getClass());
    }

    private static Object get(Map<String, Object> row, String key) {
        if (row == null) throw new IllegalStateException("Row is null");
        if (key == null) throw new IllegalStateException("Key is null");

        // 1) accès direct
        if (row.containsKey(key)) {
            return row.get(key);
        }

        // 2) fallback insensible à la casse + trim
        String wanted = normalize(key);
        for (String k : row.keySet()) {
            if (normalize(k).equals(wanted)) {
                return row.get(k);
            }
        }

        throw new IllegalStateException("Missing column: " + key);
    }

    private static String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase(Locale.ROOT);
    }

}
