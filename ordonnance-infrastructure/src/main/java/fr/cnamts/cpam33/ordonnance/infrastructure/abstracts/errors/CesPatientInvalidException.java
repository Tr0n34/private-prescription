package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.enums.InfraStructureExceptionCode;

public class CesPatientInvalidException extends InfrastructureException {

    public CesPatientInvalidException(InfraStructureExceptionCode code) {
        super(code);
    }

}
