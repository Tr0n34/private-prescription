package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch;

import fr.cnamts.cpam33.ordonnance.infrastructure.technical.DebouncedReloadExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class LoaderConfiguration {

    public static final String LOADER_THREAD_NAME_PREFIX = "loader-";
    public static final String WATCHER_THREAD_NAME_PREFIX = "watcher-";

    private final int corePoolSize;

    public LoaderConfiguration(@Value("${ordonnance.loaders.core_pool_size:1}") int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    @Bean("taskExecutor")
    public TaskExecutor loadTaskExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(corePoolSize);
        exec.setThreadNamePrefix(LOADER_THREAD_NAME_PREFIX);
        exec.initialize();
        return exec;
    }

    @Bean("watcherTaskExecutor")
    public TaskExecutor watcherTaskExecutor() {
        SimpleAsyncTaskExecutor exec = new SimpleAsyncTaskExecutor(WATCHER_THREAD_NAME_PREFIX);
        exec.setVirtualThreads(true);
        return exec;
    }

    @Bean("errorCatalogDebounceExecutor")
    public DebouncedReloadExecutor errorCatalogDebounceExecutor() {
        return new DebouncedReloadExecutor();
    }

    @Bean("actesMetiersDebounceExecutor")
    public DebouncedReloadExecutor actesMetiersDebounceExecutor() {
        return new DebouncedReloadExecutor();
    }

}
