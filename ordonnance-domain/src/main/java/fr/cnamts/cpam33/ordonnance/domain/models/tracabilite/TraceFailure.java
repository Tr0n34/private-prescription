package fr.cnamts.cpam33.ordonnance.domain.models.tracabilite;

public record TraceFailure(
        String type,
        String message,
        String code,
        String details
) {

}
