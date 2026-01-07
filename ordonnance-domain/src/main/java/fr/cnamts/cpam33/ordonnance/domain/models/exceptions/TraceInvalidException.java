package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.TraceExceptionCode;

public class TraceInvalidException extends DomainException {

    public TraceInvalidException(TraceExceptionCode code) {
        super(code);
    }

}
