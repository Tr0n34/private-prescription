package fr.cnamts.cpam33.ordonnance.domain.models.aggregates;

import fr.cnamts.cpam33.ordonnance.domain.exceptions.OrdonnanceVersionException;

public record OrdonnanceVersion(
        int numero
) {

    private static final int INIT_VERSION = 1;

    public OrdonnanceVersion {
        if ( numero < 1 ) {
            throw new OrdonnanceVersionException("La version de l'ordonnance doit être supérieure ou égale à 1.");
        }
    }

    public static OrdonnanceVersion first() {
        return new OrdonnanceVersion(INIT_VERSION);
    }

    public OrdonnanceVersion next() {
        return new OrdonnanceVersion(numero + 1);
    }

}
