package fr.cnamts.cpam33.ordonnance.domain.abstracts;

import java.util.List;
import java.util.Optional;

public interface RepositoryPort<D extends DomainObject, I extends DomainObjectId> {

    Optional<D> findById(I domainObjectId) throws DomainObjectNotFound;

    List<D> findAll();

    D save(D domainObject);

}
