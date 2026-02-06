package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.*;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.persistences.repositories.traces.ActeMetierJpaRepository;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.ActeMetierEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.medecins.MedecinEntityMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.mappers.TraceValueNormalizer;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", uses = {
        ActeMetierEntityMapper.class, MedecinEntityMapper.class
})
public interface TraceEntityMapper {

    TypeReference<List<java.util.Map<String, Object>>> TYPE_REF_SAFE =
            new TypeReference<>() {};

    @Mapping(target = "acteMetier", source = "acteMetierId", qualifiedByName = "mapActeMetier")
    @Mapping(target = "utilisateurId", source = "utilisateurId.id")
    @Mapping(target = "createdOn", source = "timestamp")
    @Mapping(target = "traceContext", source = "trace", qualifiedByName = "mapTraceContext")
    TraceEntity toEntity(Trace trace,
                         @Context ActeMetierJpaRepository acteMetierJpaRepository,
                         @Context @Qualifier("traceObjectMapper") ObjectMapper traceObjectMapper);

    @Named("mapActeMetier")
    default ActeMetierEntity mapActeMetier(ActeMetierId acteMetierId, @Context ActeMetierJpaRepository acteMetierJpaRepository) {
        if ( acteMetierId == null ) return null;
        return acteMetierJpaRepository.findByCode(acteMetierId.code()).orElseThrow();
    }

    @Named("mapTraceContext")
    default String mapTraceContext(Trace trace, @Context ObjectMapper traceObjectMapper) {
        try {
            var safeAttrs = trace.context().attributes().stream()
                    .map(traceAttribute -> new LinkedHashMap<String, Object>() {{
                        put("name", traceAttribute.name());
                        put("value", TraceValueNormalizer.normalize(
                                traceAttribute.value() == null ? null : traceAttribute.value().value(),
                                traceObjectMapper
                        ));
                    }})
                    .toList();
            return traceObjectMapper.writeValueAsString(safeAttrs);
        } catch (Exception e) {
            throw new IllegalStateException("Impossible de sérialiser le TraceContext", e);
        }
    }

    default Trace toDomain(TraceEntity entity, @Qualifier("traceObjectMapper") ObjectMapper traceObjectMapper) {
        if (entity == null) return null;
        List<Map<String, Object>> safe;
        try {
            safe = traceObjectMapper.readValue(entity.traceContext(), TYPE_REF_SAFE);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Impossible de désérialiser le contexte de trace", ex);
        }
        List<TraceAttribute> attributes = safe.stream()
                .map(m -> new TraceAttribute(
                        (String) m.get("name"),
                        new TraceValue(m.get("value"))
                ))
                .toList();

        return new Trace(
                new ActeMetierId(entity.acteMetier().getCode()),
                new UtilisateurId(entity.utilisateurId(), null),
                entity.createdOn(),
                new TraceContext(attributes)
        );
    }

}

