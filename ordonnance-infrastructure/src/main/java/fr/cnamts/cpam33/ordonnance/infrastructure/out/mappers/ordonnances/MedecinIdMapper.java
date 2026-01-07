package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.Rpps;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.traces.TraceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {
        RppsMapper.class
})
public interface MedecinIdMapper {

    default MedecinId toDomain(String value, String rpps) {
        return value != null && rpps != null
                ? new MedecinId(value, new Rpps(rpps))
                : null;
    }

    @Mapping(target = "medecinId", source = "id")
    @Mapping(target = "medecinRpps", source = "rpps")
    void toEntity(MedecinId domain, @MappingTarget TraceEntity entity);


    @Mapping(target = "id", source = "medecinId")
    @Mapping(target = "rpps", source = "medecinRpps")
    MedecinId toDomain(TraceEntity entity);

}
