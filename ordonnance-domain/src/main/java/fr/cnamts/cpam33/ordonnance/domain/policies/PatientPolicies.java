package fr.cnamts.cpam33.ordonnance.domain.policies;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.PatientExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.policies.Policies;
import fr.cnamts.cpam33.ordonnance.domain.kernel.policies.Policy;
import fr.cnamts.cpam33.ordonnance.domain.kernel.policies.Rule;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;

public final class PatientPolicies {

    private PatientPolicies() { throw new UnsupportedOperationException("Policy factory"); }

    public static final Rule<Patient, PatientExceptionCode> PATIENT_NOT_NULL =
            Policies.rule(PatientExceptionCode.BS_PATIENT_INVALID, (patient, fail) ->
                Policies.require(patient != null, () -> fail.of("patient", "null"))
            );

    public static final Rule<Patient, PatientExceptionCode> EXTERNAL_ID_REQUIRED =
            Policies.rule(PatientExceptionCode.BS_PATIENT_EXTERNAL_ID_MISSING, (patient, fail) -> {
                Policies.require(patient.externalPatientId() != null, () -> fail.of("externalPatientId", "null"));
                String numero = patient.externalPatientId().numero();
                Policies.require(numero != null && !numero.isBlank(), () -> fail.of("externalPatientId.numero", "blank"));
            });

    public static Policy<Patient> forImport() {
        return Policies.forOperation(
                Patient.class.getSimpleName(),
                "IMPORT",
                Policies.allOf(PATIENT_NOT_NULL, EXTERNAL_ID_REQUIRED)
        );
    }

    public static Policy<Patient> forCreate() {
        return Policies.forOperation(
                Patient.class.getSimpleName(),
                "CREATE",
                Policies.allOf(PATIENT_NOT_NULL)
        );
    }

    public static Policy<Patient> forUpdate() {
        return Policies.forOperation(
                Patient.class.getSimpleName(),
                "UPDATE",
                Policies.allOf(PATIENT_NOT_NULL)
        );
    }

}
