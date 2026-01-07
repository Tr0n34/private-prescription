package fr.cnamts.cpam33.ordonnance.infrastructure.out.providers;

import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceNumGenerator;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidOrdonnanceNumGenerator implements OrdonnanceNumGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }

}
