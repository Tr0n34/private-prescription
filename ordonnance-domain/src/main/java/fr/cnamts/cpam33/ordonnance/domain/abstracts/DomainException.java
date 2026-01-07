package fr.cnamts.cpam33.ordonnance.domain.abstracts;

import java.util.Objects;

public abstract class DomainException extends RuntimeException implements DomainObject {

    private final transient ExceptionCode code;

    protected DomainException(ExceptionCode code) {
        this.code = Objects.requireNonNull(code);
    }

    public ExceptionCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return code.toString();
    }

}
