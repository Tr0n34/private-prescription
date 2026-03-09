package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.sorting;

import fr.cnamts.cpam33.ordonnance.application.filters.PageRequest;
import fr.cnamts.cpam33.ordonnance.application.filters.SortProvider;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Cette classe est l'implémentation d'un provider de tri des records fournis généralement
 * dans un objet métier. Ils sont pilotés par le fichier resources/configuration/sorts/ordonnances.yml
 * @param <T> L'objet métier a trier
 * @param <C> Le champ à trier
 */
public class RecordSortProvider<T, C extends HasEnabled> implements SortProvider<T> {

    private final List<PageRequest.SortField> defaultSort;
    private final Map<String, C> cfgByCanonicalField;
    private final Map<String, String> canonicalByAlias;
    private final Map<String, Method> accessorByField;

    public RecordSortProvider(
            Class<T> recordClass,
            List<PageRequest.SortField> defaultSort,
            Map<String, C> cfgByCanonicalField,
            Map<String, String> canonicalByAlias
    ) {
        this.defaultSort = defaultSort == null ? List.of() : defaultSort;
        this.cfgByCanonicalField = (cfgByCanonicalField == null ? Map.<String, C>of() : cfgByCanonicalField)
                .entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        e -> e.getKey().toLowerCase(),
                        Map.Entry::getValue
                ));
        this.canonicalByAlias = (canonicalByAlias == null ? Map.<String, String>of() : canonicalByAlias)
                .entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        e -> e.getKey().toLowerCase(),
                        e -> e.getValue().toLowerCase()
                ));
        this.accessorByField = RecordComparableFactory.buildAccessorMap(recordClass).entrySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        e -> e.getKey().toLowerCase(),
                        Map.Entry::getValue
                ));
    }

    @Override
    public List<PageRequest.SortField> defaultSort() {
        return defaultSort;
    }

    @Override
    public Comparator<T> comparatorFor(PageRequest.SortField sortField) {
        return RecordComparableFactory.comparatorFor(
                sortField,
                cfgByCanonicalField,
                canonicalByAlias,
                accessorByField
        );
    }

}