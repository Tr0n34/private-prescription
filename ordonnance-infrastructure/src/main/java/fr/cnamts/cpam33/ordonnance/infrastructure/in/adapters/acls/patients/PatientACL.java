package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.acls.patients;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.RegisterPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientNumGenerator;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.CesPatientInvalidException;
import fr.cnamts.cpam33.ordonnance.infrastructure.exceptions.enums.CesPatientExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients.CesPatientDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.PatientDto;
import org.springframework.stereotype.Component;

@Component
public class PatientACL {

    private final PatientNumGenerator patientNumGenerator;

    public PatientACL(PatientNumGenerator patientNumGenerator) {
        this.patientNumGenerator = patientNumGenerator;
    }

    public RegisterPatientCmd toDomain(PatientDto dto, String userId) {
        return new RegisterPatientCmd(
                new ExternalPatientId(dto.externalId()),
                new Nom(dto.nom()),
                new Prenom(dto.prenom()),
                dto.dateNaissance(),
                new UtilisateurId(userId)

        );
    }

    public Patient toDomain(CesPatientDto dto) {
        validate(dto);
        return new Patient(
                new PatientId(patientNumGenerator.generate()),
                new ExternalPatientId(dto.externalId()),
                new Nom(dto.nom()),
                new Prenom(dto.prenom()),
                dto.dateNaissance()
        );
    }

    private void validate(CesPatientDto dto) {
        if ( !dto.externalId().matches("\\d{13}")) {
            throw new CesPatientInvalidException(CesPatientExceptionCode.TECH_CES_PATIENT_ID_NOT_LENGTH);
        }
        if ( !dto.externalId().substring(0, 3).equals(dto.cesRattachement()) ) {
            throw new CesPatientInvalidException(CesPatientExceptionCode.TECH_CES_PATIENT_ID_CES_RATTACHEMENT_INVALID);
        }
    }

}
