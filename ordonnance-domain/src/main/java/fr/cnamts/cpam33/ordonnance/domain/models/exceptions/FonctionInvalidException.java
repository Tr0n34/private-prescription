package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.FonctionExceptionCode;

public class FonctionInvalidException extends DomainException {

    public FonctionInvalidException(FonctionExceptionCode code) {
        super(code);
    }

}
