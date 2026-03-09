package fr.cnamts.cpam33.ordonnance.domain.policies;

import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.PatientExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class PatientPoliciesTest {

    @Test
    void patient_not_null_should_throw_when_patient_is_null() {
        DomainException ex = assertThrows(DomainException.class, () ->
                PatientPolicies.PATIENT_NOT_NULL.enforce(null)
        );
        assertEquals(PatientExceptionCode.BS_PATIENT_INVALID, ex.getCode());
    }

    @Test
    void patient_not_null_should_not_throw_when_patient_is_not_null() {
        Patient patient = PatientFixtures.patientValide();
        assertDoesNotThrow(() -> PatientPolicies.PATIENT_NOT_NULL.enforce(patient));
    }

    @Test
    void external_id_required_should_throw_when_external_id_is_null() {
        Patient patient = PatientFixtures.patientValideWitExternalId(null);
        DomainException ex = assertThrows(DomainException.class, () ->
                PatientPolicies.EXTERNAL_ID_REQUIRED.enforce(patient)
        );
        assertEquals(PatientExceptionCode.BS_PATIENT_EXTERNAL_ID_MISSING, ex.getCode());
    }

    @Test
    void external_id_required_should_not_throw_when_external_id_is_present() {
        Patient patient = PatientFixtures.patientValideWithIdAndCes("123456789", "EXT-1");
        assertDoesNotThrow(() -> PatientPolicies.EXTERNAL_ID_REQUIRED.enforce(patient));
    }

    @Test
    void for_import_should_require_patient_not_null_first() {
        DomainException ex = assertThrows(DomainException.class, () ->
                PatientPolicies.forImport().enforce(null)
        );
        assertEquals(PatientExceptionCode.BS_PATIENT_INVALID, ex.getCode());
    }

    @Test
    void for_import_should_require_external_id_when_patient_is_not_null() {
        Patient patient = PatientFixtures.patientValideWitExternalId(null);
        DomainException ex = assertThrows(DomainException.class, () ->
                PatientPolicies.forImport().enforce(patient)
        );
        assertEquals(PatientExceptionCode.BS_PATIENT_EXTERNAL_ID_MISSING, ex.getCode());
    }

    @Test
    void for_import_should_pass_when_patient_is_valid() {
        Patient patient = PatientFixtures.patientValide();
        assertDoesNotThrow(() -> PatientPolicies.forImport().enforce(patient));
    }

    @Test
    void for_creation_should_only_require_patient_not_null() {
        Patient patient = PatientFixtures.patientValideWitExternalId(null);
        assertDoesNotThrow(() -> PatientPolicies.forCreate().enforce(patient));
    }

    @Test
    void for_update_should_only_require_patient_not_null() {
        Patient patient = PatientFixtures.patientValideWitExternalId(null);
        assertDoesNotThrow(() -> PatientPolicies.forUpdate().enforce(patient));
    }

    @Test
    void constructor_should_be_unusable() throws Exception {
        Constructor<PatientPolicies> ctor = PatientPolicies.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, ctor::newInstance);
        assertInstanceOf(UnsupportedOperationException.class, ex.getTargetException());
        assertEquals("Policy factory", ex.getTargetException().getMessage());
    }

}

