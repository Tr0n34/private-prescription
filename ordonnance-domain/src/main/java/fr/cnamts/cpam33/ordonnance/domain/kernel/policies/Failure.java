package fr.cnamts.cpam33.ordonnance.domain.kernel.policies;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.ExceptionCode;

@FunctionalInterface
public interface Failure<E extends Enum<E> & ExceptionCode> {

    DomainException of(String field, String reason);

}
