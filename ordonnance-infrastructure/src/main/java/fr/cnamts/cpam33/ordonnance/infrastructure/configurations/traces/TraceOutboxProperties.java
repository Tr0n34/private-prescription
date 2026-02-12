package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "ordonnance.traces.outbox")
public record TraceOutboxProperties(
        int batchSize,
        Duration pollInterval,
        int maxAttempts
) {
    public static final int DEFAULT_BATCH_SIZE = 500;
    public static final Duration DEFAULT_POLL_INTERVAL = Duration.ofMillis(200);
    public static final int DEFAULT_MAX_ATTEMPTS = 20;

    public TraceOutboxProperties {
        if ( batchSize <= 0 ) {
            batchSize = DEFAULT_BATCH_SIZE;
        }
        if ( pollInterval == null || pollInterval.isZero() || pollInterval.isNegative() ) {
            pollInterval = DEFAULT_POLL_INTERVAL;
        }
        if ( maxAttempts <= 0 ) {
            maxAttempts = DEFAULT_MAX_ATTEMPTS;
        }
    }

}