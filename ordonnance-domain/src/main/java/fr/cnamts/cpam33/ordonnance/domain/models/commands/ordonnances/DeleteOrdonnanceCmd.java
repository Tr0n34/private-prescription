package fr.cnamts.cpam33.ordonnance.domain.models.commands.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidationResult;

public record DeleteOrdonnanceCmd(
        OrdonnanceId ordonnanceId,
        MedecinId medecinId
) implements Command, ValidatableCommand {

    @Override
    public ValidationResult validate() {
        return ValidationResult.ok();
    }

}
