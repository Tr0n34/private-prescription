package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceId;

import java.util.Optional;

public interface TraceRepository extends RepositoryPort<Trace, TraceId> {

    Optional<Trace> findByUtilisateurId(UtilisateurId utilisateurId);

    Optional<Trace> findByTraceId(TraceId traceId);

    Optional<Trace> findByActeMetierId(ActeMetierId acteMetierId);

}
