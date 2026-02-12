package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trace_outbox")
public class TraceOutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payload_json", nullable = false, columnDefinition = "text")
    private String payloadJson;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "attempts", nullable = false)
    private int attempts;

    @Column(name = "last_error", columnDefinition = "text")
    private String lastError;

    public Long id() {
        return id;
    }

    public TraceOutboxEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String payloadJson() {
        return payloadJson;
    }

    public TraceOutboxEntity setPayloadJson(String payloadJson) {
        this.payloadJson = payloadJson;
        return this;
    }

    public LocalDateTime createdAt() {
        return createdAt;
    }

    public TraceOutboxEntity setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public int attempts() {
        return attempts;
    }

    public TraceOutboxEntity setAttempts(int attempts) {
        this.attempts = attempts;
        return this;
    }

    public String lastError() {
        return lastError;
    }

    public TraceOutboxEntity setLastError(String lastError) {
        this.lastError = lastError;
        return this;
    }

}
