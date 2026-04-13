package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.ErrorDescriptor;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.ErrorCatalogEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ErrorEntityMapper {

    @Mapping(target = "timestamp", expression = "java(LocalDateTime.now())")
    ErrorDescriptor toDescriptor(ErrorCatalogEntity entity);

    @Mapping(target = "description", ignore = true)
    @Mapping(target = "active", constant = "true")
    ErrorCatalogEntity toEntity(ErrorDescriptor descriptor);

}
