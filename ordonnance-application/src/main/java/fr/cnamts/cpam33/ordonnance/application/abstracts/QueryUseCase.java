package fr.cnamts.cpam33.ordonnance.application.abstracts;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Query;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.application.filters.PageRequest;
import fr.cnamts.cpam33.ordonnance.application.filters.PageResult;

import java.util.List;

public interface QueryUseCase <Q extends Query, R> {

    List<R> execute(Q query) throws DomainException;

    PageResult<R> execute(Q query, PageRequest pageRequest) throws DomainException;

}
