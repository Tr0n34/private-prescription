package fr.cnamts.cpam33.ordonnance.infrastructure.contexts;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ErrorCatalogLoader;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.DebouncedReloadExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import static org.mockito.Mockito.mock;

@Configuration
public class TestWatcherConfig {

    @Bean
    ErrorCatalogLoader errorCatalogLoader() {
        return mock(ErrorCatalogLoader.class);
    }

    @Bean(name = "errorCatalogDebounceExecutor")
    DebouncedReloadExecutor errorCatalogDebounceExecutor() {
        return mock(DebouncedReloadExecutor.class);
    }

    @Bean(name = "watcherTaskExecutor")
    TaskExecutor watcherTaskExecutor() {
        return mock(TaskExecutor.class);
    }

}
