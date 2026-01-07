package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetier;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                ActeMetierIdMapper.class,
                FonctionEntityMapper.class,
                ObjetMetierMapper.class
        }
)
public interface ActeMetierEntityMapper {

    @Mapping(target = "code", source = "acteMetierId")
    @Mapping(target = "objetMetier", source = "object")
    ActeMetierEntity toEntity(ActeMetier acteMetier);

    @Mapping(target = "acteMetierId", source = "code")
    @Mapping(target = "object", source = "objetMetier")
    ActeMetier toDomain(ActeMetierEntity entity);

}
