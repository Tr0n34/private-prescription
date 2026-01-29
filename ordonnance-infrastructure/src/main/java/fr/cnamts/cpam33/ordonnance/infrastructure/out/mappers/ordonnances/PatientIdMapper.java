package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientIdMapper {

    default PatientId toDomain(String externalId) {
        return new PatientId(externalId);
    }

    default String toEntityId(PatientId patientId) {
        return patientId != null ? patientId.externalId() : null;
    }

}
