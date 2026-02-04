package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PrescrptionExceptionCode;

import java.util.Collections;
import java.util.List;

public record LignePrescription(
        PrescriptionId prescriptionId,
        List<Medicament> medicaments
) implements DomainObject {

    public LignePrescription {
        if ( medicaments == null ) {
            throw new DomainException(PrescrptionExceptionCode.BS_MEDICAMENT_MISSING);
        }
        medicaments = Collections.unmodifiableList(medicaments);
    }

    public static LignePrescription of(PrescriptionId prescriptionId) {
        return new LignePrescription(prescriptionId, List.of());
    }

    public static LignePrescription of(PrescriptionId prescriptionId, List<Medicament> medicament) {
        return new LignePrescription(prescriptionId, medicament);
    }

}
