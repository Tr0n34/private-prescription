package fr.cnamts.cpam33.ordonnance.domain.models.aggregates;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.LignePrescription;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.PrescriptionId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.Medecin;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.OrdonnanceExceptionCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ordonnance implements DomainObject {

    public static final int VERSION_INITIALE = 1;

    private final OrdonnanceId ordonnanceId;
    private Patient patient;
    private Medecin medecin;
    private List<LignePrescription> lignePrescriptions;
    private int version;
    private OrdonnanceStatus status;
    private final LocalDate createdOn;
    private LocalDate modifiedOn;
    private LocalDateTime signedOn;

    private Ordonnance(
            OrdonnanceId ordonnanceId,
            Patient patient,
            Medecin medecin,
            List<LignePrescription> lignePrescriptions,
            OrdonnanceStatus status,
            int version,
            LocalDate createdOn,
            LocalDate modifiedOn,
            LocalDateTime signedOn
    ) {
        this.ordonnanceId = ordonnanceId;
        this.patient = patient;
        this.medecin = medecin;
        this.lignePrescriptions = new ArrayList<>(lignePrescriptions);
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
            List<LignePrescription> lignePrescriptions) {
        return new Ordonnance(ordonnanceId, patient, medecin, lignePrescriptions,
                OrdonnanceStatus.CREATED, VERSION_INITIALE, LocalDate.now(), LocalDate.now(), null);
    }

    public void validate() {
        checkPrescriptionState();
        status = OrdonnanceStatus.VALIDATED;
    }

    public List<LignePrescription> prescriptions() {
        return List.copyOf(lignePrescriptions); /* Copie défensive */
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
        if ( lignePrescriptions == null || lignePrescriptions.isEmpty() ) {
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

    public Ordonnance replacePrescriptions(List<LignePrescription> lignePrescriptions) {
        ensureOrdonnanceCantBeChanged();
        incrementVersion();
        if ( lignePrescriptions == null || lignePrescriptions.isEmpty()) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_MISSING);
        }
        lignePrescriptions = new ArrayList<>(lignePrescriptions); // copie mutable
        modifiedOn = LocalDate.now();
        return this;
    }

    public Ordonnance replacePrescription(LignePrescription newLignePrescription) {
        ensureOrdonnanceCantBeChanged();
        incrementVersion();
        boolean exists = lignePrescriptions.stream().anyMatch(p -> p.prescriptionId().equals(newLignePrescription.prescriptionId()));
        if ( !exists ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_NOT_FOUND);
        }
        lignePrescriptions = new ArrayList<>(lignePrescriptions.stream()
                .map(p -> p.prescriptionId().equals(newLignePrescription.prescriptionId()) ? newLignePrescription : p)
                .toList());
        modifiedOn = LocalDate.now();
        return this;
    }

    public Ordonnance addPrescription(LignePrescription newLignePrescription) {
        ensureOrdonnanceCantBeChanged();
        incrementVersion();
        boolean exists = lignePrescriptions.stream().anyMatch(p -> p.prescriptionId().equals(newLignePrescription.prescriptionId()));
        if ( exists ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_DUPLICATE);
        }
        List<LignePrescription> updated = new ArrayList<>(lignePrescriptions); // copie mutable
        updated.add(newLignePrescription);
        lignePrescriptions = updated;
        modifiedOn = LocalDate.now();
        return this;
    }

    public Ordonnance removePrescription(PrescriptionId prescriptionId) {
        incrementVersion();
        boolean removed = lignePrescriptions.removeIf(p -> p.prescriptionId().equals(prescriptionId));
        if ( !removed ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_NOT_FOUND);
        }
        modifiedOn = LocalDate.now();
        return this;
    }

}