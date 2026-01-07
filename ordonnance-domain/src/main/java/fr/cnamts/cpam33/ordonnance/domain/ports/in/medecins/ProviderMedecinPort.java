package fr.cnamts.cpam33.ordonnance.domain.ports.in.medecins;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObjectNotFound;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.Medecin;

public interface ProviderMedecinPort {

    Medecin provideMedecin(Medecin medecin) throws DomainObjectNotFound;

}
