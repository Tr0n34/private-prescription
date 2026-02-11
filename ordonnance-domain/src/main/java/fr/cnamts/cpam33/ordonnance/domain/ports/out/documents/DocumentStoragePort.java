package fr.cnamts.cpam33.ordonnance.domain.ports.out.documents;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.Document;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;

public interface DocumentStoragePort<D extends Document> {

    void store(D document) throws DomainException;

    Document load(String key) throws DomainException;

    boolean exists(String key) throws DomainException;

}
