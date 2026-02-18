package fr.cnamts.cpam33.ordonnance.application.kernel;

import fr.cnamts.cpam33.ordonnance.domain.kernel.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;

public interface CommandUseCase<C extends Command, D extends DomainObject> {

    D execute(C command) throws DomainException;

}
