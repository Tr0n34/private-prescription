package fr.cnamts.cpam33.ordonnance.infrastructure.in.events;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.Event;

import java.time.Instant;
import java.util.UUID;

public record ActeMetierLoadedEvent(
        UUID eventId,
        Instant occuredAt
) implements Event {

}
