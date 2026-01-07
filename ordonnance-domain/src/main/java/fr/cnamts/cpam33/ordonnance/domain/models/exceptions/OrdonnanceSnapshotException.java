package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.ExceptionCode;

public class OrdonnanceSnapshotException extends DomainException {

    public OrdonnanceSnapshotException(ExceptionCode code) {
        super(code);
    }

}
