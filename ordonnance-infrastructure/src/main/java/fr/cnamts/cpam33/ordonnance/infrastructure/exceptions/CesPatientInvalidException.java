package fr.cnamts.cpam33.ordonnance.infrastructure.exceptions;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfraStructureExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;

public class CesPatientInvalidException extends InfrastructureException {

    public CesPatientInvalidException(InfraStructureExceptionCode code) {
        super(code);
    }

}
