package fr.cnamts.cpam33.ordonnance.domain.fixtures;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Traitement;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Posologie;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.PrescriptionId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;

import java.util.List;

public class PrescriptionFixtures {

    public static Traitement onePrescription() {
        return new Traitement(
                new PrescriptionId("123456"),
                List.of(
                        new Medicament("Ventoline", List.of(
                                new Posologie("2 fois par jour matin et soir"))
                        )));
    }

    public static List<Traitement> twoPrescription() {
        return List.of(
                new Traitement(
                        new PrescriptionId("123456"),
                        List.of(
                                new Medicament("Ventoline", List.of(
                                        new Posologie("2 fois par jour matin et soir"))
                                ))),
                new Traitement(
                        new PrescriptionId("1234567"),
                        List.of(
                                new Medicament("Doliprane", List.of(
                                        new Posologie("2 grammes maximum toutes les 24 h"))
                                )))
                );
    }

    public static List<Traitement> prescriptionVide() {
        return List.of();
    }

}
