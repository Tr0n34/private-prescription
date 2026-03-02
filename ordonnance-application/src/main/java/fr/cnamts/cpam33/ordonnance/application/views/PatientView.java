package fr.cnamts.cpam33.ordonnance.application.views;

import java.time.LocalDate;

public record PatientView(
        String patientId,
        String externalPatientId,
        String nom,
        String prenom,
        LocalDate dateNaissance
) {
}
