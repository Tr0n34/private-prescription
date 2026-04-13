package fr.cnamts.cpam33.ordonnance.domain.abstracts.exceptions;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;

import java.io.*;
import java.util.Map;
import java.util.Objects;

public class DomainException extends RuntimeException implements DomainObject {

    @Serial
    private static final long serialVersionUID = 1L;

    private final transient ExceptionCode code;
    private transient Map<String, Object> placeHolders;

    public DomainException(ExceptionCode code) {
        this.code = Objects.requireNonNull(code);
        this.placeHolders = null;
    }

    public DomainException(ExceptionCode code, Map<String, Object> placeHolders) {
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

    public Map<String, Object> getPlaceHolders() {
        return placeHolders;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.placeHolders = null;
    }

}
