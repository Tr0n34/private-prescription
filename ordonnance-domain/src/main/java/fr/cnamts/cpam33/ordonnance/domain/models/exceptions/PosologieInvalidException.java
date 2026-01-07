package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PosologieExceptionCode;

public class PosologieInvalidException extends DomainException {

    public PosologieInvalidException(PosologieExceptionCode code) {
        super(code);
    }

}
