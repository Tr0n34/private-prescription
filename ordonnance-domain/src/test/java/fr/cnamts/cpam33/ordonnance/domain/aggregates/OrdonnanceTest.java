package fr.cnamts.cpam33.ordonnance.domain.aggregates;

import fr.cnamts.cpam33.ordonnance.domain.fixtures.MedecinFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.OrdonnanceFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PrescriptionFixtures;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Traitement;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.Medecin;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.OrdonnanceId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrdonnanceTest {

    @Test
    void should_create_valid_ordonnance() {
        Medecin medecin = MedecinFixtures.medecinValide();
        Patient patient = PatientFixtures.patientValide();
        OrdonnanceId ordonnanceId = OrdonnanceFixtures.ordonnanceId();
        List<Traitement> traitements = List.of(
                PrescriptionFixtures.onePrescription()
        );
        Ordonnance ordonnance = Ordonnance.of(
                ordonnanceId,
                patient,
                medecin,
                traitements
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
        assertThrows(DomainException.class, ordonnance::validate);
    }



}
