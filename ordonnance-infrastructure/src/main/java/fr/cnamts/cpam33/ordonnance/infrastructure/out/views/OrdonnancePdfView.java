package fr.cnamts.cpam33.ordonnance.infrastructure.out.views;

import java.util.List;

public record OrdonnancePdfView(
        String patientNomComplet,
        String patientDateNaissance,
        String medecinNom,
        List<LigneOrdonnanceView> lignes
) {

}
