package fr.cnamts.cpam33.ordonnance.domain.kernel.events;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;

public interface TraceCommand extends Command {

    UtilisateurId utilisateurId();

}
