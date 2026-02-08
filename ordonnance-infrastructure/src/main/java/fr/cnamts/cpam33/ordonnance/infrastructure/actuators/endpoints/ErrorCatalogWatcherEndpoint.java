package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.endpoints;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.watchers.AbstractWatcherEndpoint;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.ErrorCatalogWatchService;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;

@Component
@WebEndpoint(id = "error-catalog-watcher")
public class ErrorCatalogWatcherEndpoint extends AbstractWatcherEndpoint {

    public ErrorCatalogWatcherEndpoint(ErrorCatalogWatchService watcher) {
        super(watcher);
    }

}