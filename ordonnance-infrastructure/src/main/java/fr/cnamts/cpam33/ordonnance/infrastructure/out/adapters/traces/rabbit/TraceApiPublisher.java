package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.rabbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.infrastructure.configurations.traces.rabbit.TraceErrorRetryProperties;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.TraceOutboxJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.traces.TraceDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceOutboxEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.TraceApiMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.TraceOutboxMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Clock;
import java.time.OffsetDateTime;

@Component
@ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "RABBIT_MQ_QUEUEING")
public class TraceApiPublisher implements TracePublisher {

    private static final Logger logger = LoggerFactory.getLogger(TraceApiPublisher.class);

    private final RestClient restClientTrace;
    private final TraceApiMapper traceApiMapper;
    private final ObjectMapper objectMapperTrace;
    private final TraceOutboxJpaRepository TraceOutboxJpaRepository;
    private final TraceOutboxMapper traceOutboxMapper;
    private final TraceErrorRetryProperties traceErrorRetryProperties;
    private final Clock clock;

    public TraceApiPublisher(@Qualifier("restClientTrace") RestClient restClientTrace,
                             @Qualifier("traceContextMapper")  ObjectMapper objectMapperTrace,
                             TraceApiMapper traceApiMapper,
                             TraceOutboxJpaRepository TraceOutboxJpaRepository,
                             TraceOutboxMapper traceOutboxMapper,
                             TraceErrorRetryProperties traceErrorRetryProperties,
                             Clock clock) {
        this.restClientTrace = restClientTrace;
        this.traceApiMapper = traceApiMapper;
        this.objectMapperTrace = objectMapperTrace;
        this.TraceOutboxJpaRepository = TraceOutboxJpaRepository;
        this.traceOutboxMapper = traceOutboxMapper;
        this.traceErrorRetryProperties = traceErrorRetryProperties;
        this.clock = clock;
    }

    @Override
    public void publish(Trace trace) {
        TraceDto traceDto = traceApiMapper.toDto(trace, objectMapperTrace);
        try {
            restClientTrace.post()
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .body(traceDto)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ex) {
            logger.error("Error publishing trace to RabbitMQ", ex);
            String payloadJson = writeJson(traceDto);
            OffsetDateTime now = OffsetDateTime.now(clock);
            TraceOutbox failed = traceOutboxMapper.toRetryOutbox(
                    trace,
                    trace.acteMetierId().code(),
                    payloadJson,
                    TraceOutboxStatus.RETRYING,
                    0,
                    now,
                    now.plus(traceErrorRetryProperties.initialDelay()),
                    now,
                    ex.getClass().getSimpleName() + ": " + safeMessage(ex.getMessage())
            );
            TraceOutboxEntity traceOutboxEntity = traceOutboxMapper.toEntityForInsert(failed);
            TraceOutboxJpaRepository.save(traceOutboxEntity);
        }
    }

    private String writeJson(TraceDto dto) {
        try {
            return objectMapperTrace.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot serialize TraceDto for outbox", e);
        }
    }

    private String safeMessage(String message) {
        return message != null
                ? message.length() > 800 ? message.substring(0, 800) : message
                : "";
    }

}
