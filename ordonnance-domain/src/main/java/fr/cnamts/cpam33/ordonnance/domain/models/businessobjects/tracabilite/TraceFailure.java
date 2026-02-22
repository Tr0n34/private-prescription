package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite;

public record TraceFailure(
        String type,
        String message,
        String code,
        String details
) {

}
