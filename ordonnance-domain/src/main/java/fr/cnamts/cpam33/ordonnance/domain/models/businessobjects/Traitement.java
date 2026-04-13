package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.PrescrptionExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;

import java.util.Collections;
import java.util.List;

public record Traitement(
        PrescriptionId prescriptionId,
        List<Medicament> medicaments
) implements DomainObject {

    public Traitement {
        if ( medicaments == null ) {
            throw new DomainException(PrescrptionExceptionCode.BS_MEDICAMENT_MISSING);
        }
        medicaments = Collections.unmodifiableList(medicaments);
    }

    public static Traitement of(PrescriptionId prescriptionId) {
        return new Traitement(prescriptionId, List.of());
    }

    public static Traitement of(PrescriptionId prescriptionId, List<Medicament> medicament) {
        return new Traitement(prescriptionId, medicament);
    }

}
