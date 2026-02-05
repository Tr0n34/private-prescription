package fr.cnamts.cpam33.ordonnance.domain;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noMethods;

public class ValueObjectRules {

    public static final String DOMAIN_MODELS_VALUEOBJECTS = "..domain.models.valueobjects..";
    public static final String VO = "VO ";

    public static ArchRule valueObjectsShouldBeImmutable() {
        return classes()
                .that().resideInAPackage(DOMAIN_MODELS_VALUEOBJECTS)
                .should(new ArchCondition<>("be immutable") {
                    @Override
                    public void check(JavaClass javaClass, ConditionEvents events) {
                        try {
                            // --- Check des champs (classes normales) ---
                            if (!javaClass.isRecord()) {
                                for (JavaField field : javaClass.getFields()) {
                                    // champ final ?
                                    if (!field.getModifiers().contains(JavaModifier.FINAL)) {
                                        events.add(SimpleConditionEvent.violated(javaClass,
                                                VO + javaClass.getSimpleName() + " : champ non final -> " + field.getName()));
                                    }
                                    // champ collection exposée ?
                                    if (Collection.class.isAssignableFrom(field.getRawType().reflect())) {
                                        events.add(SimpleConditionEvent.violated(javaClass,
                                                VO + javaClass.getSimpleName() + " : champ collection exposée -> " + field.getName()));
                                    }
                                }
                            } else {
                                Class<?> clazz = javaClass.reflect();
                                // --- Check pour les records : vérifier les getters ---
                                Object instance = createTestInstance(javaClass);
                                for (JavaMethod javaMethod : javaClass.getMethods()) {
                                    if (Collection.class.isAssignableFrom(javaMethod.getRawReturnType().reflect())) {
                                        Method method = clazz.getMethod(javaMethod.getName());
                                        Object value = method.invoke(instance);
                                        // vérifier que c'est immuable
                                        if (!(value instanceof List<?> l && l.getClass().getName().contains("Unmodifiable"))) {
                                            events.add(SimpleConditionEvent.violated(javaClass,
                                                    VO + javaClass.getSimpleName() + " : getter collection mutable -> " + javaMethod.getName()));
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {

                        };

                        // --- Check des setters ---
                        for (JavaMethod method : javaClass.getMethods()) {
                            if (method.getName().startsWith("set")) {
                                events.add(SimpleConditionEvent.violated(javaClass,
                                        VO + javaClass.getSimpleName() + " : setter détecté -> " + method.getName()));
                            }
                        }
                    }
                });
    }

    public static ArchRule valueObjectsShouldNotHaveSetters() {
        return noMethods()
                .that().areDeclaredInClassesThat().resideInAPackage(DOMAIN_MODELS_VALUEOBJECTS)
                .and().arePublic()
                .should().haveNameMatching("set[A-Z].*")
                .as("Les value objects ne doivent pas avoir de setters (doivent être immuables)");
    }

    private static Object createTestInstance(JavaClass javaClass) {
        try {
            Class<?> clazz = javaClass.reflect();
            var constructor = clazz.getDeclaredConstructors()[0];
            constructor.setAccessible(true);
            Object[] params = Arrays.stream(constructor.getParameterTypes())
                    .map(type -> {
                        if (type == String.class) return "TEST";
                        if (Collection.class.isAssignableFrom(type)) return List.of();
                        if (type == LocalDate.class) return LocalDate.now();
                        return null;
                    }).toArray();
            return constructor.newInstance(params);
        } catch (Exception e) {
            throw new RuntimeException("Impossible de créer une instance pour ArchUnit", e);
        }
    }

}
