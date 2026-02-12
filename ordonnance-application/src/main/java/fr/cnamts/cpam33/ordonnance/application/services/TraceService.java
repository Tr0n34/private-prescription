package fr.cnamts.cpam33.ordonnance.application.services;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.documents.DocumentStoragePort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.ArchiverTracePort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TraceRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TraceService implements ArchiverTracePort, TracePort {

    private final Clock clock;
    private final TracePublisher tracePublisher;
    private final DocumentStoragePort documentStoragePort;
    private final TraceRepository traceRepository;

    public TraceService(Clock clock, TracePublisher tracePublisher, DocumentStoragePort documentStoragePort, TraceRepository traceRepository) {
        this.clock = clock;
        this.tracePublisher = tracePublisher;
        this.documentStoragePort = documentStoragePort;
        this.traceRepository = traceRepository;
    }

    @Override
    public void trace(ActeMetierCode acteMetierCode, UtilisateurId utilisateurId, TraceContext traceContext) {
        tracePublisher.publish(Trace.of(acteMetierCode, utilisateurId, traceContext, LocalDateTime.now(clock), clock));
    }

    @Override
    public void trace(ActeMetier acteMetier, UtilisateurId utilisateurId, TraceContext traceContext) {
        tracePublisher.publish(Trace.of(acteMetier.acteMetierId(), utilisateurId, traceContext, LocalDateTime.now(clock), clock));
    }

    @Override
    public void archiver(TraceId traceId) {
        traceRepository.findByTraceId(traceId).ifPresent(documentStoragePort::store);
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
