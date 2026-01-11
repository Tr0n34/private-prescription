package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.errors;

import java.util.Objects;

public abstract class  InfrastructureException extends RuntimeException {

    private final transient InfraStructureExceptionCode code;
    private final transient String[] placeHolders;

    protected InfrastructureException(InfraStructureExceptionCode code) {
        this.code = Objects.requireNonNull(code);
        this.placeHolders = null;
    }

    protected InfrastructureException(InfraStructureExceptionCode code, String[] placeHolders) {
        this.code = Objects.requireNonNull(code);
        this.placeHolders = Objects.requireNonNull(placeHolders);
    }

    public InfraStructureExceptionCode getCode() {
        return code;
    }

    public String[] getPlaceHolders() {
        return placeHolders;
    }

    public String getPlaceHolder(int position) {
        return placeHolders[position];
    }

    @Override
    public String getMessage() {
        return code.toString();
    }

}
