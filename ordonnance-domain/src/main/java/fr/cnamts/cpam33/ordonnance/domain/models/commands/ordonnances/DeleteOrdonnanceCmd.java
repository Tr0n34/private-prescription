package fr.cnamts.cpam33.ordonnance.domain.models.commands.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.OrdonnanceId;

public record DeleteOrdonnanceCmd(
        OrdonnanceId ordonnanceId,
        MedecinId medecinId
) implements Command, ValidatableCommand {

    @Override
    public ValidationResult validate() {
        return ValidationResult.ok();
    }

}
