package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.PatientExceptionCode;

public record Maladie(
        String nom
) {

    public Maladie {
        if ( nom == null || nom.isEmpty() ) {
            throw new DomainException(PatientExceptionCode.BS_MALADIE_NOM_IS_MISSING);
        }

    }

}
