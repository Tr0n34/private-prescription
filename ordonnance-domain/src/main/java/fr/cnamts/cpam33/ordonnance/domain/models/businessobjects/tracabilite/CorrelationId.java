package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;

public record CorrelationId(
        String numero
) implements DomainObject {
}
