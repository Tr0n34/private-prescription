package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class LoaderConfiguration {

    public static final String THREAD_NAME_PREFIX = "loader-";

    @Bean("taskExecutor")
    public TaskExecutor loadTaskExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(1);
        exec.setThreadNamePrefix(THREAD_NAME_PREFIX);
        exec.initialize();
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
