package fr.cnamts.cpam33.ordonnance.infrastructure;

import com.tngtech.archunit.lang.ArchRule;
import fr.cnamts.cpam33.ordonnance.ClassSuffix;
import fr.cnamts.cpam33.ordonnance.Module;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class DtoRules {

    public static ArchRule dtoShouldBeInInfrastructure() {
        return classes()
                .that().haveSimpleNameEndingWith(ClassSuffix.DTO.getSuffix())
                .should().resideInAPackage(Module.INFRASTRUCTURE.getArchPackage())
                .as("Les DTOs doivent rester dans l'infrastructure");
    }

    public static ArchRule dtoShouldNotBeInDomain() {
        return noClasses()
                .that().resideInAPackage(Module.DOMAIN.getArchPackage())
                .should().haveSimpleNameEndingWith(ClassSuffix.DTO.getSuffix())
                .as("Les DTOs ne doivent pas être dans le domaine");
    }

    public static ArchRule dtoShouldNotBeInApplication() {
        return noClasses()
                .that().resideInAPackage(Module.APPLICATION.getArchPackage())
                .should().haveSimpleNameEndingWith(ClassSuffix.DTO.getSuffix())
                .as("Les DTOs ne doivent pas être dans le domaine");
    }

}
