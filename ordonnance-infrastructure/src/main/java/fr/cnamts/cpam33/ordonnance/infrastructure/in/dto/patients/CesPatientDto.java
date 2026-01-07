package fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CesPatientDto(
    @NotBlank String externalId,
    @NotBlank String nom,
    @NotBlank String prenom,
    @NotNull
    @Past
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate dateNaissance,
    @NotBlank String cesRattachement
) {

}
