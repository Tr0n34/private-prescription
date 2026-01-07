package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.ActeMetierExceptionCode;

public class ActeMetierInvalidException extends DomainException {

    public ActeMetierInvalidException(ActeMetierExceptionCode code) {
        super(code);
    }

}
