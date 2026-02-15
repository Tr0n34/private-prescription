package fr.cnamts.cpam33.ordonnance.application.usecases.patients;

import fr.cnamts.cpam33.ordonnance.application.abstracts.CommandUseCase;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.enums.ActeMetierCode;
import fr.cnamts.cpam33.ordonnance.domain.kernel.events.ActeMetierEvent;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.CommandValidation;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.patients.ImportPatientCmd;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.ImportPatientCandidate;
import fr.cnamts.cpam33.ordonnance.domain.policies.PatientPolicies;
import fr.cnamts.cpam33.ordonnance.domain.ports.in.patients.ImportPatientPort;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.FetchPatientGateway;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientNumGenerator;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientRepository;
import org.springframework.stereotype.Service;

@Service
@ActeMetierEvent(ActeMetierCode.PATIENT_IMPORTER)
public class ImportPatientUseCase implements ImportPatientPort, CommandUseCase<ImportPatientCmd, Patient> {

    private final FetchPatientGateway fetchPatientGateway;
    private final PatientNumGenerator patientNumGenerator;
    private final PatientRepository patientRepository;

    public ImportPatientUseCase(FetchPatientGateway fetchPatientGateway,
                                PatientNumGenerator patientNumGenerator,
                                PatientRepository patientRepository) {
        this.fetchPatientGateway = fetchPatientGateway;
        this.patientNumGenerator = patientNumGenerator;
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient importerPatient(ExternalPatientId externalPatientId) {
        return execute(new ImportPatientCmd(externalPatientId));
    }

    @Override
    public Patient execute(ImportPatientCmd command) throws DomainException {
        CommandValidation.ensureValid(command);
        ImportPatientCandidate patientCandidate = fetchPatientGateway.fetchById(command.externalPatientId());
        Patient patient = Patient.of(
                new PatientId(patientNumGenerator.generate()),
                patientCandidate.externalPatientId(),
                patientCandidate.nom(),
                patientCandidate.prenom(),
                patientCandidate.dateNaissance()
        );
        PatientPolicies.forImport().enforce(patient);
        patientRepository.save(patient);
        return patient;
    }

}
