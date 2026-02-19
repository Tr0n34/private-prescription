package fr.cnamts.cpam33.ordonnance.application.fixtures.stubs;

import fr.cnamts.cpam33.ordonnance.domain.ports.out.traces.TraceNumGenerator;

public class TraceIdNumGeneratorStub implements TraceNumGenerator {

   public static final String GENERATED_NUMBER = "123456";

    @Override
    public String generate() {
        return GENERATED_NUMBER;
    }

}
