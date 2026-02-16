package fr.cnamts.cpam33.ordonnance.infrastructure.kernel.errors;

import java.io.IOException;

public final class InfrastructureError {

    private InfrastructureError() {}

    @FunctionalInterface
    public interface IoSupplier<T> {
        T get() throws IOException;
    }

    @FunctionalInterface
    public interface IoRunnable {
        void run() throws IOException;
    }

    @FunctionalInterface
    public interface ExceptionFactory<E extends InfrastructureException> {
        E create(Throwable cause);
    }

    public static <T, E extends InfrastructureException> T io(
            IoSupplier<T> operation,
            ExceptionFactory<E> errorFactory) {
        try {
            return operation.get();
        } catch (IOException e) {
            throw errorFactory.create(e);
        }
    }

    public static <E extends InfrastructureException> void io(
            IoRunnable operation,
            ExceptionFactory<E> errorFactory) {
        try {
            operation.run();
        } catch (IOException e) {
            throw errorFactory.create(e);
        }
    }

    public static <E extends InfrastructureException> String requireNotBlank(
            String value,
            ExceptionFactory<E> errorFactory) {
        if ( value == null || value.isBlank() ) {
            throw errorFactory.create(null);
        }
        return value;
    }

    public static <E extends InfrastructureException> Object requireNotNull(
            Object value,
            ExceptionFactory<E> errorFactory) {
        if ( value == null ) {
            throw errorFactory.create(null);
        }
        return value;
    }

}
