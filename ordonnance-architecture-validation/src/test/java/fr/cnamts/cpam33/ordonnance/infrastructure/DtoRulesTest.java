package fr.cnamts.cpam33.ordonnance.infrastructure;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "fr.cnamts.cpam33.ordonnance")
public class DtoRulesTest {

    @ArchTest
    static final ArchRule dtos_should_be_in_infrastructure = DtoRules.dtoShouldBeInInfrastructure();

    @ArchTest
    static final ArchRule no_dto_in_domain = DtoRules.dtoShouldNotBeInDomain();

    @ArchTest
    static final ArchRule no_dto_in_application = DtoRules.dtoShouldNotBeInApplication();

}
