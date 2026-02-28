package fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.traces;

public record TracePublicationContext(
        String schemaVersion,
        String applicationId,
        String utilisateurIp,
        String correlationId,
        String frontPage
) {

}
