package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.MedicamentId;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.medicaments.MedicamentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        FormeDtoMapper.class,
        VoieAdministrationDtoDomainMapper.class
})
public interface MedicamentDtoDomainMapper {

    @Mapping(target = "codeSp", source = "medicamentId", qualifiedByName = "medicamentIdToCodeSp")
    MedicamentDto toDto(Medicament domain);

    List<MedicamentDto> toDtoList(List<Medicament> domains);

    @Mapping(target = "medicamentId", source = "codeSp", qualifiedByName = "codeSpToMedicamentId")
    @Mapping(target = "posologies", ignore = true)
    Medicament toDomain(MedicamentDto dto);

    @Named("medicamentIdToCodeSp")
    static String medicamentIdToCodeSp(MedicamentId medicamentId) {
        return medicamentId == null ? null : medicamentId.codeSq();
    }

    @Named("codeSpToMedicamentId")
    static MedicamentId codeSpToMedicamentId(String codeSp) {
        return codeSp == null ? null : new MedicamentId(codeSp);
    }

}
