package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.metrics;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.LongAdder;

@Component
public class TraceDropMetrics {

    private final LongAdder dropped = new LongAdder();

    public void incrementDropped() {
        dropped.increment();
    }

    public long droppedCount() {
        return dropped.sum();
    }

}
