package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.OrdonnanceExceptionCode;

public class OrdonnanceInvalideException extends DomainException {

    public OrdonnanceInvalideException(OrdonnanceExceptionCode code) {
        super(code);
    }

}
