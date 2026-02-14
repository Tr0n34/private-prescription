package fr.cnamts.cpam33.ordonnance.domain.policies;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PatientExceptionCode;

public final class PatientPolicies {

    @FunctionalInterface
    public interface PatientPolicy {
        void enforce(Patient patient);
    }

    public record Rule(String name, PatientPolicy patientPolicy) implements PatientPolicy {

        @Override
        public void enforce(Patient patient) {
            patientPolicy.enforce(patient);
        }
    }

    private PatientPolicies() {
        throw new UnsupportedOperationException("Policy factory");
    }

    public static final Rule PATIENT_NOT_NULL =
            new Rule("PATIENT_NOT_NULL", patient -> {
                if ( patient == null ) {
                    throw new DomainException(PatientExceptionCode.BS_PATIENT_INVALID);
                }
            });

    public static final Rule EXTERNAL_ID_REQUIRED =
            new Rule("EXTERNAL_ID_REQUIRED", patient -> {
                if ( patient.externalPatientId() == null
                        || patient.externalPatientId().numero() == null
                        || patient.externalPatientId().numero().isBlank() ) {
                    throw new DomainException(PatientExceptionCode.BS_PATIENT_EXTERNAL_ID_MISSING);
                }
            });

    public static PatientPolicy forImport() {
        return allOf(PATIENT_NOT_NULL, EXTERNAL_ID_REQUIRED);
    }

    public static PatientPolicy forCreation() {
        return allOf(PATIENT_NOT_NULL);
    }

    public static PatientPolicy forUpdate() {
        return allOf(PATIENT_NOT_NULL);
    }

    private static PatientPolicy allOf(Rule... rules) {
        return patient -> {
            for (Rule rule : rules) {
                rule.enforce(patient);
            }
        };
    }

}

