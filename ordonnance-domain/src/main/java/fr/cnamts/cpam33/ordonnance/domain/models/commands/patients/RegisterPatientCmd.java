package fr.cnamts.cpam33.ordonnance.domain.models.commands.patients;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.TraceCommand;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.Validator;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;

import java.time.LocalDate;

public record RegisterPatientCmd(
        ExternalPatientId externalPatientId,
        Nom nom,
        Prenom prenom,
        LocalDate dateNaissance,
        UtilisateurId utilisateurId
) implements Command, TraceCommand, ValidatableCommand {

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
