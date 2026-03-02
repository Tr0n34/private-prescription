package fr.cnamts.cpam33.ordonnance.application.commands.ordonnances;

import fr.cnamts.cpam33.ordonnance.application.commands.CommandValidation;
import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.Validator;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Traitement;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.PatientId;

import java.util.List;

public record CreateOrdonnanceCmd(
        PatientId patientId,
        MedecinId medecinId,
        List<Traitement> traitements
) implements Command, ValidatableCommand {

    public CreateOrdonnanceCmd {
        CommandValidation.failFast(
                CreateOrdonnanceCmd.class.getSimpleName(),
                validateSelf(patientId, medecinId, traitements)
        );
    }

    @Override
    public ValidationResult validate() {
        return validateSelf(patientId, medecinId, traitements);
    }

    public static ValidationResult validateSelf(
            PatientId patientId,
            MedecinId medecinId,
            List<Traitement> traitements) {
        return new Validator()
                .notNull(patientId, "patientId")
                .notNull(medecinId, "medecinId")
                .notNull(traitements, "traitements")
                .validate();
    }

}
