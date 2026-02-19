package fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.traces;

public record TraceFailureDto(
        String type,
        String message,
        String causeType,
        String causeMessage
) {
}