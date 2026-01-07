package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObjectId;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.TraceInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.TraceExceptionCode;

public record TraceId(
        String numero
) implements DomainObjectId {

    public TraceId {
        if ( numero == null || numero.isEmpty() ) {
            throw new TraceInvalidException(TraceExceptionCode.BS_TRACE_ID_MALFORMED);
        }
    }

}
