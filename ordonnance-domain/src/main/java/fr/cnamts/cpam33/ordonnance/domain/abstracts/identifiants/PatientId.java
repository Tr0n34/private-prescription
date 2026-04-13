package fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObjectId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.PatientExceptionCode;

public record PatientId(
        String numero
) implements DomainObjectId {

    public PatientId {
        checkId(numero);
    }

    public static void checkId(String id)  {
        if ( id == null || id.isBlank() ) {
            throw new DomainException(PatientExceptionCode.BS_PATIENT_INVALID);
        }
    }

}
