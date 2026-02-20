package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.TraceId;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.TraceOutboxStatus;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.rabbit.TraceOutbox;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceOutboxEntity;
import org.mapstruct.*;

import java.time.OffsetDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TraceOutboxMapper {

    @Mappings({
            @Mapping(target = "traceId", source = "trace.traceId"),
            @Mapping(target = "acteMetierId", expression = "java(trace.acteMetierId().code())"),
            @Mapping(target = "payloadJson", source = "payloadJson"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "retryCount", source = "retryCount"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "nextRetryAt", source = "nextRetryAt"),
            @Mapping(target = "lastFailureAt", ignore = true),
            @Mapping(target = "sentAt", ignore = true),
            @Mapping(target = "reason", ignore = true)
    })
    TraceOutbox toOutbox(Trace trace,
                         String acteMetierId,
                         String payloadJson,
                         TraceOutboxStatus status,
                         int retryCount,
                         OffsetDateTime createdAt,
                         OffsetDateTime nextRetryAt);

    @Mappings({
            @Mapping(target = "traceId", source = "trace.traceId"),
            @Mapping(target = "acteMetierId", expression = "java(trace.acteMetierId().code())"),
            @Mapping(target = "payloadJson", source = "payloadJson"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "retryCount", source = "retryCount"),
            @Mapping(target = "createdAt", source = "createdAt"),
            @Mapping(target = "nextRetryAt", source = "nextRetryAt"),
            @Mapping(target = "lastFailureAt", source = "lastFailureAt"),
            @Mapping(target = "reason", source = "reason"),
            @Mapping(target = "sentAt", ignore = true)
    })
    TraceOutbox toRetryOutbox(Trace trace,
                              String acteMetierId,
                              String payloadJson,
                              TraceOutboxStatus status,
                              int retryCount,
                              OffsetDateTime createdAt,
                              OffsetDateTime nextRetryAt,
                              OffsetDateTime lastFailureAt,
                              String reason);

    @Mappings({
            @Mapping(target = "payloadJson", source = "payloadJson", qualifiedByName = "stringToJsonNode")
    })
    TraceOutboxEntity toEntity(TraceOutbox outbox);

    @Mappings({
            @Mapping(target = "payloadJson", source = "payloadJson", qualifiedByName = "jsonNodeToString")
    })
    TraceOutbox toOutbox(TraceOutboxEntity entity);

    default String map(TraceId traceId) {
        return traceId == null ? null : traceId.numero();
    }

    default TraceId map(String traceId) {
        return traceId == null ? null : new TraceId(traceId);
    }

    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "eventCode", source = "acteMetierId")
    @Mapping(target = "payloadJson", source = "payloadJson", qualifiedByName = "stringToJsonNode")
    TraceOutboxEntity toEntityForInsert(TraceOutbox outbox);

    @Named("stringToJsonNode")
    default JsonNode stringToJsonNode(String payloadJson) {
        try {
            return payloadJson == null ? null : new ObjectMapper().readTree(payloadJson);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Named("jsonNodeToString")
    default String jsonNodeToString(JsonNode payload) {
        try {
            return payload == null ? null : new ObjectMapper().writeValueAsString(payload);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
