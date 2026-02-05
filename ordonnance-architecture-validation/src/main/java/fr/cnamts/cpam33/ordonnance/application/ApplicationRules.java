package fr.cnamts.cpam33.ordonnance.application;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import fr.cnamts.cpam33.ordonnance.Module;
import fr.cnamts.cpam33.ordonnance.domain.models.events.ActeMetierEvent;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ApplicationRules {

    public static final String USE_CASE = "UseCase";

    public static ArchRule useCasesShouldBeInApplication() {
        return classes()
                .that().haveSimpleNameEndingWith(USE_CASE)
                .should().resideInAPackage(Module.APPLICATION.getArchPackage())
                .as("Les use cases doivent résider dans la couche application");
    }

    public static ArchRule useCasesShouldOnlyDependOnDomainAndApplication() {
        return classes()
                .that().resideInAPackage(Module.APPLICATION_USE_CASES.getArchPackage())
                .should().onlyDependOnClassesThat()
                .resideInAnyPackage(
                        Module.DOMAIN.getArchPackage(),
                        Module.APPLICATION.getArchPackage(),
                        "java..", // classes JDK autorisées
                        "org.springframework.stereotype..", // @Service
                        "org.springframework.transaction.annotation..", // @Transactional
                        "jakarta.annotation.." // @Resource
                )
                .as("Les use cases ne doivent dépendre que du domain, de l'application et du JDK");
    }

    public static ArchRule useCasesMayOnlyUseAllowedSpringAnnotations() {
        return classes()
                .that().resideInAPackage(Module.APPLICATION_USE_CASES.getArchPackage())
                .should(new ArchCondition<JavaClass>("n'utiliser que des annotations Spring autorisées") {
                    @Override
                    public void check(JavaClass item, ConditionEvents events) {
                        item.getAnnotations().forEach(a -> {
                            Class<?> annotationType = a.getRawType().reflect();
                            if (!(annotationType.equals(Service.class) ||
                                    annotationType.equals(Transactional.class) ||
                                    annotationType.equals(Resource.class) ||
                                    annotationType.equals(ActeMetierEvent.class)
                            )) {
                                String message = String.format("%s utilise une annotation interdite : %s",
                                        item.getName(), annotationType.getName());
                                events.add(SimpleConditionEvent.violated(a, message));
                            }
                        });
                    }
                })
                .as("Les use cases ne peuvent utiliser que @Service, @Transactional et @Resource et annotation ");
    }

}
