package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.ImportPatientCandidate;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.ImportPatientApiClient;
import org.springframework.stereotype.Component;

@Component
public class ExternalPatientPersistenceAdapter implements Adapter {

    ImportPatientApiClient importPatientApiClient;

    public ExternalPatientPersistenceAdapter(ImportPatientApiClient importPatientApiClient) {
        this.importPatientApiClient = importPatientApiClient;
    }

    public ImportPatientCandidate fetchById(ExternalPatientId externalPatientId) {
        return importPatientApiClient.fetchById(externalPatientId);
    }

}
