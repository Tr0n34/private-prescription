package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects;

public record IdentiteMedecin(
        MedecinId medecinId,
        String nom,
        String prenom,
        Rpps rpps
) {
}
