package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Traitement;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.PrescriptionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PrescriptionEntityMapper {

    PrescriptionEntity toEntity(Traitement traitement);

    Traitement toDomain(PrescriptionEntity entity);

}
