package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces;

import jakarta.persistence.*;

@Entity
@Table(name = "fonction")
public class FonctionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "description", nullable = false)
    private String description;

    public String getCode() {
        return code;
    }

    public FonctionEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public FonctionEntity setDescription(String description) {
        this.description = description;
        return this;
    }

}
