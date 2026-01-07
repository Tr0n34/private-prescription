package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.RppsExceptionCode;

public class RppsInvalidException extends DomainException {

    public RppsInvalidException(RppsExceptionCode code) {
        super(code);
    }

}
