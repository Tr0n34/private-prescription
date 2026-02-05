package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.traces.TraceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Clock;

@Mapper(componentModel = "spring")
public interface TraceApiMapper {

    @Mapping(target = "acteMetierId", source = "dto.acteMetierCode")
    @Mapping(target = "medecinId", source = "dto.medecinId")
    Trace toDomain(TraceDto dto, Clock clock);

    default ActeMetierId map(String code) {
        if (code == null) return null;
        return new ActeMetierId(code);
    }

    default MedecinId mapMedecin(String medecinId) {
        if (medecinId == null) return null;
        return new MedecinId(medecinId, null);
    }

}
