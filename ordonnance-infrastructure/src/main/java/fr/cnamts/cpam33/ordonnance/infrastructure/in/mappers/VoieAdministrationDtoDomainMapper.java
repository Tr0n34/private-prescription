package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.VoieAdministration;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.medicaments.VoieAdministrationDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VoieAdministrationDtoDomainMapper {

    VoieAdministrationDto toDto(VoieAdministration domain);

    VoieAdministration toDomain(VoieAdministrationDto dto);

}
