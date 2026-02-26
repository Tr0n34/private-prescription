package fr.cnamts.cpam33.ordonnance.domain.kernel.filters;

import java.util.List;

public record PageRequest(
        int page,
        int size,
        List<SortField> sort
) {

    public int offset() {
        return page * size;
    }

    public record SortField(String field, Direction direction) {}

    public enum Direction {
        ASC, DESC
    }

}
