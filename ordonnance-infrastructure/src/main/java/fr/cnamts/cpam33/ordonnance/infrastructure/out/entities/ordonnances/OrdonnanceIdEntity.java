package fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.kernel.domain.DomainObject;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.IEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OrdonnanceIdEntity implements IEntity, Serializable, DomainObject {

    @Column(name = "numero", nullable = false)
    private String numero;

    public OrdonnanceIdEntity(String numero) {
        this.numero = numero;
    }

    public String getNumero() {
        return numero;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        OrdonnanceIdEntity that = (OrdonnanceIdEntity) o;
        return Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numero);
    }

}