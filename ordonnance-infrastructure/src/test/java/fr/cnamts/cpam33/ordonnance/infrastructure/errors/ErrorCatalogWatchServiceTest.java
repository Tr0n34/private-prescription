package fr.cnamts.cpam33.ordonnance.infrastructure.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.DebouncedReloadExecutor;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ErrorCatalogLoader;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.providers.ErrorCatalogWatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ErrorCatalogWatchServiceTest {

    @Mock
    private ErrorCatalogLoader loader;

    @Mock
    private DebouncedReloadExecutor debounce;

    @Mock
    private TaskExecutor taskExecutor;

    private ErrorCatalogWatchService service;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        service = new ErrorCatalogWatchService(loader, debounce, taskExecutor);
    }

    @Test
    void constructor_throwsNullPointerException_whenLoaderIsNull() {
        assertThrows(NullPointerException.class,
                () -> new ErrorCatalogWatchService(null, debounce, taskExecutor));
    }

    @Test
    void constructor_throwsNullPointerException_whenDebounceIsNull() {
        assertThrows(NullPointerException.class,
                () -> new ErrorCatalogWatchService(loader, null, taskExecutor));
    }

    @Test
    void constructor_throwsNullPointerException_whenTaskExecutorIsNull() {
        assertThrows(NullPointerException.class,
                () -> new ErrorCatalogWatchService(loader, debounce, null));
    }

    @Test
    void init_startsWatching_whenFileProtocol() {
        ReflectionTestUtils.setField(service, "errorFile", "file:/tmp/errors.json");
        doNothing().when(taskExecutor).execute(any(Runnable.class));
        service.init();
        assertTrue(service.isRunning());
        verify(taskExecutor).execute(service);
    }

    @Test
    void init_doesNotStartWatching_whenClasspathProtocol() {
        ReflectionTestUtils.setField(service, "errorFile", "classpath:errors.json");
        service.init();
        assertFalse(service.isRunning());
        verifyNoInteractions(taskExecutor);
    }

    @Test
    void startWatching_setsRunningAndExecutesTask() {
        doNothing().when(taskExecutor).execute(any(Runnable.class));
        service.startWatching();
        assertTrue(service.isRunning());
        verify(taskExecutor).execute(service);
    }

    @Test
    void startWatching_doesNotExecuteTwice_whenAlreadyRunning() {
        doNothing().when(taskExecutor).execute(any(Runnable.class));
        service.startWatching();
        service.startWatching();
        assertTrue(service.isRunning());
        verify(taskExecutor, times(1)).execute(service);
    }

    @Test
    void startWatching_resetsRunning_whenTaskExecutorThrowsException() {
        doThrow(new TaskRejectedException("Execution failed")).when(taskExecutor).execute(any(Runnable.class));
        assertThrows(IllegalStateException.class, () -> service.startWatching());
        assertFalse(service.isRunning());
    }

    @Test
    void stopWatching_stopsRunning_whenWatcherIsRunning() {
        doNothing().when(taskExecutor).execute(any(Runnable.class));
        service.startWatching();
        assertTrue(service.isRunning());
        service.stopWatching();
        assertFalse(service.isRunning());
    }

    @Test
    void stopWatching_doesNothing_whenWatcherNotRunning() {
        assertFalse(service.isRunning());
        service.stopWatching();
        assertFalse(service.isRunning());
    }

    @Test
    void destroy_stopsWatching() {
        doNothing().when(taskExecutor).execute(any(Runnable.class));
        service.startWatching();
        assertTrue(service.isRunning());
        service.destroy();
        assertFalse(service.isRunning());
    }

    @Test
    void processWatchEvents_triggersDebounce_whenFilenameMatches() throws IOException {
        Path errorFile = tempDir.resolve("errors.json");
        Files.createFile(errorFile);

        ReflectionTestUtils.setField(service, "errorFile", errorFile.toUri().toString());
        // utilise AtomicReference
        ReflectionTestUtils.setField(service, "watchedFilePathRef", new java.util.concurrent.atomic.AtomicReference<>(errorFile));
        ReflectionTestUtils.setField(service, "reloadDelay", 5000);

        @SuppressWarnings("unchecked")
        WatchEvent<Path> event = mock(WatchEvent.class);
        when(event.context()).thenReturn(errorFile.getFileName());

        java.nio.file.WatchKey watchKey = mock(java.nio.file.WatchKey.class);
        when(watchKey.pollEvents()).thenReturn(java.util.List.of(event));

        ReflectionTestUtils.invokeMethod(service, "processWatchEvents", watchKey);
        verify(debounce).trigger(anyString(), any(Runnable.class), eq(5000L));
    }

    @Test
    void processWatchEvents_doesNotTriggerDebounce_whenFilenameDifferent() throws IOException {
        Path errorFile = tempDir.resolve("errors.json");
        Path otherFile = tempDir.resolve("other.json");
        Files.createFile(errorFile);

        ReflectionTestUtils.setField(service, "watchedFilePathRef", new java.util.concurrent.atomic.AtomicReference<>(errorFile));

        @SuppressWarnings("unchecked")
        WatchEvent<Path> event = mock(WatchEvent.class);
        when(event.context()).thenReturn(otherFile.getFileName());

        java.nio.file.WatchKey watchKey = mock(java.nio.file.WatchKey.class);
        when(watchKey.pollEvents()).thenReturn(java.util.List.of(event));

        ReflectionTestUtils.invokeMethod(service, "processWatchEvents", watchKey);
        verifyNoInteractions(debounce);
    }

    @Test
    void validateFilePath_throwsException_whenPathHasNoParent() {
        Path path = Paths.get("errors.json");
        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ReflectionTestUtils.invokeMethod(service, "validateFilePath", path)
        );
        assertTrue(ex.getMessage().contains("Error file must be in a directory"));
    }

    @Test
    void validateFilePath_succeeds_whenPathHasParent() throws IOException {
        Path path = tempDir.resolve("errors.json");
        Files.createFile(path);
        assertDoesNotThrow(
                () -> ReflectionTestUtils.invokeMethod(service, "validateFilePath", path)
        );
    }

    @Test
    void resolveFilePath_throwsException_whenInvalidUri() {
        ReflectionTestUtils.setField(service, "errorFile", "file:\0invalid"); // caractère nul
        assertThrows(
                IllegalStateException.class,
                () -> ReflectionTestUtils.invokeMethod(service, "resolveFilePath")
        );
    }

    @Test
    void getWatchedFilePath_returnsNull_initially() {
        assertNull(service.getWatchedFilePath());
    }

    @Test
    void isRunning_returnsFalse_initially() {
        assertFalse(service.isRunning());
    }

    @Test
    void isRunning_returnsTrue_afterStart() {
        doNothing().when(taskExecutor).execute(any(Runnable.class));
        service.startWatching();
        assertTrue(service.isRunning());
    }
}
