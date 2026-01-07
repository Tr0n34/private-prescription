package fr.cnamts.cpam33.ordonnance;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "fr.cnamts.cpam33.ordonnance")
public class CycleRulesTest {

    @ArchTest
    static final ArchRule no_cycles_in_domain = CycleRules.no_cycles_in_domain();

    @ArchTest
    static final ArchRule no_cycle_in_application = CycleRules.no_cycles_in_application();

}
