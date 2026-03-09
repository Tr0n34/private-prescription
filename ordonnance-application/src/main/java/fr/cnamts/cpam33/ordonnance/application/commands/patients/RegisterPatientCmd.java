package fr.cnamts.cpam33.ordonnance.application.commands.patients;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.traces.AutoTraceData;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.traces.TraceMask;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.traces.TraceMaskMode;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.traces.Traceable;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.validation.Validator;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;

import java.time.LocalDate;

@AutoTraceData
public record RegisterPatientCmd(
        ExternalPatientId externalPatientId,
        @TraceMask(mode = TraceMaskMode.PREFIX, keep = 1) Nom nom,
        Prenom prenom,
        LocalDate dateNaissance,
        UtilisateurId utilisateurId
) implements Command, Traceable, ValidatableCommand {

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
