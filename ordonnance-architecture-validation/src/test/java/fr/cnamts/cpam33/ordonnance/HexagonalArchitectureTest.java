package fr.cnamts.cpam33.ordonnance;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "fr.cnamts.cpam33.ordonnance")
public class HexagonalArchitectureTest {

    // ═══════════════════════════════════════════════════════════════
    // RÈGLE 1 : Domain ne dépend JAMAIS d'Infrastructure
    // ═══════════════════════════════════════════════════════════════

    @ArchTest
    static final ArchRule domain_should_not_depend_on_infrastructure =
            noClasses()
                    .that().resideInAPackage(Module.DOMAIN.getArchPackage())
                    .should().dependOnClassesThat()
                    .resideInAPackage(Module.INFRASTRUCTURE.getArchPackage())
                    .as("Le domaine ne doit JAMAIS dépendre de l'infrastructure");

    // ═══════════════════════════════════════════════════════════════
    // RÈGLE 2 : Domain ne dépend JAMAIS d'Application
    // ═══════════════════════════════════════════════════════════════

    @ArchTest
    static final ArchRule domain_should_not_depend_on_application =
            noClasses()
                    .that().resideInAPackage(Module.DOMAIN.getArchPackage())
                    .should().dependOnClassesThat()
                    .resideInAPackage(Module.APPLICATION.getArchPackage())
                    .as("Le domaine ne doit JAMAIS dépendre de l'application");

    // ═══════════════════════════════════════════════════════════════
    // RÈGLE 3 : Application ne dépend PAS d'Infrastructure
    // ═══════════════════════════════════════════════════════════════

    @ArchTest
    static final ArchRule application_should_not_depend_on_infrastructure =
            noClasses()
                    .that().resideInAPackage(Module.APPLICATION.getArchPackage())
                    .should().dependOnClassesThat()
                    .resideInAPackage(Module.INFRASTRUCTURE.getArchPackage())
                    .as("L'application ne doit PAS dépendre de l'infrastructure");


}

