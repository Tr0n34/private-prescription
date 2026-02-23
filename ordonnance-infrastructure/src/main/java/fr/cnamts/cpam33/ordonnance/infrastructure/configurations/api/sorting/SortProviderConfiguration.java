package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.api.sorting;

import fr.cnamts.cpam33.ordonnance.domain.kernel.filters.SortProvider;
import fr.cnamts.cpam33.ordonnance.domain.models.businessobjects.medicaments.Medicament;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.sorting.RecordAliasMaps;
import fr.cnamts.cpam33.ordonnance.infrastructure.akernel.sorting.RecordSortProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SortProviderConfiguration {

    @Bean("medicamentSortProvider")
    public SortProvider<Medicament> medicamentSortProvider(MedicamentSortProperties props) {
        return new RecordSortProvider<>(
                Medicament.class,
                props.defaultSort(),
                props.fields(),
                RecordAliasMaps.buildAliasMap(props.fields())
        );
    }

}
