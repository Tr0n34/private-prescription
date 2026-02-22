package fr.cnamts.cpam33.ordonnance.domain.models.queries;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Query;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.Traceable;

public record ListerMedicamentByCodeIdQuery(
    String codeId,
    String varType,
    UtilisateurId utilisateurId
) implements Query, Traceable {

}
