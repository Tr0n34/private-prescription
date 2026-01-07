package fr.cnamts.cpam33.ordonnance.domain;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "fr.cnamts.cpam33.ordonnance")
public class DomainRulesTest {

    @ArchTest
    static final ArchRule domain_should_be_framework_agnostic = DomainRules.domainShouldBeFrameworkAgnostic();

    @ArchTest
    static final ArchRule repository_interfaces_should_be_in_domain = DomainRules.repositoryInterfaceShouldBeInDomain();


}
