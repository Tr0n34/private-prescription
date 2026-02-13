package fr.cnamts.cpam33.ordonnance.domain.models.commands.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.Validator;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.CommandValidation;
import fr.cnamts.cpam33.ordonnance.domain.models.events.TraceCommand;
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
