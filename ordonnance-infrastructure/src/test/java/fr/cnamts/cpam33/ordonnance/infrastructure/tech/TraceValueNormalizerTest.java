package fr.cnamts.cpam33.ordonnance.infrastructure.tech;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.mappers.TraceValueNormalizer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class TraceValueNormalizerTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void should_return_null_when_value_is_null() {
        Object out = TraceValueNormalizer.normalize(null, mapper);
        assertNull(out);
    }

    @Test
    void should_keep_number_boolean_enum_uuid_temporal_as_simple_values() {
        Object n = TraceValueNormalizer.normalize(42, mapper);
        Object b = TraceValueNormalizer.normalize(Boolean.TRUE, mapper);
        Object e = TraceValueNormalizer.normalize(TestEnum.A, mapper);
        UUID uuid = UUID.randomUUID();
        Object u = TraceValueNormalizer.normalize(uuid, mapper);
        LocalDate date = LocalDate.of(2026, 2, 7);
        Object d = TraceValueNormalizer.normalize(date, mapper);
        assertEquals(42, n);
        assertEquals(Boolean.TRUE, b);
        assertEquals("A", e);
        assertEquals(uuid.toString(), u);
        assertEquals(date.toString(), d);
    }

    @Test
    void should_truncate_long_string() {
        String s = "x".repeat(2100);
        Object out = TraceValueNormalizer.normalize(s, mapper);
        assertInstanceOf(String.class, out);
        String normalized = (String) out;
        assertTrue(normalized.length() < s.length());
        assertTrue(normalized.endsWith("…(truncated)"));
    }

    @Test
    void should_normalize_array_and_truncate_items_over_limit() {
        Integer[] array = IntStream.range(0, 60).boxed().toArray(Integer[]::new); // MAX_COLLECTION=50
        Object out = TraceValueNormalizer.normalize(array, mapper);
        assertInstanceOf(List.class, out);
        List<?> list = (List<?>) out;
        assertEquals(51, list.size());
        Object last = list.getLast();
        assertInstanceOf(Map.class, last);
        Map<?, ?> summary = (Map<?, ?>) last;
        assertEquals("truncatedItems", summary.get("_reason"));
    }

    @Test
    void should_normalize_collection_and_truncate_items_over_limit() {
        List<Integer> col = IntStream.range(0, 60).boxed().toList();
        Object out = TraceValueNormalizer.normalize(col, mapper);
        assertInstanceOf(List.class, out);
        List<?> list = (List<?>) out;
        assertEquals(51, list.size());
        Object last = list.getLast();
        assertInstanceOf(Map.class, last);
        Map<?, ?> summary = (Map<?, ?>) last;
        assertEquals("truncatedItems", summary.get("_reason"));
    }

    @Test
    void should_normalize_map_and_truncate_entries_over_limit() {
        Map<String, Object> map = new LinkedHashMap<>();
        for ( int i = 0; i < 60; i++ ) {
            map.put("k" + i, i);
        }
        Object out = TraceValueNormalizer.normalize(map, mapper);
        assertInstanceOf(Map.class, out);
        Map<?, ?> normalized = (Map<?, ?>) out;
        assertTrue(normalized.containsKey("_truncated"));
        Object truncated = normalized.get("_truncated");
        assertInstanceOf(Map.class, truncated);
        Map<?, ?> summary = (Map<?, ?>) truncated;
        assertEquals("truncatedEntries", summary.get("_reason"));
    }

    @Test
    void should_return_maxDepth_summary_when_depth_exceeded() {
        Object nested = deepNestedMap();
        Object out = TraceValueNormalizer.normalize(nested, mapper);
        Map<String, Object> found = findFirstSummaryWithReason(out, "maxDepth");
        assertNotNull(found);
        assertEquals("maxDepth", found.get("_reason"));
        assertNotNull(found.get("_type"));
        assertNotNull(found.get("_value"));
    }

    @Test
    void should_detect_cycle_and_return_cycle_summary() {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("self", m);
        Object out = TraceValueNormalizer.normalize(m, mapper);
        assertInstanceOf(Map.class, out);
        Map<?, ?> normalized = (Map<?, ?>) out;
        Object self = normalized.get("self");
        assertInstanceOf(Map.class, self);
        Map<?, ?> summary = (Map<?, ?>) self;
        assertEquals("cycle", summary.get("_reason"));
    }

    @Test
    void should_convert_pojo_with_jackson_to_map_like_structure() {
        Person p = new Person("Nicolas", 39);
        Object out = TraceValueNormalizer.normalize(p, mapper);
        assertInstanceOf(Map.class, out);
        Map<?, ?> map = (Map<?, ?>) out;
        assertEquals("Nicolas", map.get("name"));
        assertEquals(39, map.get("age"));
    }

    @Test
    void should_fallback_to_toString_summary_when_jackson_fails() {
        SelfRefPojo x = new SelfRefPojo();
        Object out = TraceValueNormalizer.normalize(x, mapper);
        assertInstanceOf(Map.class, out);
        Map<?, ?> summary = (Map<?, ?>) out;
        assertEquals("toString", summary.get("_reason"));
        assertTrue(((String) summary.get("_type")).contains(SelfRefPojo.class.getName()));
        assertNotNull(summary.get("_value"));
    }

    private static Object deepNestedMap() {
        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> cur = root;
        for (int i = 0; i < 6; i++) {
            Map<String, Object> next = new LinkedHashMap<>();
            cur.put("level" + i, next);
            cur = next;
        }
        return root;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> findFirstSummaryWithReason(Object obj, String reason) {
        Map<String, Object> found = null;

        if (obj instanceof Map<?, ?> m) {
            Object r = m.get("_reason");
            if (reason.equals(r)) {
                found = (Map<String, Object>) m;
            } else {
                for (Object v : m.values()) {
                    Map<String, Object> sub = findFirstSummaryWithReason(v, reason);
                    if (sub != null) {
                        found = sub;
                        break;
                    }
                }
            }
        } else if (obj instanceof Collection<?> c) {
            for (Object v : c) {
                Map<String, Object> sub = findFirstSummaryWithReason(v, reason);
                if (sub != null) {
                    found = sub;
                    break;
                }
            }
        }

        return found;
    }

    private enum TestEnum { A, B }

    private record Person(String name, int age) {}

    private static final class SelfRefPojo {
        public SelfRefPojo getSelf() { return this; }
        @Override public String toString() { return "SelfRefPojo"; }
    }

}
