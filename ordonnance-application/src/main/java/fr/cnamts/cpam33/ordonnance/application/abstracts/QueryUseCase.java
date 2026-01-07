package fr.cnamts.cpam33.ordonnance.application.abstracts;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.Query;

import java.util.List;

public interface QueryUseCase <Q extends Query, D extends DomainObject> {

    List<D> execute(Q query);

}
