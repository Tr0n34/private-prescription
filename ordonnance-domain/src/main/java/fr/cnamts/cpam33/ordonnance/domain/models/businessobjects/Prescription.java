package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.PrescriptionInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PrescrptionExceptionCode;

public record Prescription(
        PrescriptionId prescriptionId,
        Medicament medicament,
        Posologie posologie
) implements DomainObject {

    public Prescription {
        if ( medicament == null ) {
            throw new PrescriptionInvalidException(PrescrptionExceptionCode.BS_MEDICAMENT_MISSING);
        }
        if ( posologie == null ) {
            throw new PrescriptionInvalidException(PrescrptionExceptionCode.BS_POSOLOGIE_MISSING);
        }
    }

    public static Prescription of(PrescriptionId prescriptionId, Medicament medicament, Posologie posologie) {
        return new Prescription(prescriptionId, medicament, posologie);
    }

}
