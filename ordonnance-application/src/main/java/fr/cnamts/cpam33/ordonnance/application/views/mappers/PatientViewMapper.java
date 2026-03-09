package fr.cnamts.cpam33.ordonnance.application.views.mappers;

import fr.cnamts.cpam33.ordonnance.application.views.PatientView;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.patients.Patient;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.ExternalPatientId;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.identifiants.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Nom;
import fr.cnamts.cpam33.ordonnance.domain.models.valueobjects.identites.Prenom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PatientViewMapper {


    @Mapping(target = "patientId", source = "patientId")
    @Mapping(target = "externalPatientId", source = "externalPatientId")
    @Mapping(target = "nom", source = "nom")
    @Mapping(target = "prenom", source = "prenom")
    PatientView toView(Patient patient);

    default String map(PatientId id) {
        return id == null ? null : id.numero();
    }

    default String map(ExternalPatientId id) {
        return id == null ? null : id.numero();
    }

    default String map(Nom nom) {
        return nom == null ? null : nom.value();
    }

    default String map(Prenom prenom) {
        return prenom == null ? null : prenom.value();
    }

}
