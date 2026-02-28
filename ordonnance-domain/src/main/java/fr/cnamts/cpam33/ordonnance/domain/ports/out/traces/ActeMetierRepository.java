package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.kernel.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Fonction;

public interface ActeMetierRepository extends RepositoryPort<ActeMetier, ActeMetierId> {

    Fonction findFonctionByActeMetierId(ActeMetierId acteMetierId);

}
