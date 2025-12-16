package fr.cnamts.cpam33.ordonnance.domain.ports.externals;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.IdentiteMedecin;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.MedecinId;

import java.util.List;

public interface MedecinInfoPort {

    IdentiteMedecin fetchMedecin(MedecinId medecinId);

    List<IdentiteMedecin> fetchMedecins(List<MedecinId> medecinIds);

}
