package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetierId;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ActeMetierIdMapper {

    default ActeMetierId toDomain(String code) {
        return code == null ? null : new ActeMetierId(code);
    }

    default String toEntity(ActeMetierId id) {
        return id == null ? null : id.code();
    }
}