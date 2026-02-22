package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.internal;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propriété du gestionnaire de Queue pour les traces
 * @param corePoolSize La taille du pool pour gérer la mise en queue ou le dépilage
 * @param maxPoolSize La taille maximum de ce pool
 * @param queueCapacity La capacité maximal de cette queue (en nombre de traces)
 * @param threadNamePrefix Le prefix de nommage des threads utilisés par ce pool
 * @param waitForTasksToCompleteOnShutdown Le temps d'attente avant l'arret d'une tâche
 */
@ConfigurationProperties(prefix = "ordonnance.traces.enqueue-executor")
public record TraceEnqueueExecutorProperties(
        int corePoolSize,
        int maxPoolSize,
        int queueCapacity,
        String threadNamePrefix,
        boolean waitForTasksToCompleteOnShutdown
) {

    public static final int DEFAULT_CORE_POOL_SIZE = 2;
    public static final int DEFAULT_MAX_POOL_SIZE = 4;
    public static final int DEFAULT_QUEUE_CAPACITY = 20_000;
    public static final String TRACE_ENQ = "trace-enq-";
    public static final int MIN_POOL_SIZE = 0;
    public static final int MIN_QUEUE_CAPACITY = 0;

    public TraceEnqueueExecutorProperties {
        if ( corePoolSize <= MIN_POOL_SIZE) {
            corePoolSize = DEFAULT_CORE_POOL_SIZE;
        }
        if ( maxPoolSize <= MIN_POOL_SIZE) {
            maxPoolSize = DEFAULT_MAX_POOL_SIZE;
        }
        if ( queueCapacity <= MIN_QUEUE_CAPACITY ) {
            queueCapacity = DEFAULT_QUEUE_CAPACITY;
        }
        if ( threadNamePrefix == null || threadNamePrefix.isBlank() ) {
            threadNamePrefix = TRACE_ENQ;
        }
    }

}