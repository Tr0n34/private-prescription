package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances;

import fr.cnamts.cpam33.ordonnance.infrastructure.kernel.IEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "medecin")
public class MedecinEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", nullable = false, unique = true)
    private String externalId;

    @Column(name = "rpps", nullable = false, unique = true)
    private String rpps;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    public Long getId() {
        return id;
    }

    public MedecinEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getExternalId() {
        return externalId;
    }

    public MedecinEntity setExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    public String getRpps() {
        return rpps;
    }

    public MedecinEntity setRpps(String rpps) {
        this.rpps = rpps;
        return this;
    }

    public String getNom() {
        return nom;
    }

    public MedecinEntity setNom(String nom) {
        this.nom = nom;
        return this;
    }

    public String getPrenom() {
        return prenom;
    }

    public MedecinEntity setPrenom(String prenom) {
        this.prenom = prenom;
        return this;
    }

}
