package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects;

public record IdentitePatient(
        PatientId PatientId,
        String nom,
        String prenom
) {
}
