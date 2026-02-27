package fr.cnamts.cpam33.ordonnance.domain.fixtures;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.PrescriptionId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Traitement;

import java.util.List;

public class PrescriptionFixtures {

    public static Traitement onePrescription() {
        return new Traitement(
                new PrescriptionId("123456"),
                List.of());
    }

    public static List<Traitement> twoPrescription() {
        return List.of(
                new Traitement(
                        new PrescriptionId("123456"),
                        List.of()),
                new Traitement(
                        new PrescriptionId("1234567"),
                        List.of())
                );
    }

    public static List<Traitement> prescriptionVide() {
        return List.of();
    }

}
