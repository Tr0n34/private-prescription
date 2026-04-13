package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.MedecinExceptionCode;

public record Rpps(
        String numero
) {

    public Rpps {
        if ( numero == null || numero.isBlank() ) {
            throw new DomainException(MedecinExceptionCode.BS_RPPS_MISSING);
        }
        if ( numero.length() != 11 && numero.chars().allMatch(Character::isDigit) ) {
            throw new DomainException(MedecinExceptionCode.BS_RPPS_LENGTH);
        }
    }

}
