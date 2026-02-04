package fr.cnamts.cpam33.ordonnance.domain.models.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions.ExceptionCode;

import java.util.Map;
import java.util.Objects;

public class DomainException extends RuntimeException implements DomainObject {

    private final transient ExceptionCode code;
    private final Map<String, ?> placeHolders;

    public DomainException(ExceptionCode code) {
        this.code = code;
        this.placeHolders = null;
    }

    public DomainException(ExceptionCode code, Map<String, ?> placeHolders) {
        this.code = Objects.requireNonNull(code);
        this.placeHolders = placeHolders;
    }

    public ExceptionCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return code.toString();
    }

    public Map<String, ?> getPlaceHolders() {
        return placeHolders;
    }

}
