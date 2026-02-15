package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.Fonction;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.FonctionId;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.FonctionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FonctionEntityMapper {

    @Mapping(target = "code", source = "fonctionId.code")
    @Mapping(target = "description", source = "description")
    FonctionEntity toEntity(Fonction fonction);

    @Mapping(target = "fonctionId", source = "code")
    @Mapping(target = "description", source = "description")
    Fonction toDomain(FonctionEntity entity);

    default FonctionId mapCodeToFonctionId(String code) {
        return code == null ? null : new FonctionId(code);
    }

    default String mapFonctionIdToCode(FonctionId fonctionId) {
        return fonctionId == null ? null : fonctionId.code();
    }


}
