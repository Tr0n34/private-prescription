package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObjectId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Rpps;

public record MedecinId(
        String id,
        Rpps rpps
) implements DomainObjectId {

    public MedecinId {
        if ( id == null || id.isBlank() ) {
            throw new IllegalArgumentException("MedecinId invalide");
        }
    }
}
