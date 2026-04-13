package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.ExternalPatientId;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExternalIdPatientMapper {

    default String mapExternalPatientId(ExternalPatientId externalPatientId) {
        return externalPatientId == null ? null : externalPatientId.numero();
    }

    default ExternalPatientId mapExternalPatientId(String externalId) {
        return externalId == null ? null : new ExternalPatientId(externalId);
    }

}
