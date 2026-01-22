package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.ExceptionCode;

public class InsInvalidException extends DomainException {

    public InsInvalidException(ExceptionCode code) {
        super(code);
    }

}
