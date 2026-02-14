package fr.cnamts.cpam33.ordonnance.domain.kernel.policies;

@FunctionalInterface
public interface Policy<T> {

    void enforce(T target);

}
