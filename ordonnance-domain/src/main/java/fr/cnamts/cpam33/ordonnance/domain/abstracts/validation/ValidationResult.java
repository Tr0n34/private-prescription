package fr.cnamts.cpam33.ordonnance.domain.abstracts.validation;

import java.util.Map;

public record ValidationResult(
        boolean valid,
        Map<String, ?> errors
) {

    public static ValidationResult ok() {
        return new ValidationResult(true, Map.of());
    }

    public static ValidationResult ko(Map<String, ?> errors) {
        return new ValidationResult(false, errors);
    }

    public boolean isValid() {
        return errors == null || errors.isEmpty();
    }

}
