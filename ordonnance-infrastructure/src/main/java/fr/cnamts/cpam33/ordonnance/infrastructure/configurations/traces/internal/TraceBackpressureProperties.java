package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "ordonnance.traces.backpressure")
public record TraceBackpressureProperties(
        Duration offerTimeout,
        Duration degradedWindow,
        double degradedFallbackRateThreshold
) {
    public static final Duration DEFAULT_OFFER_TIMEOUT = Duration.ofMillis(20);
    public static final Duration DEFAULT_DEGRADED_WINDOW = Duration.ofSeconds(60);
    public static final double DEFAULT_DEGRADED_FALLBACK_RATE_THRESHOLD = 0.001;

    public TraceBackpressureProperties {
        if ( offerTimeout == null || offerTimeout.isZero() || offerTimeout.isNegative() ) {
            offerTimeout = DEFAULT_OFFER_TIMEOUT;
        }
        if ( degradedWindow == null || degradedWindow.isZero() || degradedWindow.isNegative() ) {
            degradedWindow = DEFAULT_DEGRADED_WINDOW;
        }
        if ( degradedFallbackRateThreshold <= 0 ) {
            degradedFallbackRateThreshold = DEFAULT_DEGRADED_FALLBACK_RATE_THRESHOLD;
        }
    }

}
