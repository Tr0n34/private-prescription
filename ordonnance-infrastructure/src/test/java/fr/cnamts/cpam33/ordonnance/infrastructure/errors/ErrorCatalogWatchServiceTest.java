package fr.cnamts.cpam33.ordonnance.infrastructure.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.Batch;
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
        doThrow(new TaskRejectedException("Execution failed"))
                .when(taskExecutor).execute(any(Runnable.class));
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
    void destroy_cancelsPendingReloads() {
        when(debounce.cancel(Batch.ERROR.getName())).thenReturn(true);
        service.destroy();
        verify(debounce).cancel(Batch.ERROR.getName());
    }

    @Test
    void hasPendingReload_returnsTrueWhenDebounceHasPendingTask() {
        when(debounce.hasPendingTask(Batch.ERROR.getName())).thenReturn(true);
        assertTrue(service.hasPendingReload());
        verify(debounce).hasPendingTask(Batch.ERROR.getName());
    }

    @Test
    void hasPendingReload_returnsFalseWhenNoDebounceTask() {
        when(debounce.hasPendingTask(Batch.ERROR.getName())).thenReturn(false);
        assertFalse(service.hasPendingReload());
        verify(debounce).hasPendingTask(Batch.ERROR.getName());
    }

    @Test
    void getServiceName_returnsErrorCatalog() {
        String serviceName = ReflectionTestUtils.invokeMethod(service, "getServiceName");
        assertEquals(Batch.ERROR.getName(), serviceName);
    }

    @Test
    void getBatch_returnsErrorBatch() {
        Batch batch = ReflectionTestUtils.invokeMethod(service, "getBatch");
        assertEquals(Batch.ERROR, batch);
    }

    @Test
    void getFilePath_returnsConfiguredPath() {
        ReflectionTestUtils.setField(service, "errorFile", "file:/tmp/errors.json");
        String filePath = ReflectionTestUtils.invokeMethod(service, "getFilePath");
        assertEquals("file:/tmp/errors.json", filePath);
    }

    @Test
    void getReloadDelay_returnsConfiguredDelay() {
        ReflectionTestUtils.setField(service, "reloadDelay", 3000);
        int delay = ReflectionTestUtils.invokeMethod(service, "getReloadDelay");
        assertEquals(3000, delay);
    }

    @Test
    void performReload_callsLoaderReload() {
        doNothing().when(loader).reload();

        ReflectionTestUtils.invokeMethod(service, "performReload");

        verify(loader).reload();
    }

    @Test
    void validateFilePath_throwsException_whenPathHasNoParent() throws IOException {
        // Créer un fichier temporaire sans parent explicite
        Path tempFile = Files.createTempFile("test", ".json");
        Path relativeFile = tempFile.getFileName(); // Juste le nom, pas de parent

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> ReflectionTestUtils.invokeMethod(service, "validateFilePath", relativeFile)
        );
        assertTrue(ex.getMessage().contains("File must be in a directory"));

        Files.deleteIfExists(tempFile);
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
    void resolveFilePath_removesFileProtocol() throws IOException {
        Path errorFile = tempDir.resolve("errors.json");
        Files.createFile(errorFile);

        ReflectionTestUtils.setField(service, "errorFile", "file:" + errorFile.toString());

        Path resolved = ReflectionTestUtils.invokeMethod(service, "resolveFilePath");
        assertNotNull(resolved);
        assertEquals(errorFile.toString(), resolved.toString());
    }

    @Test
    void resolveFilePath_throwsException_whenInvalidPath() {
        // Utiliser un chemin invalide avec des caractères non autorisés
        ReflectionTestUtils.setField(service, "errorFile", "file:\u0000invalid");

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

    @Test
    void isRunning_returnsFalse_afterStop() {
        doNothing().when(taskExecutor).execute(any(Runnable.class));

        service.startWatching();
        assertTrue(service.isRunning());

        service.stopWatching();
        assertFalse(service.isRunning());
    }

}