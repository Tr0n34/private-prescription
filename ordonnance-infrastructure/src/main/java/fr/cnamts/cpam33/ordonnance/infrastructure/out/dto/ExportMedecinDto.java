package fr.cnamts.cpam33.ordonnance.infrastructure.out.dto;

public record ExportMedecinDto(
        String rpps,
        String nom,
        String prenom,
        String specialite
) {
}
