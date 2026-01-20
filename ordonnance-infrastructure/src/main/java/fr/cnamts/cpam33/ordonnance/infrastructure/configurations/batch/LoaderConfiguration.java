package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch;

import fr.cnamts.cpam33.ordonnance.infrastructure.technical.DebouncedReloadExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class LoaderConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(LoaderConfiguration.class);

    public static final String LOADER_THREAD_NAME_PREFIX = "loader-";
    public static final String WATCHER_THREAD_NAME_PREFIX = "watcher-";

    private final int corePoolSize;

    public LoaderConfiguration(@Value("${ordonnance.loaders.core_pool_size:1}") int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    @Bean("taskExecutor")
    public TaskExecutor loadTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setThreadNamePrefix(LOADER_THREAD_NAME_PREFIX);
        threadPoolTaskExecutor.initialize();
        logger.debug("taskExecutor : {}, corePoolSize={}",
                threadPoolTaskExecutor.getThreadNamePrefix(),
                threadPoolTaskExecutor.getCorePoolSize());
        return threadPoolTaskExecutor;
    }

    @Bean("watcherTaskExecutor")
    public TaskExecutor watcherTaskExecutor() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor(WATCHER_THREAD_NAME_PREFIX);
        simpleAsyncTaskExecutor.setVirtualThreads(true);
        logger.debug("watcherTaskExecutor : {} isActive={}",
                simpleAsyncTaskExecutor.getThreadNamePrefix(),
                simpleAsyncTaskExecutor.isActive());
        return simpleAsyncTaskExecutor;
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
