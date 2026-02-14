package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;

public record Medecin(
        MedecinId medecinId,
        Nom nom,
        Prenom prenom
) implements DomainObject {

    public static Medecin of(MedecinId medecinId, Nom nom, Prenom prenom) {
        return new Medecin(medecinId, nom, prenom);
    }

}
