package fr.cnamts.cpam33.ordonnance.domain.models.aggregates;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Prescription;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.PrescriptionId;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.OrdonnanceInvalideException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.OrdonnanceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.Medecin;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ordonnance implements DomainObject {

    public static final int VERSION_INITIALE = 1;

    private final OrdonnanceId ordonnanceId;
    private Patient patient;
    private Medecin medecin;
    private List<Prescription> prescriptions;
    private OrdonnanceStatus status;
    private int version;
    private final LocalDate createdOn;
    private LocalDate modifiedOn;
    private LocalDateTime signedOn;

    private Ordonnance(
            OrdonnanceId ordonnanceId,
            Patient patient,
            Medecin medecin,
            List<Prescription> prescriptions,
            OrdonnanceStatus status,
            int version,
            LocalDate createdOn,
            LocalDate modifiedOn,
            LocalDateTime signedOn
    ) {
        this.ordonnanceId = ordonnanceId;
        this.patient = patient;
        this.medecin = medecin;
        this.prescriptions = new ArrayList<>(prescriptions);
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
            List<Prescription> prescriptions) {
        return new Ordonnance(ordonnanceId, patient, medecin, prescriptions,
                OrdonnanceStatus.CREATED, VERSION_INITIALE, LocalDate.now(), LocalDate.now(), null);
    }

    public void validate() {
        checkPrescriptionState();
        status = OrdonnanceStatus.VALIDATED;
    }

    public List<Prescription> prescriptions() {
        return List.copyOf(prescriptions); /* Copie défensive */
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
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_ID_MISSING);
        }
        if ( patient == null ) {
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_PATIENT_MISSING);
        }
        if ( medecin == null ) {
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_MEDECIN_MISSING);
        }
    }

    public void checkPrescriptionState() {
        if ( prescriptions == null || prescriptions.isEmpty() ) {
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_MISSING);
        }
    }

    private void ensureOrdonnanceCantBeChanged() {
        if ( status == OrdonnanceStatus.SIGNED ) {
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_IMMUTABLE_WHEN_SIGNED);
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

    public Ordonnance replacePrescriptions(List<Prescription> prescriptions) {
        ensureOrdonnanceCantBeChanged();
        incrementVersion();
        if ( prescriptions == null || prescriptions.isEmpty()) {
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_MISSING);
        }
        prescriptions = new ArrayList<>(prescriptions); // copie mutable
        modifiedOn = LocalDate.now();
        return this;
    }

    public Ordonnance replacePrescription(Prescription newPrescription) {
        ensureOrdonnanceCantBeChanged();
        incrementVersion();
        boolean exists = prescriptions.stream().anyMatch(p -> p.prescriptionId().equals(newPrescription.prescriptionId()));
        if ( !exists ) {
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_NOT_FOUND);
        }
        prescriptions = new ArrayList<>(prescriptions.stream()
                .map(p -> p.prescriptionId().equals(newPrescription.prescriptionId()) ? newPrescription : p)
                .toList());
        modifiedOn = LocalDate.now();
        return this;
    }

    public Ordonnance addPrescription(Prescription newPrescription) {
        ensureOrdonnanceCantBeChanged();
        incrementVersion();
        boolean exists = prescriptions.stream().anyMatch(p -> p.prescriptionId().equals(newPrescription.prescriptionId()));
        if ( exists ) {
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_DUPLICATE);
        }
        List<Prescription> updated = new ArrayList<>(prescriptions); // copie mutable
        updated.add(newPrescription);
        prescriptions = updated;
        modifiedOn = LocalDate.now();
        return this;
    }

    public Ordonnance removePrescription(PrescriptionId prescriptionId) {
        incrementVersion();
        boolean removed = prescriptions.removeIf(p -> p.prescriptionId().equals(prescriptionId));
        if ( !removed ) {
            throw new OrdonnanceInvalideException(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_NOT_FOUND);
        }
        modifiedOn = LocalDate.now();
        return this;
    }

}