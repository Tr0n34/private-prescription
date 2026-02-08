package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.storages;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.Document;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.documents.DocumentStoragePort;
import org.springframework.stereotype.Component;

@Component
public class DocumentStorage implements DocumentStoragePort {

    @Override
    public void store(Document document) throws DomainException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
