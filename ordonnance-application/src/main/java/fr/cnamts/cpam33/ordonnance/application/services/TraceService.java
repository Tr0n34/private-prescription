package fr.cnamts.cpam33.ordonnance.application.services;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class TraceService {

    private final Clock clock;
    private final TracePublisher tracePublisher;

    public TraceService(Clock clock, TracePublisher tracePublisher) {
        this.clock = clock;
        this.tracePublisher = tracePublisher;
    }

    public void trace(ActeMetierCode acteMetierCode, UtilisateurId utilisateurId, TraceContext traceContext) {
        tracePublisher.publish(Trace.of(acteMetierCode, utilisateurId, traceContext, LocalDateTime.now(clock), clock));
    }

    public void trace(ActeMetier acteMetier, UtilisateurId utilisateurId, TraceContext traceContext) {
        tracePublisher.publish(Trace.of(acteMetier.acteMetierId(), utilisateurId, traceContext, LocalDateTime.now(clock), clock));
    }

}
