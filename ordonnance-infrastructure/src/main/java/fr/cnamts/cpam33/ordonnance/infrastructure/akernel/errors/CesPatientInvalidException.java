package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.enums.InfraStructureExceptionCode;

public class CesPatientInvalidException extends InfrastructureException {

    public CesPatientInvalidException(InfraStructureExceptionCode code) {
        super(code);
    }

}
