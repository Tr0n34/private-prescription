package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.Rpps;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RppsMapper {

    default Rpps toDomain(String value) {
        return value != null ? new Rpps(value) : null;
    }

    default String toEntity(Rpps rpps) {
        return rpps != null ? rpps.value() : null;
    }

}
