package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.PatientExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;

import java.time.Clock;
import java.time.LocalDate;

public record Patient(
        PatientId patientId,
        ExternalPatientId externalPatientId,
        Nom nom,
        Prenom prenom,
        LocalDate dateNaissance
) implements DomainObject {

    public Patient {
        if ( patientId == null || dateNaissance == null ) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_INVALID);
        }
        if ( nom == null ) {
            throw new DomainException(PatientExceptionCode.BS_MALADIE_NOM_IS_MISSING);
        }
        if ( prenom == null ) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_PRENOM_IS_MISSING);
        }
        if ( dateNaissance.isAfter(LocalDate.now())) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_DATE_NAISSANCE_INVALID);
        }
    }

    public static Patient of(PatientId patientId,
                             ExternalPatientId externalPatientId,
                             Nom nom,
                             Prenom prenom,
                             LocalDate dateNaissance) {
        return new Patient(patientId, externalPatientId, nom, prenom, dateNaissance);
    }


    public Patient rename(Nom newNom, Prenom newPrenom) {
        return copy(this.patientId, this.externalPatientId, newNom, newPrenom, this.dateNaissance);
    }

    public Patient attachExternalId(ExternalPatientId newExternalId) {
        return copy(this.patientId, newExternalId, this.nom, this.prenom, this.dateNaissance);
    }

    public Patient changeDateNaissance(LocalDate newDateNaissance, Clock clock) {
        if ( newDateNaissance == null ) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_INVALID);
        }
        if ( newDateNaissance.isAfter(LocalDate.now(clock) )) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_DATE_NAISSANCE_INVALID);
        }

        return copy(this.patientId, this.externalPatientId, this.nom, this.prenom, newDateNaissance);
    }

    private Patient copy(PatientId id,
                         ExternalPatientId ext,
                         Nom nom,
                         Prenom prenom,
                         LocalDate dateNaissance) {
        return new Patient(id, ext, nom, prenom, dateNaissance);
    }

}
