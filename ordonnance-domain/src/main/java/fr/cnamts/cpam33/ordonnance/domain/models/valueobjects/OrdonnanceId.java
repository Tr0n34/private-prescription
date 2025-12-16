package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects;

import fr.cnamts.cpam33.ordonnance.domain.exceptions.OrdonnanceInvalideException;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceVersion;

import java.time.LocalDateTime;

public record OrdonnanceId(
        String numero,
        Rpps rpps,
        OrdonnanceVersion version
) {

    public OrdonnanceId {
        if ( numero == null || numero.isBlank()) {
            throw new OrdonnanceInvalideException("Le numéro de l'ordonnance ne doit pas être vide.");
        }
        if (!numero.chars().allMatch(Character::isDigit)) {
            throw new OrdonnanceInvalideException("Le numéro de l'ordonnance doit être numérique");
        }
    }

}
