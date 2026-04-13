package fr.cnamts.cpam33.ordonnance.domain.abstracts.policies;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.ExceptionCode;

@FunctionalInterface
public interface Failure<E extends Enum<E> & ExceptionCode> {

    DomainException of(String field, String reason);

}
