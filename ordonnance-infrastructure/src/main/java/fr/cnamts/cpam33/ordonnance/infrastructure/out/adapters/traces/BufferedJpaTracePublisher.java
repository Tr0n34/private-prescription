package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.infrastructure.actuators.metrics.TraceDropMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class BufferedJpaTracePublisher implements TracePublisher {

    private static final Logger logger = LoggerFactory.getLogger(BufferedJpaTracePublisher.class);

    private final BlockingQueue<Trace> traceQueue;
    private final TraceDropMetrics dropMetrics;

    public BufferedJpaTracePublisher(BlockingQueue<Trace> traceQueue, TraceDropMetrics dropMetrics) {
        this.traceQueue = traceQueue;
        this.dropMetrics = dropMetrics;
    }

    @Async("traceEnqueueExecutor")
    @Override
    public void publish(Trace trace) {
        boolean accepted = traceQueue.offer(trace);
        if ( !accepted ) {
            dropMetrics.incrementDropped();
            logger.warn("Trace queue full -> dropping trace");
        }
    }

}
