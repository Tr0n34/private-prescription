package fr.cnamts.cpam33.ordonnance.infrastructure.out.adapters.providers;

import fr.cnamts.cpam33.ordonnance.domain.kernel.identifiants.PatientId;
import fr.cnamts.cpam33.ordonnance.domain.ports.out.patients.PatientNumGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UuidPatientNumGenerator implements PatientNumGenerator {

    private static final Logger logger = LoggerFactory.getLogger(UuidPatientNumGenerator.class);

    @Override
    public String generate() {
        logger.trace("generate() called");
        return UUID.randomUUID().toString();
    }

    @Override
    public PatientId newpatientId(String externalId) {
        return new PatientId(generate());
    }

}
