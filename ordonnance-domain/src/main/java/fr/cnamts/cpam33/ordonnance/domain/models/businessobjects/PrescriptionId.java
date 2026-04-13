package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.PrescrptionExceptionCode;

public record PrescriptionId (
        String numero
){

    public PrescriptionId {
        if ( numero == null || numero.isEmpty() ) {
            throw new DomainException(PrescrptionExceptionCode.BS_PRESCRIPTION_ID_MISSING);
        }
    }

}
