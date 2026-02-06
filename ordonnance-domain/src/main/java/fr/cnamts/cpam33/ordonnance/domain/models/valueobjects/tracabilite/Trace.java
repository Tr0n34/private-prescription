package fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite;

import com.google.common.base.MoreObjects;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.enums.TraceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.enums.ActeMetierCode;

import java.time.Clock;
import java.time.LocalDateTime;

public record Trace(
        ActeMetierId acteMetierId,
        UtilisateurId utilisateurId,
        LocalDateTime timestamp,
        TraceContext context
) implements DomainObject {

    public Trace {
        if ( acteMetierId == null ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_ACTE_METIER_MISSING);
        }
        if ( utilisateurId == null ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_MEDECIN_MISSING);
        }
        if ( timestamp == null ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_TIMESTAMP_MISSING);
        }
    }

    public static Trace of(ActeMetierId acteMetierId, UtilisateurId utilisateurId, TraceContext traceContext, LocalDateTime timestamp, Clock clock) {
        Trace trace = new Trace(acteMetierId, utilisateurId, timestamp, traceContext);
        if ( timestamp.isAfter(LocalDateTime.now(clock)) ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_TIMESTAMP_INVALID);
        }
        return trace;
    }

    public static Trace of(ActeMetierCode acteMetierCode, UtilisateurId utilisateurId, TraceContext traceContext, LocalDateTime timestamp, Clock clock) {
        Trace trace = new Trace(new ActeMetierId(acteMetierCode.name()), utilisateurId, timestamp, traceContext);
        if ( timestamp.isAfter(LocalDateTime.now(clock)) ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_TIMESTAMP_INVALID);
        }
        return trace;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("acteMetierId", acteMetierId)
                .add("utilisateurId", utilisateurId)
                .add("timestamp", timestamp)
                .add("context", context)
                .toString();
    }

}
