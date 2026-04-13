package fr.cnamts.cpam33.ordonnance.application.filters;

import java.util.Comparator;
import java.util.List;

public interface SortProvider<T> {

    List<PageRequest.SortField> defaultSort();

    Comparator<T> comparatorFor(PageRequest.SortField sortField);

}
