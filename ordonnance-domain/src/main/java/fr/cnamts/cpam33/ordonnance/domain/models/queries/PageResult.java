package fr.cnamts.cpam33.ordonnance.domain.models.queries;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public record PageResult<T>(
        List<T> content,
        long totalElements,
        int page,
        int size
) {

    public PageResult {
        content = Objects.requireNonNullElse(content, List.of());
    }

    public <R> PageResult<R> map(Function<? super T, R> mapper) {
        return new PageResult<>(
                content.stream().map(mapper).toList(),
                totalElements,
                page,
                size
        );
    }

}
