package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients;

import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients.ExternalPatientResponseDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients.ImportPatientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExternalPatientDtoMapper {

    @Mapping(target = "externalId", source = "conIdExterne")
    @Mapping(target = "prenom", source = "conPrenom")
    @Mapping(target = "dateNaissance", source = "conDateNaissance")
    @Mapping(target = "nom", expression = "java(selectNom(dto))")
    ImportPatientDto toImportDto(ExternalPatientResponseDto dto);

    default String selectNom(ExternalPatientResponseDto dto) {
        if (dto == null) return null;
        if (dto.conNomMarital() != null && !dto.conNomMarital().isBlank()) return dto.conNomMarital();
        return dto.conNomNaissance();
    }

}
