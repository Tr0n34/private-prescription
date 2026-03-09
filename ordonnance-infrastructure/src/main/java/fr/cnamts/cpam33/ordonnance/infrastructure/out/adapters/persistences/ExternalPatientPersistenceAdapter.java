package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.ImportPatientCandidate;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Adapter;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.ImportPatientAdapter;
import org.springframework.stereotype.Component;

@Component
public class ExternalPatientPersistenceAdapter implements Adapter {

    ImportPatientAdapter importPatientAdapter;

    public ExternalPatientPersistenceAdapter(ImportPatientAdapter importPatientAdapter) {
        this.importPatientAdapter = importPatientAdapter;
    }

    public ImportPatientCandidate fetchById(ExternalPatientId externalPatientId) {
        return importPatientAdapter.fetchById(externalPatientId);
    }

}
