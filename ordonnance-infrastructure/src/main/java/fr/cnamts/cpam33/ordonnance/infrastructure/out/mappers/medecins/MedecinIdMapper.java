package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.medecins;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Rpps;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        RppsMapper.class
})
public interface MedecinIdMapper {

    default MedecinId toDomain(String value, String rpps) {
        return value != null && rpps != null
                ? new MedecinId(value, new Rpps(rpps))
                : null;
    }

}
