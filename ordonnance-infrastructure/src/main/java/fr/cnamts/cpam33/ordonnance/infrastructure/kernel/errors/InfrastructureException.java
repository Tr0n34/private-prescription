package fr.cnamts.cpam33.ordonnance.infrastructure.kernel.errors;

import java.util.Map;
import java.util.Objects;

public class InfrastructureException extends RuntimeException {

    private final transient InfraStructureExceptionCode code;
    private final transient Map<String, Object> placeHolders;

    public InfrastructureException(InfraStructureExceptionCode code) {
        this.code = Objects.requireNonNull(code);
        this.placeHolders = null;
    }

    public InfrastructureException(InfraStructureExceptionCode code, Map<String, Object> placeHolders) {
        this.code = Objects.requireNonNull(code);
        this.placeHolders = Objects.requireNonNull(placeHolders);
    }

    public InfraStructureExceptionCode getCode() {
        return code;
    }

    public Map<String, Object> getPlaceHolders() {
        return placeHolders;
    }

    @Override
    public String getMessage() {
        return code.toString();
    }

}
