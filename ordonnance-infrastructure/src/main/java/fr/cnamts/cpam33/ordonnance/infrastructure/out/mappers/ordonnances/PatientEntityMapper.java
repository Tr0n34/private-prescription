package fr.cnamts.cpam33.ordonnance.infrastructure.out.mappers.ordonnances;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.entities.ordonnances.PatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        NomPrenomMapper.class,
        PatientIdMapper.class
})
public interface PatientEntityMapper {

    @Mapping(target = "externalId", source = "patientId.externalId")
    @Mapping(target = "nom", source = "nom", qualifiedByName = "nomToString")
    @Mapping(target = "prenom", source = "prenom", qualifiedByName = "prenomToString")
    PatientEntity toEntity(Patient patient);

    @Mapping(target = "patientId", expression = "java(new PatientId(entity.getExternalId()))")
    @Mapping(target = "nom", source = "nom", qualifiedByName = "stringToNom")
    @Mapping(target = "prenom", source = "prenom", qualifiedByName = "stringToPrenom")
    Patient toDomain(PatientEntity entity);
}
