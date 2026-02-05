package fr.cnamts.cpam33.ordonnance.domain.ports.out.documents;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.Document;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;

public interface DocumentStoragePort {

    void store(Document document) throws DomainException;

}
