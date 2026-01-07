package fr.cnamts.cpam33.ordonnance.infrastructure;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "fr.cnamts.cpam33.ordonnance")
public class InfrastructureRulesTest {

    @ArchTest
    static final ArchRule configuration_should_be_in_infrastructure = InfrastructureRules.configurationShouldBeInInfrastructure();

    @ArchTest
    static final ArchRule mappers_should_be_in_infrastructure = InfrastructureRules.mappersShouldBeInInfrastructure();

    @ArchTest
    static final ArchRule mappers_should_not_be_in_domain = InfrastructureRules.mappersShouldNotBeInDomain();

    @ArchTest
    static final ArchRule repository_implementations_should_be_in_infrastructure = InfrastructureRules.repositoryImplementationShouldBeInInfrastructure();

    @ArchTest
    static final ArchRule controllers_should_only_use_use_cases = InfrastructureRules.controllersShouldOnlyUseUseCases();

    @ArchTest
    static final ArchRule controllers_should_not_access_domain_directly = InfrastructureRules.controllersShouldNotAccessDomainDirectly();

    @ArchTest
    static final ArchRule controllers_should_be_in_infrastructure = InfrastructureRules.controllersShouldBeInInfrastructure();

}
