package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.medecins.Rpps;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        RppsMapper.class
})
public interface MedecinIdMapper {

    default MedecinId toDomain(String value, String rpps) {
        return value != null && rpps != null
                ? new MedecinId(value, new Rpps(rpps))
                : null;
    }

}
