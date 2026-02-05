package fr.cnamts.cpam33.ordonnance.domain.abstracts.validation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Validator {

    private final Map<String, Object> errors = new LinkedHashMap<>();

    public Validator notNull(Object value, String field) {
        if ( value == null ) errors.put(field, null);
        return this;
    }

    public Validator notBlank(String value, String field) {
        if ( value == null || value.isBlank()) errors.put(field, null);
        return this;
    }

    public Validator notEmpty(Collection<?> value, String field) {
        if ( value == null || value.isEmpty() ) errors.put(field, value);
        return this;
    }

    public ValidationResult validate() {
        return errors.isEmpty() ? ValidationResult.ok() : ValidationResult.ko(errors);
    }

}
