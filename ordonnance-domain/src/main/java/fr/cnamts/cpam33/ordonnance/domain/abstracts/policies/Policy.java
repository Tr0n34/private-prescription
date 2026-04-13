package fr.cnamts.cpam33.ordonnance.domain.abstracts.policies;

@FunctionalInterface
public interface Policy<T> {

    void enforce(T target);

}
