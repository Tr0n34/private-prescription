package fr.cnamts.cpam33.ordonnance.infrastructure.fixtures;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.PatientDto;

import java.time.LocalDate;
import java.time.Month;

public final class ExternalPatientDtoFixtures {

    private ExternalPatientDtoFixtures() {}

    public static PatientDto patientValide1() {
        return new PatientDto(
                "uiid-rte6-45tY-12e3",
                "EXT-123",
                "Dupont",
                "Jean",
                LocalDate.of(1980, Month.SEPTEMBER, 5)
        );
    }

    public static PatientDto patientValide2() {
        return new PatientDto(
                "uiid-nb47-45tY-456e",
                "EXT-231",
                "Martin",
                "Claire",
                LocalDate.of(1981, Month.SEPTEMBER, 6));
    }

    public static PatientDto patientSansId() {
        return new PatientDto(null,
                null,
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
