package fr.cnamts.cpam33.ordonnance.application.commands.patients;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.Traceable;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.Validator;
import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.ExternalPatientId;

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
