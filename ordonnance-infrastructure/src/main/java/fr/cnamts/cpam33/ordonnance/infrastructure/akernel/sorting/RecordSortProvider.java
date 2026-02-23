package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.sorting;

import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.SortProvider;
import fr.cnamts.cpam33.ordonnance.domain.models.queries.PageRequest;

import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
        this.cfgByCanonicalField = cfgByCanonicalField == null ? Map.of() : cfgByCanonicalField;
        this.canonicalByAlias = canonicalByAlias == null ? Map.of() : canonicalByAlias;
        this.accessorByField = RecordComparableFactory.buildAccessorMap(recordClass);
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
