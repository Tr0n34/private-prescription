package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.patients;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.PatientEntity;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.identites.NomPrenomMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {
        NomPrenomMapper.class,
        PatientIdMapper.class,
        ExternalIdPatientMapper.class
})
public interface PatientEntityMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patientId", source = "patientId")
    @Mapping(target = "externalId", source = "externalPatientId")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    @Mapping(target = "dateNaissance", source = "dateNaissance")
    PatientEntity toEntity(Patient patient);

    @Mapping(target = "patientId", source = "patientId")
    @Mapping(target = "externalPatientId", source = "externalId")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    @Mapping(target = "dateNaissance", source = "dateNaissance")
    Patient toDomain(PatientEntity entity);

}
