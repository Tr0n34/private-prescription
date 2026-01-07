package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface NomPrenomMapper {

    @Named("nomToString")
    default String nomToString(Nom nom) {
        return nom != null ? nom.value() : null;
    }

    @Named("stringToNom")
    default Nom stringToNom(String value) {
        return value != null ? new Nom(value) : null;
    }

    @Named("prenomToString")
    default String prenomToString(Prenom prenom) {
        return prenom != null ? prenom.value() : null;
    }

    @Named("stringToPrenom")
    default Prenom stringToPrenom(String value) {
        return value != null ? new Prenom(value) : null;
    }
}