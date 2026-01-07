package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.watchers;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.providers.ErrorCatalogWatchService;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@WebEndpoint(id = "error-catalog-watcher")
public class ErrorCatalogWatcherEndpoint {

    private final ErrorCatalogWatchService watcher;

    public ErrorCatalogWatcherEndpoint(ErrorCatalogWatchService watcher) {
        this.watcher = watcher;
    }

    @ReadOperation
    public WatcherStatusResponse status() {
        WatcherStatus status = watcher.isRunning()
                ? WatcherStatus.RUNNING
                : WatcherStatus.STOPPED;
        return new WatcherStatusResponse(status, Instant.now());
    }

    @WriteOperation
    public WatcherActionResponse executeAction(@Selector String action) {
        WatcherAction watcherAction = WatcherAction.fromString(action);
        WatcherStatus status = processAction(watcherAction);
        return new WatcherActionResponse(watcherAction, status, Instant.now());
    }

    private WatcherStatus processAction(WatcherAction action) {
        return switch (action) {
            case START -> {
                watcher.startWatching();
                yield WatcherStatus.STARTED;
            }
            case STOP -> {
                watcher.stopWatching();
                yield WatcherStatus.STOPPED;
            }
            case UNKNOWN -> WatcherStatus.UNKNOWN;
        };
    }

}