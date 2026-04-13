package fr.cnamts.cpam33.ordonnance.application.views.medicaments;

import java.util.List;

public record MedicamentView(
        String codeSp,
        String nom,
        String nomLong,
        String cdfNom,
        String catcCode,
        String cipUcd,
        FormeView forme,
        String atu,
        String t2a,
        VoieAdministrationView voieAdministration,
        List<PosologieView> posologies
) {
}
