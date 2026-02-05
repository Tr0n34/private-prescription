package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces;


import jakarta.persistence.*;

@Entity
@Table(name = "acte_metier")
public class ActeMetierEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "objet_metier", nullable = false)
    private String objetMetierName;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fonction_id", nullable = false)
    private FonctionEntity fonction;

    protected ActeMetierEntity() {
    }

    public ActeMetierEntity(String code, String objetMetierName, FonctionEntity fonction) {
        this.code = code;
        this.objetMetierName = objetMetierName;
        this.fonction = fonction;
    }

    public String getCode() {
        return code;
    }

    public ActeMetierEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public String getObjetMetierName() {
        return objetMetierName;
    }

    public ActeMetierEntity setObjetMetierName(String objetMetierName) {
        this.objetMetierName = objetMetierName;
        return this;
    }

    public FonctionEntity getFonction() {
        return fonction;
    }

    public ActeMetierEntity setFonction(FonctionEntity fonction) {
        this.fonction = fonction;
        return this;
    }

}