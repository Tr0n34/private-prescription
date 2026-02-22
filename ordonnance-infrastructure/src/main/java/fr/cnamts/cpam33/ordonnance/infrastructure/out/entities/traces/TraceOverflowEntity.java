package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

/**
 * Entité de débordement lorsque la file d'attente InMemory est pleine
 */
@Entity
@Table(name = "trace_overflow")
public class TraceOverflowEntity {

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

    public TraceOverflowEntity() {
    }

    public TraceOverflowEntity(Long id, String traceId, ActeMetierEntity acteMetier, String utilisateurId,
                               String boundedContext, String traceIn, String traceOut, String createdOn) {
        this.id = id;
        this.traceId = traceId;
        this.acteMetier = acteMetier;
        this.utilisateurId = utilisateurId;
        this.boundedContext = boundedContext;
        this.traceIn = traceIn;
        this.traceOut = traceOut;
        this.createdOn = createdOn;
    }

    public Long id() {
        return id;
    }

    public TraceOverflowEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String traceId() {
        return traceId;
    }

    public TraceOverflowEntity setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public ActeMetierEntity acteMetier() {
        return acteMetier;
    }

    public TraceOverflowEntity setActeMetier(ActeMetierEntity acteMetier) {
        this.acteMetier = acteMetier;
        return this;
    }

    public String utilisateurId() {
        return utilisateurId;
    }

    public TraceOverflowEntity setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
        return this;
    }

    public String boundedContext() {
        return boundedContext;
    }

    public TraceOverflowEntity setBoundedContext(String boundedContext) {
        this.boundedContext = boundedContext;
        return this;
    }

    public String traceIn() {
        return traceIn;
    }

    public TraceOverflowEntity setTraceIn(String traceIn) {
        this.traceIn = traceIn;
        return this;
    }

    public String traceOut() {
        return traceOut;
    }

    public TraceOverflowEntity setTraceOut(String traceOut) {
        this.traceOut = traceOut;
        return this;
    }

    public String createdOn() {
        return createdOn;
    }

    public TraceOverflowEntity setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        return this;
    }

}
