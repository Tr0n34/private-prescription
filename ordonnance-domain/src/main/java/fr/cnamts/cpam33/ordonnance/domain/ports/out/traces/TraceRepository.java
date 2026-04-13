package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.TraceId;

import java.util.Optional;

public interface TraceRepository extends RepositoryPort<Trace, TraceId> {

    Optional<Trace> findByUtilisateurId(UtilisateurId utilisateurId);

    Optional<Trace> findByTraceId(TraceId traceId);

    Optional<Trace> findByActeMetierId(ActeMetierId acteMetierId);

}
