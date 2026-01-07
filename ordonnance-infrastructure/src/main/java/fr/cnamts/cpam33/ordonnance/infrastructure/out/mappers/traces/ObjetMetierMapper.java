package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.DomainObject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ObjetMetierMapper {

    default String toEntity(DomainObject object) {
        return object == null ? null : object.getClass().getSimpleName();
    }

    /**
     * On ne reconstruit PAS l'objet métier ici.
     * Le Trace n’a besoin que de la référence sémantique.
     */
    default DomainObject toDomain(String objetMetier) {
        return null;
    }
}