package fr.cnamts.cpam33.ordonnance.domain.kernel.policies;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.ExceptionCode;

@FunctionalInterface
public interface RulePolicy<T, E extends Enum<E> & ExceptionCode> {

    void enforce(T target, Failure<E> failure);

}
