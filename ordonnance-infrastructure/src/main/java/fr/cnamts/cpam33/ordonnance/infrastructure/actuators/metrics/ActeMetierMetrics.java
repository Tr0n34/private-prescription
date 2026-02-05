package fr.cnamts.cpam33.ordonnance.infrastructure.actuators.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class ActeMetierMetrics {

    public static final String ACTES_METIERS_RELOADS_SUCCESS = "acte_metier.reloads.success";
    public static final String ACTES_METIERS_RELOADS_FAILURE = "acte_metier.reloads.failure";
    public static final String ACTES_METIERS_RELOADS_DURATION = "acte_metier.reloads.duration";

    private final Counter reloadSuccessCounter;
    private final Counter reloadFailureCounter;
    private final Timer reloadTimer;

    public ActeMetierMetrics(MeterRegistry registry) {
        this.reloadSuccessCounter = Counter.builder(ACTES_METIERS_RELOADS_SUCCESS)
                .description("Number of successful acte_metier catalog reloads")
                .register(registry);
        this.reloadFailureCounter = Counter.builder(ACTES_METIERS_RELOADS_FAILURE)
                .description("Number of failed acte_metier catalog reloads")
                .register(registry);
        this.reloadTimer = Timer.builder(ACTES_METIERS_RELOADS_DURATION)
                .description("Duration of acte_metier catalog reloads")
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
