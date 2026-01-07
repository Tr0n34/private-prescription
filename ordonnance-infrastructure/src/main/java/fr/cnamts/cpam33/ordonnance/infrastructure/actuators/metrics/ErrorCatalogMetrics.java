package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class ErrorCatalogMetrics {

    public static final String ERROR_CATALOG_RELOADS_SUCCESS = "error_catalog.reloads.success";
    public static final String ERROR_CATALOG_RELOADS_FAILURE = "error_catalog.reloads.failure";
    public static final String ERROR_CATALOG_RELOADS_DURATION = "error_catalog.reloads.duration";

    private final Counter reloadSuccessCounter;
    private final Counter reloadFailureCounter;
    private final Timer reloadTimer;

    public ErrorCatalogMetrics(MeterRegistry registry) {
        this.reloadSuccessCounter = Counter.builder(ERROR_CATALOG_RELOADS_SUCCESS)
                .description("Number of successful error catalog reloads")
                .register(registry);
        this.reloadFailureCounter = Counter.builder(ERROR_CATALOG_RELOADS_FAILURE)
                .description("Number of failed error catalog reloads")
                .register(registry);
        this.reloadTimer = Timer.builder(ERROR_CATALOG_RELOADS_DURATION)
                .description("Duration of error catalog reloads")
                .register(registry);
    }

    public void recordReload(Runnable reloadLogic) {
        try {
            reloadTimer.record(reloadLogic);
            reloadSuccessCounter.increment();
        } catch (Exception e) {
            reloadFailureCounter.increment();
            throw e;
        }
    }

}
