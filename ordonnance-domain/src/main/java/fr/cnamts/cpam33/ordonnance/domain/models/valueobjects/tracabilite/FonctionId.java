package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObjectId;

public record FonctionId(
        String code
) implements DomainObjectId {
}
