package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.utilisateurs.MedecinId;
import fr.cnamts.cpam33.ordonnance.domain.models.commands.ordonnances.CreateOrdonnanceCmd;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Rpps;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.OrdonnanceDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.medecins.MedecinIdDto;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients.PatientIdDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {
})
public interface OrdonnanceApiMapper {

    @Mapping(target = "patientId", source = "patientId", qualifiedByName = "toPatientId")
    @Mapping(target = "medecinId", source = "medecinId", qualifiedByName = "toMedecinId")
    @Mapping(target = "lignePrescriptions", ignore = true)
    CreateOrdonnanceCmd toCommand(OrdonnanceDto dto);

    @Mapping(target = "patientId", source = "patientId")
    @Mapping(target = "medecinId", source = "medecinId")
    OrdonnanceDto toDto(CreateOrdonnanceCmd cmd);

    @Named("toPatientId")
    default PatientId mapToPatientId(PatientIdDto dto) {
        if (dto == null || dto.numero() == null || dto.numero().isBlank()) {
            return null;
        }
        return new PatientId(dto.numero());
    }

    @Named("toMedecinId")
    default MedecinId mapToMedecinId(MedecinIdDto dto) {
        if (dto == null || dto.externalId().isBlank() || dto.rpps().isBlank()) {
            return null;
        }
        return new MedecinId(dto.externalId(), new Rpps(dto.rpps()));
    }

    default PatientIdDto mapToPatientIdDto(PatientId patientId) {
        if (patientId == null) return null;
        return new PatientIdDto(patientId.numero());
    }

    default MedecinIdDto mapToMedecinIdDto(MedecinId medecinId) {
        if (medecinId == null) return null;
        return new MedecinIdDto(medecinId.id(), medecinId.rpps().numero());
    }

}
