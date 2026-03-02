package fr.cnamts.cpam33.ordonnance.infrastructure.out.providers;

import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientNumGenerator;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.UuidPatientNumGenerator;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UuidPatientNumGeneratorTest {

    private final UuidPatientNumGenerator generator = new UuidPatientNumGenerator();

    @Test
    void generate_should_return_non_null_value() {
        String value = generator.generate();
        assertNotNull(value);
    }

    @Test
    void generate_should_return_valid_uuid() {
        String value = generator.generate();
        assertDoesNotThrow(() -> UUID.fromString(value));
    }

    @Test
    void generate_should_return_different_values_on_each_call() {
        String first = generator.generate();
        String second = generator.generate();
        assertNotEquals(first, second);
    }

    @Test
    void newpatientId_should_return_non_null_patient_id() {
        PatientId patientId = generator.newpatientId("whatever");
        assertNotNull(patientId);
    }

    @Test
    void newpatientId_should_wrap_a_valid_uuid_generated_value() {
        PatientId patientId = generator.newpatientId("external-123");
        assertDoesNotThrow(() -> UUID.fromString(patientId.numero()));
    }

    @Test
    void newpatientId_should_ignore_external_id_and_still_generate_unique_ids() {
        PatientId first = generator.newpatientId("ext-A");
        PatientId second = generator.newpatientId("ext-A");
        assertNotEquals(first.numero(), second.numero());
    }

    @Test
    void generator_should_satisfy_PatientNumGenerator_contract() {
        PatientNumGenerator contract = generator;
        String value = contract.generate();
        assertNotNull(value);
        assertDoesNotThrow(() -> UUID.fromString(value));
    }

}
