package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.PatientInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PatientExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;

import java.time.LocalDate;

public record Patient(
        PatientId patientId,
        Nom nom,
        Prenom prenom,
        LocalDate dateNaissance
) implements DomainObject {

    public Patient {
        if ( patientId == null || dateNaissance == null ) {
            throw new PatientInvalidException(PatientExceptionCode.BS_PATIENT_INVALID);
        }
        if ( dateNaissance.isAfter(LocalDate.now()) ) {
            throw new PatientInvalidException(PatientExceptionCode.BS_PATIENT_DATE_NAISSANCE_INVALID);
        }
    }

}
