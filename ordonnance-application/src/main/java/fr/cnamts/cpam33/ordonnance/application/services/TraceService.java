package fr.cnamts.cpam33.ordonnance.application.services;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ArchiverTracePort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "INTERNAL_QUEUEING")
public class TraceService implements ArchiverTracePort, TracePort {

    private final Clock clock;
    private final TracePublisher tracePublisher;

    public TraceService(Clock clock,
                        TracePublisher tracePublisher) {
        this.clock = clock;
        this.tracePublisher = tracePublisher;
    }

    @Override
    public void trace(ActeMetierCode acteMetierCode, UtilisateurId utilisateurId, String boundedContext, TraceContext traceContext) {
        tracePublisher.publish(Trace.of(acteMetierCode, utilisateurId, boundedContext, traceContext, LocalDateTime.now(clock), clock));
    }

    @Override
    public void trace(ActeMetier acteMetier, UtilisateurId utilisateurId, String boundedContext, TraceContext traceContext) {
        tracePublisher.publish(Trace.of(acteMetier.acteMetierId(), utilisateurId, boundedContext, traceContext, LocalDateTime.now(clock), clock));
    }

    @Override
    public void archiver(TraceId traceId) {
        throw new UnsupportedOperationException("not supported yet");
    }

    @Override
    public void archiver(List<TraceId> traceIds) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void archiver(LocalDate before) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
