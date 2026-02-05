package fr.cnamts.cpam33.ordonnance.domain.models.commands.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.Validator;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.CommandValidation;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;

public record ImportPatientCmd(
        ExternalPatientId externalPatientId
) implements Command, ValidatableCommand {

    public ImportPatientCmd {
        CommandValidation.failFast(
                getClass().getSimpleName(),
                validateSelf(externalPatientId)
        );
    }

    @Override
    public ValidationResult validate() {
        return validateSelf(externalPatientId);
    }

    public ValidationResult validateSelf(ExternalPatientId externalPatientId) {
        return new Validator()
                .notBlank(externalPatientId.numero(), "externalPatientId.numero")
                .validate();
    }

}
