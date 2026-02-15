package fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients;

import java.time.LocalDate;

public record ExportPatientDto(
        String ins,
        String nom,
        String prenom,
        LocalDate dateNaissance,
        String sexe
) {
}
