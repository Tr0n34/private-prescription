package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.init;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ErrorCatalogLoader;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Component
@EnableAsync
public class ErrorCatalogStartup {

    private final ErrorCatalogLoader errorCatalogLoader;

    public ErrorCatalogStartup(ErrorCatalogLoader errorCatalogLoader) {
        this.errorCatalogLoader = errorCatalogLoader;
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        errorCatalogLoader.reload();
    }

}
