package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.rabbit;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TracePublisher;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.traces.TraceDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces.TraceApiMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ConditionalOnProperty(name = "ordonnance.traces.mode", havingValue = "RABBIT_MQ_QUEUEING")
public class TraceApiPublisher implements TracePublisher {

    private final RestClient restClientTrace;
    private final TraceApiMapper traceApiMapper;
    private final ObjectMapper objectMapperTrace;

    public TraceApiPublisher(@Qualifier("restClientTrace") RestClient restClientTrace,
                             @Qualifier("traceContextMapper")  ObjectMapper objectMapperTrace,
                             TraceApiMapper traceApiMapper) {
        this.restClientTrace = restClientTrace;
        this.traceApiMapper = traceApiMapper;
        this.objectMapperTrace = objectMapperTrace;
    }

    @Override
    public void publish(Trace trace) {
        TraceDto traceDto = traceApiMapper.toDto(trace, objectMapperTrace);
        restClientTrace.post()
                .uri("/traces")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(traceDto)
                .retrieve()
                .toBodilessEntity();
    }

}
