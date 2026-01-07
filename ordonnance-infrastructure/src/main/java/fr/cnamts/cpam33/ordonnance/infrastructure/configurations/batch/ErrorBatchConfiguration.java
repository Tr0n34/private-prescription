package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.batch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ErrorBatchConfiguration {

    public static final String THREAD_NAME_PREFIX = "error-catalog-";

    @Bean
    public TaskExecutor errorCatalogTaskExecutor() {
        ThreadPoolTaskExecutor exec = new ThreadPoolTaskExecutor();
        exec.setCorePoolSize(1);
        exec.setThreadNamePrefix(THREAD_NAME_PREFIX);
        exec.initialize();
        return exec;
    }

    @Bean
    public DebouncedReloadExecutor errorCatalogDebounceExecutor() {
        return new DebouncedReloadExecutor();
    }

}
