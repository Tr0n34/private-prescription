package fr.cnamts.cpam33.ordonnance.infrastructure.fixtures;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients.ExternalPatientDto;

import java.time.LocalDate;
import java.time.Month;

public final class ExternalPatientDtoFixtures {

    private ExternalPatientDtoFixtures() {}

    public static ExternalPatientDto patientValide() {
        return new ExternalPatientDto(
                "EXT-123",
                "Dupont",
                "Jean",
                LocalDate.of(1980, Month.SEPTEMBER, 5)
        );
    }

    public static String patientSansIdExterne() {
        return """
            {
              "nom": "Dupont",
              "prenom": "Jean"
            }
            """;
    }

    public static String patientSansNom() {
        return """
            {
              "id": "EXT-123",
              "nom": "",
              "prenom": "Jean"
            }
            """;
    }

}
