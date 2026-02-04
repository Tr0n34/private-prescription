package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PatientIdMapper {

    default String mapPatientId(PatientId patientId) {
        return patientId.numero();
    }

    default PatientId mapPatientId(String numero) {
        return new PatientId(numero);
    }

}
