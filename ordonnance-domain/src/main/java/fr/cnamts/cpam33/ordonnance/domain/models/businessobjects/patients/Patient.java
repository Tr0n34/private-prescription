package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
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
            throw new DomainException(PatientExceptionCode.BS_PATIENT_INVALID);
        }
        if ( dateNaissance.isAfter(LocalDate.now()) ) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_DATE_NAISSANCE_INVALID);
        }
    }

    public static Patient of(PatientId patientId, Nom nom, Prenom prenom, LocalDate dateNaissance) {
        return new Patient(patientId, nom, prenom, dateNaissance);
    }

}
