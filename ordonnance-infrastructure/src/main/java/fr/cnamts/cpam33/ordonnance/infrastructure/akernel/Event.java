package fr.cnamts.cpam33.ordonnance.infrastructure.akernel;

import java.time.Instant;
import java.util.UUID;

public interface Event {

    UUID eventId();

    Instant occuredAt();

}
