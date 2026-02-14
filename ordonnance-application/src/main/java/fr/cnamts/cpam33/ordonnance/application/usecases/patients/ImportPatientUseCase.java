package fr.cnamts.cpam33.ordonnance.application.usecases.patients;

import fr.cnamts.cpam33.ordonnance.application.abstracts.CommandUseCase;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.CommandValidation;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.ImportPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.policies.PatientPolicies;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.patients.ImportPatientPort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.FetchPatientGateway;
import org.springframework.stereotype.Service;

@Service
public class ImportPatientUseCase implements ImportPatientPort, CommandUseCase<ImportPatientCmd, Patient> {

    private final FetchPatientGateway fetchPatientGateway;

    public ImportPatientUseCase(FetchPatientGateway fetchPatientGateway) {
        this.fetchPatientGateway = fetchPatientGateway;
    }

    @Override
    public Patient importerPatient(ExternalPatientId externalPatientId) {
        return execute(new ImportPatientCmd(externalPatientId));
    }

    /**
     * Gérer un mapper, Gérer le mapping d'un objet externe sur le service d'import etc.
     * @param command
     * @return
     * @throws DomainException
     */
    @Override
    public Patient execute(ImportPatientCmd command) throws DomainException {
        CommandValidation.ensureValid(command);
        Patient patient = fetchPatientGateway.fetchById(command.externalPatientId());
        PatientPolicies.forImport().enforce(patient);
        return patient;
    }

}
