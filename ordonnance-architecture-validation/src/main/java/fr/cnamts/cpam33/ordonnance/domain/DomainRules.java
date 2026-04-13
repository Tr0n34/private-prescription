package fr.cnamts.cpam33.ordonnance.domain;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import fr.cnamts.cpam33.ordonnance.ClassSuffix;
import fr.cnamts.cpam33.ordonnance.Module;
import fr.cnamts.cpam33.ordonnance.domain.abstracts.domain.DomainObject;

import java.util.Map;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

public class DomainRules {

    private static final String BUSINESS_OBJECTS_PACKAGE = ".domain.models.businessObjects.";
    private static final String AGGREGATES_PACKAGE = ".domain.models.aggregates.";

    public static final String OF = "of";

    private static final Map<String, DomainRole> ROLE_BY_PACKAGE = Map.of(
            BUSINESS_OBJECTS_PACKAGE, DomainRole.BUSINESS_OBJECT,
            AGGREGATES_PACKAGE, DomainRole.AGGREGATE_ROOT
    );
    public static final String REPOSITORY = "Repository";
    public static final String DOMAIN_PORTS = "..domain.ports..";

    public static ArchCondition<JavaClass> havePublicStaticOfFactory() {
        return new ArchCondition<>("have a public static 'of' factory method") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {

                boolean hasOf = hasOfMethod(javaClass);
                boolean hasPublicStaticOf = hasPublicStaticOfMethod(javaClass);

                if (!hasOf) {
                    events.add(SimpleConditionEvent.violated(
                            javaClass,
                            buildErrorMessage(javaClass, "absent")
                    ));
                } else if (!hasPublicStaticOf) {
                    events.add(SimpleConditionEvent.violated(
                            javaClass,
                            buildErrorMessage(javaClass, "notPublicStatic")
                    ));
                }
            }
        };
    }

    private static boolean hasOfMethod(JavaClass javaClass) {
        return javaClass.getMethods().stream()
                .anyMatch(method -> method.getName().equals(OF));
    }

    private static boolean hasPublicStaticOfMethod(JavaClass javaClass) {
        return javaClass.getMethods().stream()
                .filter(method -> method.getName().equals(OF))
                .anyMatch(method ->
                        method.getModifiers().contains(JavaModifier.STATIC)
                                && method.getModifiers().contains(JavaModifier.PUBLIC)
                                && method.getRawReturnType().equals(javaClass)
                );
    }

    private static String buildErrorMessage(JavaClass javaClass, String violationType) {
        String className = javaClass.getName();
        String simpleName = javaClass.getSimpleName();
        DomainRole role = resolveRole(className);

        switch (violationType) {
            case "absent":
                return String.format(
                        "%s invalide : %s%n→ %s : la factory `of(...)` est absente",
                        role.label, className, role.rule
                );
            case "notPublicStatic":
                return String.format(
                        "%s invalide : %s%n→ %s : la factory `of(...)` existe mais n'est pas `public static %s of(...)`",
                        role.label, className, role.rule, simpleName
                );
            default:
                return String.format(
                        "%s invalide : %s%n→ %s : violation inconnue",
                        role.label, className, role.rule
                );
        }
    }

    private static DomainRole resolveRole(String className) {
        return ROLE_BY_PACKAGE.entrySet().stream()
                .filter(e -> className.contains(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(DomainRole.DOMAIN_OBJECT);
    }

    public static ArchRule businessObjectsAndAggregatesMustHaveOfMethod() {
        return classes()
                .that().implement(DomainObject.class)
                .and().areNotInterfaces()
                .should(havePublicStaticOfFactory());
    }

    public static ArchRule domainShouldBeFrameworkAgnostic() {
        return noClasses()
                .that().resideInAPackage(Module.DOMAIN.getArchPackage())
                .should().dependOnClassesThat()
                .resideInAnyPackage(
                        "org.springframework..",
                        "jakarta.persistence..",
                        "javax.persistence.."
                )
                .as("Le domaine doit être agnostique des frameworks (pas de Spring, JPA, etc.)");
    }

    public static ArchRule repositoryInterfaceShouldBeInDomain() {
        return classes()
                .that().areInterfaces()
                .and().haveSimpleNameEndingWith(ClassSuffix.REPOSITORY.getSuffix())
                .and().haveSimpleNameNotEndingWith(ClassSuffix.JPA_REPOSITORY.getSuffix())
                .and().haveSimpleNameNotEndingWith(ClassSuffix.REPOSITORY_ADAPTER.getSuffix())
                .and().haveSimpleNameNotEndingWith(ClassSuffix.REPOSITORY_IMPL.getSuffix())
                .should().resideInAPackage(DOMAIN_PORTS)
                .as("Les interfaces Repository doivent être dans les ports du domaine");
    }

}