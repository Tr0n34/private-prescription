package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.TraceWriterProperties;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.InMemoryActeMetierCache;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.TraceEntityMapper;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Component
@ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "INTERNAL_QUEUEING")
public class TraceWriterSupervisor implements SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(TraceWriterSupervisor.class);

    private static final String PREFIX_THREAD_TRACEWRITER = "trace-writer-";
    private static final String SUPERVISOR_THREAD_NAME = "trace-supervisor";

    private static final int FIRST_WORKER_ID = 0;
    private static final int MONITOR_INITIAL_DELAY_SEC = 1;
    private static final int MONITOR_DELAY_SEC = 1;

    private static final boolean NOT_STARTED = false;
    private static final boolean STARTED = true;

    private static final int JPA_ENTITY_BEFORE_FLUSH = 200;

    private final TraceEntityMapper traceEntityMapper;
    private final ActeMetierJpaRepository acteMetierJpaRepository;
    private final InMemoryActeMetierCache acteMetierCache;
    private final ObjectMapper traceObjectMapper;
    private final EntityManagerFactory traceEmf;
    private final BlockingQueue<Trace> traceQueue;
    private final int batchSize;
    private final Duration flushInterval;
    private final int workers;
    private final Duration restartDelay;
    private final ExecutorService workerPool;
    private final ScheduledExecutorService supervisor;

    private final ConcurrentMap<Integer, Future<?>> runningWorkers = new ConcurrentHashMap<>();
    private final AtomicBoolean isStarted = new AtomicBoolean(NOT_STARTED);
    private final AtomicReference<ScheduledFuture<?>> monitorFuture = new AtomicReference<>();

    private volatile boolean running = NOT_STARTED;



    public TraceWriterSupervisor(
            TraceEntityMapper traceEntityMapper,
            ActeMetierJpaRepository acteMetierJpaRepository,
            InMemoryActeMetierCache acteMetierCache,
            @Qualifier("traceContextMapper") ObjectMapper traceObjectMapper,
            @Qualifier("traceEntityManagerFactory") EntityManagerFactory traceEmf,
            BlockingQueue<Trace> traceQueue,
            TraceWriterProperties props
    ) {
        this.traceEntityMapper = traceEntityMapper;
        this.acteMetierJpaRepository = acteMetierJpaRepository;
        this.acteMetierCache = acteMetierCache;
        this.traceObjectMapper = traceObjectMapper;
        this.traceEmf = traceEmf;
        this.traceQueue = traceQueue;
        this.batchSize = props.batchSize();
        this.flushInterval = props.flushInterval();
        this.workers = props.workers();
        this.restartDelay = props.restartDelay();
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().name(PREFIX_THREAD_TRACEWRITER, FIRST_WORKER_ID).factory();
        this.workerPool = Executors.newThreadPerTaskExecutor(virtualThreadFactory);
        this.supervisor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, SUPERVISOR_THREAD_NAME);
            thread.setDaemon(true);
            return thread;
        });
    }

    @Override
    public synchronized void start() {
        if ( !acteMetierCache.isReady() ) {
            logger.error("TraceWriterSupervisor not started: acteMetierCache.isReady() = {}", acteMetierCache.isReady());
            throw new IllegalStateException("TraceWriterSupervisor not started: no data in cache");
        }
        if ( isStarted.compareAndSet(NOT_STARTED, STARTED) ) {
            running = true;
            if ( workers > 0 ) {
                startAllWorkers();
            }
            startMonitoring();
            logger.info("Trace writer supervisor started with workers: {}", workers);
        }
    }

    @Override
    public synchronized void stop() {
        running = false;
        ScheduledFuture<?> future = monitorFuture.getAndSet(null);
        if ( future != null ) {
            future.cancel(true);
        }
        for ( Future<?> f : runningWorkers.values() ) {
            f.cancel(true);
        }
        runningWorkers.clear();
        isStarted.set(NOT_STARTED);
        logger.info("Trace writer supervisor stopped");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public int getPhase() {
        return Integer.MAX_VALUE;
    }

    @PreDestroy
    public void shutdown() {
        try {
            stop();
        } catch (Exception e) {
            logger.warn("Error while stopping TraceWriterSupervisor during shutdown", e);
        }
        workerPool.shutdownNow();
        supervisor.shutdownNow();
        logger.info("Trace writer supervisor shutdown");
    }

    private void startAllWorkers() {
        for ( int i = FIRST_WORKER_ID; i < workers; i++ ) {
            startWorker(i);
        }
    }

    private void startWorker(int workerId) {
        if ( running ) {
            Future<?> future = workerPool.submit(() -> runLoop(workerId));
            runningWorkers.put(workerId, future);
            logger.info("Trace worker {} started", workerId);
        }
    }

    private void startMonitoring() {
        ScheduledFuture<?> existing = monitorFuture.get();
        boolean shouldStart = existing == null || existing.isDone() || existing.isCancelled();
        if ( shouldStart ) {
            ScheduledFuture<?> newFuture = supervisor.scheduleWithFixedDelay(
                    this::monitorWorkers,
                    MONITOR_INITIAL_DELAY_SEC,
                    MONITOR_DELAY_SEC,
                    TimeUnit.SECONDS
            );
            monitorFuture.set(newFuture);
        }
    }

    private void monitorWorkers() {
        if ( running ) {
            for ( int i = FIRST_WORKER_ID; i < workers; i++ ) {
                Future<?> future = runningWorkers.get(i);
                if ( future == null ) {
                    scheduleRestart(i, "missing-future");
                } else if ( future.isDone() || future.isCancelled() ) {
                    scheduleRestart(i, "done/cancelled");
                }
            }
        }
    }

    private void scheduleRestart(int workerId, String reason) {
        logger.warn("Trace writer {} not running ({}). Restart scheduled in {} ms",
                workerId, reason, restartDelay.toMillis());
        runningWorkers.remove(workerId);
        supervisor.schedule( () -> {
            if ( running ) {
                try {
                    startWorker(workerId);
                } catch (Exception e) {
                    logger.error("Failed to restart trace writer {}", workerId, e);
                }
            }
        }, restartDelay.toMillis(), TimeUnit.MILLISECONDS);
    }

    private void runLoop(int workerId) {
        final List<Trace> buffer = new ArrayList<>(Math.max(1, batchSize));
        while ( running && !Thread.currentThread().isInterrupted() ) {
            try {
                buffer.clear();
                Trace first = traceQueue.poll(flushInterval.toMillis(), TimeUnit.MILLISECONDS);
                if ( first == null ) {
                    continue;
                }
                buffer.add(first);
                int remaining = Math.max(0, batchSize - 1);
                if (remaining > 0) {
                    traceQueue.drainTo(buffer, remaining);
                }
                persistBatch(buffer);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                logger.warn("Trace writer {} interrupted", workerId);
                return;
            } catch (Exception ex) {
                logger.error("Trace writer {} crashed", workerId, ex);
                return; // Future.isDone() => monitor -> restart
            }
        }
    }

    private void persistBatch(List<Trace> traces) {
        EntityTransaction transaction = null;
        try ( EntityManager entityManager = traceEmf.createEntityManager() ) {
            try {
                transaction = entityManager.getTransaction();
                transaction.begin();
                int i = 0;
                for ( Trace trace : traces ) {
                    TraceEntity entity = traceEntityMapper.toEntity(trace, acteMetierJpaRepository, traceObjectMapper);
                    entityManager.persist(entity);
                    i++;
                    if ( i % JPA_ENTITY_BEFORE_FLUSH == 0 ) {
                        entityManager.flush();
                        entityManager.clear();
                    }
                }
                entityManager.flush();
                entityManager.clear();
                transaction.commit();
            } catch (RuntimeException e) {
                if ( transaction != null && transaction.isActive() ) {
                    transaction.rollback();
                }
                throw e;
            }
        } catch (Exception closeEx) {
            logger.warn("Failed to close EntityManager", closeEx);
        }
    }

}
