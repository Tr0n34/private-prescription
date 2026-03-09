package fr.cnamts.cpam33.ordonnance.application.abstracts;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.cqrs.Command;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;

public interface CommandUseCase<C extends Command, R> {

    R execute(C command) throws DomainException;

}
