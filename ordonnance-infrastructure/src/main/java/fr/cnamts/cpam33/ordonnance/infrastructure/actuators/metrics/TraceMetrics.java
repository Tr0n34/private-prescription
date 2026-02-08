package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.metrics;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.LongAdder;

@Component
public class TraceMetrics {

    private final LongAdder offered = new LongAdder();
    private final LongAdder enqueued = new LongAdder();
    private final LongAdder outboxed = new LongAdder();
    private final LongAdder outboxWriteFailed = new LongAdder();
    private final LongAdder enqueueTimeout = new LongAdder();

    public void incrementOffered() {
        offered.increment();
    }

    public void incrementEnqueued() {
        enqueued.increment();
    }

    public void incrementOutboxed() {
        outboxed.increment();
    }

    public void incrementOutboxWriteFailed() {
        outboxWriteFailed.increment();
    }

    public void incrementEnqueueTimeout() {
        enqueueTimeout.increment();
    }

    public long offeredCount() {
        return offered.sum();
    }

    public long enqueuedCount() {
        return enqueued.sum();
    }

    public long outboxedCount() {
        return outboxed.sum();
    }

    public long outboxWriteFailedCount() {
        return outboxWriteFailed.sum();
    }

    public long enqueueTimeoutCount() {
        return enqueueTimeout.sum();
    }

}
