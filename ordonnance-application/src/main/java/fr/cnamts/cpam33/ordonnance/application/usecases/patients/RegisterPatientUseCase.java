package fr.cnamts.cpam33.ordonnance.application.usecases.patients;

import fr.cnamts.cpam33.ordonnance.application.abstracts.CommandUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.RegisterPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.patients.RegisterPatientPort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientNumGenerator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import org.springframework.stereotype.Service;

@Service
@ActeMetierEvent(ActeMetierCode.ACT_PATIENT_CREER)
public class RegisterPatientUseCase implements RegisterPatientPort, CommandUseCase<RegisterPatientCmd, Patient> {

    private final PatientRepository patientRepository;
    private final PatientNumGenerator patientNumGenerator;

    public RegisterPatientUseCase(PatientRepository patientRepository,
                                  PatientNumGenerator patientNumGenerator) {
        this.patientRepository = patientRepository;
        this.patientNumGenerator = patientNumGenerator;
    }


    public Patient registerPatient(RegisterPatientCmd registerPatientCmd) {
        return execute(registerPatientCmd);
    }

    @Override
    public Patient execute(RegisterPatientCmd command) throws DomainException {
        Patient patientToRegister = Patient.of(
                new PatientId(patientNumGenerator.generate()),
                command.externalPatientId(),
                command.nom(),
                command.prenom(),
                command.dateNaissance()
        );
        return patientRepository.findByExternalId(command.externalPatientId())
                .orElseGet(() -> patientRepository.save(patientToRegister));
    }

}
