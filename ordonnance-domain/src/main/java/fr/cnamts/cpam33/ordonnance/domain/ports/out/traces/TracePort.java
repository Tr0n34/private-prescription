package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.*;

import java.time.Clock;
import java.time.Instant;

public interface TracePort {

    void trace(
            CorrelationId correlationId,
            ActeMetierId acteMetierId,
            FonctionId fonctionId,
            UtilisateurId utilisateurId,
            String boundedContext,
            Ecran ecran,
            TraceContext traceContext,
            Instant createdOn
    );

}
