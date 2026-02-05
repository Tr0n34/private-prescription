package fr.cnamts.cpam33.ordonnance.application.usecases.patients;

import fr.cnamts.cpam33.ordonnance.application.abstracts.CommandUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.ImportPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.patients.ImportPatientPort;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.patients.RegisterPatientPort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.FetchPatientGateway;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientNumGenerator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import org.springframework.stereotype.Service;

@Service
public class ImportPatientUseCase implements ImportPatientPort, CommandUseCase<ImportPatientCmd, Patient> {

    private final RegisterPatientPort registerPatientPort;
    private final FetchPatientGateway fetchPatientGateway;
    private final PatientRepository patientRepository;

    public ImportPatientUseCase(RegisterPatientPort registerPatientPort,
                                FetchPatientGateway fetchPatientGateway,
                                PatientRepository patientRepository) {
        this.registerPatientPort = registerPatientPort;
        this.fetchPatientGateway = fetchPatientGateway;
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient importerPatient(ExternalPatientId externalPatientId) {
        return execute(new ImportPatientCmd(externalPatientId));
    }

    @Override
    public Patient execute(ImportPatientCmd command) throws DomainException {
        return patientRepository.findByExternalId(command.externalPatientId())
                .orElseGet(() -> registerPatientPort.registerPatient(fetchPatientGateway.fetchById(command.externalPatientId())
                ));
    }

}
