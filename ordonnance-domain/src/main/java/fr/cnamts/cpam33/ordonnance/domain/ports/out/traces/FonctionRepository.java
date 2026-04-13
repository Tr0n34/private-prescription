package fr.cnamts.cpam33.ordonnance.domain.ports.out.traces;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Fonction;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.FonctionId;

public interface FonctionRepository extends RepositoryPort<Fonction, FonctionId> {

}
