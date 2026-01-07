package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObjectId;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.PatientInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.PatientExceptionCode;

public record PatientId(
        String externalId
) implements DomainObjectId {

    public PatientId {
        checkId(externalId);
    }

    public static void checkId(String id)  {
        if ( id == null || id.isBlank() ) {
            throw new PatientInvalidException(PatientExceptionCode.BS_PATIENT_INVALID);
        }
    }

}
