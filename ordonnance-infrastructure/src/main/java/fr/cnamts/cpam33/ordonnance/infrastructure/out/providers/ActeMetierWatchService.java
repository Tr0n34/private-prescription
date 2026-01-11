package fr.cnamts.cpam33.ordonnance.infrastructure.out.providers;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.watchers.AbstractFileWatchService;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.Batch;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch.DebouncedReloadExecutor;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ActeMetierLoader;
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

@Component
@ConditionalOnProperty(
        prefix = "ordonnance.loaders.actesMetiers.watcher",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class ActeMetierWatchService extends AbstractFileWatchService implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(ActeMetierWatchService.class);
    private static final String FILE_PROTOCOL = "file:";

    @Value("${ordonnance.loaders.actesMetiers.file:classpath:actes_metiers.json}")
    private String acteMetiersJson;

    @Value("${ordonnance.loaders.actesMetiers.reload-delay:5000}")
    private int reloadDelay;

    private final ActeMetierLoader acteMetierLoader;

    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicReference<WatchService> watchServiceRef = new AtomicReference<>();
    private final AtomicReference<Path> watchedFilePathRef = new AtomicReference<>();

    public ActeMetierWatchService(ActeMetierLoader acteMetierLoader,
                                  @Qualifier("actesMetiersDebounceExecutor") DebouncedReloadExecutor debouncedReloadExecutor,
                                  TaskExecutor taskExecutor) {
        super(debouncedReloadExecutor, taskExecutor);
        this.acteMetierLoader = Objects.requireNonNull(acteMetierLoader);
    }


    @Override
    public String getFilePath() {
        return acteMetiersJson;
    }

    @Override
    
    public int getReloadDelay() {
        return reloadDelay;
    }

    @Override
    public Batch getBatch() {
        return Batch.ACTE_METIER;
    }

    @Override
    public String getServiceName() {
        return Batch.ACTE_METIER.getName();
    }

    @Override
    public void performReload() {
        acteMetierLoader.reload();
    }

}
