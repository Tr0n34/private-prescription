package fr.cnamts.cpam33.ordonnance.application.usecases.patients;

import fr.cnamts.cpam33.ordonnance.application.abstracts.CommandUseCase;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.ImportPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.patients.ImportPatientPort;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.patients.RegisterPatientPort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.FetchPatientGateway;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class ImportPatientUseCase implements ImportPatientPort, CommandUseCase<ImportPatientCmd, Patient> {

    private final RegisterPatientPort registerPatientPort;
    private final FetchPatientGateway fetchPatientGateway;
    private final PatientRepository patientRepository;

    public ImportPatientUseCase(RegisterPatientPort registerPatientPort,
                                FetchPatientGateway fetchPatientGateway, PatientRepository patientRepository) {
        this.registerPatientPort = registerPatientPort;
        this.fetchPatientGateway = fetchPatientGateway;
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient importerPatient(PatientId patientId) {
        return execute(new ImportPatientCmd(patientId));
    }

    @Override
    public Patient execute(ImportPatientCmd command) throws DomainException {
        return patientRepository.findById(command.patientId())
                .orElseGet(() -> registerPatientPort.registerPatient(
                        fetchPatientGateway.fetchById(command.patientId())
                ));
    }

}
