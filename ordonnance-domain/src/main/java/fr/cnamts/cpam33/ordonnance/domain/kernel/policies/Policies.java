package fr.cnamts.cpam33.ordonnance.domain.kernel.policies;

import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.DomainException;
import fr.cnamts.cpam33.ordonnance.domain.kernel.exceptions.ExceptionCode;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class Policies {

    private Policies() {
        throw new UnsupportedOperationException("Policies");
    }

    public static <T, E extends Enum<E> & ExceptionCode> Rule<T, E> rule(E code, RulePolicy<T, E> policy) {
        Failure<E> fail = (field, reason) -> new DomainException(code, Map.of("field", field, "reason", reason));
        return new Rule<>(code, target -> policy.enforce(target, fail));
    }

    @SafeVarargs
    public static <T> Policy<T> allOf(Rule<T, ?>... rules) {
        return target -> {
            for ( Rule<T, ?> rule : rules ) {
                rule.enforce(target);
            }
        };
    }

    public static <T> Policy<T> forOperation(String object, String operation, Policy<T> policy) {
        return target -> {
            try {
                policy.enforce(target);
            } catch (DomainException e) {
                throw enrich(e, Map.of("object", object, "operation", operation));
            }
        };
    }

    public static void require(boolean condition, Supplier<? extends RuntimeException> exceptionSupplier) {
        if ( !condition ) {
            throw exceptionSupplier.get();
        }
    }

    public static DomainException enrich(DomainException e, Map<String, Object> extra) {
        Map<String, Object> placeHolders = e.getPlaceHolders();
        HashMap<String, Object> merged = new HashMap<>();
        if ( placeHolders != null ) {
            merged.putAll(placeHolders);
        }
        for ( Map.Entry<String, Object> entry : extra.entrySet() ) {
            merged.putIfAbsent(entry.getKey(), entry.getValue());
        }
        Map<String, Object> out = merged.isEmpty() ? null : Map.copyOf(merged);
        return new DomainException(e.getCode(), out);
    }

}
