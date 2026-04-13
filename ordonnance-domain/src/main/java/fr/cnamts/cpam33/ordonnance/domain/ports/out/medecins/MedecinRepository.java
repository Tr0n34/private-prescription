package fr.cnamts.cpam33.ordonnance.domain.ports.out.medecins;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.ports.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.Medecin;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;

public interface MedecinRepository extends RepositoryPort<Medecin, MedecinId> {

}
