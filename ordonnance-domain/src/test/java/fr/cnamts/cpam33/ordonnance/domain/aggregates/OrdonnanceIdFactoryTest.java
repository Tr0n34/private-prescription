package fr.cnamts.cpam33.ordonnance.domain.aggregates;

import fr.cnamts.cpam33.ordonnance.domain.fixtures.stubs.OrdonnanceNumGeneratorStub;
import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.OrdonnanceId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceIdFactory;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceNumGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrdonnanceIdFactoryTest {

    @Test
    void should_create_ordonnance_id_from_generator() {
        OrdonnanceNumGenerator generator = new OrdonnanceNumGeneratorStub();
        OrdonnanceIdFactory factory = new OrdonnanceIdFactory(generator);
        OrdonnanceId id = factory.create();
        assertEquals(OrdonnanceNumGeneratorStub.GENERATED_NUMBER, id.numero());
    }

}
