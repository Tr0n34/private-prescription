package fr.cnamts.cpam33.ordonnance.domain.aggregates;

import fr.cnamts.cpam33.ordonnance.domain.fixtures.MedecinFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.OrdonnanceFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PrescriptionFixtures;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Prescription;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.OrdonnanceInvalideException;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.Medecin;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.Patient;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrdonnanceTest {

    @Test
    void should_create_valid_ordonnance() {
        Medecin medecin = MedecinFixtures.medecinValide();
        Patient patient = PatientFixtures.patientValide();
        OrdonnanceId ordonnanceId = OrdonnanceFixtures.ordonnanceId();
        List<Prescription> prescriptions = List.of(
                PrescriptionFixtures.onePrescription()
        );
        Ordonnance ordonnance = Ordonnance.of(
                ordonnanceId,
                patient,
                medecin,
                prescriptions
        );
        assertNotNull(ordonnance);
        assertEquals(1, ordonnance.prescriptions().size());
    }

    @Test
    void should_fail_when_validate_and_no_prescription() {
        Medecin medecin = MedecinFixtures.medecinValide();
        Patient patient = PatientFixtures.patientValide();
        OrdonnanceId ordonnanceId = OrdonnanceFixtures.ordonnanceId();
        Ordonnance ordonnance = Ordonnance.of(ordonnanceId, patient, medecin, List.of());
        assertThrows(OrdonnanceInvalideException.class, ordonnance::validate);
    }



}
