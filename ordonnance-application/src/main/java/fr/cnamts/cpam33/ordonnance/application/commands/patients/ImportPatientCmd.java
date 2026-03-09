package fr.cnamts.cpam33.ordonnance.application.commands.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.traces.Traceable;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.Validator;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.ExternalPatientId;

public record ImportPatientCmd(
        ExternalPatientId externalPatientId,
        UtilisateurId utilisateurId
) implements Command, ValidatableCommand, Traceable {

    @Override
    public ValidationResult validate() {
        Validator validator = new Validator();
        validator.notNull(externalPatientId, "externalPatientId");
        if ( externalPatientId != null ) {
            validator.notBlank(externalPatientId.numero(), "externalPatientId.numero");
        }
        return validator.validate();
    }

}
