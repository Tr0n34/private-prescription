package fr.cnamts.cpam33.ordonnance.domain.policies;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PatientExceptionCode;

public final class PatientRules {

    public interface PatientRule {
        void check(Patient patient);
    }

    private PatientRules() {}

    public static PatientRule forImport() {
        return allOf(
                requirePatientNotNull(),
                requireExternalPatientId()
        );
    }

    public static PatientRule forCreation() {
        return allOf(
                requirePatientNotNull()
        );
    }

    public static PatientRule forUpdate() {
        return allOf(
                requirePatientNotNull()
        );
    }

    private static PatientRule allOf(PatientRule... rules) {
        return patient -> {
            for ( PatientRule rule : rules ) {
                rule.check(patient);
            }
        };
    }

    private static PatientRule requireExternalPatientId() {
        return patient -> {
            if (patient.externalPatientId() == null) {
                throw new DomainException(PatientExceptionCode.BS_PATIENT_EXTERNAL_ID_MISSING);
            }
        };
    }

    private static PatientRule requirePatientNotNull() {
        return patient ->  {
            if ( patient == null ) {
                throw new DomainException(PatientExceptionCode.BS_PATIENT_INVALID);
            }
        };
    }

}

