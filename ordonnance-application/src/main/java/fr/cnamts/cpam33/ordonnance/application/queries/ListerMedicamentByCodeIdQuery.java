package fr.cnamts.cpam33.ordonnance.application.queries;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Query;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.traces.Traceable;

public record ListerMedicamentByCodeIdQuery(
    String codeSp,
    String varType,
    UtilisateurId utilisateurId
) implements Query, Traceable {

}
