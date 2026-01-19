package fr.cnamts.cpam33.ordonnance.infrastructure.fixtures;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ExternalPatientDto;

import java.time.LocalDate;
import java.time.Month;

public final class ExternalPatientDtoFixtures {

    private ExternalPatientDtoFixtures() {}

    public static ExternalPatientDto patientValide1() {
        return new ExternalPatientDto(
                "EXT-123",
                "Dupont",
                "Jean",
                LocalDate.of(1980, Month.SEPTEMBER, 5)
        );
    }

    public static ExternalPatientDto patientValide2() {
        return new ExternalPatientDto("EXT-456",
                "Martin",
                "Claire",
                LocalDate.of(1981, Month.SEPTEMBER, 6));
    }

    public static ExternalPatientDto patientSansId() {
        return new ExternalPatientDto(null,
                "Martin",
                "Claire",
                LocalDate.of(1981, Month.SEPTEMBER, 6));
    }


    public static String patientSansIdExterneJson() {
        return """
            {
              "nom": "Dupont",
              "prenom": "Jean"
            }
            """;
    }

    public static String patientSansNomJson() {
        return """
            {
              "id": "EXT-123",
              "nom": "",
              "prenom": "Jean"
            }
            """;
    }

}
