package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.medecins;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Rpps;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RppsMapper {

    default Rpps toDomain(String numero) {
        return new Rpps(numero);
    }

    default String toEntity(Rpps rpps) {
        return rpps != null ? rpps.numero() : null;
    }

}
