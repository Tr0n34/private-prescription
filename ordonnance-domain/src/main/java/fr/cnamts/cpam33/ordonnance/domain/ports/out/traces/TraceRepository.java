package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.kernel.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceId;

import java.util.Optional;

public interface TraceRepository extends RepositoryPort<Trace, TraceId> {

    Optional<Trace> findByUtilisateurId(UtilisateurId utilisateurId);

    Optional<Trace> findByTraceId(TraceId traceId);

    Optional<Trace> findByActeMetierId(ActeMetierId acteMetierId);

}
