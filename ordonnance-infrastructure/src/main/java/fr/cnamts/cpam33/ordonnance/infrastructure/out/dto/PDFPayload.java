package fr.cnamts.cpam33.ordonnance.infrastructure.out.dto;

import jakarta.validation.constraints.NotBlank;

public record PDFPayload(
        @NotBlank String filename,
        @NotBlank String contentType,
        @NotBlank String base64
) {

}
