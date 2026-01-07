package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces;


import jakarta.persistence.*;

@Entity
@Table(name = "actes_metier")
public class ActeMetierEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "objet_metier", nullable = false)
    private String objetMetier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fonction_code", nullable = false)
    private FonctionEntity fonction;

    protected ActeMetierEntity() {
    }

    public ActeMetierEntity(String code, String objetMetier, FonctionEntity fonction) {
        this.code = code;
        this.objetMetier = objetMetier;
        this.fonction = fonction;
    }

    public String getCode() {
        return code;
    }

    public String getObjetMetier() {
        return objetMetier;
    }

    public FonctionEntity getFonction() {
        return fonction;
    }

}