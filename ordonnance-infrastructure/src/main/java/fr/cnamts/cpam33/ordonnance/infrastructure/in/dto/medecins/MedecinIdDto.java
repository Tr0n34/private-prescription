package fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.medecins;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MedecinIdDto(
        @NotBlank @Pattern(regexp = "[0-9]{13}")String externalId,
        @NotBlank @Pattern(regexp = "[0-9]{11}")String rpps
) {
}
