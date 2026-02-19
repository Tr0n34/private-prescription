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

    @Column(name = "trace_id", nullable = false)
    private String traceId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "acte_metier_id", nullable = false)
    private ActeMetierEntity acteMetier;

    @Column(name = "utilisateur_id", nullable = false)
    private String utilisateurId;

    @Column(name = "bounded_context", nullable = false, length = 80)
    private String boundedContext;

    @Column(name = "trace_in", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String traceIn;

    @Column(name = "trace_out", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String traceOut;

    @Column(name = "created_on", nullable = false)
    private String createdOn;

    protected TraceEntity() {}

    public TraceEntity(Long id, ActeMetierEntity acteMetier, String utilisateurId,
                       String traceIn, String traceOut, String createdOn) {
        this.id = id;
        this.acteMetier = acteMetier;
        this.utilisateurId = utilisateurId;
        this.traceIn = traceIn;
        this.traceOut = traceOut;
        this.createdOn = createdOn;
    }

    public Long id() {
        return id;
    }

    public TraceEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String traceId() {
        return traceId;
    }

    public TraceEntity setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public ActeMetierEntity acteMetier() {
        return acteMetier;
    }

    public TraceEntity setActeMetier(ActeMetierEntity acteMetier) {
        this.acteMetier = acteMetier;
        return this;
    }

    public String utilisateurId() {
        return utilisateurId;
    }

    public TraceEntity setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
        return this;
    }

    public String boundedContext() {
        return boundedContext;
    }

    public TraceEntity setBoundedContext(String boundedContext) {
        this.boundedContext = boundedContext;
        return this;
    }

    public String traceIn() {
        return traceIn;
    }

    public TraceEntity setTraceIn(String traceIn) {
        this.traceIn = traceIn;
        return this;
    }

    public String traceOut() {
        return traceOut;
    }

    public TraceEntity setTraceOut(String traceOut) {
        this.traceOut = traceOut;
        return this;
    }

    public String createdOn() {
        return createdOn;
    }

    public TraceEntity setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }
}