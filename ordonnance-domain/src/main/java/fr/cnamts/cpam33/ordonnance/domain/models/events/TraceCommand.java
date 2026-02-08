package fr.cnamts.cpam33.ordonnance.domain.models.events;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;

public interface TraceCommand extends Command {

    UtilisateurId utilisateurId();

}
