package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.kernel.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.ActeMetierId;

public interface ActeMetierRepository extends RepositoryPort<ActeMetier, ActeMetierId> {


}
