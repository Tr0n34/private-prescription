package fr.cnamts.cpam33.ordonnance.domain.commands;

import fr.cnamts.cpam33.ordonnance.domain.fixtures.MedecinFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PrescriptionFixtures;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.CommandCodeException;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Traitement;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.ordonnances.CreateOrdonnanceCmd;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateOrdonnanceCmdTest {

    @Test
    void constructor_shouldCreateSuccessfully_whenAllParametersProvided() {
        PatientId patientId = PatientFixtures.patientValide().patientId();
        MedecinId medecinId = MedecinFixtures.medecinValide().medecinId();
        Traitement traitement = PrescriptionFixtures.onePrescription();
        CreateOrdonnanceCmd cmd = new CreateOrdonnanceCmd(patientId, medecinId, List.of(traitement));
        assertNotNull(cmd);
        assertEquals(patientId, cmd.patientId());
        assertEquals(medecinId, cmd.medecinId());
        assertEquals(1, cmd.traitements().size());
        assertEquals(traitement, cmd.traitements().getFirst());
    }

    @Test
    void debug_validation_errors() {
        var patientId = PatientFixtures.patientValide().patientId();
        var medecinId = MedecinFixtures.medecinValide().medecinId();
        var lp = PrescriptionFixtures.onePrescription();

        var vr = CreateOrdonnanceCmd.validateSelf(patientId, medecinId, List.of(lp));
        System.out.println(vr.errors());
    }

    @Test
    void constructor_shouldThrowException_whenPatientIdIsNull() {
        MedecinId medecinId = MedecinFixtures.medecinValide().medecinId();
        Traitement traitement = PrescriptionFixtures.onePrescription();
        DomainException ex = assertThrows(
                DomainException.class,
                () -> new CreateOrdonnanceCmd(null, medecinId, List.of(traitement))
        );
        assertEquals(CommandCodeException.CMD_PARAMETRE_MANQUANT, ex.getCode());
    }

    @Test
    void constructor_shouldThrowException_whenMedecinIdIsNull() {
        PatientId patientId = PatientFixtures.patientValide().patientId();
        Traitement traitement = PrescriptionFixtures.onePrescription();
        DomainException ex = assertThrows(
                DomainException.class,
                () -> new CreateOrdonnanceCmd(patientId, null, List.of(traitement))
        );
        assertEquals(CommandCodeException.CMD_PARAMETRE_MANQUANT, ex.getCode());
    }

    @Test
    void constructor_shouldThrowException_whenPrescriptionsIsNull() {
        PatientId patientId = PatientFixtures.patientValide().patientId();
        MedecinId medecinId = MedecinFixtures.medecinValide().medecinId();
        DomainException ex = assertThrows(
                DomainException.class,
                () -> new CreateOrdonnanceCmd(patientId, medecinId, null)
        );
        assertEquals(CommandCodeException.CMD_PARAMETRE_MANQUANT, ex.getCode());
    }
}
