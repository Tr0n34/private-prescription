package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PrescrptionExceptionCode;

public class PrescriptionInvalidException extends DomainException {

    public PrescriptionInvalidException(PrescrptionExceptionCode code) {
        super(code);
    }

}
