package fr.cnamts.cpam33.ordonnance.domain.kernel.filters;

import fr.cnamts.cpam33.ordonnance.domain.models.queries.PageRequest;

import java.util.Comparator;
import java.util.List;

public interface SortProvider<T> {

    List<PageRequest.SortField> defaultSort();

    Comparator<T> comparatorFor(PageRequest.SortField sortField);

}
