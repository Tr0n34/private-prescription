package fr.cnamts.cpam33.ordonnance.domain.models.commands.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.Validator;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Traitement;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.CommandValidation;

import java.util.List;

public record CreateOrdonnanceCmd(
        PatientId patientId,
        MedecinId medecinId,
        List<Traitement> traitements
) implements Command, ValidatableCommand {

    public CreateOrdonnanceCmd {
        CommandValidation.failFast(
                getClass().getSimpleName(),
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
