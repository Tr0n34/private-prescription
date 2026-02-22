package fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.medicaments;

public record MedicamentDto(
        String codeSp,
        String nom,
        String cdfNom,
        String catcCode,
        String cipUcd,
        FormeDto forme,
        String atu,
        String t2a,
        VoieAdministrationDto voieAdministration
) {
}
