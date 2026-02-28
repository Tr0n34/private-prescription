package fr.cnamts.cpam33.ordonnance.infrastructure.in.mappers;

import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identifiants.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import fr.cnamts.cpam33.ordonnance.infrastructure.out.dto.patients.PatientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PatientDtoDomainMapper {

    @Mapping(target = "patientId", source = "numero")
    @Mapping(target = "externalPatientId", source = "externalId")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    @Mapping(target = "dateNaissance", source = "dateNaissance")
    Patient toDomain(PatientDto dto);

    @Mapping(target = "numero", source = "patientId")
    @Mapping(target = "externalId", source = "externalPatientId")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    @Mapping(target = "dateNaissance", source = "dateNaissance")
    PatientDto toDto(Patient patient);


    default PatientId mapPatientId(String numero) {
        return new PatientId(numero);
    }

    default String mapPatientId(PatientId patientId) {
        return patientId.numero();
    }

    default ExternalPatientId mapExternalPatientId(String externalId) {
        return externalId == null ? null : new ExternalPatientId(externalId);
    }

    default String mapExternalPatientId(ExternalPatientId externalPatientId) {
        return externalPatientId == null ? null : externalPatientId.numero();
    }

    default Nom mapNom(String nom) {
        return nom == null ? null : new Nom(nom);
    }

    default String mapNom(Nom nom) {
        return nom == null ? null : nom.value();
    }

    default Prenom mapPrenom(String prenom) {
        return prenom == null ? null : new Prenom(prenom);
    }

    default String mapPrenom(Prenom prenom) {
        return prenom == null ? null : prenom.value();
    }

}