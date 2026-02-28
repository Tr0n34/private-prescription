package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceId;

import java.time.OffsetDateTime;

public record TraceOutbox(
        TraceId traceId,
        String acteMetierId,
        String payloadJson,
        TraceOutboxStatus status,
        int retryCount,
        OffsetDateTime nextRetryAt,
        OffsetDateTime createdAt,
        OffsetDateTime lastFailureAt,
        OffsetDateTime sentAt,
        String reason
) {

}