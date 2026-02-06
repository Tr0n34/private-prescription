package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PatientExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;

import java.time.LocalDate;

public class Patient implements DomainObject {

    private PatientId patientId;
    private ExternalPatientId externalPatientId;
    private Nom nom;
    private Prenom prenom;
    private LocalDate dateNaissance;

    public Patient(PatientId patientId,
                    ExternalPatientId externalPatientId,
                    Nom nom,
                    Prenom prenom,
                    LocalDate dateNaissance) {
        if ( patientId == null || dateNaissance == null ) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_INVALID);
        }
        if ( dateNaissance.isAfter(LocalDate.now()) ) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_DATE_NAISSANCE_INVALID);
        }
        if ( nom == null ) {
            throw new DomainException(PatientExceptionCode.BS_MALADIE_NOM_IS_MISSING);
        }
        if ( prenom == null ) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_PRENOM_IS_MISSING);
        }
        this.patientId = patientId;
        this.externalPatientId = externalPatientId;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
    }

    public static Patient of(PatientId patientId, ExternalPatientId externalPatientId,
                             Nom nom, Prenom prenom, LocalDate dateNaissance) {
        return new Patient(patientId, externalPatientId, nom, prenom, dateNaissance);
    }

    public PatientId patientId() {
        return patientId;
    }

    public Patient setPatientId(PatientId patientId) {
        this.patientId = patientId;
        return this;
    }

    public ExternalPatientId externalPatientId() {
        return externalPatientId;
    }

    public Patient setExternalPatientId(ExternalPatientId externalPatientId) {
        this.externalPatientId = externalPatientId;
        return this;
    }

    public Nom nom() {
        return nom;
    }

    public Patient setNom(Nom nom) {
        this.nom = nom;
        return this;
    }

    public Prenom prenom() {
        return prenom;
    }

    public Patient setPrenom(Prenom prenom) {
        this.prenom = prenom;
        return this;
    }

    public LocalDate dateNaissance() {
        return dateNaissance;
    }

    public Patient setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
        return this;
    }

}
