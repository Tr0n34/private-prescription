package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.sorting;

import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.PageRequest;

import java.lang.reflect.Method;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecordComparableFactory {

    public static Map<String, Method> buildAccessorMap(Class<?> recordClass) {
        return recordClass.isRecord()
                ? Arrays.stream(recordClass.getRecordComponents())
                .collect(Collectors.toUnmodifiableMap(
                        RecordComponent::getName,
                        RecordComponent::getAccessor
                ))
                : Map.of();
    }

    public static <T> Comparator<T> comparatorFor(
            PageRequest.SortField sortField,
            Map<String, ? extends HasEnabled> cfgByCanonicalField,
            Map<String, String> canonicalByAlias,
            Map<String, Method> accessorByField
    ) {
        String canonical = resolveCanonical(sortField.field(), cfgByCanonicalField, canonicalByAlias);

        System.out.println("---- SORT DEBUG ----");
        System.out.println("requested      = " + sortField.field());
        System.out.println("canonical      = " + canonical);
        System.out.println("cfgKeys        = " + cfgByCanonicalField.keySet());
        System.out.println("aliasKeys      = " + canonicalByAlias.keySet());
        System.out.println("accessorKeys   = " + accessorByField.keySet());
        System.out.println("---------------------");
        return Optional.of(sortField)
                .map(PageRequest.SortField::field)
                .map(requested -> resolveCanonical(requested, cfgByCanonicalField, canonicalByAlias))
                .filter(c -> {
                    var cfg = cfgByCanonicalField.get(c);
                    return cfg != null && cfg.enabled();
                })
                .map(accessorByField::get)
                .map(accessor -> {
                    Comparator<T> c = Comparator.comparing(
                            m -> (Comparable<?>) ComparableReader.readComparable(accessor, m),
                            ComparableReader.comparableComparatorFor(accessor.getReturnType())
                    );
                    return sortField.direction() == PageRequest.Direction.DESC ? c.reversed() : c;
                })
                .orElseGet(() -> {
                    System.out.println("SORT IGNORED: " + sortField);
                    return null;
                });
    }

    private static String resolveCanonical(
            String requested,
            Map<String, ?> cfgByCanonicalField,
            Map<String, String> canonicalByAlias
    ) {
        String canonical = null;
        if ( requested != null ) {
            String key = requested.toLowerCase();
            canonical = cfgByCanonicalField.containsKey(key) ? key : canonicalByAlias.get(key);
        }
        return canonical;
    }

}
