package fr.cnamts.cpam33.ordonnance.domain.models.events;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.Command;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;

public interface TraceCommand extends Command {

    MedecinId medecinId();

}
