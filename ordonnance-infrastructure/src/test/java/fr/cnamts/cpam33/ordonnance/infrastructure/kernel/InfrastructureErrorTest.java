package fr.cnamts.cpam33.ordonnance.infrastructure.kernel;

import fr.cnamts.cpam33.ordonnance.infrastructure.kernel.errors.InfraStructureExceptionCode;
import fr.cnamts.cpam33.ordonnance.infrastructure.kernel.errors.InfrastructureError;
import fr.cnamts.cpam33.ordonnance.infrastructure.kernel.errors.InfrastructureException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class InfrastructureErrorTest {

    enum InfrastructureExceptionCodeTest implements InfraStructureExceptionCode {
        TEST_ERROR
    }

    private static final InfrastructureExceptionCodeTest CODE = InfrastructureExceptionCodeTest.TEST_ERROR;

    private static InfrastructureError.ExceptionFactory<InfrastructureException> factory() {
        return cause -> {
            InfrastructureException ex = new InfrastructureException(CODE, Map.of("key", "value"));
            if (cause != null) {
                ex.initCause(cause);
            }
            return ex;
        };
    }

    @Test
    void should_return_value_when_io_supplier_success() {
        String result = InfrastructureError.io(() -> "ok", factory());
        assertEquals("ok", result);
    }

    @Test
    void should_wrap_ioexception_when_io_supplier_fails() {
        IOException io = new IOException("boom");
        InfrastructureException ex = assertThrows(
                InfrastructureException.class,
                () -> InfrastructureError.io(() -> { throw io; }, factory())
        );
        assertEquals(CODE, ex.getCode());
        assertEquals(Map.of("key", "value"), ex.getPlaceHolders());
        assertSame(io, ex.getCause());
        assertEquals(CODE.toString(), ex.getMessage());
    }

    @Test
    void should_execute_when_io_runnable_success() {
        AtomicBoolean executed = new AtomicBoolean(false);
        InfrastructureError.io(() -> executed.set(true), factory());
        assertTrue(executed.get());
    }

    @Test
    void should_wrap_ioexception_when_io_runnable_fails() {
        IOException io = new IOException("boom");
        InfrastructureException ex = assertThrows(
                InfrastructureException.class,
                () -> InfrastructureError.io(() -> { throw io; }, factory())
        );
        assertEquals(CODE, ex.getCode());
        assertSame(io, ex.getCause());
    }

    @Test
    void should_return_value_when_requireNotBlank_valid() {
        String result = InfrastructureError.requireNotBlank("abc", factory());
        assertEquals("abc", result);
    }

    @Test
    void should_throw_when_requireNotBlank_null() {
        InfrastructureException ex = assertThrows(
                InfrastructureException.class,
                () -> InfrastructureError.requireNotBlank(null, factory())
        );
        assertEquals(CODE, ex.getCode());
        assertNull(ex.getCause());
    }

    @Test
    void should_throw_when_requireNotBlank_blank() {
        InfrastructureException ex = assertThrows(
                InfrastructureException.class,
                () -> InfrastructureError.requireNotBlank("   ", factory())
        );
        assertEquals(CODE, ex.getCode());
    }

    @Test
    void should_return_value_when_requireNotNull_valid() {
        Object obj = new Object();
        Object result = InfrastructureError.requireNotNull(obj, factory());
        assertSame(obj, result);
    }

    @Test
    void should_throw_when_requireNotNull_null() {
        InfrastructureException ex = assertThrows(
                InfrastructureException.class,
                () -> InfrastructureError.requireNotNull(null, factory())
        );
        assertEquals(CODE, ex.getCode());
        assertNull(ex.getCause());
    }

}
