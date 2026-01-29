package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObjectId;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PatientExceptionCode;

public record PatientId(
        String externalId
) implements DomainObjectId {

    public PatientId {
        checkId(externalId);
    }

    public static void checkId(String id)  {
        if ( id == null || id.isBlank() ) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_INVALID);
        }
    }

}
