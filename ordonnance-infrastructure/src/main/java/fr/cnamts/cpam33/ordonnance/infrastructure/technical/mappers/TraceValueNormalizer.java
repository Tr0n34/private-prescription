package fr.cnamts.cpam33.ordonnance.infrastructure.technical.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Array;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public final class TraceValueNormalizer {

    private static final int MAX_DEPTH = 4;
    private static final int MAX_COLLECTION = 50;
    private static final int MAX_STRING = 2000;
    private static final Object NO_MATCH = new Object();

    private TraceValueNormalizer() {}

    @FunctionalInterface
    private interface NormalizerStep {
        Object apply(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen);
    }

    public static Object normalize(Object value, ObjectMapper mapper) {
        return normalize(value, mapper, 0, new IdentityHashMap<>());
    }

    private static Object normalize(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        Object result = null;
        if ( value != null ) {
            result = normalizeNonNull(value, mapper, depth, seen);
        }
        return result;
    }

    private static Object normalizeNonNull(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        Object result = NO_MATCH;
        List<NormalizerStep> steps = List.of(
                TraceValueNormalizer::stepDepth,
                TraceValueNormalizer::stepCycle,
                TraceValueNormalizer::stepSimple,
                TraceValueNormalizer::stepArray,
                TraceValueNormalizer::stepCollection,
                TraceValueNormalizer::stepMap,
                TraceValueNormalizer::stepJackson,
                TraceValueNormalizer::stepFallback
        );
        for ( NormalizerStep step : steps ) {
            Object out = step.apply(value, mapper, depth, seen);
            if ( out != NO_MATCH)  {
                result = out;
                break;
            }
        }
        return result;
    }

    private static Object stepDepth(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        Object out = NO_MATCH;
        if ( isDepthExceeded(depth) ) {
            out = summary(value, "maxDepth");
        }
        return out;
    }

    private static Object stepCycle(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        Object out = NO_MATCH;
        if ( isCycle(value, seen) ) {
            out = summary(value, "cycle");
        }
        return out;
    }

    private static Object stepSimple(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        return asSimpleValue(value);
    }

    private static Object stepArray(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        Object out = NO_MATCH;
        if ( isArray(value) ) {
            out = normalizeArray(value, mapper, depth, seen);
        }
        return out;
    }

    private static Object stepCollection(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        Object out = NO_MATCH;
        if ( value instanceof Collection<?> collection ) {
            out = normalizeCollection(collection, mapper, depth, seen);
        }
        return out;
    }

    private static Object stepMap(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        Object out = NO_MATCH;
        if (value instanceof Map<?, ?> map) {
            out = normalizeMap(map, mapper, depth, seen);
        }
        return out;
    }

    private static Object stepJackson(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        return tryNormalizeWithJackson(value, mapper, depth, seen);
    }

    private static Object stepFallback(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        return summary(value, "toString");
    }

    private static boolean isDepthExceeded(int depth) {
        return depth > MAX_DEPTH;
    }

    private static boolean isCycle(Object value, IdentityHashMap<Object, Boolean> seen) {
        return isComplex(value) && seen.put(value, Boolean.TRUE) != null;
    }

    private static boolean isArray(Object value) {
        boolean array = false;
        if ( value != null ) {
            array = value.getClass().isArray();
        }
        return array;
    }

    private static Object asSimpleValue(Object value) {
        Object out = NO_MATCH;
        if ( value instanceof String s ) {
            out = truncate(s);
        } else if (value instanceof Number) {
            out = value;
        } else if (value instanceof Boolean) {
            out = value;
        } else if (value instanceof Enum<?> e) {
            out = e.name();
        } else if (value instanceof java.time.temporal.TemporalAccessor t) {
            out = t.toString();
        } else if (value instanceof java.util.UUID u) {
            out = u.toString();
        }
        return out;
    }

    private static Object normalizeArray(Object array, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        int length = Array.getLength(array);
        int limit = Math.min(length, MAX_COLLECTION);
        List<Object> out = new ArrayList<>(limit);
        for ( int i = 0; i < limit; i++ ) {
            Object element = java.lang.reflect.Array.get(array, i);
            out.add(normalize(element, mapper, depth + 1, seen));
        }
        if ( length > limit ) {
            out.add(summary(length - limit, "truncatedItems"));
        }
        return out;
    }

    private static Object normalizeCollection(Collection<?> col, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        int size = col.size();
        int limit = Math.min(size, MAX_COLLECTION);
        List<Object> out = new ArrayList<>(limit);
        Iterator<?> it = col.iterator();
        int i = 0;
        while ( it.hasNext() && i < limit ) {
            Object value = it.next();
            out.add(normalize(value, mapper, depth + 1, seen));
            i++;
        }
        if ( size > limit ) {
            out.add(summary(size - limit, "truncatedItems"));
        }
        return out;
    }

    private static Object normalizeMap(Map<?, ?> map, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        Map<String, Object> out = new LinkedHashMap<>();
        int size = map.size();
        int limit = Math.min(size, MAX_COLLECTION);
        Iterator<? extends Map.Entry<?, ?>> it = map.entrySet().iterator();
        int i = 0;
        while ( it.hasNext() && i < limit ) {
            Map.Entry<?, ?> e = it.next();
            String key = String.valueOf(e.getKey());
            Object value = normalize(e.getValue(), mapper, depth + 1, seen);
            out.put(key, value);
            i++;
        }
        if ( size > limit ) {
            out.put("_truncated", summary(size - limit, "truncatedEntries"));
        }
        return out;
    }

    private static Object tryNormalizeWithJackson(Object value, ObjectMapper mapper, int depth, IdentityHashMap<Object, Boolean> seen) {
        Object out;
        try {
            Object tree = mapper.valueToTree(value);
            Object plain = mapper.convertValue(tree, Object.class);
            out = normalize(plain, mapper, depth + 1, seen);
        } catch (Exception ignored) {
            out = NO_MATCH;
        }
        return out;
    }

    private static boolean isComplex(Object v) {
        return !(v instanceof String
                || v instanceof Number
                || v instanceof Boolean
                || v instanceof Enum<?>
                || v instanceof TemporalAccessor
                || v instanceof UUID);
    }

    private static String truncate(String s) {
        return s.length() <= MAX_STRING ? s : s.substring(0, MAX_STRING) + "…(truncated)";
    }

    private static Map<String, Object> summary(Object value, String reason) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("_type", value == null ? "null" : value.getClass().getSimpleName());
        m.put("_reason", reason);
        m.put("_value", truncate(String.valueOf(value)));
        return m;
    }

}
