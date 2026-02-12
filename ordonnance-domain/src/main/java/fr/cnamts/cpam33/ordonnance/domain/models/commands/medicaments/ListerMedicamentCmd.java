package fr.cnamts.cpam33.ordonnance.domain.models.commands.medicaments;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;

public record ListerMedicamentCmd(
        String nom,
        UtilisateurId utilisateurId
) implements Command {

}
