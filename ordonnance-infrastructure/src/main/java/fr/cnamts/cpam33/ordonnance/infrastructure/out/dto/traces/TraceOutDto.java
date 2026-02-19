package fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.traces;

import java.util.List;

public record TraceOutDto(
        TraceStatusDto status,
        List<TraceAttributeDto> traceAttributes,
        TraceFailureDto error
) {
}
