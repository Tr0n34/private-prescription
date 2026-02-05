package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.watchers;

import java.time.Instant;

public record WatcherStatusResponse(
        WatcherStatus status,
        Instant timestamp
) {

}
