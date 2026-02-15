package fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record ImportPatientDto(
        @NotNull
        String externalId,
        String nom,
        @NotBlank
        String prenom,
        @NotNull
        @Past
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dateNaissance
) {

}
