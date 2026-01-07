package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;

import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.patients.PatientId;
import fr.cnamts.cpam33.ordonnance.infrastructure.in.dto.patients.ExternalPatientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientApiMapper {

    @Mapping(target = "patientId", source = "externalId")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    Patient toDomain(ExternalPatientDto dto);

    @Mapping(target = "externalId", source = "patientId")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    ExternalPatientDto toDto(Patient patient);


    default PatientId mapPatientId(String externalId) {
        return new PatientId(externalId);
    }

    default String mapPatientId(PatientId patientId) {
        return patientId.externalId();
    }

    default Nom mapNom(String nom) {
        return new Nom(nom);
    }

    default String mapNom(Nom nom) {
        return nom.value();
    }

    default Prenom mapPrenom(String prenom) {
        return  new Prenom(prenom);
    }

    default String mapPrenom(Prenom prenom) {
        return prenom.value();
    }

}

