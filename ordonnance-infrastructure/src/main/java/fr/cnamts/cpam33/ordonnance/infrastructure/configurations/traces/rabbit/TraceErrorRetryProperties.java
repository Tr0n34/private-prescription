package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.rabbit;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "ordonnance.traces.errors")
public class TraceErrorRetryProperties {

    private RetryJobMode mode;
    private int poolSize;
    private Duration schedulerDelay;
    private int maxRetries;
    private Duration initialDelay;
    private Duration maxDelay;
    private double backoffMultiplier;

    public TraceErrorRetryProperties(RetryJobMode mode,
                                     int poolSize,
                                     Duration schedulerDelay,
                                     int maxRetries,
                                     Duration initialDelay,
                                     Duration maxDelay,
                                     double backoffMultiplier) {
        this.mode = mode;
        this.poolSize = poolSize;
        this.schedulerDelay = schedulerDelay;
        this.maxRetries = maxRetries;
        this.initialDelay = initialDelay;
        this.maxDelay = maxDelay;
        this.backoffMultiplier = backoffMultiplier;
    }

    public RetryJobMode mode() {
        return mode;
    }

    public TraceErrorRetryProperties setMode(RetryJobMode mode) {
        this.mode = mode;
        return this;
    }

    public int poolSize() {
        return poolSize;
    }

    public TraceErrorRetryProperties setPoolSize(int poolSize) {
        this.poolSize = poolSize;
        return this;
    }

    public Duration schedulerDelay() {
        return schedulerDelay;
    }

    public TraceErrorRetryProperties setSchedulerDelay(Duration schedulerDelay) {
        this.schedulerDelay = schedulerDelay;
        return this;
    }

    public int maxRetries() {
        return maxRetries;
    }

    public TraceErrorRetryProperties setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    public Duration initialDelay() {
        return initialDelay;
    }

    public TraceErrorRetryProperties setInitialDelay(Duration initialDelay) {
        this.initialDelay = initialDelay;
        return this;
    }

    public Duration maxDelay() {
        return maxDelay;
    }

    public TraceErrorRetryProperties setMaxDelay(Duration maxDelay) {
        this.maxDelay = maxDelay;
        return this;
    }

    public double backoffMultiplier() {
        return backoffMultiplier;
    }

    public TraceErrorRetryProperties setBackoffMultiplier(double backoffMultiplier) {
        this.backoffMultiplier = backoffMultiplier;
        return this;
    }

}
