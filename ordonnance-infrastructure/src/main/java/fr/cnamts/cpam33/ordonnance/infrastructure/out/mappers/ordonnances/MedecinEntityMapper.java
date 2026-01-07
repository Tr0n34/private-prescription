package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.Medecin;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.MedecinEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        NomPrenomMapper.class,
        MedecinIdMapper.class,
        RppsMapper.class
})
public interface MedecinEntityMapper {

    @Mapping(target = "externalId", source = "medecinId.id")
    @Mapping(target = "rpps", source = "medecinId.rpps.value")
    @Mapping(target = "nom", source = "nom", qualifiedByName = "nomToString")
    @Mapping(target = "prenom", source = "prenom", qualifiedByName = "prenomToString")
    MedecinEntity toEntity(Medecin patient);

    @Mapping(target = "medecinId", source = "entity")
    @Mapping(target = "nom", source = "nom", qualifiedByName = "stringToNom")
    @Mapping(target = "prenom", source = "prenom", qualifiedByName = "stringToPrenom")
    Medecin toDomain(MedecinEntity entity);

}