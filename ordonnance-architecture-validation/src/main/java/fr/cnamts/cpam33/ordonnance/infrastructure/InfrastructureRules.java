package fr.cnamts.cpam33.ordonnance.infrastructure;

import com.tngtech.archunit.lang.ArchRule;
import fr.cnamts.cpam33.ordonnance.ClassSuffix;
import fr.cnamts.cpam33.ordonnance.Module;
import fr.cnamts.cpam33.ordonnance.TechPackage;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class InfrastructureRules {

    public static final String CONTEXT_ANNOTATION_CONFIGURATION = "org.springframework.context.annotation.Configuration";

    public static ArchRule configurationShouldBeInInfrastructure() {
        return classes()
                .that().areAnnotatedWith(CONTEXT_ANNOTATION_CONFIGURATION)
                .should().resideInAPackage(Module.INFRASTRUCTURE.getArchPackage())
                .as("Les classes @Configuration doivent être dans l'infrastructure");
    }

    public static ArchRule mappersShouldBeInInfrastructure() {
        return classes()
                .that().haveSimpleNameEndingWith(ClassSuffix.MAPPER.getSuffix())
                .and().areNotInterfaces()
                .should().resideInAPackage(Module.INFRASTRUCTURE.getArchPackage())
                .as("Les mappers doivent être dans l'infrastructure");
    }

    public static ArchRule mappersShouldNotBeInDomain() {
        return noClasses()
                .that().haveSimpleNameEndingWith(ClassSuffix.MAPPER.getSuffix())
                .should().resideInAPackage(Module.DOMAIN.getArchPackage())
                .as("Les mappers ne doivent pas être dans le domaine");
    }

    public static ArchRule repositoryImplementationShouldBeInInfrastructure() {
        return classes()
                .that().haveSimpleNameEndingWith(ClassSuffix.REPOSITORY_ADAPTER.getSuffix())
                .or().haveSimpleNameEndingWith(ClassSuffix.JPA_REPOSITORY.getSuffix())
                .or().haveSimpleNameEndingWith(ClassSuffix.REPOSITORY_IMPL.getSuffix())
                .should().resideInAPackage(Module.INFRASTRUCTURE.getArchPackage())
                .as("Les implémentations de repositories doivent être dans l'infrastructure");

    }

    public static ArchRule controllersShouldOnlyUseUseCases() {
        return classes()
                .that().haveSimpleNameEndingWith("Controller")
                .should().onlyDependOnClassesThat(
                        resideInAPackage(Module.APPLICATION_USE_CASES.getArchPackage())
                                .or(resideInAPackage(Module.INFRASTRUCTURE.getArchPackage()))
                                .or(resideInAPackage(TechPackage.JDK.getPackage()))               // JDK
                                .or(resideInAPackage(TechPackage.SPRING.getPackage()))// Spring annotations/utilitaires
                                .or(resideInAPackage(TechPackage.SLF_4J.getPackage()))         // logging
                                .or(resideInAPackage(TechPackage.JAKARTA.getPackage()))           // validations, annotations
                )
                .as("Les controllers doivent uniquement utiliser les use cases de l'application");
    }

    public static ArchRule controllersShouldNotAccessDomainDirectly() {
        return noClasses()
                .that().haveSimpleNameEndingWith(ClassSuffix.CONTROLLER.getSuffix())
                .should().dependOnClassesThat()
                .resideInAPackage(Module.DOMAIN_MODELS.getArchPackage())
                .as("Les controllers ne doivent pas accéder directement au domaine (utiliser les use cases)");
    }

    public static ArchRule controllersShouldBeInInfrastructure() {
        return classes()
                .that().haveSimpleNameEndingWith(ClassSuffix.CONTROLLER.getSuffix())
                .should().resideInAPackage(Module.INFRASTRUCTURE.getArchPackage())
                .as("Les controllers doivent résider dans l'infrastructure");
    }

}
