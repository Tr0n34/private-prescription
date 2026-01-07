package fr.cnamts.cpam33.ordonnance.infrastructure.exceptions;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.InfraStructureExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.InfrastructureException;

public class CesPatientInvalidException extends InfrastructureException {

    public CesPatientInvalidException(InfraStructureExceptionCode code) {
        super(code);
    }

}
