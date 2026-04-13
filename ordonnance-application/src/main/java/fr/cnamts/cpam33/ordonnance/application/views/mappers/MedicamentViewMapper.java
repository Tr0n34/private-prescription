package fr.cnamts.cpam33.ordonnance.application.views.mappers;

import fr.cnamts.cpam33.ordonnance.application.views.medicaments.FormeView;
import fr.cnamts.cpam33.ordonnance.application.views.medicaments.MedicamentView;
import fr.cnamts.cpam33.ordonnance.application.views.medicaments.PosologieView;
import fr.cnamts.cpam33.ordonnance.application.views.medicaments.VoieAdministrationView;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.Posologie;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Forme;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.VoieAdministration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface MedicamentViewMapper {

    @Mapping(target = "codeSp", source = "medicamentId.codeSp")
    MedicamentView toView(Medicament medicament);

    PosologieView toView(Posologie posologie);

    FormeView toView(Forme forme);

    VoieAdministrationView toView(VoieAdministration voieAdministration);

}
