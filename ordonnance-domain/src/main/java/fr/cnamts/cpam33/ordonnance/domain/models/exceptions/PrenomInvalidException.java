package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PrenomExceptionCode;

public class PrenomInvalidException extends DomainException {

    public PrenomInvalidException(PrenomExceptionCode code) {
        super(code);
    }

}
