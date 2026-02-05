package fr.cnamts.cpam33.ordonnance.domain.objects;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PatientTest {

    @Test
    public void should_create_patient_id() {
        PatientId patientId = new PatientId("1234567891234");
        assertEquals("1234567891234", patientId.numero());
    }

    @Test
    public void should_create_external_patient_id() {
        ExternalPatientId externalPatientId = new ExternalPatientId("123");
        assertEquals("123", externalPatientId.numero());
    }

    @Test
    public void should_fail_when_id_is_blank() {
        assertThrows(DomainException.class, () -> new PatientId(""));
    }

}
