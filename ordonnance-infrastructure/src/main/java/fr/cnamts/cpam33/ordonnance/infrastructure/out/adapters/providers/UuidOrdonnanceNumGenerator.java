package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers;

import fr.cnamts.cpam33.ordonnance.domain.ports.out.ordonnances.OrdonnanceNumGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidOrdonnanceNumGenerator implements OrdonnanceNumGenerator {

    private static final Logger logger = LoggerFactory.getLogger(UuidOrdonnanceNumGenerator.class);

    @Override
    public String generate() {
        logger.trace("generate() called");
        return UUID.randomUUID().toString();
    }

}
