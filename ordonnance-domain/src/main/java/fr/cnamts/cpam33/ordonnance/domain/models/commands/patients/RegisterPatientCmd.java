package fr.cnamts.cpam33.ordonnance.domain.models.commands.patients;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.AutoTraceData;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.TraceMask;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.TraceMaskMode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.traces.Traceable;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.ValidatableCommand;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.ValidationResult;
import fr.cnamts.cpam33.ordonnance.domain.kernel.validation.Validator;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
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
