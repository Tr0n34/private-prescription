package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.api.sorting;

import fr.cnamts.cpam33.ordonnance.application.filters.SortProvider;
import fr.cnamts.cpam33.ordonnance.application.views.medicaments.MedicamentView;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.sorting.RecordAliasMaps;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.sorting.RecordSortProvider;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MedicamentSortProperties.class)
public class SortProviderConfiguration {

    @Bean("medicamentSortProvider")
    public SortProvider<MedicamentView> medicamentSortProvider(MedicamentSortProperties props) {
        return new RecordSortProvider<>(
                MedicamentView.class,
                props.defaultSort(),
                props.fields(),
                RecordAliasMaps.buildAliasMap(props.fields())
        );
    }

}
