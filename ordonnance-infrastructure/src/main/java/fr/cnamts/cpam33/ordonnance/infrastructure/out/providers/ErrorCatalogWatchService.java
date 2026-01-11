package fr.cnamts.cpam33.ordonnance.infrastructure.out.providers;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.watchers.AbstractFileWatchService;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.Batch;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.DebouncedReloadExecutor;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ErrorCatalogLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.WatchService;
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
public class ErrorCatalogWatchService extends AbstractFileWatchService implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ErrorCatalogWatchService.class);
    private static final String FILE_PROTOCOL = "file:";

    @Value("${ordonnance.loaders.errors.file:classpath:errors.json}")
    private String errorFile;

    @Value("${ordonnance.loaders.errors.reload-delay:5000}")
    private int reloadDelay;

    private final ErrorCatalogLoader errorCatalogLoader;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicReference<WatchService> watchServiceRef = new AtomicReference<>();
    private final AtomicReference<Path> watchedFilePathRef = new AtomicReference<>();

    public ErrorCatalogWatchService(
            ErrorCatalogLoader errorCatalogLoader,
            @Qualifier("errorCatalogDebounceExecutor") DebouncedReloadExecutor debouncedReloadExecutor,
            TaskExecutor taskExecutor) {
        super(debouncedReloadExecutor, taskExecutor);
        this.errorCatalogLoader = Objects.requireNonNull(errorCatalogLoader);
    }


    @Override
    public String getFilePath() {
        return errorFile;
    }

    @Override
    public int getReloadDelay() {
        return reloadDelay;
    }

    @Override
    public Batch getBatch() {
        return Batch.ERROR;
    }

    @Override
    public String getServiceName() {
        return Batch.ERROR.getName();
    }

    @Override
    public void performReload() {
        errorCatalogLoader.reload();
    }
}
