package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceContext;

public interface TracePort {

    void trace(ActeMetierCode acteMetierCode, UtilisateurId utilisateurId, TraceContext traceContext);

    void trace(ActeMetier acteMetier, UtilisateurId utilisateurId, TraceContext traceContext);

}
