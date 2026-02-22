package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.internal;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.metrics.TraceMetrics;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.internal.TraceBackpressureProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

@Component
@ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "INTERNAL_QUEUEING")
public class BufferedJpaTracePublisher implements TracePublisher {

    private static final Logger logger = LoggerFactory.getLogger(BufferedJpaTracePublisher.class);

    private final BlockingQueue<Trace> traceQueue;
    private final TraceBackpressureProperties traceBackpressureProperties;
    private final TraceOutboxWriter traceOutboxWriter;
    private final TraceMetrics traceMetrics;

    public BufferedJpaTracePublisher(BlockingQueue<Trace> traceQueue,
                                     TraceBackpressureProperties traceBackpressureProperties,
                                     TraceOutboxWriter traceOutboxWriter,
                                     TraceMetrics traceMetrics) {
        this.traceQueue = traceQueue;
        this.traceBackpressureProperties = traceBackpressureProperties;
        this.traceOutboxWriter = traceOutboxWriter;
        this.traceMetrics = traceMetrics;
    }

    @Async("traceEnqueueExecutor")
    @Override
    public void publish(Trace trace) {
        boolean handled = false;
        traceMetrics.incrementOffered();
        if ( trace != null ) {
            if ( tryEnqueue(trace) ) {
                traceMetrics.incrementEnqueued();
            } else {
                traceMetrics.incrementEnqueueTimeout();
                persistToOutbox(trace);
            }
            handled = true;
        }
        logger.trace("Trace published: {} handled: {}", trace, handled);
    }

    private boolean tryEnqueue(Trace trace) {
        boolean accepted = false;
        try {
            accepted = traceQueue.offer(
                    trace,
                    traceBackpressureProperties.offerTimeout().toMillis(),
                    TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.error("Error while trying to enqueue trace {}", trace, e);
            Thread.currentThread().interrupt();
        }
        return accepted;
    }

    private void persistToOutbox(Trace trace) {
        try {
            traceOutboxWriter.write(trace);
            traceMetrics.incrementOutboxed();
            logger.warn("Trace queue saturated -> persisted to outbox");
        } catch (RuntimeException ex) {
            traceMetrics.incrementOutboxWriteFailed();
            logger.error("Trace queue saturated AND outbox write failed -> TRACE AT RISK", ex);
            throw ex;
        }
    }

}
