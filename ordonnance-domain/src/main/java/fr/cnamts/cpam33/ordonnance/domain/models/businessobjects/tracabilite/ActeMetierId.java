package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObjectId;

public record ActeMetierId(
        String code
) implements DomainObjectId {
}
