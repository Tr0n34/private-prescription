package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.patients;

import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.ImportPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.RegisterPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.CesPatientInvalidException;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.enums.CesPatientExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients.PatientDto;
import org.springframework.stereotype.Component;

@Component
public class PatientACL {

    public RegisterPatientCmd toDomain(PatientDto dto, String userId) {
        return new RegisterPatientCmd(
                new ExternalPatientId(dto.externalId()),
                new Nom(dto.nom()),
                new Prenom(dto.prenom()),
                dto.dateNaissance(),
                new UtilisateurId(userId)

        );
    }

    public ImportPatientCmd toDomain(String externalId, String userId) {
        validate(externalId);
        return new ImportPatientCmd(
                new ExternalPatientId(externalId),
                new UtilisateurId(userId)
        );
    }

    private void validate(String externalId) {
        if ( !externalId.matches("\\d{13}")) {
            throw new CesPatientInvalidException(CesPatientExceptionCode.TECH_CES_PATIENT_ID_NOT_LENGTH);
        }
    }

}
