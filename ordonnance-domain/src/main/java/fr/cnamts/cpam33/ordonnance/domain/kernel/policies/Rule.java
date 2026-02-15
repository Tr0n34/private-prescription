package fr.cnamts.cpam33.ordonnance.domain.kernel.policies;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.ExceptionCode;

import java.util.Map;
import java.util.logging.Logger;

public record Rule<T, E extends Enum<E> & ExceptionCode>(
        E code,
        Policy<T> policy
) implements Policy<T> {

    public static final String RULE_KEY = "rule";

    @Override
    public void enforce(T target) {
        try {
            policy.enforce(target);
        } catch (DomainException e) {
            throw Policies.enrich(e, Map.of(RULE_KEY, code.name()));
        }
    }

    public String name() {
        return code.name();
    }

}
