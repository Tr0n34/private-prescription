package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.medecins;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.Medecin;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Rpps;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.MedecinEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.identites.NomPrenomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        NomPrenomMapper.class,
        MedecinIdMapper.class,
        RppsMapper.class
})
public interface MedecinEntityMapper {

    @Mapping(target = "externalId", source = "medecinId.id")
    @Mapping(target = "rpps", source = "medecinId.rpps.numero")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    MedecinEntity toEntity(Medecin patient);

    @Mapping(target = "medecinId", source = "entity")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    Medecin toDomain(MedecinEntity entity);

    default MedecinEntity toEntity(MedecinId medecinId) {
        if (medecinId == null) return null;
        MedecinEntity entity = new MedecinEntity();
        entity.setExternalId(medecinId.id());
        entity.setRpps(medecinId.rpps().numero());
        return entity;
    }

    default MedecinId toMedecinId(MedecinEntity entity) {
        if (entity == null) return null;
        return new MedecinId(
                entity.getExternalId(),
                new Rpps(entity.getRpps())
        );
    }

}