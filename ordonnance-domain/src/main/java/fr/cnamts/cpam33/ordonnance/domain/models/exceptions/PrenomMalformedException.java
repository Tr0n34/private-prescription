package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PrenomExceptionCode;

public class PrenomMalformedException extends DomainException {

    public PrenomMalformedException(PrenomExceptionCode code) {
        super(code);
    }

}
