package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObjectId;

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
