package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;

import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.UtilisateurId;
import fr.cnamts.cpam33.ordonnance.application.queries.ListerMedicamentByCodeIdQuery;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.adapters.controllers.filters.MedicamentFilter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        FormeDtoMapper.class,
        VoieAdministrationDtoDomainMapper.class
})
public interface MedicamentApiMapper {

    @Mapping(target = "codeSp", source = "filter.codeSp")
    @Mapping(target = "varType", source = "filter.varType")
    @Mapping(target = "utilisateurId", source = "userId",  qualifiedByName = "mapUtilisteurId")
    ListerMedicamentByCodeIdQuery toQuery(MedicamentFilter filter, String userId);

    @Named("mapUtilisteurId")
    static UtilisateurId map(String userId) {
        return userId == null ? null : new UtilisateurId(userId);
    }

}
