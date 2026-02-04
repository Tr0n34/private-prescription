package fr.cnamts.cpam33.ordonnance.infrastructure.exceptions;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfraStructureExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors.InfrastructureException;

import java.util.Map;

public class PdfInvalidException extends InfrastructureException {

    public PdfInvalidException(InfraStructureExceptionCode code) {
        super(code);
    }

    public PdfInvalidException(InfraStructureExceptionCode code, Map<String, ?> placeHolders) {
        super(code, placeHolders);
    }

}
