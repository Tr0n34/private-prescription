package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects;

import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.PrescriptionInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PrescrptionExceptionCode;

public record PrescriptionId (
        String numero
){

    public PrescriptionId {
        if ( numero == null || numero.isEmpty() ) {
            throw new PrescriptionInvalidException(PrescrptionExceptionCode.BS_PRESCRIPTION_ID_MISSING);
        }
    }

}
