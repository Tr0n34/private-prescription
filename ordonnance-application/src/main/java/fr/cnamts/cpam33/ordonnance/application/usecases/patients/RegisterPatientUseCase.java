package fr.cnamts.cpam33.ordonnance.application.usecases.patients;

import fr.cnamts.cpam33.ordonnance.application.abstracts.CommandUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.RegisterPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.patients.RegisterPatientPort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientNumGenerator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisterPatientUseCase implements RegisterPatientPort, CommandUseCase<RegisterPatientCmd, Patient> {

    private final PatientRepository patientRepository;
    private final PatientNumGenerator patientNumGenerator;

    public RegisterPatientUseCase(PatientRepository patientRepository,
                                  PatientNumGenerator patientNumGenerator) {
        this.patientRepository = patientRepository;
        this.patientNumGenerator = patientNumGenerator;
    }

    @Override
    public Patient registerPatient(Patient patient) {
        Patient patientWithNum = Patient.of(
                new PatientId(patientNumGenerator.generate()),
                patient.externalPatientId(),
                patient.nom(),
                patient.prenom(),
                patient.dateNaissance()
        );
        return patientRepository.save(patientWithNum);
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
