package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.thesorimed;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Helper de lecture typée pour éviter les casts lors du mapping d'une sortie Thesorimed vers
 * un objet métier
 */
public final class Row {

    private Row() {}

    public static String getString(Map<String, Object> row, String key) {
        Object v = row.get(key);
        if (v == null) throw new IllegalStateException("Missing column: " + key);
        return String.valueOf(v);
    }

    public static String getNullableString(Map<String, Object> row, String key) {
        Object v = row.get(key);
        return (v == null) ? null : String.valueOf(v);
    }

    static Long getLong(Map<String, Object> row, String key) {
        Object v = row.get(key);
        if (v == null) throw new IllegalStateException("Missing column: " + key);
        if (v instanceof Long l) return l;
        if (v instanceof Integer i) return i.longValue();
        if (v instanceof BigDecimal bd) return bd.longValue();
        if (v instanceof String s) return Long.parseLong(s);
        throw new IllegalStateException("Unsupported numeric type for " + key + ": " + v.getClass());
    }
}
