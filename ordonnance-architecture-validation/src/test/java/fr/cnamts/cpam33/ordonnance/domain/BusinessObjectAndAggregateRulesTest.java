package fr.cnamts.cpam33.ordonnance.domain;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

@AnalyzeClasses(packages = {
        "fr.cnamts.cpam33.ordonnance.domain.models.businessObjects",
        "fr.cnamts.cpam33.ordonnance.domain.models.aggregates",
})
public class BusinessObjectAndAggregateRulesTest {

    public static final String AGGREGATES = "..aggregates..";

    @ArchTest
    static final ArchRule business_objects_and_aggregates_must_have_of_method =
            DomainRules.businessObjectsAndAggregatesMustHaveOfMethod().allowEmptyShould(true);

    @ArchTest
    static final ArchRule aggregates_encapsulation =
            classes()
                    .that().resideInAPackage(AGGREGATES)
                    .should(AggregateEncapsulationRules.shouldNotExposeModifiableCollections());

    @ArchTest
    static final ArchRule aggregates_collections_unmodifiable =
            classes()
                    .that().resideInAPackage(AGGREGATES)
                    .should(AggregateEncapsulationRules.collectionsMustBeUnmodifiable());

}