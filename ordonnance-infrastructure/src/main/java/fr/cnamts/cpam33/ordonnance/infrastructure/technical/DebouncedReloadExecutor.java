package fr.cnamts.cpam33.ordonnance.infrastructure.technical;

import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.InfrastructureException;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.errors.enums.DebouncerExceptionCode;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

@Component
public class DebouncedReloadExecutor {

    private static final Logger logger = LoggerFactory.getLogger(DebouncedReloadExecutor.class);

    private static final long SHUTDOWN_TIMEOUT_SECONDS = 5;
    public static final String DEFAULT_DEBOUNCED_RELOAD = "debounced-reload";
    public static final boolean IS_DAEMON = true;

    private final ScheduledExecutorService scheduler;
    private final Map<String, ScheduledFuture<?>> pendingTasks;

    public DebouncedReloadExecutor() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, DEFAULT_DEBOUNCED_RELOAD);
            thread.setDaemon(IS_DAEMON);
            return thread;
        });
        this.pendingTasks = new ConcurrentHashMap<>();
    }

    /**
     * Déclenche un reload avec debounce, séparé par clé.
     * Si une tâche est déjà en attente pour cette clé, elle est annulée et remplacée.
     * @param key identifie le batch ou le fichier (ne doit pas être null)
     * @param action l'action à exécuter (ne doit pas être null)
     * @param delay délai en ms pour le debounce (doit être positif)
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public void trigger(String key, Runnable action, long delay) {
        validateParameters(key, action, delay);
        cancelPendingTask(key);
        scheduleNewTask(key, action, delay);
    }

    /**
     * Annule toutes les tâches en attente pour une clé donnée.
     * @param key la clé de la tâche à annuler
     * @return true si une tâche a été annulée, false sinon
     */
    public boolean cancel(String key) {
        ScheduledFuture<?> pending = pendingTasks.remove(key);
        return pending != null && !pending.isDone() && pending.cancel(false);
    }

    /**
     * Annule toutes les tâches en attente.
     * @return le nombre de tâches annulées
     */
    public int cancelAll() {
        int cancelled = 0;
        for ( String key : pendingTasks.keySet() ) {
            if ( cancel(key) ) {
                cancelled++;
            }
        }
        return cancelled;
    }

    public int getPendingTaskCount() {
        return pendingTasks.size();
    }

    public boolean hasPendingTask(String key) {
        ScheduledFuture<?> future = pendingTasks.get(key);
        return future != null && !future.isDone();
    }

    /**
     * Eteint l'ordonnanceur et l'ensemble des tâches
     */
    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down DebouncedReloadExecutor...");
        cancelAll();
        scheduler.shutdown();
        try {
            if ( !scheduler.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS) ) {
                logger.warn("Executor did not terminate in time, forcing shutdown");
                scheduler.shutdownNow();
                if ( !scheduler.awaitTermination(SHUTDOWN_TIMEOUT_SECONDS, TimeUnit.SECONDS) ) {
                    logger.error("Executor did not terminate after forced shutdown");
                }
            }
        } catch (InterruptedException e) {
            logger.error("Shutdown interrupted", e);
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        logger.info("DebouncedReloadExecutor shut down complete");
    }

    private void validateParameters(String key, Runnable action, long delay) {
        if ( key == null || key.isBlank() ) {
            throw new InfrastructureException(DebouncerExceptionCode.TECH_PENDING_TASK_KEY_NOT_NULL);
        }
        if ( action == null ) {
            throw new InfrastructureException(DebouncerExceptionCode.TECH_PENDING_TASK_ACTION_NOT_NULL);
        }
        if ( delay < 0 )  {
            throw new InfrastructureException(DebouncerExceptionCode.TECH_PENDING_TASK_DELAY_POSITIVE, Map.of("delay", delay));
        }
    }

    private void cancelPendingTask(String key) {
        ScheduledFuture<?> pending = pendingTasks.get(key);
        if (pending != null && !pending.isDone()) {
            boolean cancelled = pending.cancel(false);
            logger.debug("Cancelled pending task for key '{}': {}", key, cancelled);
        }
    }

    private void scheduleNewTask(String key, Runnable action, long delay) {
        ScheduledFuture<?> future = scheduler.schedule(
                () -> executeTask(key, action),
                delay,
                TimeUnit.MILLISECONDS
        );
        pendingTasks.put(key, future);
        logger.debug("Scheduled task for key '{}' with delay {}ms", key, delay);
    }

    private void executeTask(String key, Runnable action) {
        try {
            logger.debug("Executing debounced task for key '{}'", key);
            action.run();
            logger.debug("Successfully executed task for key '{}'", key);
        } catch (Exception e) {
            logger.error("Error executing debounced task for key '{}'", key, e);
        } finally {
            pendingTasks.remove(key);
        }
    }

}