package fr.cnamts.cpam33.ordonnance.domain.abstracts;

import java.util.ArrayList;
import java.util.List;

public interface Specification<T> {

    SpecResult isSatisfiedBy(T candidate);

    default Specification<T> and(Specification<T> other) {
        return candidate -> {
            SpecResult left = this.isSatisfiedBy(candidate);
            SpecResult right = other.isSatisfiedBy(candidate);
            boolean ok = left.ok() && right.ok();
            List<SpecViolation> violations = new ArrayList<>();
            violations.addAll(left.violations());
            violations.addAll(right.violations());
            return new SpecResult(ok, violations);
        };
    }

    record SpecResult(boolean ok, List<SpecViolation> violations) {
            public SpecResult(boolean ok, List<SpecViolation> violations) {
                this.ok = ok;
                this.violations = List.copyOf(violations);
            }
        }

    record SpecViolation(String code) {}

}
