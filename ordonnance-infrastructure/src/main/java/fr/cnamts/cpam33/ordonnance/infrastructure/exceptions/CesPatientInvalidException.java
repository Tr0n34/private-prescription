package fr.cnamts.cpam33.ordonnance.infrastructure.exceptions;

import fr.cnamts.cpam33.ordonnance.infrastructure.kernel.errors.InfraStructureExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.kernel.errors.InfrastructureException;

public class CesPatientInvalidException extends InfrastructureException {

    public CesPatientInvalidException(InfraStructureExceptionCode code) {
        super(code);
    }

}
