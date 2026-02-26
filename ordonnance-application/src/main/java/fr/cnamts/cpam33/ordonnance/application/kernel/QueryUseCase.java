package fr.cnamts.cpam33.ordonnance.application.kernel;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Query;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.PageRequest;
import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.PageResult;

import java.util.List;

public interface QueryUseCase <Q extends Query, D extends DomainObject> {

    List<D> execute(Q query) throws DomainException;

    PageResult<D> execute(Q query, PageRequest pageRequest) throws DomainException;

}
