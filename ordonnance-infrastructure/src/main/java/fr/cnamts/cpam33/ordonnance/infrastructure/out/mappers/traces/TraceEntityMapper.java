package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.kernel.ids.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.tracabilite.*;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.medecins.MedecinEntityMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.mappers.TraceValueNormalizer;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        ActeMetierEntityMapper.class, MedecinEntityMapper.class
})
public interface TraceEntityMapper {

    @Mapping(target = "traceId", source = "traceId.numero")
    @Mapping(target = "acteMetier", source = "acteMetierId", qualifiedByName = "mapActeMetier")
    @Mapping(target = "utilisateurId", source = "utilisateurId.numero")
    @Mapping(target = "createdOn", source = "timestamp")
    @Mapping(target = "traceIn", source = "trace", qualifiedByName = "mapTraceIn")
    @Mapping(target = "traceOut", source = "trace", qualifiedByName = "mapTraceOut")
    TraceEntity toEntity(Trace trace,
                         @Context ActeMetierJpaRepository acteMetierJpaRepository,
                         @Context @Qualifier("traceObjectMapper") ObjectMapper traceObjectMapper);

    @Named("mapActeMetier")
    default ActeMetierEntity mapActeMetier(ActeMetierId acteMetierId,
                                           @Context ActeMetierJpaRepository acteMetierJpaRepository) {
        return acteMetierId != null
                ? acteMetierJpaRepository.findByCode(acteMetierId.code()).orElseThrow()
                : null;
    }

    @Named("mapTraceIn")
    default String mapTraceIn(Trace trace, @Context ObjectMapper traceObjectMapper) {
        try {
            TraceIn safeIn = new TraceIn(
                    trace.context().in().method(),
                    trace.context().in().signature(),
                    safeAttributes(trace.context().in().params(), traceObjectMapper)
            );
            return traceObjectMapper.writeValueAsString(safeIn);
        } catch (Exception e) {
            throw new IllegalStateException("Impossible de sérialiser TraceIn", e);
        }
    }

    @Named("mapTraceOut")
    default String mapTraceOut(Trace trace, @Context ObjectMapper traceObjectMapper) {
        try {
            TraceOut out = trace.context().out();
            List<TraceAttribute> safeAttrs = safeAttributes(out.traceAttributes(), traceObjectMapper);
            TraceFailure err = out.error();
            TraceOut safeOut = ( out.status() == TraceStatus.FAILURE )
                    ? new TraceOut(TraceStatus.FAILURE, safeAttrs, err)
                    : new TraceOut(TraceStatus.SUCCESS, safeAttrs, null);
            return traceObjectMapper.writeValueAsString(safeOut);
        } catch (Exception e) {
            throw new IllegalStateException("Impossible de sérialiser TraceOut", e);
        }
    }

    default Trace toDomain(TraceEntity entity, @Qualifier("traceObjectMapper") ObjectMapper traceObjectMapper) {
        if ( entity == null ) return null;
        try {
            TraceIn in = traceObjectMapper.readValue(entity.traceIn(), TraceIn.class);
            TraceOut out = traceObjectMapper.readValue(entity.traceOut(), TraceOut.class);
            return new Trace(
                    new TraceId(entity.traceId()),
                    new ActeMetierId(entity.acteMetier().getCode()),
                    new UtilisateurId(entity.utilisateurId()),
                    entity.boundedContext(),
                    LocalDateTime.parse(entity.createdOn()),
                    new TraceContext(in, out)
            );
        } catch (Exception ex) {
            throw new IllegalArgumentException("Impossible de désérialiser TraceIn/TraceOut", ex);
        }
    }

    private List<TraceAttribute> safeAttributes(List<TraceAttribute> attrs,
                                                ObjectMapper traceObjectMapper) {
        return ( attrs == null || attrs.isEmpty() )
                ? List.of()
                : attrs.stream()
                .map(a -> new TraceAttribute(
                        a.name(),
                        new TraceValue(
                                TraceValueNormalizer.normalize(
                                        a.value().value(),
                                        traceObjectMapper
                                )
                        )
                ))
                .toList();
    }

}
