package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceAttribute;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.TraceContext;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances.MedecinEntityMapper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Mapper(componentModel = "spring", uses = {
        ActeMetierEntityMapper.class, MedecinEntityMapper.class
})
public interface TraceEntityMapper {

    TypeReference<List<TraceAttribute>> TYPE_REF = new TypeReference<>() {};

    @Mapping(target = "acteMetier", source = "acteMetierId", qualifiedByName = "mapActeMetier")
    @Mapping(target = "medecinId", source = "medecinId.id")
    @Mapping(target = "createdOn", source = "timestamp")
    @Mapping(target = "traceContext", source = "trace", qualifiedByName = "mapTraceContext")
    TraceEntity toEntity(Trace trace, @Context ActeMetierJpaRepository acteMetierJpaRepository, @Context @Qualifier("traceObjectMapper") ObjectMapper traceObjectMapper);

    @Named("mapActeMetier")
    default ActeMetierEntity mapActeMetier(ActeMetierId acteMetierId, @Context ActeMetierJpaRepository acteMetierJpaRepository) {
        if ( acteMetierId == null ) return null;
        return acteMetierJpaRepository.findByCode(acteMetierId.code()).orElseThrow();
    }

    @Named("mapTraceContext")
    default String mapTraceContext(Trace trace, @Context ObjectMapper traceObjectMapper) {
        try {
            return traceObjectMapper.writeValueAsString(trace.context().attributes());
        } catch (Exception e) {
            throw new IllegalStateException("Impossible de sérialiser le TraceContext", e);
        }
    }

    default Trace toDomain(TraceEntity entity, @Qualifier("traceObjectMapper") ObjectMapper traceObjectMapper) {
        if ( entity == null ) return null;
        List<TraceAttribute> attributes;
        try {
            attributes = traceObjectMapper.readValue(
                    entity.traceContext(),
                    TYPE_REF
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException("Impossible de désérialiser le contexte de trace", ex);
        }
        return new Trace(
                new ActeMetierId(entity.acteMetier().getCode()),
                new MedecinId(entity.medecinId(), null),
                entity.createdOn(),
                new TraceContext(attributes)
        );
    }

}

