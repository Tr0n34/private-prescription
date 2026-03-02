package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.traces.TracePublicationContext;
import fr.cnamts.cpam33.traces.contract.dto.TraceDto;

public final class TraceContextAssembler {

    private TraceContextAssembler() {
        // prevent instantiation
    }

    public static TraceDto enrich(TraceDto partial, TracePublicationContext ctx) {
        return new TraceDto(
                partial.traceId(),
                ctx.schemaVersion(),
                ctx.applicationId(),
                partial.acteMetierCode(),
                partial.fonction(),
                ctx.correlationId(),
                ctx.frontPage(),
                partial.boundedContext(),
                partial.utilisateurId(),
                ctx.utilisateurIp(),
                partial.createdOn(),
                partial.in(),
                partial.out()
        );
    }

}
