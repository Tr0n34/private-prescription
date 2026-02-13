package fr.cnamts.cpam33.ordonnance.infrastructure.abstracts;

import java.time.Instant;
import java.util.UUID;

public interface Event {

    UUID eventId();

    Instant occuredAt();

}
