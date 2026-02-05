package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.LignePrescription;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.PrescriptionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionEntityMapper {

    PrescriptionEntity toEntity(LignePrescription lignePrescription);

    LignePrescription toDomain(PrescriptionEntity entity);

}
