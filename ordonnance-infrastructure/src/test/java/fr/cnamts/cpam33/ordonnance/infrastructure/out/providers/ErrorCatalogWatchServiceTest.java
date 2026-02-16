package fr.cnamts.cpam33.ordonnance.infrastructure.out.providers;

import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.Batch;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ErrorCatalogLoader;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.ErrorCatalogWatchService;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.DebouncedReloadExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ErrorCatalogWatchServiceTest {

    @Test
    void getFilePath_should_return_injected_value() {
        ErrorCatalogLoader loader = mock(ErrorCatalogLoader.class);
        DebouncedReloadExecutor debounce = mock(DebouncedReloadExecutor.class);
        TaskExecutor executor = mock(TaskExecutor.class);
        ErrorCatalogWatchService service = new ErrorCatalogWatchService(loader, debounce, executor);
        ReflectionTestUtils.setField(service, "errorFile", "classpath:errors.json");
        assertEquals("classpath:errors.json", service.getFilePath());
    }

    @Test
    void getReloadDelay_should_return_injected_value() {
        ErrorCatalogLoader loader = mock(ErrorCatalogLoader.class);
        DebouncedReloadExecutor debounce = mock(DebouncedReloadExecutor.class);
        TaskExecutor executor = mock(TaskExecutor.class);
        ErrorCatalogWatchService service = new ErrorCatalogWatchService(loader, debounce, executor);
        ReflectionTestUtils.setField(service, "reloadDelay", 5000);
        assertEquals(5000, service.getReloadDelay());
    }

    @Test
    void getBatch_should_be_error() {
        ErrorCatalogLoader loader = mock(ErrorCatalogLoader.class);
        DebouncedReloadExecutor debounce = mock(DebouncedReloadExecutor.class);
        TaskExecutor executor = mock(TaskExecutor.class);
        ErrorCatalogWatchService service = new ErrorCatalogWatchService(loader, debounce, executor);
        assertEquals(Batch.ERROR, service.getBatch());
    }

    @Test
    void getServiceName_should_be_batch_error_name() {
        ErrorCatalogLoader loader = mock(ErrorCatalogLoader.class);
        DebouncedReloadExecutor debounce = mock(DebouncedReloadExecutor.class);
        TaskExecutor executor = mock(TaskExecutor.class);
        ErrorCatalogWatchService service = new ErrorCatalogWatchService(loader, debounce, executor);
        assertEquals(Batch.ERROR.getName(), service.getServiceName());
    }

    @Test
    void performReload_should_delegate_to_loader_reload() {
        ErrorCatalogLoader loader = mock(ErrorCatalogLoader.class);
        DebouncedReloadExecutor debounce = mock(DebouncedReloadExecutor.class);
        TaskExecutor executor = mock(TaskExecutor.class);
        ErrorCatalogWatchService service = new ErrorCatalogWatchService(loader, debounce, executor);
        service.performReload();
        verify(loader, times(1)).reload();
        verifyNoMoreInteractions(loader);
    }

    @Test
    void constructor_should_require_loader() {
        DebouncedReloadExecutor debounce = mock(DebouncedReloadExecutor.class);
        TaskExecutor executor = mock(TaskExecutor.class);
        assertThrows(NullPointerException.class,
                () -> new ErrorCatalogWatchService(null, debounce, executor));
    }

}

