package fr.cnamts.cpam33.ordonnance.application.exceptions;

public class InvalidErrorDescriptorException extends RuntimeException {

    public InvalidErrorDescriptorException() {
    }

    public InvalidErrorDescriptorException(String message) {
        super(message);
    }

    public InvalidErrorDescriptorException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidErrorDescriptorException(Throwable cause) {
        super(cause);
    }

    public InvalidErrorDescriptorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}