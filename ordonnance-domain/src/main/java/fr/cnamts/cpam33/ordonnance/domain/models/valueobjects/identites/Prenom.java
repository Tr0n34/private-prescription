package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.IdentiteExceptionCode;

public record Prenom(
        String value
) {

    public Prenom {
        if ( value == null || value.isBlank() ) {
            throw new DomainException(IdentiteExceptionCode.BS_NOM_MISSING);
        }
        if ( value.length() < 2 || value.length() >= 128 ) {
            throw new DomainException(IdentiteExceptionCode.BS_NOM_LENGTH);
        }
    }

}
