package fr.cnamts.cpam33.ordonnance.domain.models.aggregates;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.PrescriptionId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Traitement;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.Medecin;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ordonnance implements DomainObject {

    public static final int VERSION_INITIALE = 1;

    private final OrdonnanceId ordonnanceId;
    private Patient patient;
    private Medecin medecin;
    private List<Traitement> traitements;
    private int version;
    private OrdonnanceStatus status;
    private final LocalDate createdOn;
    private LocalDate modifiedOn;
    private LocalDateTime signedOn;

    private Ordonnance(
            OrdonnanceId ordonnanceId,
            Patient patient,
            Medecin medecin,
            List<Traitement> traitements,
            OrdonnanceStatus status,
            int version,
            LocalDate createdOn,
            LocalDate modifiedOn,
            LocalDateTime signedOn
    ) {
        this.ordonnanceId = ordonnanceId;
        this.patient = patient;
        this.medecin = medecin;
        this.traitements = new ArrayList<>(traitements);
        this.status = status;
        this.version = version;
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.signedOn = signedOn;
        checkOrdonnanceState();
    }

    public static Ordonnance of(
            OrdonnanceId ordonnanceId,
            Patient patient,
            Medecin medecin,
            List<Traitement> traitements) {
        return new Ordonnance(ordonnanceId, patient, medecin, traitements,
                OrdonnanceStatus.CREATED, VERSION_INITIALE, LocalDate.now(), LocalDate.now(), null);
    }

    public void validate() {
        checkPrescriptionState();
        status = OrdonnanceStatus.VALIDATED;
    }

    public List<Traitement> prescriptions() {
        return List.copyOf(traitements); /* Copie défensive */
    }

    public OrdonnanceId ordonnanceId() {
        return ordonnanceId;
    }

    public Patient patient() {
        return patient;
    }

    public Medecin medecin() {
        return medecin;
    }

    public OrdonnanceStatus status() {
        return status;
    }

    public LocalDate createdOn() {
        return createdOn;
    }

    public LocalDate modifiedOn() {
        return modifiedOn;
    }

    public LocalDateTime signedOn() {
        return signedOn;
    }

    public int getVersion() {
        return version;
    }

    public void checkOrdonnanceState() {
        if ( ordonnanceId == null ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_ID_MISSING);
        }
        if ( patient == null ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PATIENT_MISSING);
        }
        if ( medecin == null ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_MEDECIN_MISSING);
        }
    }

    public void checkPrescriptionState() {
        if ( traitements == null || traitements.isEmpty() ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_MISSING);
        }
    }

    private void ensureOrdonnanceCantBeChanged() {
        if ( status == OrdonnanceStatus.SIGNED ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_IMMUTABLE_WHEN_SIGNED);
        }
    }

    public Ordonnance sign() {
        ensureOrdonnanceCantBeChanged();
        status = OrdonnanceStatus.SIGNED;
        signedOn = LocalDateTime.now();
        return this;
    }

    public boolean isSigned() {
        return status == OrdonnanceStatus.SIGNED;
    }

    public void changePatient(Patient newPatient) {
        incrementVersion();
        ensureOrdonnanceCantBeChanged();
        modifiedOn = LocalDate.now();
        patient = newPatient;
    }

    public void changeMedecin(Medecin newMedecin) {
        incrementVersion();
        ensureOrdonnanceCantBeChanged();
        modifiedOn = LocalDate.now();
        medecin = newMedecin;
    }

    public void incrementVersion() {
        this.version++;
    }

    public Ordonnance replacePrescriptions(List<Traitement> traitements) {
        ensureOrdonnanceCantBeChanged();
        incrementVersion();
        if ( traitements == null || traitements.isEmpty()) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_MISSING);
        }
        this.traitements = new ArrayList<>(traitements); // copie mutable
        modifiedOn = LocalDate.now();
        return this;
    }

    public Ordonnance replacePrescription(Traitement newTraitement) {
        ensureOrdonnanceCantBeChanged();
        incrementVersion();
        boolean exists = traitements.stream().anyMatch(p -> p.prescriptionId().equals(newTraitement.prescriptionId()));
        if ( !exists ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_NOT_FOUND);
        }
        this.traitements = new ArrayList<>(traitements.stream()
                .map(p -> p.prescriptionId().equals(newTraitement.prescriptionId()) ? newTraitement : p)
                .toList());
        modifiedOn = LocalDate.now();
        return this;
    }

    public Ordonnance addPrescription(Traitement newTraitement) {
        ensureOrdonnanceCantBeChanged();
        incrementVersion();
        boolean exists = traitements.stream().anyMatch(p -> p.prescriptionId().equals(newTraitement.prescriptionId()));
        if ( exists ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_DUPLICATE);
        }
        List<Traitement> updated = new ArrayList<>(traitements); // copie mutable
        updated.add(newTraitement);
        traitements = updated;
        modifiedOn = LocalDate.now();
        return this;
    }

    public Ordonnance removePrescription(PrescriptionId prescriptionId) {
        incrementVersion();
        boolean removed = traitements.removeIf(p -> p.prescriptionId().equals(prescriptionId));
        if ( !removed ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_NOT_FOUND);
        }
        modifiedOn = LocalDate.now();
        return this;
    }

}