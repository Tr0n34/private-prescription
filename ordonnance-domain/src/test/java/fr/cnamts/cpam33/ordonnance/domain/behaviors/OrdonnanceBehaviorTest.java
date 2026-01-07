package fr.cnamts.cpam33.ordonnance.domain.behaviors;

import fr.cnamts.cpam33.ordonnance.domain.fixtures.MedecinFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.OrdonnanceFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PatientFixtures;
import fr.cnamts.cpam33.ordonnance.domain.fixtures.PrescriptionFixtures;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceStatus;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.OrdonnanceInvalideException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.OrdonnanceExceptionCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrdonnanceBehaviorTest {

    @Test
    void une_ordonnance_creee_peut_etre_signee_si_valide() {
        Ordonnance ordonnance = OrdonnanceFixtures.ordonnanceValide("123456");
        ordonnance.sign();
        assertEquals(OrdonnanceStatus.SIGNED, ordonnance.status());
    }

    @Test
    void une_ordonnance_signee_ne_peut_plus_etre_signee() {
        Ordonnance ordonnance = OrdonnanceFixtures.ordonnanceSignee();
        OrdonnanceInvalideException exception = assertThrows(
                OrdonnanceInvalideException.class,
                ordonnance::sign);
        assertEquals(OrdonnanceExceptionCode.BS_ORDONNANCE_IMMUTABLE_WHEN_SIGNED, exception.getCode());
    }

    @Test
    void une_ordonnance_signee_ne_peut_plus_etre_modifiee() {
        Ordonnance ordonnance = OrdonnanceFixtures.ordonnanceSignee();
        OrdonnanceInvalideException exception = assertThrows(
                OrdonnanceInvalideException.class,
                () -> ordonnance.changeMedecin(MedecinFixtures.medecinValide()));
        assertEquals(OrdonnanceExceptionCode.BS_ORDONNANCE_IMMUTABLE_WHEN_SIGNED, exception.getCode());
    }

    @Test
    void une_ordonnance_sans_prescription_ne_peut_pas_etre_signee_ou_valide() {
        OrdonnanceInvalideException ordonnanceInvalideException = assertThrows(OrdonnanceInvalideException.class,
                () -> {
                    Ordonnance.of(
                            OrdonnanceFixtures.ordonnanceId(),
                            PatientFixtures.patientValide(),
                            MedecinFixtures.medecinValide(),
                            PrescriptionFixtures.prescriptionVide()
                    ).validate();
                });
        assertEquals(OrdonnanceExceptionCode.BS_ORDONNANCE_PRESCRIPTION_MISSING, ordonnanceInvalideException.getCode());
    }

}
