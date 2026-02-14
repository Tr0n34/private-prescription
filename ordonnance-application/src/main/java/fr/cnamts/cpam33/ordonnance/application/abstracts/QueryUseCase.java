package fr.cnamts.cpam33.ordonnance.application.abstracts;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Query;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;

import java.util.List;

public interface QueryUseCase <Q extends Query, D extends DomainObject> {

    List<D> execute(Q query);

}
