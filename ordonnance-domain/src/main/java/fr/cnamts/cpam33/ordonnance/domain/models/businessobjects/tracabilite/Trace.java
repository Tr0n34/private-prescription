package fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.Document;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.enums.TraceExceptionCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.UtilisateurId;

import java.time.Clock;
import java.time.Instant;

public record Trace(
        TraceId traceId,
        CorrelationId correlationId,
        ActeMetierId acteMetierId,
        FonctionId fonctionId,
        UtilisateurId utilisateurId,
        String boundedContext,
        Ecran ecran,
        Instant createdOn,
        TraceContext context
) implements DomainObject, Document {

    public Trace {
        if ( acteMetierId == null ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_ACTE_METIER_MISSING);
        }
        if ( utilisateurId == null ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_MEDECIN_MISSING);
        }
        if ( createdOn == null ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_TIMESTAMP_MISSING);
        }
    }

    public static Trace of(
            TraceId traceId,
            CorrelationId correlationId,
            ActeMetierId acteMetierId,
            FonctionId fonctionId,
            UtilisateurId utilisateurId,
            String boundedContext,
            Ecran ecran,
            TraceContext traceContext,
            Instant createdOn,
            Clock clock) {
        Trace trace = new Trace(
                traceId,
                correlationId,
                acteMetierId,
                fonctionId,
                utilisateurId,
                boundedContext,
                ecran,
                createdOn,
                traceContext);
        if ( createdOn.isAfter(Instant.now(clock)) ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_TIMESTAMP_INVALID);
        }
        return trace;
    }

    public static Trace of(
            TraceId traceId,
            CorrelationId correlationId,
            ActeMetierCode acteMetierCode,
            FonctionId fonctionId,
            UtilisateurId utilisateurId,
            String boundedContext,
            Ecran ecran,
            TraceContext traceContext,
            Instant createdOn,
            Clock clock
    ) {
        Trace trace = new Trace(
                traceId,
                correlationId,
                new ActeMetierId(acteMetierCode.name()),
                fonctionId,
                utilisateurId,
                boundedContext,
                ecran,
                createdOn,
                traceContext
        );
        if ( createdOn.isAfter(Instant.now(clock)) ) {
            throw new DomainException(TraceExceptionCode.BS_TRACE_TIMESTAMP_INVALID);
        }
        return trace;
    }

    @Override
    public String toString() {
        return "Trace{" +
                "traceId=" + traceId +
                ", correlationId=" + correlationId +
                ", acteMetierId=" + acteMetierId +
                ", fonctionId=" + fonctionId +
                ", utilisateurId=" + utilisateurId +
                ", boundedContext=" + boundedContext +
                ", ecran=" + ecran +
                ", createdOn=" + createdOn +
                ", context=" + context +
                '}';
    }

}
