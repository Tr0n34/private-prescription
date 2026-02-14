package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.TraceWriterProperties;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.TraceEntityMapper;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class TraceWriterSupervisor implements SmartLifecycle {

    private static final Logger logger = LoggerFactory.getLogger(TraceWriterSupervisor.class);
    private static final String PREFIX_THREAD_TRACEWRITER = "trace-writer-";
    private static final int NUM_WORKER_DEFAULT = 0;
    private static final int INITIAL_DELAY = 1;
    private static final int DELAY = 1;
    private static final boolean INITIAL_START = false;
    private static final int JPA_ENTITY_BEFORE_FLUSH = 200;
    private static final String SUPERVISOR_THREAD_NAME = "trace-supervisor";

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
    private final ConcurrentMap<Integer, Future<?>> runningWorkers = new ConcurrentHashMap<>();
    private final AtomicBoolean started = new AtomicBoolean(INITIAL_START);
    private volatile boolean running = INITIAL_START;

    private final ScheduledExecutorService supervisor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, SUPERVISOR_THREAD_NAME);
        t.setDaemon(true);
        return t;
    });

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
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().name(PREFIX_THREAD_TRACEWRITER, NUM_WORKER_DEFAULT).factory();
        this.workerPool = Executors.newThreadPerTaskExecutor(virtualThreadFactory);
    }

    private void startAllWorkers() {
        for ( int i = NUM_WORKER_DEFAULT; i < workers; i++ ) {
            startWorker(i);
        }
    }

    private void startWorker(int workerId) {
        if ( !running ) return;
        Future<?> future = workerPool.submit(() -> runLoop(workerId));
        runningWorkers.put(workerId, future);
        logger.info("Trace worker {} started", workerId);
    }

    private void startMonitoring() {
        supervisor.scheduleWithFixedDelay(this::monitorWorkers, INITIAL_DELAY, DELAY, TimeUnit.SECONDS);
    }

    private void monitorWorkers() {
        if ( !running ) return;
        for ( int i = NUM_WORKER_DEFAULT; i < workers; i++ ) {
            Future<?> f = runningWorkers.get(i);
            if ( f == null ) {
                scheduleRestart(i, "missing-future");
                continue;
            }
            if ( f.isDone() || f.isCancelled() ) {
                scheduleRestart(i, "done/cancelled");
            }
        }
    }

    private void scheduleRestart(int workerId, String reason) {
        logger.warn("Trace writer {} not running ({}). Restart scheduled in {} ms",
                workerId, reason, restartDelay.toMillis());
        runningWorkers.remove(workerId);
        supervisor.schedule(() -> {
            if ( !running ) return;
            try {
                startWorker(workerId);
            } catch (Exception e) {
                logger.error("Failed to restart trace writer {}", workerId, e);
            }
        }, restartDelay.toMillis(), TimeUnit.MILLISECONDS);
    }

    private void runLoop(int workerId) {
        List<Trace> buffer = new ArrayList<>(batchSize);
        while ( running && !Thread.currentThread().isInterrupted() ) {
            try {
                buffer.clear();
                Trace first = traceQueue.poll(flushInterval.toMillis(), TimeUnit.MILLISECONDS);
                if ( first == null ) {
                    continue;
                }
                buffer.add(first);
                traceQueue.drainTo(buffer, batchSize - INITIAL_DELAY);
                persistBatch(buffer);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                logger.warn("Trace writer {} interrupted", workerId);
                return;
            } catch (Exception e) {
                // Throwable pour éviter qu'une Error tue silencieusement le worker
                logger.error("Trace writer {} crashed", workerId, e);
                // On sort => Future.isDone() => monitor -> restart
                return;
            }
        }
    }

    private void persistBatch(List<Trace> traces) {
        EntityManager em = traceEmf.createEntityManager();
        EntityTransaction tx = null;
        try (em) {
            tx = em.getTransaction();
            tx.begin();
            int i = NUM_WORKER_DEFAULT;
            for ( Trace t : traces ) {
                TraceEntity entity = traceEntityMapper.toEntity(t, acteMetierJpaRepository, traceObjectMapper);
                em.persist(entity);
                i++;
                if ( i % JPA_ENTITY_BEFORE_FLUSH == NUM_WORKER_DEFAULT ) {
                    em.flush();
                    em.clear();
                }
            }
            em.flush();
            em.clear();
            tx.commit();
        } catch (RuntimeException e) {
            if ( tx != null && tx.isActive() ) {
                tx.rollback();
            }
            throw e;
        }
    }

    @PreDestroy
    public void shutdown() {
        running = INITIAL_START;
        for ( Future<?> f : runningWorkers.values() ) {
            f.cancel(true);
        }
        workerPool.shutdownNow();
        supervisor.shutdownNow();
        logger.info("Trace writer supervisor shutdown");
    }

    @Override
    public void start() {
        if ( !acteMetierCache.isReady() ) {
            logger.error("TraceWriterSupervisor not started: acteMetierCache.isReady() = {}", acteMetierCache.isReady());
            throw new IllegalStateException("TraceWriterSupervisor not started before no data in cache");
        }
        if ( !started.compareAndSet(INITIAL_START, true ) ) {
            logger.error("Le worker/thread est déjà démarré");
            return;
        }
        running = true;
        startAllWorkers();
        startMonitoring();
        logger.info("Trace writer supervisor started witth workers : {}", workers);
    }

    @Override
    public void stop() {
        running = INITIAL_START;
        for ( Future<?> f : runningWorkers.values() ) {
            f.cancel(true);
        }
        runningWorkers.clear();
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

}
