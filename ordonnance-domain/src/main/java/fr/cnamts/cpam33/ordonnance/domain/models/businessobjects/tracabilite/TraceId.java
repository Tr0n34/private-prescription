package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObjectId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.enums.TraceExceptionCode;

public record TraceId(
        String numero
) implements DomainObjectId {

    public TraceId {
        if ( numero == null || numero.isEmpty() ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_ID_MALFORMED);
        }
    }

}
