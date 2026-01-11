package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ordonnance.traces.enqueue-executor")
public record TraceEnqueueExecutorProperties(
        int corePoolSize,
        int maxPoolSize,
        int queueCapacity,
        String threadNamePrefix,
        boolean waitForTasksToCompleteOnShutdown
) {

    public TraceEnqueueExecutorProperties {
        if ( corePoolSize <= 0 ) corePoolSize = 2;
        if ( maxPoolSize <= 0 ) maxPoolSize = 4;
        if ( queueCapacity <= 0 ) queueCapacity = 20_000;
        if ( threadNamePrefix == null || threadNamePrefix.isBlank() ) {
            threadNamePrefix = "trace-enq-";
        }
    }

}