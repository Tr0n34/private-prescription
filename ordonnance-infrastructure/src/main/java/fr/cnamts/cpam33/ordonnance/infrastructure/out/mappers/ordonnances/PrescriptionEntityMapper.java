package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Traitement;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.PrescriptionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionEntityMapper {

    PrescriptionEntity toEntity(Traitement traitement);

    Traitement toDomain(PrescriptionEntity entity);

}
