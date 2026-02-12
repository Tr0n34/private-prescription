package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.enums.ActeMetierCode;

public interface TracePort {

    void trace(ActeMetierCode acteMetierCode, UtilisateurId utilisateurId, TraceContext traceContext);

    void trace(ActeMetier acteMetier, UtilisateurId utilisateurId, TraceContext traceContext);

}
