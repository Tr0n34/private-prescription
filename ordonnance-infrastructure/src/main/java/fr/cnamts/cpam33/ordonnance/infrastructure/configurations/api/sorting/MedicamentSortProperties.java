package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.api.sorting;

import fr.cnamts.cpam33.ordonnance.application.filters.PageRequest;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "ordonnance.medicaments.sort")
public record MedicamentSortProperties (
        List<PageRequest.SortField> defaultSort,
        Map<String, FieldConfiguration> fields
){

}
