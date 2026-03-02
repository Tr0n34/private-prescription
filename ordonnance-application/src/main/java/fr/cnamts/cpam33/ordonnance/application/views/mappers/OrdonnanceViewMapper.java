package fr.cnamts.cpam33.ordonnance.application.views.mappers;

import fr.cnamts.cpam33.ordonnance.application.views.OrdonnanceView;
import fr.cnamts.cpam33.ordonnance.domain.models.aggregates.Ordonnance;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface OrdonnanceViewMapper {

    OrdonnanceView toView(Ordonnance ordonnance);

}
