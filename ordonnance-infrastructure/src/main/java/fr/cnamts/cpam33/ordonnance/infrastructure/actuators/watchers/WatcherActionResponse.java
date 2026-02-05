package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.watchers;

import java.time.Instant;
import java.util.Objects;

public record WatcherActionResponse(
        WatcherAction action,
        WatcherStatus status,
        Instant timestamp
) {

    public WatcherActionResponse {
        Objects.requireNonNull(action, "action cannot be null");
        Objects.requireNonNull(status, "status cannot be null");
        Objects.requireNonNull(timestamp, "timestamp cannot be null");
    }

}