package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors;

import java.util.Map;
import java.util.Objects;

public class InfrastructureException extends RuntimeException {

    private final transient InfraStructureExceptionCode code;
    private final transient Map<String, ?> placeHolders;

    protected InfrastructureException(InfraStructureExceptionCode code) {
        this.code = Objects.requireNonNull(code);
        this.placeHolders = null;
    }

    protected InfrastructureException(InfraStructureExceptionCode code, Map<String, ?> placeHolders) {
        this.code = Objects.requireNonNull(code);
        this.placeHolders = Objects.requireNonNull(placeHolders);
    }

    public InfraStructureExceptionCode getCode() {
        return code;
    }

    public Map<String, ?> getPlaceHolders() {
        return placeHolders;
    }

    @Override
    public String getMessage() {
        return code.toString();
    }

}
