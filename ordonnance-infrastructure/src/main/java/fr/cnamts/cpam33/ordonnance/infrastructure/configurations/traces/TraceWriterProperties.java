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

    public static final int QUEUE_CAPACITY = 50_000;
    public static final int BATCH_SIZE = 500;
    public static final int FLUSH_INTERVAL_IN_MILLIS = 50;
    public static final int WORKERS = 2;
    public static final int RESTART_DELAY_IN_MILLIS = 50;

    public TraceWriterProperties {
        if ( queueCapacity <= 0 ) queueCapacity = QUEUE_CAPACITY;
        if ( batchSize <= 0 ) batchSize = BATCH_SIZE;
        if ( flushInterval == null ) flushInterval = Duration.ofMillis(FLUSH_INTERVAL_IN_MILLIS);
        if  (workers <= 0 ) workers = WORKERS;
        if ( restartDelay == null ) restartDelay = Duration.ofSeconds(RESTART_DELAY_IN_MILLIS);
    }

}
