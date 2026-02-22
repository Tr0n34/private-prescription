package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.OrdonnanceIdEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrdonnanceIdMapper {

    default OrdonnanceIdEntity toEntity(OrdonnanceId id) {
        if (id == null) return null;
        return new OrdonnanceIdEntity(id.numero());
    }

    default OrdonnanceId toDomain(OrdonnanceIdEntity entity) {
        if (entity == null) return null;
        return new OrdonnanceId(entity.getNumero());
    }
}
