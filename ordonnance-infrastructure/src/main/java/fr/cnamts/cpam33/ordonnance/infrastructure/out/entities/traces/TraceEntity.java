package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trace")
public class TraceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "acte_metier_id", nullable = false)
    private ActeMetierEntity acteMetier;

    @Column(name = "medecin_id", nullable = false)
    private String medecinId;

    @Column(name = "medecin_rpps", nullable = false)
    private String medecinRpps;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    protected TraceEntity() {}

    public TraceEntity(Long id, ActeMetierEntity acteMetier, String medecinId, String medecinRpps, LocalDateTime timestamp) {
        this.id = id;
        this.acteMetier = acteMetier;
        this.medecinId = medecinId;
        this.medecinRpps = medecinRpps;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public ActeMetierEntity getActeMetier() {
        return acteMetier;
    }

    public String getMedecinId() {
        return medecinId;
    }

    public String getMedecinRpps() {
        return medecinRpps;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TraceEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public TraceEntity setActeMetier(ActeMetierEntity acteMetier) {
        this.acteMetier = acteMetier;
        return this;
    }

    public TraceEntity setMedecinId(String medecinId) {
        this.medecinId = medecinId;
        return this;
    }

    public TraceEntity setMedecinRpps(String medecinRpps) {
        this.medecinRpps = medecinRpps;
        return this;
    }

    public TraceEntity setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

}