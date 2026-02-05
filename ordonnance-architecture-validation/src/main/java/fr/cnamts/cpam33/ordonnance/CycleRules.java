package fr.cnamts.cpam33.ordonnance;


import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class CycleRules {

    public static final String ORDONNANCE_APPLICATION = "fr.cnamts.cpam33.ordonnance.application.(*)..";
    public static final String ORDONNANCE_DOMAIN = "fr.cnamts.cpam33.ordonnance.domain.(*)..";

    public static ArchRule no_cycles_in_domain() {
        return slices()
                .matching(ORDONNANCE_DOMAIN)
                .should().beFreeOfCycles()
                .as("Pas de cycles de dépendances dans le domaine");
    }


    public static ArchRule no_cycles_in_application() {
        return slices()
                .matching(ORDONNANCE_APPLICATION)
                .should().beFreeOfCycles()
                .as("Pas de cycles de dépendances dans l'application");
    }

}
