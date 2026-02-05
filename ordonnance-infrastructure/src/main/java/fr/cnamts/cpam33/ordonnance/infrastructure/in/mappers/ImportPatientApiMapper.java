package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.ImportPatientDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.identites.NomPrenomMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients.ExternalIdPatientMapper;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients.PatientIdMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        PatientIdMapper.class,
        ExternalIdPatientMapper.class,
        NomPrenomMapper.class
})
public interface ImportPatientApiMapper {

    @Mapping(target = "externalPatientId", source = "externalId")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    Patient toDomainWithoutId(ImportPatientDto dto);

    @Mapping(target = "externalId", source = "externalPatientId.numero")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    ImportPatientDto toDto(Patient patient);

}
