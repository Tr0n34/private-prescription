package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.TraceInvalidException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.TraceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;

import java.time.Clock;
import java.time.LocalDateTime;

public record Trace(
        ActeMetier acteMetier,
        MedecinId medecinId,
        LocalDateTime timestamp
) implements DomainObject {

    public Trace {
        if ( acteMetier == null ) {
            throw new TraceInvalidException(TraceExceptionCode.BS_TRACE_ACTE_METIER_MISSING);
        }
        if ( medecinId == null ) {
            throw new TraceInvalidException(TraceExceptionCode.BS_TRACE_MEDECIN_MISSING);
        }
        if ( timestamp == null ) {
            throw new TraceInvalidException(TraceExceptionCode.BS_TRACE_TIMESTAMP_MISSING);
        }
    }
    public static Trace of(ActeMetier acteMetier, MedecinId medecinId, LocalDateTime timestamp, Clock clock) {
        Trace trace = new Trace(acteMetier, medecinId, timestamp);
        if ( timestamp.isAfter(LocalDateTime.now(clock)) ) {
            throw new TraceInvalidException(TraceExceptionCode.BS_TRACE_TIMESTAMP_INVALID);
        }
        return trace;
    }

}
