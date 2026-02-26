package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;

import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.PageRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PageableApiMapper {

    @Mapping(target = "page", expression = "java(pageable.getPageNumber())")
    @Mapping(target = "size", expression = "java(pageable.getPageSize())")
    @Mapping(target = "sort", source = "pageable", qualifiedByName = "mapSort")
    PageRequest toDomain(Pageable pageable);

    @Named("mapSort")
    default List<PageRequest.SortField> mapSort(Pageable pageable) {
        if ( pageable == null || pageable.getSort().isUnsorted() ) {
            return List.of();
        }
        return pageable.getSort().stream()
                .map(this::mapOrder)
                .toList();
    }

    default PageRequest.SortField mapOrder(Sort.Order order) {
        return new PageRequest.SortField(
                order.getProperty(),
                order.isAscending()
                        ? PageRequest.Direction.ASC
                        : PageRequest.Direction.DESC
        );
    }

}
