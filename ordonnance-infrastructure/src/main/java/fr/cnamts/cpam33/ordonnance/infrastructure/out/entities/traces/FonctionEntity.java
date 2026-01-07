package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "fonction")
public class FonctionEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    public String getCode() {
        return code;
    }

    public FonctionEntity setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public FonctionEntity setName(String name) {
        this.name = name;
        return this;
    }

}
