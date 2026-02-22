package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.ImportPatientCandidate;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients.ImportPatientDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.identites.NomPrenomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        PatientIdMapper.class,
        ExternalIdPatientMapper.class,
        NomPrenomMapper.class
})
public interface ImportPatientApiMapper {

    @Mapping(target = "externalPatientId", source = "externalId")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    ImportPatientCandidate toDomainWithoutId(ImportPatientDto dto);

    @Mapping(target = "externalId", source = "externalPatientId.numero")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    ImportPatientDto toDto(ImportPatientCandidate patient);

}
