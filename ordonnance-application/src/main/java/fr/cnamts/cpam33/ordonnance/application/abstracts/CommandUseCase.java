package fr.cnamts.cpam33.ordonnance.application.abstracts;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.models.exceptions.DomainException;

public interface CommandUseCase<C extends Command, D extends DomainObject> {

    D execute(C command) throws DomainException;

}
