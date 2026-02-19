package fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.traces;

import java.time.LocalDateTime;
import java.util.List;

public record TraceDto(
        String traceId,
        String schemaVersion,
        String boundedContext,
        String acteMetierCode,
        String utilisateurId,
        LocalDateTime timestamp,
        TraceInDto in,
        TraceOutDto out
) {
}
