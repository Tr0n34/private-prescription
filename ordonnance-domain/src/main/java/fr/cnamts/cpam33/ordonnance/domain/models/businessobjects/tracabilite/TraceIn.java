package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite;

import java.util.List;

public record TraceIn(
        String method,
        String signature,
        List<TraceAttribute> params
) {
}
