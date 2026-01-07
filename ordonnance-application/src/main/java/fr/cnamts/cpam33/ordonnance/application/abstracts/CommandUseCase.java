package fr.cnamts.cpam33.ordonnance.application.abstracts;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;

public interface CommandUseCase<C extends Command, D extends DomainObject> {

    D execute(C command);

}
