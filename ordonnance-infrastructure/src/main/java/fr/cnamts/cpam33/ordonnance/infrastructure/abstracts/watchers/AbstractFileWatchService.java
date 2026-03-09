package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.watchers;

import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.Batch;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.DebouncedReloadExecutor;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class AbstractFileWatchService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFileWatchService.class);

    private static final String FILE_PROTOCOL = "file:";
    private static final WatchService SERVICE_CLEAN_VALUE = null;
    private static final Path PATH_CLEAN_VALUE  = null;
    public static final boolean STOPPED = false;
    public static final boolean STARTED = true;

    private final DebouncedReloadExecutor debouncedReloadExecutor;
    private final TaskExecutor taskExecutor;

    private final AtomicBoolean running = new AtomicBoolean(STOPPED);
    private final AtomicReference<WatchService> watchServiceRef = new AtomicReference<>();
    private final AtomicReference<Path> watchedFilePathRef = new AtomicReference<>();

    protected AbstractFileWatchService(
            DebouncedReloadExecutor debouncedReloadExecutor,
            @Qualifier("watcherTaskExecutor") TaskExecutor taskExecutor) {
        this.debouncedReloadExecutor = Objects.requireNonNull(debouncedReloadExecutor);
        this.taskExecutor = Objects.requireNonNull(taskExecutor);
    }

    public abstract String getFilePath();

    public abstract int getReloadDelay();

    public abstract Batch getBatch();

    public abstract String getServiceName();

    public abstract void performReload();

    @PostConstruct
    public void init() {
        if ( isFileProtocol() ) {
            logger.info("Initializing file watcher for: {}", getFilePath());
            startWatching();
        }
    }

    @PreDestroy
    public void destroy() {
        logger.info("Shutting down {}", getServiceName());
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
        if ( running.compareAndSet(STOPPED, STARTED) ) {
            try {
                taskExecutor.execute(this);
                logger.info("{} watcher started successfully", getServiceName());
            } catch (TaskRejectedException e) {
                running.set(false);
                logger.error("Failed to start watcher", e);
                throw new IllegalStateException("Cannot start " + getServiceName() + " watcher", e);
            }
        }
    }

    public synchronized void stopWatching() {
        if ( running.getAndSet(STOPPED) ) {
            cancelPendingReloads();
            closeWatchService();
            logger.info("{} watcher stopped", getServiceName());
        }
    }

    public boolean isRunning() {
        return running.get();
    }

    public Path getWatchedFilePath() {
        return watchedFilePathRef.get();
    }

    public boolean hasPendingReload() {
        return debouncedReloadExecutor.hasPendingTask(getBatch().getName());
    }

    private void closeWatchService() {
        WatchService watchService = watchServiceRef.getAndSet(null);
        if ( watchService != null ) {
            try {
                watchService.close();
            } catch (IOException e) {
                logger.warn("Error closing WatchService", e);
            }
        }
    }

    private void watchDirectory(Path directory) throws IOException, InterruptedException {
        try ( WatchService ws = FileSystems.getDefault().newWatchService() ) {
            watchServiceRef.set(ws);
            directory.register(ws,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE);
            logger.info("Watch service registered successfully for: {}", directory);
            while ( running.get() && !Thread.currentThread().isInterrupted() ) {
                WatchKey key = waitForWatchKey(ws);
                if ( key != null ) {
                    processWatchEvents(key);
                    if ( !key.reset() ) {
                        logger.warn("Watch key no longer valid, stopping watcher");
                        running.set(false);
                    }
                } else {
                    running.set(false);
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
        if ( filePath == null )
            return;
        key.pollEvents().forEach(event -> {
            Object context = event.context();
            boolean isModified = (context instanceof Path)
                    ? filePath.getFileName().equals(context)
                    : filePath.getFileName().toString().equals(context.toString());
            if ( isModified ) {
                logger.debug("Detected filesystem event [{}] on {}", event.kind(), filePath.getFileName());
                triggerReload();
            }
        });
    }

    private void triggerReload() {
        try {
            debouncedReloadExecutor.trigger(
                    getBatch().getName(),
                    this::performReload,
                    getReloadDelay()
            );
            logger.debug("Reload triggered for {}", getServiceName());
        } catch (Exception e) {
            logger.error("Error triggering reload", e);
        }
    }

    private void cleanup() {
        running.set(false);
        watchServiceRef.set(SERVICE_CLEAN_VALUE);
        watchedFilePathRef.set(PATH_CLEAN_VALUE);
        logger.debug("Watch service cleanup completed");
    }

    private Path resolveFilePath() {
        try {
            String filePath = getFilePath();
            if ( filePath.startsWith(FILE_PROTOCOL) ) {
                filePath = filePath.substring(FILE_PROTOCOL.length());
            }
            Path path = Paths.get(filePath);
            logger.debug("Resolved file path: {} -> {}", getFilePath(), path.toAbsolutePath());
            return path;
        } catch (Exception e) {
            throw new IllegalStateException("Cannot resolve file path: " + getFilePath(), e);
        }
    }

    private void validateFilePath(Path path) {
        if ( path.getParent() == null ) {
            throw new IllegalStateException("File must be in a directory. Invalid path: " + path);
        }
        if ( !Files.exists(path) ) {
            logger.warn("File does not exist yet: {}", path);
        }
    }

    private boolean isFileProtocol() {
        return getFilePath() != null && getFilePath().startsWith(FILE_PROTOCOL);
    }

    private void cancelPendingReloads() {
        boolean cancelled = debouncedReloadExecutor.cancel(getBatch().getName());
        if ( cancelled ) {
            logger.info("Cancelled pending reload for {}", getServiceName());
        }
    }

}