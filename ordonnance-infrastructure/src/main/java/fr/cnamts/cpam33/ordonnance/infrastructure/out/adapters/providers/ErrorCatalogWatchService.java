package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.watchers.AbstractFileWatchService;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.Batch;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ErrorCatalogLoader;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.DebouncedReloadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Objects;

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

    public ErrorCatalogWatchService(
            ErrorCatalogLoader errorCatalogLoader,
            @Qualifier("errorCatalogDebounceExecutor") DebouncedReloadExecutor debouncedReloadExecutor,
            @Qualifier("watcherTaskExecutor") TaskExecutor taskExecutor) {
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
