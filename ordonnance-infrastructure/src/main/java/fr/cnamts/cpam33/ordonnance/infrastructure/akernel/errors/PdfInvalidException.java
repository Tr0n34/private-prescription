package fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.enums.InfraStructureExceptionCode;

import java.util.Map;

public class PdfInvalidException extends InfrastructureException {

    public PdfInvalidException(InfraStructureExceptionCode code) {
        super(code);
    }

    public PdfInvalidException(InfraStructureExceptionCode code, Map<String, Object> placeHolders) {
        super(code, placeHolders);
    }

}
