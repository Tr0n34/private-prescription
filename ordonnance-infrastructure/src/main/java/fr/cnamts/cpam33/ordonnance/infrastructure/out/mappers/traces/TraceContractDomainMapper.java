package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.traces;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.tracabilite.*;
import fr.cnamts.cpam33.ordonnance.infrastructure.technical.mappers.TraceValueNormalizer;
import fr.cnamts.cpam33.traces.contract.dto.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TraceContractDomainMapper {

    @Mapping(target = "traceId", source="traceId.numero")
    @Mapping(target = "correlationId", source = "correlationId.numero")
    @Mapping(target = "boundedContext", source = "boundedContext")
    @Mapping(target = "acteMetierCode", source = "acteMetierId.code")
    @Mapping(target = "fonction", source = "fonctionId.code")
    @Mapping(target = "utilisateurId", source = "utilisateurId.numero")
    @Mapping(target = "createdOn", source = "createdOn")
    @Mapping(target = "in", source = "context.in")
    @Mapping(target = "out", source = "context.out")
    TraceDto toDto(Trace trace, @Context @Qualifier("traceObjectMapper") ObjectMapper traceObjectMapper);

    @Mapping(target = "params", source = "params", qualifiedByName = "mapAttributes")
    TraceInDto toInDto(TraceIn in, @Context ObjectMapper traceObjectMapper);

    @Mapping(target = "status", source = "status")
    @Mapping(target = "traceAttributes", source = "traceAttributes", qualifiedByName = "mapAttributes")
    @Mapping(target = "error", source = "error")
    TraceOutDto toOutDto(TraceOut out, @Context ObjectMapper traceObjectMapper);

    @Mapping(target = "type", source = "type")
    @Mapping(target = "message", source = "message")
    TraceFailureDto toFailureDto(TraceFailure failure);

    default TraceStatusDto map(TraceStatus status) {
        return status == TraceStatus.FAILURE ? TraceStatusDto.FAILURE : TraceStatusDto.SUCCESS;
    }

    @Named("mapAttributes")
    default List<TraceAttributeDto> mapAttributes(List<TraceAttribute> attrs,
                                                  @Context ObjectMapper traceObjectMapper) {
        List<TraceAttributeDto> result = List.of();
        if ( attrs != null && !attrs.isEmpty() ) {
            result = attrs.stream()
                    .map(a -> toAttributeDto(a, traceObjectMapper))
                    .toList();
        }
        return result;
    }

    default TraceAttributeDto toAttributeDto(TraceAttribute attr,
                                             @Context ObjectMapper traceObjectMapper) {
        TraceAttributeDto dto = null;
        if ( attr != null && attr.value() != null ) {
            Object raw = attr.value().value();
            Object safe = TraceValueNormalizer.normalize(raw, traceObjectMapper);
            dto = new TraceAttributeDto(attr.name(), safe);
        }
        return dto;
    }

}
