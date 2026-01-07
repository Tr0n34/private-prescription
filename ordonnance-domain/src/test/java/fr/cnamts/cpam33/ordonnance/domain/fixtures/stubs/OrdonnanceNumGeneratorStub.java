package fr.cnamts.cpam33.ordonnance.domain.fixtures.stubs;

import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceNumGenerator;

public class OrdonnanceNumGeneratorStub implements OrdonnanceNumGenerator {

    public static final String GENERATED_NUMBER = "123456";

    @Override
    public String generate() {
        return GENERATED_NUMBER;
    }

}
