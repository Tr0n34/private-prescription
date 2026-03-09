package fr.cnamts.cpam33.ordonnance.infrastructure.configurations.api.sorting;

import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.sorting.HasAliases;
import fr.cnamts.cpam33.ordonnance.infrastructure.abstracts.sorting.HasEnabled;

import java.util.List;

public record FieldConfiguration(
        boolean enabled,
        List<String> aliases
) implements HasEnabled, HasAliases {

}
