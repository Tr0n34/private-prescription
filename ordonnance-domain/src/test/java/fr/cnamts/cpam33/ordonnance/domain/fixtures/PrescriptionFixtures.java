package fr.cnamts.cpam33.ordonnance.domain.fixtures;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Posologie;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Prescription;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.PrescriptionId;

import java.util.List;

public class PrescriptionFixtures {

    public static Prescription onePrescription() {
        return new Prescription(
                new PrescriptionId("123456"),
                new Medicament("Ventoline"),
                new Posologie("2 fois par jour matin et soir")
        );
    }

    public static List<Prescription> twoPrescription() {
        return List.of(
                new Prescription(
                        new PrescriptionId("123456"),
                        new Medicament("Doliprance"),
                        new Posologie("3 fois par jour max")),
                new Prescription(
                        new PrescriptionId("123456"),
                        new Medicament("Innovair Nexthaler"),
                        new Posologie("2 bouffées matin et soir"))
        );
    }

    public static List<Prescription> prescriptionVide() {
        return List.of();
    }

}
