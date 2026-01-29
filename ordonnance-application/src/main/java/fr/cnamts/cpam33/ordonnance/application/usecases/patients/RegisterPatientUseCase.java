package fr.cnamts.cpam33.ordonnance.application.usecases.patients;

import fr.cnamts.cpam33.ordonnance.application.abstracts.CommandUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.RegisterPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.patients.RegisterPatientPort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterPatientUseCase implements RegisterPatientPort, CommandUseCase<RegisterPatientCmd, Patient> {

    private final PatientRepository patientRepository;

    public RegisterPatientUseCase(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient registerPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient registerPatient(RegisterPatientCmd registerPatientCmd) {
        return execute(registerPatientCmd);
    }

    @Override
    public Patient execute(RegisterPatientCmd command) throws DomainException {
        Patient patientToRegister = Patient.of(
                command.patientId(),
                command.nom(),
                command.prenom(),
                command.dateNaissance()
        );
        return patientRepository.save(patientToRegister);
    }

}
