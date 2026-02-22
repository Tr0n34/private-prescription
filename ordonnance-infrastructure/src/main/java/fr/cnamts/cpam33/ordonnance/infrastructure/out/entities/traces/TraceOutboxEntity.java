package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces;

import com.fasterxml.jackson.databind.JsonNode;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.traces.rabbit.TraceOutboxStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

/**
 * Entité de sauvegarde des anomalies de publication sur le microservice de Traces
 */
@Entity
@Table(name = "trace_outbox",
        uniqueConstraints = @UniqueConstraint(
                name="ux_trace_outbox_trace_id",
                columnNames = "trace_id"
        )
)
public class TraceOutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id", nullable = false, length = 80)
    private String traceId;

    @Column(name = "event_code", nullable = false, length = 120)
    private String eventCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload_json", columnDefinition = "jsonb", nullable = false)
    private JsonNode payloadJson;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TraceOutboxStatus status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "next_retry_at", nullable = false)
    private OffsetDateTime nextRetryAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_failure_at")
    private OffsetDateTime lastFailureAt;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    public Long id() {
        return id;
    }

    public TraceOutboxEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String traceId() {
        return traceId;
    }

    public TraceOutboxEntity setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String eventCode() {
        return eventCode;
    }

    public TraceOutboxEntity setEventCode(String eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public JsonNode payloadJson() {
        return payloadJson;
    }

    public TraceOutboxEntity setPayloadJson(JsonNode payloadJson) {
        this.payloadJson = payloadJson;
        return this;
    }

    public String reason() {
        return reason;
    }

    public TraceOutboxEntity setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public TraceOutboxStatus status() {
        return status;
    }

    public TraceOutboxEntity setStatus(TraceOutboxStatus status) {
        this.status = status;
        return this;
    }

    public int retryCount() {
        return retryCount;
    }

    public TraceOutboxEntity setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public OffsetDateTime nextRetryAt() {
        return nextRetryAt;
    }

    public TraceOutboxEntity setNextRetryAt(OffsetDateTime nextRetryAt) {
        this.nextRetryAt = nextRetryAt;
        return this;
    }

    public OffsetDateTime createdAt() {
        return createdAt;
    }

    public TraceOutboxEntity setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OffsetDateTime lastFailureAt() {
        return lastFailureAt;
    }

    public TraceOutboxEntity setLastFailureAt(OffsetDateTime lastFailureAt) {
        this.lastFailureAt = lastFailureAt;
        return this;
    }

    public OffsetDateTime sentAt() {
        return sentAt;
    }

    public TraceOutboxEntity setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
        return this;
    }

}
