package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.Document;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.snapshots.OrdonnanceSnapshot;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.documents.DocumentStoragePort;
import org.springframework.stereotype.Component;

@Component
public class OrdonnanceSnapshotPersistenceAdapter implements DocumentStoragePort<OrdonnanceSnapshot> {

    @Override
    public void store(OrdonnanceSnapshot document) throws DomainException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Document load(String key) throws DomainException {
        return null;
    }

    @Override
    public boolean exists(String key) throws DomainException {
        return false;
    }

}
