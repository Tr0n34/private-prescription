package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.watchers;

import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.watchers.WatcherAction;
import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.watchers.WatcherActionResponse;
import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.watchers.WatcherStatus;
import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.watchers.WatcherStatusResponse;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

import java.time.Instant;

public abstract class AbstractWatcherEndpoint {

    private final AbstractFileWatchService fileWatchService;

    protected AbstractWatcherEndpoint(AbstractFileWatchService fileWatchService) {
        this.fileWatchService = fileWatchService;
    }

    @ReadOperation
    public WatcherStatusResponse status() {
        WatcherStatus status = fileWatchService.isRunning()
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
                fileWatchService.startWatching();
                yield WatcherStatus.STARTED;
            }
            case STOP -> {
                fileWatchService.stopWatching();
                yield WatcherStatus.STOPPED;
            }
            case UNKNOWN -> WatcherStatus.UNKNOWN;
        };
    }

}
