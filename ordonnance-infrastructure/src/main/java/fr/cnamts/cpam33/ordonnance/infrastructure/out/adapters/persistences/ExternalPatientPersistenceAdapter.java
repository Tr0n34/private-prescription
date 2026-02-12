package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.ImportPatientApiClient;
import org.springframework.stereotype.Component;

@Component
public class ExternalPatientPersistenceAdapter implements Adapter {

    ImportPatientApiClient importPatientApiClient;

    public ExternalPatientPersistenceAdapter(ImportPatientApiClient importPatientApiClient) {
        this.importPatientApiClient = importPatientApiClient;
    }

    public Patient fetchById(ExternalPatientId externalPatientId) {
        return importPatientApiClient.fetchById(externalPatientId);
    }

}
