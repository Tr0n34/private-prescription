package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;

public interface EntityMapper<E extends IEntity, D extends DomainObject> {

    E toEntity(D domainObject);

    D toDomain(E entity);

}
