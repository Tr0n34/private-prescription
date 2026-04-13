package fr.cnamts.cpam33.ordonnance.infrastructure.errors;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ErrorCatalogLoader;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.ErrorCatalogWatchService;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.DebouncedReloadExecutor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.task.TaskExecutor;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ErrorCatalogWatchServiceIT {

    public static final String ERROR_FILE = "errorFile";
    public static final String RUNNING = "running";
    public static final String ERRORS_JSON = "errors.json";
    public static final String WATCHED_FILE_PATH_REF = "watchedFilePathRef";

    @TempDir
    Path tempDir;

    private ErrorCatalogLoader loader;
    private DebouncedReloadExecutor debounce;
    private ErrorCatalogWatchService service;
    private Path errorFile;

    @BeforeEach
    void setup() throws IOException {
        loader = mock(ErrorCatalogLoader.class);
        debounce = mock(DebouncedReloadExecutor.class);
        errorFile = tempDir.resolve(ERRORS_JSON);
        Files.writeString(errorFile, "{}");

        service = new ErrorCatalogWatchService(loader, debounce, mock(org.springframework.core.task.TaskExecutor.class));
        ReflectionTestUtils.setField(service, ERROR_FILE, errorFile.toAbsolutePath().toString());
        ReflectionTestUtils.setField(service, "reloadDelay", 100);
        // initialise AtomicReference
        ReflectionTestUtils.setField(service, WATCHED_FILE_PATH_REF, new AtomicReference<>(null));
    }

    @AfterEach
    void tearDown() {
        if (service.isRunning()) {
            service.stopWatching();
        }
    }

    @Test
    void startWatching_should_setRunning() {
        org.springframework.core.task.TaskExecutor taskExecutor = mock(org.springframework.core.task.TaskExecutor.class);
        ErrorCatalogWatchService testService = new ErrorCatalogWatchService(loader, debounce, taskExecutor);
        testService.startWatching();
        assertThat(testService.isRunning()).isTrue();
        verify(taskExecutor).execute(testService);
    }

    @Test
    void startWatching_whenAlreadyRunning_shouldNotExecuteAgain() {
        org.springframework.core.task.TaskExecutor taskExecutor = mock(org.springframework.core.task.TaskExecutor.class);
        ErrorCatalogWatchService testService = new ErrorCatalogWatchService(loader, debounce, taskExecutor);
        testService.startWatching();
        testService.startWatching();
        assertThat(testService.isRunning()).isTrue();
        verify(taskExecutor, times(1)).execute(testService);
    }

    @Test
    void stopWatching_should_setRunningFalse() {
        service.startWatching();
        assertThat(service.isRunning()).isTrue();
        service.stopWatching();
        assertThat(service.isRunning()).isFalse();
    }

    @Test
    void stopWatching_whenNotRunning_shouldDoNothing() {
        assertThat(service.isRunning()).isFalse();
        service.stopWatching();
        assertThat(service.isRunning()).isFalse();
    }

    @Test
    void init_should_startWatching_whenFileProtocol() {
        TaskExecutor taskExecutor = mock(org.springframework.core.task.TaskExecutor.class);
        ErrorCatalogWatchService newService = new ErrorCatalogWatchService(loader, debounce, taskExecutor);
        ReflectionTestUtils.setField(newService, ERROR_FILE, errorFile.toUri().toString());
        ReflectionTestUtils.setField(newService, WATCHED_FILE_PATH_REF, new AtomicReference<>(null));
        newService.init();
        assertThat(newService.isRunning()).isTrue();
        verify(taskExecutor).execute(newService);
        newService.stopWatching();
    }

    @Test
    void init_shouldNotStartWatching_whenClasspathProtocol() {
        TaskExecutor taskExecutor = mock(org.springframework.core.task.TaskExecutor.class);
        ErrorCatalogWatchService newService = new ErrorCatalogWatchService(loader, debounce, taskExecutor);
        ReflectionTestUtils.setField(newService, ERROR_FILE, "classpath:errors.json");
        ReflectionTestUtils.setField(newService, WATCHED_FILE_PATH_REF, new AtomicReference<>(null));
        newService.init();
        assertThat(newService.isRunning()).isFalse();
        verifyNoInteractions(taskExecutor);
    }

    @Test
    void destroy_should_stopWatching() {
        service.startWatching();
        assertThat(service.isRunning()).isTrue();
        service.destroy();
        assertThat(service.isRunning()).isFalse();
    }

    @Test
    void fileModification_should_triggerDebounce() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(debounce).trigger(anyString(), any(Runnable.class), anyLong());
        Thread watchThread = new Thread(service);
        service.startWatching();
        watchThread.start();
        await().atMost(500, TimeUnit.MILLISECONDS).until(watchThread::isAlive);
        Files.writeString(errorFile, "{\"updated\": true}");
        boolean triggered = latch.await(5, TimeUnit.SECONDS);
        service.stopWatching();
        watchThread.interrupt();
        watchThread.join(1000);
        assertThat(triggered).isTrue();
        verify(debounce, atLeastOnce()).trigger(anyString(), any(Runnable.class), anyLong());
    }

    @Test
    void isRunning_returnsFalse_initially() {
        TaskExecutor taskExecutor = mock(org.springframework.core.task.TaskExecutor.class);
        ErrorCatalogWatchService newService = new ErrorCatalogWatchService(loader, debounce, taskExecutor);
        assertThat(newService.isRunning()).isFalse();
    }

    @Test
    void getWatchedFilePath_returnsNull_initially() {
        TaskExecutor taskExecutor = mock(org.springframework.core.task.TaskExecutor.class);
        ErrorCatalogWatchService newService = new ErrorCatalogWatchService(loader, debounce, taskExecutor);
        assertThat(newService.getWatchedFilePath()).isNull();
    }

    @Test
    void getWatchedFilePath_returnsPath_afterRunStarted() throws Exception {
        Thread watchThread = new Thread(service);
        service.startWatching();
        watchThread.start();
        await().atMost(1, TimeUnit.SECONDS).until(watchThread::isAlive);
        assertThat(service.getWatchedFilePath()).isNotNull();
        assertThat(service.getWatchedFilePath()).isEqualTo(errorFile);
        service.stopWatching();
        watchThread.interrupt();
        watchThread.join(1000);
    }

    @Test
    void stopWatching_shouldCancelPendingReloads() throws Exception {
        Path tempFile = tempDir.resolve(ERRORS_JSON);
        Files.writeString(tempFile, "{}");
        ReflectionTestUtils.setField(service, ERROR_FILE, "file:" + tempFile);
        ReflectionTestUtils.setField(service, WATCHED_FILE_PATH_REF, new AtomicReference<>(null));
        service.startWatching();
        await().atMost(200, TimeUnit.MILLISECONDS).until(service::isRunning);
        Files.writeString(tempFile, "{\"updated\": true}");
        await().atMost(200, TimeUnit.MILLISECONDS).until(service::isRunning);
        service.stopWatching();
        assertFalse(service.hasPendingReload());
    }

}
