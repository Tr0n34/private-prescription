package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        ActeMetierIdMapper.class,
        FonctionEntityMapper.class
})
public interface ActeMetierEntityMapper {

    @Mapping(target = "code", source = "acteMetierId")
    @Mapping(target = "objetMetierName", source = "objetMetierName")
    ActeMetierEntity toEntity(ActeMetier acteMetier);

    @Mapping(target = "acteMetierId", source = "code")
    @Mapping(target = "objetMetierName", source = "objetMetierName")
    ActeMetier toDomain(ActeMetierEntity entity);

}
