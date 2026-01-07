package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites;

import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.NomMalFormedException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.NomExceptionCode;

public record Prenom(
        String value
) {

    public Prenom {
        if ( value == null || value.isBlank() ) {
            throw new NomMalFormedException(NomExceptionCode.BS_NOM_MISSING);
        }
        if ( value.length() < 2 || value.length() >= 128 ) {
            throw new NomMalFormedException(NomExceptionCode.BS_NOM_LENGTH);
        }
    }

}
