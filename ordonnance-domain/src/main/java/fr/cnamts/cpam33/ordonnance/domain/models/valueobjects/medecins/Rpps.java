package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins;

import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.RppsInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.RppsExceptionCode;

public record Rpps(
        String value
) {

    public Rpps {
        if ( value == null || value.isBlank() ) {
            throw new RppsInvalidException(RppsExceptionCode.BS_RPPS_MISSING);
        }
        if ( value.length() != 11 && value.chars().allMatch(Character::isDigit) ) {
            throw new RppsInvalidException(RppsExceptionCode.BS_RPPS_LENGTH);
        }
    }

}
