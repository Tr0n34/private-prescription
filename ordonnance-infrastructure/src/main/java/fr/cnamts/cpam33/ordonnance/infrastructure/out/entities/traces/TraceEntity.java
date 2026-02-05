package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "trace")
public class TraceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "acte_metier_id", nullable = false)
    private ActeMetierEntity acteMetier;

    @Column(name = "medecin_id", nullable = false)
    private String medecinId;

    @Column(name = "context", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String traceContext;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    protected TraceEntity() {}

    public TraceEntity(Long id, ActeMetierEntity acteMetier, String medecinId, String traceContext, LocalDateTime createdOn) {
        this.id = id;
        this.acteMetier = acteMetier;
        this.medecinId = medecinId;
        this.traceContext = traceContext;
        this.createdOn = createdOn;
    }

    public Long id() {
        return id;
    }

    public TraceEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public ActeMetierEntity acteMetier() {
        return acteMetier;
    }

    public TraceEntity setActeMetier(ActeMetierEntity acteMetier) {
        this.acteMetier = acteMetier;
        return this;
    }

    public String medecinId() {
        return medecinId;
    }

    public TraceEntity setMedecinId(String medecinId) {
        this.medecinId = medecinId;
        return this;
    }

    public String traceContext() {
        return traceContext;
    }

    public TraceEntity setTraceContext(String traceContext) {
        this.traceContext = traceContext;
        return this;
    }

    public LocalDateTime createdOn() {
        return createdOn;
    }

    public TraceEntity setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
        return this;
    }

}