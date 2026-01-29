package fr.cnamts.cpam33.ordonnance.domain.abstracts.domain;

import java.util.List;
import java.util.Optional;

public interface RepositoryPort<D extends DomainObject, I extends DomainObjectId> {

    Optional<D> findById(I domainObjectId);

    List<D> findAll();

    D save(D domainObject);

}
