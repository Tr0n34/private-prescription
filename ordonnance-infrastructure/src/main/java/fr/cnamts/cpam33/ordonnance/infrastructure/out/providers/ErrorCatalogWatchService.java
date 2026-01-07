package fr.cnamts.cpam33.ordonnance.infrastructure.out.providers;

import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.Batch;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.DebouncedReloadExecutor;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ErrorCatalogLoader;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Service de surveillance des modifications du fichier de catalogue d'erreurs.
 */
@Component
@ConditionalOnProperty(
        prefix = "ordonnance.loaders.errors.watcher",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class ErrorCatalogWatchService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ErrorCatalogWatchService.class);
    private static final String FILE_PROTOCOL = "file:";

    @Value("${ordonnance.loaders.errors.file:classpath:errors.json}")
    private String errorFile;

    @Value("${ordonnance.loaders.errors.reload-delay:5000}")
    private int reloadDelay;

    private final ErrorCatalogLoader errorCatalogLoader;
    private final DebouncedReloadExecutor debouncedReloadExecutor;
    private final TaskExecutor taskExecutor;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicReference<WatchService> watchServiceRef = new AtomicReference<>();
    private final AtomicReference<Path> watchedFilePathRef = new AtomicReference<>();

    public ErrorCatalogWatchService(
            ErrorCatalogLoader errorCatalogLoader,
            @Qualifier("errorCatalogDebounceExecutor") DebouncedReloadExecutor debouncedReloadExecutor,
            TaskExecutor taskExecutor) {
        this.errorCatalogLoader = Objects.requireNonNull(errorCatalogLoader);
        this.debouncedReloadExecutor = Objects.requireNonNull(debouncedReloadExecutor);
        this.taskExecutor = Objects.requireNonNull(taskExecutor);
    }

    @PostConstruct
    public void init() {
        if (isFileProtocol()) {
            logger.info("Initializing file watcher for: {}", errorFile);
            startWatching();
        }
    }

    @PreDestroy
    public void destroy() {
        logger.info("Shutting down ErrorCatalogWatchService");
        stopWatching();
        cancelPendingReloads();
    }

    @Override
    public void run() {
        try {
            Path filePath = resolveFilePath();
            watchedFilePathRef.set(filePath);
            validateFilePath(filePath);

            Path parentDirectory = filePath.getParent();
            logger.info("Starting to watch directory: {}", parentDirectory);
            watchDirectory(parentDirectory);
        } catch (InterruptedException | IOException e) {
            logger.error("Error in watch service", e);
            running.set(false);
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void startWatching() {
        if (running.compareAndSet(false, true)) {
            try {
                taskExecutor.execute(this);
                logger.info("Error catalog watcher started successfully");
            } catch (TaskRejectedException e) {
                running.set(false);
                logger.error("Failed to start watcher", e);
                throw new IllegalStateException("Cannot start error catalog watcher", e);
            }
        }
    }

    public synchronized void stopWatching() {
        if (running.getAndSet(false)) {
            cancelPendingReloads();
            closeWatchService();
            logger.info("Error catalog watcher stopped");
        }
    }

    private void closeWatchService() {
        WatchService ws = watchServiceRef.getAndSet(null);
        if (ws != null) {
            try {
                ws.close();
            } catch (IOException e) {
                logger.warn("Error closing WatchService", e);
            }
        }
    }

    private void watchDirectory(Path directory) throws IOException, InterruptedException {
        try (WatchService ws = FileSystems.getDefault().newWatchService()) {
            watchServiceRef.set(ws);
            directory.register(ws, StandardWatchEventKinds.ENTRY_MODIFY);
            logger.info("Watch service registered successfully for: {}", directory);

            while (running.get() && !Thread.currentThread().isInterrupted()) {
                WatchKey key = waitForWatchKey(ws);
                if (key == null) break;

                processWatchEvents(key);

                if (!key.reset()) {
                    logger.warn("Watch key no longer valid, stopping watcher");
                    break;
                }
            }
        } catch (ClosedWatchServiceException e) {
            logger.info("Watch service closed");
        } finally {
            cleanup();
        }
    }

    private WatchKey waitForWatchKey(WatchService ws) {
        try {
            return ws.take();
        } catch (ClosedWatchServiceException e) {
            logger.debug("Watch service closed during take()");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.info("Watcher thread interrupted");
        }
        return null;
    }

    private void processWatchEvents(WatchKey key) {
        Path filePath = watchedFilePathRef.get();
        if (filePath == null) return;

        key.pollEvents().forEach(event -> {
            Object context = event.context();
            boolean isModified = (context instanceof Path)
                    ? filePath.getFileName().equals(context)
                    : filePath.getFileName().toString().equals(context.toString());

            if (isModified) {
                logger.debug("Detected modification of: {}", filePath.getFileName());
                triggerReload();
            }
        });
    }

    private void triggerReload() {
        try {
            debouncedReloadExecutor.trigger(
                    Batch.ERROR.getName(),
                    errorCatalogLoader::reload,
                    reloadDelay
            );
            logger.debug("Reload triggered for error catalog");
        } catch (Exception e) {
            logger.error("Error triggering reload", e);
        }
    }

    private void cleanup() {
        running.set(false);
        watchServiceRef.set(null);
        watchedFilePathRef.set(null);
        logger.debug("Watch service cleanup completed");
    }

    private Path resolveFilePath() {
        try {
            String filePath = errorFile;
            if (filePath.startsWith(FILE_PROTOCOL)) {
                filePath = filePath.substring(FILE_PROTOCOL.length());
            }
            Path path = Paths.get(filePath);
            logger.debug("Resolved file path: {} -> {}", errorFile, path.toAbsolutePath());
            return path;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot resolve file path: " + errorFile, e);
        }
    }

    private void validateFilePath(Path path) {
        if (path.getParent() == null) {
            throw new IllegalStateException("Error file must be in a directory. Invalid path: " + path);
        }
        if (!Files.exists(path)) {
            logger.warn("File does not exist yet: {}", path);
        }
    }

    private boolean isFileProtocol() {
        return errorFile != null && errorFile.startsWith(FILE_PROTOCOL);
    }

    public boolean isRunning() {
        return running.get();
    }

    public Path getWatchedFilePath() {
        return watchedFilePathRef.get();
    }

    private void cancelPendingReloads() {
        boolean cancelled = debouncedReloadExecutor.cancel(Batch.ERROR.getName());
        if (cancelled) {
            logger.info("Cancelled pending reload for error catalog");
        }
    }

    public boolean hasPendingReload() {
        return debouncedReloadExecutor.hasPendingTask(Batch.ERROR.getName());
    }
}
