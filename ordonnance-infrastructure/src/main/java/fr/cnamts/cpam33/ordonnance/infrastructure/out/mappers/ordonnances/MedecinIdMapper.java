package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Rpps;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
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
