package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;


import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Forme;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.medicaments.FormeDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FormeDtoMapper {

    FormeDto toDto(Forme domain);

    Forme toDomain(FormeDto dto);

}