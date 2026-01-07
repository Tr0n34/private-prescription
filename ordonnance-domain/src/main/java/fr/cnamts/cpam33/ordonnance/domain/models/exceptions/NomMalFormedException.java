package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.NomExceptionCode;

public class NomMalFormedException extends DomainException {

    public NomMalFormedException(NomExceptionCode code) {
        super(code);
    }

}
