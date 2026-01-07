package fr.cnamts.cpam33.ordonnance.domain;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "fr.cnamts.cpam33.ordonnance.domain.models.valueobjects")
public class ValueObjectRulesTest {

    @ArchTest
    static final ArchRule value_objects_should_be_immutable = ValueObjectRules.valueObjectsShouldBeImmutable();

    @ArchTest
    static final ArchRule value_objects_should_not_have_setters = ValueObjectRules.valueObjectsShouldNotHaveSetters();

}
