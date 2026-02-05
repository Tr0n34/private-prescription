package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.init;

import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch.ErrorCatalogLoader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ErrorCatalogStartup {

    private final ErrorCatalogLoader errorCatalogLoader;

    public ErrorCatalogStartup(ErrorCatalogLoader errorCatalogLoader) {
        this.errorCatalogLoader = errorCatalogLoader;
    }

    @PostConstruct
    public void init() {
        errorCatalogLoader.reload();
    }

}
