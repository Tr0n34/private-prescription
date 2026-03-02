package fr.cnamts.cpam33.ordonnance.application.queries;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Query;
import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.Traceable;

public record ListerMedicamentByCodeIdQuery(
    String codeSp,
    String varType,
    UtilisateurId utilisateurId
) implements Query, Traceable {

}
