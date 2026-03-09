package fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObjectId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.OrdonnanceExceptionCode;

public record OrdonnanceId(
        String numero
) implements DomainObjectId {

    public OrdonnanceId {
        checkNumeroOrdonnanceId(numero);
    }

    public void checkNumeroOrdonnanceId(String numero) throws DomainException {
        if ( numero == null || numero.isBlank()) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_ID_MISSING);
        }
        if ( !numero.chars().allMatch(Character::isDigit) ) {
            throw new DomainException(OrdonnanceExceptionCode.BS_ORDONNANCE_ID_NUMERIC);
        }
    }

}
