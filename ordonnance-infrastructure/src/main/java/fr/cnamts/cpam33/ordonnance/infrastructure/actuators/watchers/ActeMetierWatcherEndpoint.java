package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.watchers;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.watchers.AbstractWatcherEndpoint;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers.ActeMetierWatchService;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;

@Component
@WebEndpoint(id = "actes-metiers-watcher")
public class ActeMetierWatcherEndpoint extends AbstractWatcherEndpoint {

    public ActeMetierWatcherEndpoint(ActeMetierWatchService watcher) {
        super(watcher);
    }

}