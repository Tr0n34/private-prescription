package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.MedecinIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {
                ActeMetierEntityMapper.class,
                MedecinIdMapper.class,
        }
)
public interface TraceEntityMapper {

    @Mapping(target = "medecinId", source = "medecinId.id")
    @Mapping(target = "medecinRpps", source = "medecinId.rpps.value")
    TraceEntity toEntity(Trace trace);

    @Mapping(target = "medecinId", expression = "java(new MedecinId(entity.getMedecinId(), new Rpps(entity.getMedecinRpps())))")
    Trace toDomain(TraceEntity entity);
}

