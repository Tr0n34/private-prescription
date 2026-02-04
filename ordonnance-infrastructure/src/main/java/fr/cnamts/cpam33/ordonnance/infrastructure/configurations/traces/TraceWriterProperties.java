package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "ordonnance.traces.writer")
public record TraceWriterProperties(
        int queueCapacity,
        int batchSize,
        Duration flushInterval,
        int workers,
        Duration restartDelay
) {

    public static final int DEFAULT_QUEUE_CAPACITY = 50_000;
    public static final int DEFAULT_BATCH_SIZE = 500;
    public static final int DEFAULT_FLUSH_INTERVAL_IN_MILLIS = 50;
    public static final int DEFAULT_WORKERS = 2;
    public static final int DEFAULT_RESTART_DELAY_IN_MILLIS = 5000;

    public static final int MIN_QUEUE_CAPACITY = 0;
    public static final int MIN_BATCH_SIZE = 0;
    public static final int MIN_WORKER = 1;


    public TraceWriterProperties {
        if ( queueCapacity <= MIN_QUEUE_CAPACITY) {
            queueCapacity = DEFAULT_QUEUE_CAPACITY;
        }
        if ( batchSize <= MIN_BATCH_SIZE) {
            batchSize = DEFAULT_BATCH_SIZE;
        }
        if ( flushInterval == null || flushInterval.isZero() || flushInterval.isNegative() ) {
            flushInterval = Duration.ofMillis(DEFAULT_FLUSH_INTERVAL_IN_MILLIS);
        }
        if ( workers < MIN_WORKER ) {
            workers = DEFAULT_WORKERS;
        }
        if ( restartDelay == null ) {
            restartDelay = Duration.ofMillis(DEFAULT_RESTART_DELAY_IN_MILLIS);
        }
    }

}
