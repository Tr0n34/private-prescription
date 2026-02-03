package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.ImportPatientApiClient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalPatientPersistenceAdapter implements Adapter {

    ImportPatientApiClient importPatientApiClient;

    public ExternalPatientPersistenceAdapter(ImportPatientApiClient importPatientApiClient) {
        this.importPatientApiClient = importPatientApiClient;
    }

    public Patient fetchById(PatientId patientId) {
        return importPatientApiClient.fetchById(patientId);
    }

    public List<Patient> fetchByIds(List<PatientId> patientIds) {
        return patientIds.stream()
                .map(this::fetchById)
                .toList();
    }

}
