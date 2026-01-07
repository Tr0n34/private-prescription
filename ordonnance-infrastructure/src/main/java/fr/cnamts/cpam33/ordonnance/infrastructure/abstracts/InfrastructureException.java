package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts;

import java.util.Objects;

public abstract class  InfrastructureException extends RuntimeException {

    private final transient InfraStructureExceptionCode code;

    public InfrastructureException(InfraStructureExceptionCode code) {
        this.code = Objects.requireNonNull(code);
    }

    public InfraStructureExceptionCode getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return code.toString();
    }

}
