package fr.cnamts.cpam33.ordonnance.infrastructure.in.acls;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.CesPatientInvalidException;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums.CesPatientExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients.CesPatientDto;

public class CesPatientACL {

    public Patient toDomain(CesPatientDto dto) {
        validate(dto);
        return new Patient(
                new PatientId(dto.externalId()),
                new Nom(dto.nom()),
                new Prenom(dto.prenom()),
                dto.dateNaissance()
        );
    }

    private void validate(CesPatientDto dto) {
        if ( !dto.externalId().matches("\\d{13}")) {
            throw new CesPatientInvalidException(CesPatientExceptionCode.TECH_CES_PATIENT_ID_NOT_LENGTH);
        }
        if ( !dto.externalId().substring(0, 3).equals(dto.cesRattachement()) ) {
            throw new CesPatientInvalidException(CesPatientExceptionCode.TECH_CES_PATIENT_ID_CES_RATTACHEMENT_INVALID);
        }
    }

}
