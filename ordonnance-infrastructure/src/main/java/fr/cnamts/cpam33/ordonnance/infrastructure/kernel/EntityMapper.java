package fr.cnamts.cpam33.ordonnance.infrastructure.kernel;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;

public interface EntityMapper<E extends IEntity, D extends DomainObject> {

    E toEntity(D domainObject);

    D toDomain(E entity);

}
