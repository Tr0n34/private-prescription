package fr.cnamts.cpam33.ordonnance.domain.ports.out.medecins;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.RepositoryPort;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.Medecin;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;

public interface MedecinRepository extends RepositoryPort<Medecin, MedecinId> {

}
