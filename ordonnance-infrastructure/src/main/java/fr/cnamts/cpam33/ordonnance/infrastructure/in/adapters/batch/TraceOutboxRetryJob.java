package fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.batch;

import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.RetryJobMode;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.TraceErrorRetryProperties;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.TraceOutboxJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.TraceOutboxStatus;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceOutboxEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.TraceOutboxMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;


@Component
@ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "RABBIT_MQ_QUEUEING")
public class TraceOutboxRetryJob {

    private static final Logger logger = LoggerFactory.getLogger(TraceOutboxRetryJob.class);

    private final TraceOutboxJpaRepository traceOutboxJpaRepository;
    private final TraceOutboxMapper mapper;
    private final RestClient restClientTrace;
    private final Clock clock;
    private final TraceErrorRetryProperties traceErrorRetryProperties;

    public TraceOutboxRetryJob(
            TraceOutboxJpaRepository traceOutboxJpaRepository,
            TraceOutboxMapper mapper,
            @Qualifier("restClientTrace") RestClient restClientTrace,
            TraceErrorRetryProperties traceErrorRetryProperties,
            Clock clock
    ) {
        this.traceOutboxJpaRepository = traceOutboxJpaRepository;
        this.mapper = mapper;
        this.restClientTrace = restClientTrace;
        this.clock = clock;
        this.traceErrorRetryProperties = traceErrorRetryProperties;
    }

    @Scheduled(
            fixedDelayString = "${ordonnance.traces.errors.scheduler-delay:1d}",
            scheduler = "traceRetryTaskScheduler"
    )
    @Transactional("traceTransactionManager")
    @ConditionalOnProperty(name = "ordonnance.traces.errors.mode", havingValue = "RETRY_AUTO")
    public void retryAuto() {
        retry();
    }

    @Scheduled(
            cron = "${ordonnance.traces.errors.cron:0 0/5 * * * ?}",
            scheduler = "traceRetryTaskScheduler"
    )
    @Transactional("traceTransactionManager")
    @ConditionalOnProperty(name = "ordonnance.traces.errors.mode", havingValue = "CRON")
    public void retryCronTask() {
        retry();
    }

    public void retry() {
        logger.trace("TraceOutboxRetryJob started mode={}", traceErrorRetryProperties.mode());
        OffsetDateTime now = OffsetDateTime.now(clock);
        List<TraceOutboxEntity> traceOutboxEntities = traceOutboxJpaRepository.findRetryable(now, 50);
        for ( TraceOutboxEntity entity : traceOutboxEntities ) {
            try {
                logger.trace("retry batch traceId={}, lastFailureAt={}, status={}",
                        entity.traceId(), entity.lastFailureAt(), entity.status());
                Duration delay = computeDelay(entity.retryCount(), traceErrorRetryProperties);
                entity.setStatus(TraceOutboxStatus.PENDING);
                traceOutboxJpaRepository.save(entity);
                String payload = mapper.jsonNodeToString(entity.payloadJson());
                restClientTrace.post()
                        .uri("/traces") // adapte l'URI
                        .header("Content-Type", "application/json")
                        .body(payload)
                        .retrieve()
                        .toBodilessEntity();
                entity.setStatus(TraceOutboxStatus.SENT);
                entity.setSentAt(now);
                entity.setReason(null);
                traceOutboxJpaRepository.save(entity);
            } catch (Exception ex) {
                logger.error(ex.getMessage(), ex);
                entity.setStatus(TraceOutboxStatus.RETRYING);
                entity.setLastFailureAt(now);
                entity.setRetryCount(entity.retryCount() + 1);
                entity.setReason(shortReason(ex));
                entity.setNextRetryAt(computeNextRetryAt(now, entity.retryCount()));
                traceOutboxJpaRepository.save(entity);
            }
        }
    }

    private Duration computeDelay(int retryCount, TraceErrorRetryProperties traceErrorRetryProperties) {
        int attempt = Math.max(1, retryCount);
        double factor = Math.pow(traceErrorRetryProperties.backoffMultiplier(), attempt - 1);
        long delayMillis = (long) (traceErrorRetryProperties.initialDelay().toMillis() * factor);
        long capped = Math.min(delayMillis, traceErrorRetryProperties.maxDelay().toMillis());
        return Duration.ofMillis(capped);
    }

    private OffsetDateTime computeNextRetryAt(OffsetDateTime now, int retryCount) {
        long baseSeconds = 10L;
        long delaySeconds = baseSeconds * (1L << Math.min(retryCount - 1, 6));
        delaySeconds = Math.min(delaySeconds, 600L);
        return now.plusSeconds(delaySeconds);
    }

    private String shortReason(Exception ex) {
        String message = ex.getClass().getSimpleName() + ": " + (ex.getMessage() == null ? "" : ex.getMessage());
        int max = 500;
        return message.length() <= max ? message : message.substring(0, max);
    }

}
