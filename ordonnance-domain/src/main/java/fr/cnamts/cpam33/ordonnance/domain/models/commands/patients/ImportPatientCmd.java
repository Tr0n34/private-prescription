package fr.cnamts.cpam33.ordonnance.domain.models.commands.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.Validator;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.CommandValidation;

public record ImportPatientCmd(
        PatientId patientId,
        ExternalPatientId externalPatientId
) implements Command, ValidatableCommand {

    public ImportPatientCmd {
        CommandValidation.failFast(
                getClass().getSimpleName(),
                validateSelf(patientId)
        );
    }

    @Override
    public ValidationResult validate() {
        return validateSelf(patientId);
    }

    public ValidationResult validateSelf(PatientId patientId) {
        return new Validator()
                .notNull(patientId, "patientId")
                .validate();
    }

}
