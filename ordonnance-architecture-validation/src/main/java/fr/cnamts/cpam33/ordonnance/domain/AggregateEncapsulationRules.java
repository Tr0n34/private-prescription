package fr.cnamts.cpam33.ordonnance.domain;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;

import java.util.Collection;

public class AggregateEncapsulationRules {

    public static ArchCondition<JavaClass> shouldNotExposeModifiableCollections() {
        return new ArchCondition<>("not expose modifiable collections") {
            @Override
            public void check(JavaClass clazz, ConditionEvents events) {
                for (JavaField field : clazz.getFields()) {
                    Class<?> fieldType = field.getRawType().reflect();
                    if (java.util.Collection.class.isAssignableFrom(fieldType)
                            || java.util.Map.class.isAssignableFrom(fieldType)) {
                        if (!field.getModifiers().contains(JavaModifier.PRIVATE)) {
                            events.add(SimpleConditionEvent.violated(clazz,
                                    String.format(
                                            "Aggregate %s expose un champ collection non privé : %s %s",
                                            clazz.getSimpleName(),
                                            field.getRawType().getSimpleName(),
                                            field.getName()
                                    )));
                        }
                    }
                }
            }
        };
    }

    public static ArchCondition<JavaClass> collectionsMustBeUnmodifiable() {
        return new ArchCondition<>("return unmodifiable collections") {
            @Override
            public void check(JavaClass clazz, ConditionEvents events) {
                for (JavaMethod method : clazz.getMethods()) {
                    if (method.getName().startsWith("get")
                            && method.getRawReturnType().isAssignableTo(Collection.class)) {
                        events.add(SimpleConditionEvent.violated(clazz,
                                String.format(
                                        "Aggregate %s expose directement une collection mutable via %s()",
                                        clazz.getSimpleName(),
                                        method.getName()
                                )));
                    }
                }
            }
        };
    }

}
