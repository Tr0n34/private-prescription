package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.sorting;

import fr.cnamts.cpam33.ordonnance.domain.models.queries.PageRequest;

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
        return Optional.ofNullable(sortField)
                .map(PageRequest.SortField::field)
                .map(requested -> resolveCanonical(requested, cfgByCanonicalField, canonicalByAlias))
                .filter(canonical -> {
                    var cfg = cfgByCanonicalField.get(canonical);
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
                .orElse(null);
    }

    private static String resolveCanonical(
            String requested,
            Map<String, ?> cfgByCanonicalField,
            Map<String, String> canonicalByAlias
    ) {
        return cfgByCanonicalField.containsKey(requested)
                ? requested
                : canonicalByAlias.get(requested.toLowerCase());
    }

}
