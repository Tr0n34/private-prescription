package fr.cnamts.cpam33.ordonnance.domain.commands;

import fr.cnamts.cpam33.ordonnance.domain.fixtures.MedecinFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PrescriptionFixtures;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Prescription;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.CreateOrdonnanceCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.PatientId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrdonnanceCmdTest {

    public static final String CANNOT_BE_NULL = "Patient or Medecin id or prescriptions cannot be null";

    @Test
    void constructor_shouldCreateSuccessfully_whenAllParametersProvided() {
        PatientId patientId = PatientFixtures.patientValide().patientId();
        MedecinId medecinId = MedecinFixtures.medecinValide().medecinId();
        Prescription prescription = PrescriptionFixtures.onePrescription();
        CreateOrdonnanceCmd cmd = new CreateOrdonnanceCmd(patientId, medecinId, List.of(prescription));
        assertNotNull(cmd);
        assertEquals(patientId, cmd.patientId());
        assertEquals(medecinId, cmd.medecinId());
        assertEquals(1, cmd.prescriptions().size());
        assertEquals(prescription, cmd.prescriptions().get(0));
    }

    @Test
    void constructor_shouldThrowException_whenPatientIdIsNull() {
        MedecinId medecinId = MedecinFixtures.medecinValide().medecinId();
        Prescription prescription = PrescriptionFixtures.onePrescription();
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new CreateOrdonnanceCmd(null, medecinId, List.of(prescription))
        );
        assertEquals(CANNOT_BE_NULL, ex.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenMedecinIdIsNull() {
        PatientId patientId = PatientFixtures.patientValide().patientId();
        Prescription prescription = PrescriptionFixtures.onePrescription();
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new CreateOrdonnanceCmd(patientId, null, List.of(prescription))
        );
        assertEquals(CANNOT_BE_NULL, ex.getMessage());
    }

    @Test
    void constructor_shouldThrowException_whenPrescriptionsIsNull() {
        PatientId patientId = PatientFixtures.patientValide().patientId();
        MedecinId medecinId = MedecinFixtures.medecinValide().medecinId();
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new CreateOrdonnanceCmd(patientId, medecinId, null)
        );
        assertEquals(CANNOT_BE_NULL, ex.getMessage());
    }
}
