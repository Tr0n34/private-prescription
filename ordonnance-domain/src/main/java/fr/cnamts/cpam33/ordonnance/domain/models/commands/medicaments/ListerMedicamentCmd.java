package fr.cnamts.cpam33.ordonnance.domain.models.commands.medicaments;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;

public record ListerMedicamentCmd(
        String nom,
        UtilisateurId utilisateurId
) implements Command {

}
