package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.identites;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NomPrenomMapper {

    default String mapPrenom(Nom nom) {
        return nom != null ? nom.value() : null;
    }

    default Nom mapNom(String value) {
        return value != null ? new Nom(value) : null;
    }

    default String mapPrenom(Prenom prenom) {
        return prenom != null ? prenom.value() : null;
    }

    default Prenom mapPrenom(String value) {
        return value != null ? new Prenom(value) : null;
    }

}