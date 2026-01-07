package fr.cnamts.cpam33.ordonnance.application;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "fr.cnamts.cpam33.ordonnance")
public class ApplicationRulesTest {

    @ArchTest
    static final ArchRule use_cases_should_be_in_application = ApplicationRules.useCasesShouldBeInApplication();

    @ArchTest
    static final ArchRule use_cases_should_only_depend_on_domain_and_application = ApplicationRules.useCasesShouldOnlyDependOnDomainAndApplication();

    @ArchTest
    static final ArchRule use_case_may_only_use_allowed_spring_annotations = ApplicationRules.useCasesMayOnlyUseAllowedSpringAnnotations();

}
