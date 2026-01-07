package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Prescription;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.PrescriptionEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrescriptionEntityMapper {

    PrescriptionEntity toEntity(Prescription prescription);

    Prescription toDomain(PrescriptionEntity entity);

}
