package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects;

import fr.cnamts.cpam33.ordonnance.domain.exceptions.RppsInvalideException;

public record Rpps(
        String value
) {

    public Rpps {
        if ( value == null || value.isBlank() ) {
            throw new RppsInvalideException("Rpps invalide");
        }
    }

}
