package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObjectId;

public record ActeMetierId(
        String code
) implements DomainObjectId {
}
