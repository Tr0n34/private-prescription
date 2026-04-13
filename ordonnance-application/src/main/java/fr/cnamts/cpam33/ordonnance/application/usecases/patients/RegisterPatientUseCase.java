package fr.cnamts.cpam33.ordonnance.application.usecases.patients;

import fr.cnamts.cpam33.ordonnance.application.abstracts.CommandUseCase;
import fr.cnamts.cpam33.ordonnance.application.views.PatientView;
import fr.cnamts.cpam33.ordonnance.application.views.mappers.PatientViewMapper;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.application.commands.CommandValidation;
import fr.cnamts.cpam33.ordonnance.application.commands.patients.RegisterPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.policies.PatientPolicies;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientNumGenerator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import org.springframework.stereotype.Service;

@Service
@ActeMetierEvent(ActeMetierCode.PATIENT_CREER)
public class RegisterPatientUseCase implements CommandUseCase<RegisterPatientCmd, PatientView> {

    private final PatientRepository patientRepository;
    private final PatientNumGenerator patientNumGenerator;
    private final PatientViewMapper  patientViewMapper;

    public RegisterPatientUseCase(PatientRepository patientRepository,
                                  PatientNumGenerator patientNumGenerator,
                                  PatientViewMapper patientViewMapper) {
        this.patientRepository = patientRepository;
        this.patientNumGenerator = patientNumGenerator;
        this.patientViewMapper = patientViewMapper;
    }

    public PatientView registerPatient(RegisterPatientCmd registerPatientCmd) {
        return execute(registerPatientCmd);
    }

    public PatientView execute(RegisterPatientCmd command) throws DomainException {
        CommandValidation.ensureValid(command);
        Patient newPatient = Patient.of(
                new PatientId(patientNumGenerator.generate()),
                command.externalPatientId(),
                command.nom(),
                command.prenom(),
                command.dateNaissance()
        );
        PatientPolicies.forCreate().enforce(newPatient);
        return patientViewMapper.toView(patientRepository.save(newPatient));
    }

}
