package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.ActeMetierId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.tracabilite.Trace;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.traces.TraceDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Clock;

@Mapper(componentModel = "spring")
public interface TraceApiMapper {

    @Mapping(target = "acteMetierId", source = "dto.acteMetierCode")
    @Mapping(target = "utilisateurId", source = "utilisateurId", qualifiedByName = "toUtilisateurId")
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now(clock))")
    Trace toDomain(TraceDto dto, String utilisateurId, Clock clock);

    default ActeMetierId map(String code) {
        if (code == null) return null;
        return new ActeMetierId(code);
    }

    @Named("toUtilisateurId")
    default UtilisateurId mapUtilisateur(String utilisateurId) {
        if (utilisateurId == null) return null;
        return new UtilisateurId(utilisateurId, null);
    }

}
